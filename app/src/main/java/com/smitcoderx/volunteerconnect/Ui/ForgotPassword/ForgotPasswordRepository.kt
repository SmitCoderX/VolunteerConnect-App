package com.smitcoderx.volunteerconnect.Ui.ForgotPassword

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smitcoderx.volunteerconnect.API.LoginData
import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Model.ErrorResponse
import com.smitcoderx.volunteerconnect.Model.Login
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ForgotPasswordRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    @ApplicationContext private val context: Context
) {

    suspend fun forgotPassword(loginData: LoginData): ResponseState<Login?> {
        if(!context.hasInternetConnection()) {
            return ResponseState.Error(context.getString(R.string.error_internet_turned_off))
        }

        val response = try {
            api.forgotPassword(loginData)
        } catch (e: HttpException) {
            return ResponseState.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return ResponseState.Error(context.getString(R.string.check_internet_connection))
        }


        return if(response.isSuccessful && response.body()?.success == true) {
            ResponseState.Success(response.body()!!)
        } else  {
            ResponseState.Error(errorResponse(response)?.error.toString())
        }

    }

}