package com.udacity.asteroidradar

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class PictureOfTheDay(
    val url: String,
    val title: String,
    val mediaType: String
)
