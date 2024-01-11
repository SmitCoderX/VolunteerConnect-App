package com.smitcoderx.volunteerconnect.Ui.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smitcoderx.volunteerconnect.Model.User.UpateData
import com.smitcoderx.volunteerconnect.Model.User.UserDataModel
import com.smitcoderx.volunteerconnect.Utils.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
): ViewModel() {
    private val _getProfileDataMutableLiveData = MutableLiveData<ResponseState<UserDataModel?>>()
    val getProfileDataLiveData: LiveData<ResponseState<UserDataModel?>>
        get() = _getProfileDataMutableLiveData

    private val _updateProfileMutableLiveData = MutableLiveData<ResponseState<UserDataModel?>>()
    val updateProfileData: LiveData<ResponseState<UserDataModel?>>
        get() = _updateProfileMutableLiveData


    fun getProfileData(token: String) = viewModelScope.launch {
        _getProfileDataMutableLiveData.value = profileRepository.getCurrentLoggedinUser(token)
    }

    fun updateProfileData(token: String, updateData: UpateData) = viewModelScope.launch {
        _updateProfileMutableLiveData.value = profileRepository.updateUser(token, updateData)
    }



}