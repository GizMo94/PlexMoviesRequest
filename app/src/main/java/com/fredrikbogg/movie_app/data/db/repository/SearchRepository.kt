package com.fredrikbogg.movie_app.data.db.repository

import androidx.lifecycle.MutableLiveData
import com.fredrikbogg.movie_app.data.db.remote.TheMovieDatabaseAPI
import com.fredrikbogg.movie_app.data.model.entity.Cast
import com.fredrikbogg.movie_app.data.model.entity.Genre
import com.fredrikbogg.movie_app.data.model.entity.Movie
import com.fredrikbogg.movie_app.data.model.entity.Video
import com.fredrikbogg.movie_app.util.ServiceBuilder

class SearchRepository : BaseRepository() {
    private val searchService =
        ServiceBuilder.buildService(TheMovieDatabaseAPI.SearchService::class.java)

    suspend fun loadQueryMovies(query: String, errorText: (String) -> Unit) =
        loadPageListCall(
            { searchService.fetchQueryList(query) },
            MutableLiveData<List<Movie>>(),
            errorText
        )
}
