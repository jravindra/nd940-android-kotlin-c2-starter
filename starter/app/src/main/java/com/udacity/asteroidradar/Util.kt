package com.udacity.asteroidradar

import android.content.res.Resources
import android.icu.text.SimpleDateFormat
import androidx.room.TypeConverter
import java.util.*
import java.util.concurrent.TimeUnit

class Util {

    /**
     * These functions create a formatted string that can be set in a TextView.
     */
    private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
    private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

    /**
     * Convert a duration to a formatted string for display.
     *
     * Examples:
     *
     * 6 seconds on Wednesday
     * 2 minutes on Monday
     * 40 hours on Thursday
     *
     * @param startTimeMilli the start of the interval
     * @param endTimeMilli the end of the interval
     * @param res resources used to load formatted strings
     */
    fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
        val durationMilli = endTimeMilli - startTimeMilli
        val weekdayString = SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)
        return when {
            durationMilli < ONE_MINUTE_MILLIS -> {
                val seconds = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
                res.getString(R.string.seconds_length, seconds, weekdayString)
            }
            durationMilli < ONE_HOUR_MILLIS -> {
                val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
                res.getString(R.string.minutes_length, minutes, weekdayString)
            }
            else -> {
                val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
                res.getString(R.string.hours_length, hours, weekdayString)
            }
        }
    }

    class Converters {
        @TypeConverter
        fun fromTimestamp(value: Long?): Date? {
            return value?.let { Date(it) }
        }

        @TypeConverter
        fun dateToTimestamp(date: Date?): Long? {
            return date?.time?.toLong()
        }
    }
}