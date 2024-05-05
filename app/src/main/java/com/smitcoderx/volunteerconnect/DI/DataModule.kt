package com.smitcoderx.volunteerconnect.DI

import android.app.Application
import androidx.room.Room
import com.smitcoderx.volunteerconnect.Db.VolunteerConnectDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) =
        Room.databaseBuilder(app, VolunteerConnectDatabase::class.java, "events_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideVolunteerDao(db: VolunteerConnectDatabase) =
        db.volunteerDao()
}