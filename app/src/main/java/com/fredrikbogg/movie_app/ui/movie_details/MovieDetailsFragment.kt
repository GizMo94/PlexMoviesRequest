package com.fredrikbogg.movie_app.ui.movie_details

import android.R.attr.button
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fredrikbogg.movie_app.R
import com.fredrikbogg.movie_app.data.db.remote.TheMovieDatabaseAPI
import com.fredrikbogg.movie_app.data.model.EventObserver
import com.fredrikbogg.movie_app.databinding.FragmentMovieDetailsBinding
import com.fredrikbogg.movie_app.ui.BaseFragment
import com.fredrikbogg.movie_app.ui.binding.bindTorrentsList
import com.fredrikbogg.movie_app.util.extension.openUrl
import com.fredrikbogg.movie_app.util.extension.showSnackBar
import okhttp3.ResponseBody
import java.io.File
import java.io.InputStream
import java.io.OutputStream


class MovieDetailsFragment : BaseFragment(true) {

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsViewModel by viewModels {
        MovieDetailsViewModelFactory(
            args.movieIdArg,
            args.movieTitleArg
        )
    }
    private lateinit var viewDataBinding: FragmentMovieDetailsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentMovieDetailsBinding.inflate(inflater, container, false)
                .apply {
                    viewmodel = viewModel
                    lifecycleOwner = this@MovieDetailsFragment.viewLifecycleOwner
                }

        return viewDataBinding.root
    }

    override fun setupViewModelObservers() {

        viewModel.registerResponse.observe(viewLifecycleOwner, Observer { it ->
        })

        viewModel.torrentList.observe(viewLifecycleOwner, Observer {it ->
                it?.forEach {
                    //Log.i("getResquest", "setupViewModelObservers: ${it.torrentToString()}")
                }
        })

        viewModel.torrentLinkResponse.observe(viewLifecycleOwner, Observer {
        })

        viewModel.snackBarText.observe(viewLifecycleOwner, EventObserver {
            view?.showSnackBar(it)
        })

        viewModel.goToCastDetailsEvent.observe(
            viewLifecycleOwner,
            EventObserver { navigateToPersonDetails(it.id, it.name) })

        viewModel.goToVideoEvent.observe(
            viewLifecycleOwner,
            EventObserver { openUrl(TheMovieDatabaseAPI.getYoutubeWatchUrl(it.key)) })

    }

    private fun navigateToPersonDetails(personId: Int, personName: String) {
        val action =
            MovieDetailsFragmentDirections.actionMovieDetailsFragmentToPersonDetailsFragment(
                personId,
                personName
            )
        findNavController().navigate(action)
    }

    private fun downloadTorrent(body : ResponseBody) {
        //Switch back to the IO thread to create the downloaded file
        val file =
            File("${Environment.getExternalStorageDirectory().path}/download/")

        file.createNewFile()
        //The GitHubService object can be directly obtained through GitHubService.getInstance()

        if(body!=null){
            var inStream: InputStream? = null
            var outStream: OutputStream? = null
            /*Note that there are no checked exceptions in Kotlin,
                                 If try catch is not written here, the compiler will not report an error.
                                 But we need to ensure that the stream is closed, so we need to operate finally*/
            try {
                //The following operations to read and write files are similar to java
                inStream = body.byteStream()
                outStream = file.outputStream()
                //Total file length
                val contentLength = body.contentLength()
                //Currently downloaded length
                var currentLength = 0L
                //Buffer
                val buff = ByteArray(1024)
                var len = inStream.read(buff)
                while (len != -1) {
                    outStream.write(buff, 0, len)
                    currentLength += len
                    len = inStream.read(buff)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inStream?.close()
                outStream?.close()

            }
        }

    }
}