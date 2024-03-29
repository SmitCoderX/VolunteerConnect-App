package com.smitcoderx.volunteerconnect.Ui.Categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Events.Data
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,

    ) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _eventCategoryWiseLiveData = MutableLiveData<ResponseState<List<Data?>?>>()
    val eventCategoryWiseLiveData = _eventCategoryWiseLiveData


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
}