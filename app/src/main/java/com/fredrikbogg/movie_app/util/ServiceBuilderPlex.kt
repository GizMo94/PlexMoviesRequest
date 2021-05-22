package com.fredrikbogg.movie_app.util


import com.fredrikbogg.movie_app.BuildConfig
import com.fredrikbogg.movie_app.data.db.remote.PlexAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.simpleframework.xml.Serializer
import org.simpleframework.xml.core.Persister
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

object ServiceBuilderPlex {
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val okHttp = OkHttpClient.Builder().apply {
        interceptors().add(logging)
        interceptors().add(RequestInterceptor())
    }

    var serializer: Serializer = Persister()

    private val retrofit = Retrofit.Builder().baseUrl(PlexAPI.BASE_API_URL)
        .addConverterFactory(SimpleXmlConverterFactory.createNonStrict(serializer))
        .client(okHttp.build()).build()

    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit.create(serviceType)
    }

    internal class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val oldRequest = chain.request()
            val url = oldRequest.url.newBuilder()
                .addQueryParameter("X-Plex-Token", BuildConfig.X_PLEX_TOKEN)
                .build()
            val newRequest = oldRequest.newBuilder().url(url).build()
            return chain.proceed(newRequest)
        }
    }
}