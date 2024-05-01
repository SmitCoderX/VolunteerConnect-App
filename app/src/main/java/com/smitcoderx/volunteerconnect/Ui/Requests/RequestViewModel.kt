package com.smitcoderx.volunteerconnect.Ui.Requests

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Requests.Requests
import com.smitcoderx.volunteerconnect.Model.Requests.RequestsData
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _recipientLiveData = MutableLiveData<ResponseState<List<RequestsData?>?>>()
    val recipientLiveData = _recipientLiveData

    private val _requesterLiveData = MutableLiveData<ResponseState<List<RequestsData?>?>>()
    val requesterLiveData = _requesterLiveData

    private val _handleRequest = MutableLiveData<ResponseState<Requests?>>()
    val handleRequest = _handleRequest

    fun getRecipientData(token: String, id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _recipientLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _recipientLiveData.value = ResponseState.Loading()

        try {
            val response =
                requestRepository.getRequestByRecipient(token, id)

            if (response.isSuccessful && response.body()?.success == true) {
                _recipientLiveData.value = ResponseState.Success(response.body()?.message)
            } else {
                _recipientLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _recipientLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _recipientLiveData.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }

    fun getRequesterData(token: String, id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _requesterLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _requesterLiveData.value = ResponseState.Loading()

        try {
            val response = requestRepository.getRequestByRequester(token, id)
            if (response.isSuccessful && response.body()?.success == true) {
                _requesterLiveData.value = ResponseState.Success(response.body()?.message)
            } else {
                _requesterLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _requesterLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _requesterLiveData.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }

    fun handleRequest(token: String, id: String, data: RequestsData) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _handleRequest.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _handleRequest.value = ResponseState.Loading()
        try {
            val response = requestRepository.handleRequest(token, id, data)
            if (response.isSuccessful && response.body()?.success == true) {
                _handleRequest.value = ResponseState.Success(response.body())
            }
        } catch (e: HttpException) {
            _handleRequest.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _handleRequest.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }

}