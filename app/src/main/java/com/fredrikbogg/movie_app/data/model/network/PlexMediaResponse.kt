package com.fredrikbogg.movie_app.data.model.network

import com.fredrikbogg.movie_app.data.model.entity.plex.Video
import com.google.gson.annotations.SerializedName


data class PlexMediaResponse(
    @SerializedName("MediaContainer")
    override var mediaContainer: List<Video>,

    ) : BasePlexResponse<Video>
