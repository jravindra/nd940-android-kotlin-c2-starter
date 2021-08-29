package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay
import java.sql.Date

@Entity(tableName = "asteroid")
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    val codename: String,
    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "is_potentially_hazardous")
    val isPotentiallyHazardous: Boolean
)

@Entity(tableName = "pic_of_the_day")
data class DatabasePictureOfTheDay(
    @PrimaryKey
    val url: String,

    val title: String,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

//    @ColumnInfo(name = "create_date")
//    val createdDate: Date
)

fun DatabasePictureOfTheDay.asDomainModel(): PictureOfTheDay {
    return PictureOfTheDay(
        url = this.url,
        title = this.title,
        mediaType = this.mediaType
    )
}

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}