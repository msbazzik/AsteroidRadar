package com.udacity.asteroidradar.work

import android.app.Application
import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import retrofit2.HttpException

class RefreshDataWorker (appContext: Context, params: WorkerParameters) :
CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidsRepository(database)
        return try {
            repository.fetchAsteroids(applicationContext.getString(R.string.api_key))
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}