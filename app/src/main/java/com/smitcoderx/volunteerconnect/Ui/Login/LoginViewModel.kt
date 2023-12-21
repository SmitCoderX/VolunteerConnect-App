package com.smitcoderx.volunteerconnect.Ui.Login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.API.LoginData
import com.smitcoderx.volunteerconnect.Model.Login
import com.smitcoderx.volunteerconnect.Utils.Constants.TAG
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
): ViewModel() {

    private val _signInDataLiveData = MutableLiveData<ResponseState<Login?>>()
    val signInDataLiveData: LiveData<ResponseState<Login?>>
        get() = _signInDataLiveData

    fun loginUser(loginData: LoginData) = viewModelScope.launch {
        when (val result = loginRepository.login(loginData)) {
            is ResponseState.Loading -> {
                _signInDataLiveData.value = ResponseState.Loading()
            }

            is ResponseState.Success -> {
                _signInDataLiveData.value = ResponseState.Success(result.data)
            }

            else -> {
                _signInDataLiveData.value = ResponseState.Error(result.message.toString())
            }
        }
    }
}