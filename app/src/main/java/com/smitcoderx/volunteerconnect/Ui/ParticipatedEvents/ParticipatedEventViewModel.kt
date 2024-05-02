package com.smitcoderx.volunteerconnect.Ui.ParticipatedEvents

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ParticipatedEventViewModel @Inject constructor(
    private val participatedEventsRepository: ParticipatedEventsRepository
) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _eventNgoLiveData = MutableLiveData<ResponseState<List<DataFetch?>?>>()
    val eventNgoLiveData = _eventNgoLiveData

    private val _eventUserLiveData = MutableLiveData<ResponseState<List<DataFetch?>?>>()
    val eventUserLiveData = _eventUserLiveData

    fun getEventsByNgo(id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventNgoLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _eventNgoLiveData.value = ResponseState.Loading()
        try {
            val response = participatedEventsRepository.getEventList()
            if (response.isSuccessful && response.body()?.success == true) {
                _eventNgoLiveData.value =
                    ResponseState.Success(response.body()?.dataList?.filter { item ->
                        item.user.equals(
                            id, ignoreCase = true
                        )
                    })
            } else {
                _eventNgoLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _eventNgoLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _eventNgoLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

    fun getEventsByUser(id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventUserLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _eventUserLiveData.value = ResponseState.Loading()
        try {
            val response = participatedEventsRepository.getEventList()
            if (response.isSuccessful && response.body()?.success == true) {
                _eventUserLiveData.value =
                    ResponseState.Success(response.body()?.dataList?.filter { item ->
                        item.volunteers?.contains(id) == true
                    })
            } else {
                _eventUserLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _eventUserLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _eventUserLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

}