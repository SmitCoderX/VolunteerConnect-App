package com.smitcoderx.volunteerconnect.Ui.ParticipatedEvents

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParticipatedEventsRepository @Inject constructor(private val api: VolunteerConnectApi) {

    suspend fun getEventList() = api.getEventsList()
}