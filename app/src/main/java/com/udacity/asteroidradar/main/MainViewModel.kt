package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    init {
        refreshAsteroidList()
    }

    private fun refreshAsteroidList() {
        val apiKey = getApplication<Application>().resources.getString(R.string.api_key);

        NasaApi.retrofitService.getAsteroids("2022-07-12", "2022-07-19", apiKey)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {

                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val parsedAsteroid =
                        parseAsteroidsJsonResult(JSONObject(response.body().toString()))
                    _asteroidList.value = parsedAsteroid
                }
            })
    }
}