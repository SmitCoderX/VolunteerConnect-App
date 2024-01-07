package com.smitcoderx.volunteerconnect.Ui.Login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Auth.LoginData
import com.smitcoderx.volunteerconnect.Model.Auth.Login
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _signInDataLiveData = MutableLiveData<ResponseState<Login?>>()
    val signInDataLiveData: LiveData<ResponseState<Login?>>
        get() = _signInDataLiveData

    fun loginUser(loginData: LoginData) = viewModelScope.launch {
        _signInDataLiveData.value = loginRepository.login(loginData)
    }
}