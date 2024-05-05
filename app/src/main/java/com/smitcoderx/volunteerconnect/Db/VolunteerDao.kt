package com.smitcoderx.volunteerconnect.Db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch

@Dao
interface VolunteerDao {

    @Query("SELECT * FROM Events")
    fun getAllEvents(): LiveData<List<DataFetch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DataFetch)

    @Update
    suspend fun update(data: DataFetch)

    @Delete
    suspend fun delete(data: DataFetch)

}