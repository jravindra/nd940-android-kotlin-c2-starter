package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfTheDay
import com.udacity.asteroidradar.api.AsteroidQueryDateParams
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

enum class FilterOptions { WEEK, TODAY, SAVED }
enum class AsteroidApiStatus { LOADING, ERROR, DONE }


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getDatabaseInstance(application).asteroidDao()
    private val asteroidRepository = AsteroidsRepository(database)
    private var filter = AsteroidsFilter(asteroidRepository.asteroids)

    private val _filteredAsteroidList = MutableLiveData<List<Asteroid>>()
    val filteredAsteroidList: LiveData<List<Asteroid>>
        get() = _filteredAsteroidList

    // Display status bar while loading the content
    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus>
        get() = _status

    var pictureOfTheDay: PictureOfTheDay? = asteroidRepository.pictureOfTheDay

    init {
        getAsteriodsData()
    }

    private fun getAsteriodsData() {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                asteroidRepository.refreshAsteroids()
                asteroidRepository.refreshPictureOfTheDay()
                pictureOfTheDay = asteroidRepository.pictureOfTheDay!!
                filter = AsteroidsFilter(asteroidRepository.asteroids)
                filter.asteroids.observeForever {
                    _filteredAsteroidList.value = filter.filteredAsteroids
                }

                _status.value = AsteroidApiStatus.DONE
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
                _filteredAsteroidList.value = ArrayList<Asteroid>()
                pictureOfTheDay = PictureOfTheDay("", "", "")
                _status.value = AsteroidApiStatus.ERROR
            }
        }
    }

    fun updateFilter(status: FilterOptions) {
        filter.option = status
        _filteredAsteroidList.value = filter.filteredAsteroids
    }


    private class AsteroidsFilter(val asteroids: LiveData<List<Asteroid>?>) {
        var option = FilterOptions.TODAY

        val filteredAsteroids: List<Asteroid>
            get() =
                if (asteroids.value != null) {
                    when (option) {
                        FilterOptions.WEEK -> asteroids.value!!.filterWeek()
                        FilterOptions.TODAY -> asteroids.value!!.filterToday()
                        else -> asteroids.value!!
                    }
                } else {
                    listOf<Asteroid>()
                }


        private fun List<Asteroid>.filterToday(): List<Asteroid> {
            return this.filter {
                it.closeApproachDate == AsteroidQueryDateParams.dateFormat.format(Date())
            }
        }

        private fun List<Asteroid>.filterWeek(): List<Asteroid> {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, 6)
            val endOfWeek = cal.time
            val today = Date()
            return this.filter {
                val approchDate = requireNotNull(AsteroidQueryDateParams.dateFormat.parse(it.closeApproachDate))
                (approchDate >= today) && (approchDate <= endOfWeek)
            }
        }
    }

}