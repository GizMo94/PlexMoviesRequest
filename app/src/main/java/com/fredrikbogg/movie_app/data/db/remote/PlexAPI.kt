package com.fredrikbogg.movie_app.data.db.remote

import com.fredrikbogg.movie_app.data.model.entity.plex.MediaContainer
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

object PlexAPI {

    const val BASE_API_URL = "http://192.168.1.92:32400/"

    interface SearchMediaService {
        @GET("search?type=1")
        fun movieSearch(@Query("query") query: String?): Call<MediaContainer>
    }

}

