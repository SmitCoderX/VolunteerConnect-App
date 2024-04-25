package com.smitcoderx.volunteerconnect.Ui.Events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Events.Data
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.Model.Events.EventDataArrayModel
import com.smitcoderx.volunteerconnect.Model.Events.EventDataModel
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SingleEventViewModel @Inject constructor(
    private val repository: SingleEventRepository
) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _eventLiveData = MutableLiveData<ResponseState<DataFetch?>>()
    val eventLiveData = _eventLiveData

    fun getEventData(id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _eventLiveData.value = ResponseState.Loading()
        try {
            val response = repository.getEventDetails(id)
            if (response.isSuccessful && response.body()?.success == true) {
                _eventLiveData.value = ResponseState.Success(response.body()?.data)
            } else {
                _eventLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _eventLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _eventLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }
}