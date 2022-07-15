package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase) {

    val asteroidsList = database.databaseDao.getAsteroids()

    suspend fun fetchAsteroids(apiKey: String) {
        val response = NasaApi.retrofitService.getAsteroids(
            "2022-07-15", "2022-07-22", apiKey
        )
        val parsedAsteroid =
            parseAsteroidsJsonResult(JSONObject(response))
        database.databaseDao.insertAll(parsedAsteroid)
    }
}