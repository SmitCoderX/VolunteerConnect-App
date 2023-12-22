package com.smitcoderx.volunteerconnect.Ui.Register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.API.RegisterData
import com.smitcoderx.volunteerconnect.Model.Login
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
): ViewModel() {

    private val _registerLiveData = MutableLiveData<ResponseState<Login?>>()
    val registerLiveData: LiveData<ResponseState<Login?>>
        get() = _registerLiveData


    fun register(registerData: RegisterData) = viewModelScope.launch {
        _registerLiveData.value = registerRepository.register(registerData)
    }

}