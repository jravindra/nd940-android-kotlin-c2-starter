package com.udacity.asteroidradar.repository

import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.AsteroidQueryDateParams
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteriodDao
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidsRepository(private val databaseDao: AsteriodDao) {

    var asteroids = Transformations.map(databaseDao.getAsteroids()) { it?.asDomainModel() }
    var pictureOfTheDay = databaseDao.getPicOfTheDay().value?.asDomainModel()

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val (startDate, endDate) = AsteroidQueryDateParams.getDateParams()
                val asteroidsJSON = AsteroidApi.asteroidsService.getAstroids(startDate, endDate, Constants.API_KEY)
                val asteroidsNetwork = parseAsteroidsJsonResult(JSONObject(asteroidsJSON))
                databaseDao.insertAll(*asteroidsNetwork.asDatabaseModel())
                asteroids = Transformations.map(databaseDao.getAsteroids()) { it?.asDomainModel() }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val pictureOfTheDayNetwork = AsteroidApi.pictureOfTheDayService.getPictureOfTheDay(Constants.API_KEY)
                with(pictureOfTheDayNetwork) {
                    if (this.mediaType == "image") {
                        databaseDao.insertPicOfTheDay(pictureOfTheDayNetwork.asDatabaseModel())
                        databaseDao.deletePicOfTheDay(pictureOfTheDayNetwork.url)
                        pictureOfTheDay = pictureOfTheDayNetwork.asDomainModel()
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}