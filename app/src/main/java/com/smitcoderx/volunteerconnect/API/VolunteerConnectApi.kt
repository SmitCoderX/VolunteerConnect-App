package com.smitcoderx.volunteerconnect.API

import com.smitcoderx.volunteerconnect.Model.Auth.Login
import com.smitcoderx.volunteerconnect.Model.Auth.LoginData
import com.smitcoderx.volunteerconnect.Model.Auth.RegisterData
import com.smitcoderx.volunteerconnect.Model.Category.CategoryResponse
import com.smitcoderx.volunteerconnect.Model.User.UpateData
import com.smitcoderx.volunteerconnect.Model.User.UserDataModel
import com.smitcoderx.volunteerconnect.Ui.Events.Data
import com.smitcoderx.volunteerconnect.Ui.Events.EventDataModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT

interface VolunteerConnectApi {

    companion object {
        const val BASE_URL = "https://volunteer-connect-eta.vercel.app/api/v1/"
    }

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/login")
    suspend fun loginUser(
        @Body loginData: LoginData
    ): Response<Login?>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/register")
    suspend fun registerUser(
        @Body registerBody: RegisterData
    ): Response<Login?>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("auth/forgotpassword")
    suspend fun forgotPassword(
        // Passing LoginData has i don't want to create a new data class for just passing an email address
        @Body loginData: LoginData
    ): Response<Login?>

    @GET("auth/me")
    suspend fun getLoggedInUser(
        @Header("Authorization") token: String
    ): Response<UserDataModel>

    @PUT("auth/updatedetails")
    suspend fun updateUser(
        @Header("Authorization") token: String,
        @Body updateData: UpateData
    ): Response<UserDataModel>

    @GET("category")
    suspend fun getCategoryList(): Response<CategoryResponse>

    @Headers("Content-Type: Application/Json;charset=UTF-8")
    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body eventData: Data
    ): Response<EventDataModel>


}