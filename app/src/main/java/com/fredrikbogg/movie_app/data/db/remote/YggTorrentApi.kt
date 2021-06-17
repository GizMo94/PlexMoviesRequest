package com.fredrikbogg.movie_app.data.db.remote

import com.fredrikbogg.movie_app.data.model.network.TorrentResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

object YggTorrentApi {

    const val BASE_HOST_API_URL = "https://yggtorrent.li"
    const val BASE_API_URL = "https://www4.yggtorrent.li/"
    interface SearchTorrentService {
        @GET("engine/search")
        fun movieSearch(@Query("name") name: String?,
                        @Query("do") action: String?,
                        @Query("category") category: Int?,
                        @Query("sub_category") subCategory: Int?,
                        @Query("order") order: String?,
                        @Query("sort") sort: String?): Call<TorrentResponse>

        @FormUrlEncoded
        @POST("user/login")
        fun register(@Field("id") login : String, @Field("pass") password : String): Call<ResponseBody>

        @Streaming
        @GET("/engine/download_torrent")
        fun downloadTorrentMagnet(@Query("id") id : String?): Call<ResponseBody>

    }

}

