package com.fredrikbogg.movie_app.data.db.repository

import androidx.lifecycle.MutableLiveData
import com.fredrikbogg.movie_app.data.db.remote.PlexAPI
import com.fredrikbogg.movie_app.data.model.entity.plex.MediaContainer
import com.fredrikbogg.movie_app.util.ServiceBuilderPlex

class MediaRepository : BaseRepository() {
    val mediaService =
        ServiceBuilderPlex.buildService(PlexAPI.SearchMediaService::class.java)

    suspend fun loadMedia(query: String, errorText: (String) -> Unit) =
        loadCall(
            { mediaService.movieSearch(query) },
            MutableLiveData<MediaContainer>(),
            errorText
        )
}
