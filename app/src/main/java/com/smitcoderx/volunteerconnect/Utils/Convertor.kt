package com.smitcoderx.volunteerconnect.Utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Convertor {
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType= object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromDouble(value: String?): List<Double> {
        val listType= object : TypeToken<List<Double?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromDoubleArrayList(list: List<Double?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}