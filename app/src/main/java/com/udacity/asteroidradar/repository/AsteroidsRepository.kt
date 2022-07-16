package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.NasaDatabaseFilter
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {

    fun getAsteroidsList(filter: NasaDatabaseFilter): LiveData<List<Asteroid>> {
        return when (filter) {
            NasaDatabaseFilter.WEEK_ASTEROIDS -> database.databaseDao.getAsteroids("2022-07-15", "2022-07-22")
            NasaDatabaseFilter.TODAY_ASTEROIDS -> database.databaseDao.getAsteroids("2022-07-15")
            else -> database.databaseDao.getAsteroids()
        }
    }

    suspend fun fetchAsteroids(apiKey: String) {
        val response = NasaApi.retrofitScalarsService.getAsteroids(
            "2022-07-15", "2022-07-22", apiKey
        )
        val parsedAsteroid =
            parseAsteroidsJsonResult(JSONObject(response))
        database.databaseDao.insertAll(parsedAsteroid)
    }
}