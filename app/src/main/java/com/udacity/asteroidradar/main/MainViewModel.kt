package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    init {
        getAsteroidList()
    }

    private fun getAsteroidList() {
        val apiKey = getApplication<Application>().resources.getString(R.string.api_key);

        NasaApi.retrofitService.getAsteroids("2022-07-11", "2022-07-12", apiKey)
            .enqueue(object : Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    val resp = response.body().toString()
                    _response.value = resp
                }
            })
        _response.value = "Set the Nasa API Response here!"
    }
}