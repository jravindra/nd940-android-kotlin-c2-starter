package com.udacity.asteroidradar.repository

import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidQueryDateParams
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidsRepository(private val database: AsteroidDatabase) {

    var asteroids = Transformations.map(database.asteroidDao().getAsteroids()) { it?.asDomainModel() }
    var pictureOfTheDay = Transformations.map(database.asteroidDao().getPicOfTheDay()) { it?.asDomainModel() }


    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val (startDate, endDate) = AsteroidQueryDateParams.getDateParams()
                val asteroidsJSON =
                    AsteroidApi.asteroidsService.getAstroids(startDate, endDate, Constants.API_KEY)
                val asteroidsNetwork = parseAsteroidsJsonResult(JSONObject(asteroidsJSON))
                database.asteroidDao().insertAll(*asteroidsNetwork.asDatabaseModel())
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfTheDayNetwork =
                    AsteroidApi.pictureOfTheDayService.getPictureOfTheDay(Constants.API_KEY)
                with(pictureOfTheDayNetwork) {
                    if (this.mediaType == "image") {
                        database.asteroidDao().insertPicOfTheDay(pictureOfTheDayNetwork.asDatabaseModel())
                        database.asteroidDao().deletePicOfTheDay(pictureOfTheDayNetwork.url)
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}