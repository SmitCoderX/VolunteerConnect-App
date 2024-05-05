package com.smitcoderx.volunteerconnect.Db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smitcoderx.volunteerconnect.Model.Events.DataFetch
import com.smitcoderx.volunteerconnect.Utils.Convertor

@Database(
    entities = [DataFetch::class],
    version = 2
)
@TypeConverters(Convertor::class)
abstract class VolunteerConnectDatabase : RoomDatabase() {

    abstract fun volunteerDao(): VolunteerDao

}