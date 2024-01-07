package com.smitcoderx.volunteerconnect.Ui.Home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.Category.CategoryResponse
import com.smitcoderx.volunteerconnect.Model.User.UserDataModel
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _categoryDataLiveData = MutableLiveData<ResponseState<CategoryResponse?>>()
    val categoryDataLiveData: LiveData<ResponseState<CategoryResponse?>>
        get() = _categoryDataLiveData

    private val _userLiveData = MutableLiveData<ResponseState<UserDataModel?>>()
    val userLiveData: LiveData<ResponseState<UserDataModel?>>
        get() = _userLiveData

    fun getCategoryList() = viewModelScope.launch {
        _categoryDataLiveData.value = homeRepository.getCategoryList()
    }

    fun getLoggedInData(token: String) = viewModelScope.launch {
        _userLiveData.value = homeRepository.getCurrentLoggedinUser(token)
    }


}