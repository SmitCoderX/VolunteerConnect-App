package com.smitcoderx.volunteerconnect.Ui.EventsCreation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Events.Data
import com.smitcoderx.volunteerconnect.Model.Events.EventDataModel
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _createEventLiveData = MutableLiveData<ResponseState<EventDataModel?>>()
    val createEventLiveData = _createEventLiveData


    fun createEvent(token: String, data: Data) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _createEventLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _createEventLiveData.value = ResponseState.Loading()
        try {
            val response = repository.createEvent(token, data)
            if (response.isSuccessful && response.body()?.success == true) {
                _createEventLiveData.value = ResponseState.Success(response.body())
            } else {
                _createEventLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _createEventLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _createEventLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

}