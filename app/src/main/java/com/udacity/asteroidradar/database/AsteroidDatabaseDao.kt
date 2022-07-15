package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

enum class NasaDatabaseFilter(val value: String) {
    TODAY_ASTEROIDS("today"), WEEK_ASTEROIDS("week"), ALL_ASTEROIDS("all")
}

@Dao
interface AsteroidDatabaseDao {

    @Query("SELECT * FROM asteroid_table ORDER BY close_approach_date asc")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date >= (:startDate) " +
            "AND close_approach_date <= (:endDate) ORDER BY close_approach_date asc")
    fun getAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroid_table WHERE close_approach_date = (:today) ORDER BY close_approach_date asc")
    fun getAsteroids(today: String): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: ArrayList<Asteroid>)

}