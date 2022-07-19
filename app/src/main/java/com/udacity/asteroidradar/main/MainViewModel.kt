package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.NasaDatabaseFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import java.util.*

enum class MainApiStatus { LOADING, ERROR, DONE }

class MainViewModel(app: Application) : AndroidViewModel(app) {
    val apiKey = getApplication<Application>().resources.getString(R.string.api_key);

    private val _status = MutableLiveData<MainApiStatus>()
    val status: LiveData<MainApiStatus>
        get() = _status

    private val _filter = MutableLiveData<NasaDatabaseFilter>(NasaDatabaseFilter.ALL_ASTEROIDS)

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val database = AsteroidDatabase.getInstance(app)
    private val asteroidsRepository = AsteroidsRepository(database)

    var asteroidList = Transformations.switchMap(_filter) { filter ->
        asteroidsRepository
            .getAsteroidsList(filter)
    }

    var errorMessage: String = ""

    init {
        viewModelScope.launch {
            refreshAsteroid()
        }
    }

    suspend fun refreshAsteroid() {
        _status.value = MainApiStatus.LOADING
        try {
            asteroidsRepository.fetchAsteroids(apiKey)
            _pictureOfDay.value = NasaApi.retrofitMoshiService.getImageOfTheDay(apiKey)
            _status.value = MainApiStatus.DONE
        } catch (e: Exception) {
            errorMessage = e.message.toString()
            _status.value = MainApiStatus.ERROR
        }
    }

    fun updateFilter(filter: NasaDatabaseFilter) {
        _filter.value = filter
    }

    fun onErrorShown() {
        _status.value = MainApiStatus.DONE
    }
}