package com.fredrikbogg.movie_app.ui.movies

import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.*
import com.fredrikbogg.movie_app.data.db.repository.MovieRepository
import com.fredrikbogg.movie_app.data.db.repository.SearchRepository
import com.fredrikbogg.movie_app.data.model.Event
import com.fredrikbogg.movie_app.data.model.GoToMovie
import com.fredrikbogg.movie_app.data.model.entity.Movie
import com.fredrikbogg.movie_app.ui.BaseViewModel
import com.fredrikbogg.movie_app.ui.person_details.PersonDetailsViewModel
import com.fredrikbogg.movie_app.util.extension.appendList
import com.fredrikbogg.movie_app.util.extension.clear
import com.fredrikbogg.movie_app.util.extension.liveDataBlockScope

class MoviesViewModel() : BaseViewModel(), GoToMovie {

    private val movieRepository = MovieRepository()
    private val searchRepository = SearchRepository()

    private val moviesPage = MutableLiveData<Int>().apply { value = 1 }
    private val searchPage = MutableLiveData<String>().apply { value = "" }

    val movieList = MediatorLiveData<MutableList<Movie>>()

    override val goToMovieDetailsEvent: MutableLiveData<Event<Movie>> = MutableLiveData()

    private var queryHasBeenHandle: Boolean = false

    init {
        loadAllMovies()
    }

    fun getOnQueryTextChange(): SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(term: String?): Boolean {
               /* Update query result every 2 characters */
               /* if (term != null)
                    if (term.length % 2 == 0)
                        loadQueryMovies(term)*/
                return false
            }

            override fun onQueryTextSubmit(term: String?): Boolean {
                if (term != null)
                    loadQueryMovies(term)
                else
                    loadAllMovies()

                return false
            }
        }

    fun getOnQueryClear(): SearchView.OnCloseListener =
        SearchView.OnCloseListener {
            loadAllMovies()
            false
        }

    private fun loadQueryMovies(query: String) {
        queryHasBeenHandle = true
        searchPage.value = query
        val loadedQueryMovie = searchPage.switchMap {
            liveDataBlockScope {
                searchRepository.loadQueryMovies(it) { mSnackBarText.postValue(Event(it)) }
            }
        }
        movieList.clear()
        movieList.addSource(loadedQueryMovie) { it?.let { list -> movieList.appendList(list) } }
    }

    private fun loadAllMovies() {
        queryHasBeenHandle = false
        val loadedMovies = moviesPage.switchMap {
            liveDataBlockScope {
                movieRepository.loadDiscoverList(it) { mSnackBarText.postValue(Event(it)) }
            }
        }
        movieList.clear()
        movieList.addSource(loadedMovies) { it?.let { list -> movieList.appendList(list) } }
    }

    fun loadMoreMovies() {
        if (!queryHasBeenHandle)
            moviesPage.value = moviesPage.value?.plus(1)
    }

}