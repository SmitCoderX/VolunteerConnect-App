package com.smitcoderx.volunteerconnect.Ui.Categories

import com.smitcoderx.volunteerconnect.API.VolunteerConnectApi
import com.smitcoderx.volunteerconnect.Db.VolunteerConnectDatabase
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepository @Inject constructor(
    private val api: VolunteerConnectApi,
    private val db: VolunteerConnectDatabase
) {

    suspend fun getEventsList() = api.getEventsList()

    suspend fun insert(data: DataFetch) = db.volunteerDao().insert(data)
    suspend fun delete(data: DataFetch) = db.volunteerDao().delete(data)
    suspend fun update(data: DataFetch) = db.volunteerDao().update(data)


}