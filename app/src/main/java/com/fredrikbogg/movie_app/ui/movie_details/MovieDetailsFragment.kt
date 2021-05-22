package com.fredrikbogg.movie_app.ui.movie_details

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.fredrikbogg.movie_app.data.db.remote.TheMovieDatabaseAPI
import com.fredrikbogg.movie_app.data.model.EventObserver
import com.fredrikbogg.movie_app.data.model.entity.plex.MediaContainer
import com.fredrikbogg.movie_app.databinding.FragmentMovieDetailsBinding
import com.fredrikbogg.movie_app.ui.BaseFragment
import com.fredrikbogg.movie_app.util.extension.openUrl
import com.fredrikbogg.movie_app.util.extension.showSnackBar


class MovieDetailsFragment : BaseFragment(true) {

    private val args: MovieDetailsFragmentArgs by navArgs()
    private val viewModel: MovieDetailsViewModel by viewModels { MovieDetailsViewModelFactory(args.movieIdArg, args.movieTitleArg) }
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
        viewModel.snackBarText.observe(viewLifecycleOwner, EventObserver {
            view?.showSnackBar(it)})

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
}