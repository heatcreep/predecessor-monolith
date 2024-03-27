package com.aowen.monolith.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Cache
import okhttp3.OkHttpClient

fun String.trimExtraNewLine() = this
    .replace("\n\n", "")
    .replace("\r\n\r\n", "\n")

object NetworkUtil {
    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun getOkHttpClientWithCache(appContext: Context): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024L // 10 MB
        val maxRequestStale = 60 * 60 * 24 * 7 // 7 days
        val maxRequestAge = 60 // 1 minute
        val maxResponseAge = 3600 // 1 hour
        val cache = Cache(appContext.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor { chain ->
                val response = chain.proceed(chain.request())
                response.newBuilder()
                    .header(
                        name = "Cache-Control",
                        value = "public, max-age=$maxResponseAge")
                    .removeHeader("Pragma")
                    .build()
            }
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (isNetworkAvailable(appContext))
                    request.newBuilder().header(
                        name = "Cache-Control",
                        value = "public, max-age=$maxRequestAge"
                    ).build()
                else
                    request.newBuilder().header(
                        "Cache-Control",
                        "public, only-if-cached, max-stale=$maxRequestStale"
                    ).build()
                chain.proceed(request)
            }
            .build()
    }
}