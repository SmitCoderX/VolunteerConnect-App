package com.smitcoderx.volunteerconnect.Ui.Events

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Db.VolunteerConnectDatabase
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.Model.Requests.RequestsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SingleEventRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    private val db: VolunteerConnectDatabase
) {

    suspend fun getEventDetails(id: String) = api.getEventsById(id)
    suspend fun sendRequest(token: String, requestsData: RequestsData) =
        api.sendVolunteerRequest(token, requestsData)

    suspend fun insert(data: DataFetch) = db.volunteerDao().insert(data)
    suspend fun delete(data: DataFetch) = db.volunteerDao().delete(data)
    suspend fun update(data: DataFetch) = db.volunteerDao().update(data)
}