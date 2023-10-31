package com.wapazock.veliqolab.utils.http

import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class http {
    companion object {
        // Http client builder instance
        private var okHttpClientBuilder: OkHttpClient.Builder? = null;
        private var okHttpClient: OkHttpClient? = null
        public final var BASE_URL = "https://challenge.veliqo.com/mobile/api/v1"

        // Trust all certificates
        private var trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        // Get a builder
        private fun getOkHttpClientBuilder(): OkHttpClient.Builder {
            // If null create a builder
            if (okHttpClientBuilder == null) {
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())

                okHttpClientBuilder = OkHttpClient.Builder()
                okHttpClientBuilder!!.sslSocketFactory(
                    sslContext.socketFactory,
                    trustAllCerts[0] as X509TrustManager
                )
                okHttpClientBuilder!!.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
            }
            return okHttpClientBuilder as OkHttpClient.Builder
        }

        // Get Http Client
        public fun getClient(): OkHttpClient {
            //check null
            if (okHttpClient == null){
                okHttpClient = getOkHttpClientBuilder().build()
            }
            return okHttpClient as OkHttpClient
        }
    }

}