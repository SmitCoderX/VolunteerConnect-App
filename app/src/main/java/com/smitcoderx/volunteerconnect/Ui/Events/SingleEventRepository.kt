package com.smitcoderx.volunteerconnect.Ui.Events

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingleEventRepository @Inject constructor(private val api: VolunteerConnectApi) {

    suspend fun getEventDetails(id: String) = api.getEventsById(id)
}