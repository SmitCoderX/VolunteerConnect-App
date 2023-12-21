package com.smitcoderx.volunteerconnect.API

import com.smitcoderx.volunteerconnect.Model.Login
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface VolunteerConnectApi {

    companion object {
        const val BASE_URL = "https://volunteer-connect-eta.vercel.app/api/v1/"
    }

   @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginData: LoginData
    ): Response<Login?>
}