package com.fredrikbogg.movie_app.data.db.repository

import androidx.lifecycle.MutableLiveData
import com.fredrikbogg.movie_app.data.db.remote.YggTorrentApi
import com.fredrikbogg.movie_app.data.model.entity.*
import com.fredrikbogg.movie_app.data.model.network.TorrentResponse
import com.fredrikbogg.movie_app.util.ServiceBuilderTorrent
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

class TorrentRepository : BaseRepository() {
    private val torrentService =
        ServiceBuilderTorrent.buildService(YggTorrentApi.SearchTorrentService::class.java)
    private val hostService =
        ServiceBuilderTorrent.buildServiceHost(YggTorrentApi.SearchTorrentService::class.java)

    suspend fun searchTorrent(query: String, errorText: (String) -> Unit) =
        loadListCall(
            { torrentService.movieSearch(query, "search",2145,2183,"desc", "seed") },
            MutableLiveData<List<Torrent>>(),
            errorText
        )

    suspend fun register(errorText: (String) -> Unit) =
        loadCall(
            { hostService.register("lumanaro","lorinlucas44500") },
            MutableLiveData<ResponseBody>(),
            errorText
        )

    suspend fun downloadTorrent(id: String, errorText: (String) -> Unit) =
        loadCall(
            { torrentService.downloadTorrentMagnet(id) },
            MutableLiveData<ResponseBody>(),
            errorText
        )
}
