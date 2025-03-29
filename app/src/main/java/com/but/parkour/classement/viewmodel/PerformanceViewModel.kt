package com.but.parkour.classement.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.PerformanceObstaclesApi
import com.but.parkour.clientkotlin.apis.PerformancesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.clientkotlin.models.PerformanceObstacle
import kotlinx.coroutines.launch

class PerformanceViewModel : ViewModel() {

    private val _performances = MutableLiveData<List<Performance>>()
    val performances: LiveData<List<Performance>> = _performances

    private val _performancesObstacle = MutableLiveData<List<PerformanceObstacle>>()
    val performancesObstacle: LiveData<List<PerformanceObstacle>> = _performancesObstacle

    private val apiClient = ApiClient(
        bearerToken = BuildConfig.API_TOKEN
    )

    val performancesApi = apiClient.createService(PerformancesApi::class.java)
    val performancesObstacleApi = apiClient.createService(PerformanceObstaclesApi::class.java)

    init{
        fetchPerformances()
    }

    private fun fetchPerformances() {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Fetching competitions...")

                val call = performancesApi.getAllPerformances()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("CompetitionViewModel", "Competitions received: $data")
                        _performances.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                        _performances.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _performances.postValue(emptyList())
            }
        }
    }

    private fun fetchPerformancesObstacle() {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Fetching performances by obstacle...")

                val call = performancesObstacleApi.getAllPerformanceObstacles()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("CompetitionViewModel", "Performances received: $data")
                        _performancesObstacle.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                        _performancesObstacle.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _performancesObstacle.postValue(emptyList())
            }
        }
    }


}