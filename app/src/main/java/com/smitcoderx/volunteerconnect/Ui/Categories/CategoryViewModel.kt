package com.smitcoderx.volunteerconnect.Ui.Categories

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
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _eventCategoryWiseLiveData = MutableLiveData<ResponseState<List<DataFetch?>?>>()
    val eventCategoryWiseLiveData = _eventCategoryWiseLiveData


    private val _eventLiveData = MutableLiveData<ResponseState<List<DataFetch?>?>>()
    val eventLiveData = _eventLiveData

    fun saveEvent(data: DataFetch) = viewModelScope.launch {
        categoryRepository.insert(data)
    }

    fun deleteEvent(data: DataFetch) = viewModelScope.launch {
        categoryRepository.delete(data)
    }


    fun getEventListCategoryWise(category: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventCategoryWiseLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _eventCategoryWiseLiveData.value = ResponseState.Loading()
        try {
            val response = categoryRepository.getEventsList()
            if (response.isSuccessful && response.body()?.success == true) {
                _eventCategoryWiseLiveData.value =
                    ResponseState.Success(response.body()!!.dataList?.filter {
                        it.category?.contains(category) == true
                    })
            } else {
                _eventCategoryWiseLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _eventCategoryWiseLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _eventCategoryWiseLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

    fun getEventList() = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }
        _eventLiveData.value = ResponseState.Loading()
        try {
            val response = categoryRepository.getEventsList()
            if (response.isSuccessful && response.body()?.success == true) {
                _eventLiveData.value =
                    ResponseState.Success(response.body()!!.dataList)
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