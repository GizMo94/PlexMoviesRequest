package com.fredrikbogg.movie_app.ui.home

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fredrikbogg.movie_app.data.db.remote.YggTorrentApi
import com.fredrikbogg.movie_app.data.model.EventObserver
import com.fredrikbogg.movie_app.data.model.MovieListType
import com.fredrikbogg.movie_app.databinding.FragmentHomeBinding
import com.fredrikbogg.movie_app.ui.BaseFragment
import com.fredrikbogg.movie_app.util.ServiceBuilderTorrent
import com.fredrikbogg.movie_app.util.extension.showSnackBar
import com.zhkrb.cloudflare_scrape_webview.CfCallback
import com.zhkrb.cloudflare_scrape_webview.Cloudflare
import java.net.HttpCookie
import java.util.*

class HomeFragment : BaseFragment(false) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var viewDataBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewDataBinding =
            FragmentHomeBinding.inflate(inflater, container, false).apply {
                viewmodel = viewModel
                lifecycleOwner = this@HomeFragment.viewLifecycleOwner
            }
        getRequest(requireContext(),YggTorrentApi.BASE_HOST_API_URL)
        return viewDataBinding.root
    }

    override fun setupViewModelObservers() {
        viewModel.snackBarText.observe(viewLifecycleOwner, EventObserver { view?.showSnackBar(it) })
        viewModel.goToShowAllEvent.observe(
            viewLifecycleOwner,
            EventObserver { navigateToShowAll(it) })
        viewModel.goToMovieDetailsEvent.observe(
            viewLifecycleOwner,
            EventObserver { navigateToMovieDetails(it.id, it.title) })
    }

    private fun navigateToShowAll(movieListType: MovieListType) {
        val action = HomeFragmentDirections.actionNavigationHomeToShowAllFragment(movieListType)
        findNavController().navigate(action)
    }

    private fun navigateToMovieDetails(movieId: Int, movieTitle: String) {
        val action =
            HomeFragmentDirections.actionNavigationHomeToMovieDetailsFragment(movieId, movieTitle)
        findNavController().navigate(action)
    }

    private fun getRequest(context: Context, url: String) {
        val cloudFlare = Cloudflare(context, url)
        cloudFlare.user_agent = ServiceBuilderTorrent.USER_AGENT
        cloudFlare.setCfCallback(object : CfCallback {
            override fun onSuccess(
                cookieList: MutableList<HttpCookie>?,
                hasNewUrl: Boolean,
                newUrl: String?
            ) {
                ServiceBuilderTorrent.cookiesFromCloudflare = cookieList
                val cookies = PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .getStringSet("appCookies", HashSet()) as HashSet<String>

                cookieList?.forEach {
                    cookies.add("${it.name}=${it.value}")
                }
                PreferenceManager
                    .getDefaultSharedPreferences(context)
                    .edit()
                    .putStringSet("appCookies", cookies)
                    .apply()
            }

            override fun onFail(code: Int, msg: String?) {
                throw RuntimeException()
            }

        })
        cloudFlare.getCookies()
    }

}

