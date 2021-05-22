package com.fredrikbogg.movie_app.data.model.network

interface BasePlexResponse<PlexMovie> {
    var mediaContainer: List<PlexMovie>
}