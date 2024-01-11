package com.smitcoderx.volunteerconnect.Ui.Profile

import android.content.Context
import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Model.User.UpateData
import com.smitcoderx.volunteerconnect.Model.User.UserDataModel
import com.smitcoderx.volunteerconnect.R
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import com.smitcoderx.volunteerconnect.Utils.hasInternetConnection
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    @ApplicationContext private val context: Context
){

    suspend fun getCurrentLoggedinUser(token: String): ResponseState<UserDataModel?> {
        if(!context.hasInternetConnection()) {
            return ResponseState.Error(context.getString(R.string.error_internet_turned_off))
        }

        val response = try {
            api.getLoggedInUser(token)
        }catch (e: HttpException) {
            return ResponseState.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return ResponseState.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isSuccessful && response.body()?.success == true) {
            ResponseState.Success(response.body()!!)
        } else {
            ResponseState.Error(errorResponse(response)?.error.toString())
        }
    }

    suspend fun updateUser(token: String, updateData: UpateData): ResponseState<UserDataModel?> {
        if(!context.hasInternetConnection()) {
            return ResponseState.Error(context.getString(R.string.error_internet_turned_off))
        }

        val response = try {
            api.updateUser(token, updateData)
        }catch (e: HttpException) {
            return ResponseState.Error(context.getString(R.string.error_http))
        } catch (e: IOException) {
            return ResponseState.Error(context.getString(R.string.check_internet_connection))
        }

        return if (response.isSuccessful && response.body()?.success == true) {
            ResponseState.Success(response.body()!!)
        } else {
            ResponseState.Error(errorResponse(response)?.error.toString())
        }
    }
}