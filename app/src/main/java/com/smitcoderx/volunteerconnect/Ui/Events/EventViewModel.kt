package com.smitcoderx.volunteerconnect.Ui.Events

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Ui.Events.Models.Data
import com.smitcoderx.volunteerconnect.Ui.Events.Models.EventDataModel
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _createEventLiveData = MutableLiveData<ResponseState<EventDataModel?>>()
    val createEventLiveData = _createEventLiveData

    fun createEvent(token: String, data: Data) = viewModelScope.launch {
        _createEventLiveData.value = repository.createEvent(token, data)
    }

}