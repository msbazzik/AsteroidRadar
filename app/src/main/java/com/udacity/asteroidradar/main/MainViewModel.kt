package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    init {
        viewModelScope.launch {
            refreshAsteroidList()
        }
    }

    suspend fun refreshAsteroidList() {
        val apiKey = getApplication<Application>().resources.getString(R.string.api_key);
        try {
            val response = NasaApi.retrofitService.getAsteroids(
                "2022-07-13", "2022-07-20", apiKey
            )
            val parsedAsteroid =
                parseAsteroidsJsonResult(JSONObject(response))
            _asteroidList.value = parsedAsteroid
        } catch (e: Exception) {
            _asteroidList.value = ArrayList()
        }
    }
}