package com.smitcoderx.volunteerconnect.Ui.Posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Posts.PostsData
import com.smitcoderx.volunteerconnect.Model.Posts.PostsDataList
import com.smitcoderx.volunteerconnect.Model.Posts.PostsDataResponse
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import com.smitcoderx.volunteerconnect.Utils.errorResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: PostsRepository) : ViewModel() {

    val isNetworkConnectedLiveData = MutableLiveData<Boolean>()
    private val _postListLiveData = MutableLiveData<ResponseState<PostsDataList?>>()
    val postLiveLiveData = _postListLiveData


    private val _createPostLiveData = MutableLiveData<ResponseState<PostsDataResponse?>>()
    val createPostLiveData = _createPostLiveData

    private val _updatePostLiveData = MutableLiveData<ResponseState<PostsDataResponse?>>()
    val updatePostLiveData = _updatePostLiveData

    private val _deletePostLiveData = MutableLiveData<ResponseState<PostsDataList>>()
    val deletePostLiveData = _deletePostLiveData

    fun handlePostList(token: String, id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _postListLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _postListLiveData.value = ResponseState.Loading()
        try {
            val response = repository.getPostsList(token, id)
            if (response.isSuccessful && response.body()?.success == true) {
                _postListLiveData.value = ResponseState.Success(response.body())
            } else {
                _postListLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _postListLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _postListLiveData.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }

    fun createPost(token: String, postsData: PostsData) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _createPostLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _createPostLiveData.value = ResponseState.Loading()
        try {
            val response = repository.createPost(token, postsData)
            if (response.isSuccessful && response.body()?.success == true) {
                _createPostLiveData.value = ResponseState.Success(response.body())
            } else {
                _createPostLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _createPostLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _createPostLiveData.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }

    fun updatePost(token: String, id: String, postsData: PostsData) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _updatePostLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _updatePostLiveData.value = ResponseState.Loading()
        try {
            val response = repository.updatePost(token, id, postsData)
            if (response.isSuccessful && response.body()?.success == true) {
                _updatePostLiveData.value = ResponseState.Success(response.body())
            } else {
                _updatePostLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _updatePostLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _updatePostLiveData.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }

    fun deletePost(token: String, id: String) = viewModelScope.launch {
        if (isNetworkConnectedLiveData.value == false) {
            _deletePostLiveData.value =
                ResponseState.Error("This app requires an active internet connection to be used.")
        }

        _deletePostLiveData.value = ResponseState.Loading()
        try {
            val response = repository.deletePost(token, id)
            if (response.isSuccessful && response.body()?.success == true) {
                _deletePostLiveData.value = ResponseState.Success(response.body()!!)
            } else {
                _deletePostLiveData.value =
                    ResponseState.Error(errorResponse(response)?.error.toString())
            }
        } catch (e: HttpException) {
            _deletePostLiveData.value =
                ResponseState.Error("Something went wrong. Please try again later.")
        } catch (e: IOException) {
            _deletePostLiveData.value =
                ResponseState.Error("Couldn't reach server. Check your internet connection.")
        }
    }


}