package com.fredrikbogg.movie_app.data.model.network

import com.fredrikbogg.movie_app.data.model.entity.Cast
import com.fredrikbogg.movie_app.data.model.entity.Torrent
import com.google.gson.annotations.SerializedName
import pl.droidsonroids.jspoon.annotation.Selector
import java.lang.reflect.Constructor

class TorrentResponse : BaseListResponse<Torrent>{

    @Selector(".table > tbody > tr")
    override lateinit var results: List<Torrent>
}


