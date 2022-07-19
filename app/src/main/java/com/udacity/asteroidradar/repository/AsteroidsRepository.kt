package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.NasaDatabaseFilter
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AsteroidsRepository(private val database: AsteroidDatabase) {
    private val startDay: String
        get() {
            val current = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
            return current.format(formatter)
        }

    private val endDay: String
        get() {
            val current = LocalDate.now().plusDays(Constants.DEFAULT_END_DATE_DAYS.toLong())
            val formatter = DateTimeFormatter.ofPattern(Constants.API_QUERY_DATE_FORMAT)
            return current.format(formatter)
        }

    fun getAsteroidsList(filter: NasaDatabaseFilter): LiveData<List<Asteroid>> {
        return when (filter) {
            NasaDatabaseFilter.WEEK_ASTEROIDS -> database.databaseDao.getAsteroids(startDay, endDay)
            NasaDatabaseFilter.TODAY_ASTEROIDS -> database.databaseDao.getAsteroids(startDay)
            else -> database.databaseDao.getAsteroids()
        }
    }

    suspend fun fetchAsteroids(apiKey: String) {
        val response = NasaApi.retrofitScalarsService.getAsteroids(
            startDay, endDay, apiKey
        )
        val parsedAsteroid =
            parseAsteroidsJsonResult(JSONObject(response))
        database.databaseDao.insertAll(parsedAsteroid)
    }
}