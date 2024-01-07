package com.smitcoderx.volunteerconnect.DI

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.inject.Singleton
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

//    val okHttpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
////            val request = chain.request().newBuilder().addHeader("Authorization", )
//        }
//    })

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideVolunteerConnectApi(retrofit: Retrofit): VolunteerConnectApi =
        retrofit.create(VolunteerConnectApi::class.java)

}