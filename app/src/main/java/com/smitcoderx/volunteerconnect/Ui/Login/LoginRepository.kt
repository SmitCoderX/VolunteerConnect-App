package com.smitcoderx.volunteerconnect.Ui.Login

import android.content.Context
import android.util.Log
import com.smitcoderx.volunteerconnect.API.LoginData
import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Model.Login
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class LoginRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    @ApplicationContext private val context: Context
) {
    suspend fun login(loginData: LoginData): ResponseState<Login> {
        if(!context.hasInternetConnection()) {
            return ResponseState.Error(context.getString(R.string.error_internet_turned_off))
        }

        val response = try {
            api.loginUser(loginData)
        } catch (e: HttpException) {
            return ResponseState.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return ResponseState.Error(context.getString(R.string.check_internet_connection))
        }

        return if(response.isSuccessful && response.body()?.success == true) {
            ResponseState.Success(response.body()!!)
        } else if(response.body()?.success == false) {
            ResponseState.Error(response.body()!!.error.toString())
        } else {
            ResponseState.Error(context.getString(R.string.error_unknown))
        }

    }
}