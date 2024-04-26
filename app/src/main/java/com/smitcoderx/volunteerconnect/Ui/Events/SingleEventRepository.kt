package com.smitcoderx.volunteerconnect.Ui.Events

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Model.Requests.RequestsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingleEventRepository @Inject constructor(private val api: VolunteerConnectApi) {

    suspend fun getEventDetails(id: String) = api.getEventsById(id)
    suspend fun sendRequest(token: String, requestsData: RequestsData) =
        api.sendVolunteerRequest(token, requestsData)
}