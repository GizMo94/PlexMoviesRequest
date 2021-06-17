package com.fredrikbogg.movie_app.util


import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.fredrikbogg.movie_app.MainApplication
import com.fredrikbogg.movie_app.data.db.remote.YggTorrentApi
import com.fredrikbogg.movie_app.ui.main.MainActivity
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import pl.droidsonroids.retrofit2.JspoonConverterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.net.HttpCookie


object ServiceBuilderTorrent {
    const val USER_AGENT =
        "Mozilla/5.0 (Linux; Android 6.0.1; SM-G920V Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.98 Mobile Safari/537.36"
    var cookiesFromCloudflare: MutableList<HttpCookie>? = null

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val okHttp = OkHttpClient.Builder().apply {
        followRedirects(true)
        retryOnConnectionFailure(true)
        interceptors().add(RequestInterceptor())
        interceptors().add(logging)
        setCookieStore(MainApplication.applicationContext())
        cookieJar(UvCookieJar())
    }
    private val retrofit = Retrofit.Builder().baseUrl(YggTorrentApi.BASE_API_URL)
        .addConverterFactory(JspoonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttp.build()).build()


    fun <T> buildService(serviceType: Class<T>): T {
        return retrofit
            .create(serviceType)

    }

    private val retrofitLogging = Retrofit.Builder().baseUrl(YggTorrentApi.BASE_HOST_API_URL)
        .addConverterFactory(JspoonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttp.build()).build()

    fun <T> buildServiceHost(serviceType: Class<T>): T {
        return retrofitLogging
            .create(serviceType)

    }

    internal class RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val oldRequest = chain.request()
            val url = oldRequest.url.newBuilder()
                .build()
            val newRequest = oldRequest.newBuilder().url(url)
                .addHeader("User-Agent", USER_AGENT)
                .build()
            return chain.proceed(newRequest)
        }
    }

}

private class UvCookieJar : CookieJar {

    private val cookies = mutableListOf<Cookie>()

    override fun saveFromResponse(url: HttpUrl, cookieList: List<Cookie>) {
        cookies.addAll(cookieList)
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookies
    }

}


private val cookiesKey = "appCookies"

class SendSavedCookiesInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val preferences = PreferenceManager
            .getDefaultSharedPreferences(context)
            .getStringSet(cookiesKey, HashSet()) as HashSet<String>
        preferences.forEach {
            builder.addHeader("Cookie", it)
        }

        return chain.proceed(builder.build())
    }
}

class SaveReceivedCookiesInterceptor(private val context: Context) : Interceptor {

    @JvmField
    val setCookieHeader = "Set-Cookie"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.headers(setCookieHeader).isEmpty()) {
            val cookies = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getStringSet(cookiesKey, HashSet()) as HashSet<String>
            originalResponse.headers(setCookieHeader).forEach {
                cookies.add(it)
            }
            PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putStringSet(cookiesKey, cookies)
                .apply()
        }

        return originalResponse
    }

}

fun OkHttpClient.Builder.setCookieStore(context: Context): OkHttpClient.Builder {
    return this
        .addInterceptor(SendSavedCookiesInterceptor(context))
        .addInterceptor(SaveReceivedCookiesInterceptor(context))
}