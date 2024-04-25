package com.smitcoderx.volunteerconnect.Ui.EventsCreation

import android.content.Context
import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Model.Events.Data
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    @ApplicationContext private val context: Context
) {

    suspend fun createEvent(token: String, data: Data) = api.createEvent(token, data)


}