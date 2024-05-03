package com.smitcoderx.volunteerconnect.Ui.EventJobs

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Forum.ForumData
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ForumListViewModel @Inject constructor(private val repository: ForumListRepository) :
    ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _eventForumListLiveData = MutableLiveData<ResponseState<List<ForumData?>?>>()
    val eventForumListLiveData = _eventForumListLiveData


    private val _eventForumListUserLiveData = MutableLiveData<ResponseState<List<ForumData?>?>>()
    val eventForumListUserLiveData = _eventForumListUserLiveData


    fun handleNGOForumList(token: String, id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventForumListLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _eventForumListLiveData.value = ResponseState.Loading()
        try {
            val response = repository.getForumList(token)
            if (response.isSuccessful && response.body()?.success == true) {
                _eventForumListLiveData.value =
                    ResponseState.Success(response.body()?.data?.filter { item ->
                        item?.userId?.equals(
                            id, ignoreCase = true
                        ) == true
                    })
            } else {
                _eventForumListLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _eventForumListLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _eventForumListLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

    fun handleUserForumList(token: String, id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _eventForumListUserLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _eventForumListUserLiveData.value = ResponseState.Loading()
        try {
            val response = repository.getForumList(token)
            if (response.isSuccessful && response.body()?.success == true) {
                _eventForumListUserLiveData.value =
                    ResponseState.Success(response.body()?.data?.filter { item ->
                        item?.participants?.contains(id) == true
                    })
            } else {
                _eventForumListUserLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _eventForumListUserLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _eventForumListUserLiveData.value =
                ResponseState.Error("Couldn\'t reach server. Check your internet connection.")
        }
    }

}