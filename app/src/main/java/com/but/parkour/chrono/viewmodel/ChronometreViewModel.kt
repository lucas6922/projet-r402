// ChronometreViewModel.kt
package com.but.parkour.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.apis.PerformanceObstaclesApi
import com.but.parkour.clientkotlin.apis.PerformancesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.clientkotlin.models.PerformanceCreate
import com.but.parkour.clientkotlin.models.PerformanceObstacleCreate
import kotlinx.coroutines.launch

class ChronometreViewModel : ViewModel() {
    private val _obstacles = MutableLiveData<List<CourseObstacle>>()
    val obstacles: LiveData<List<CourseObstacle>> = _obstacles

    private val _performances = MutableLiveData<List<Performance>>()
    val performances: LiveData<List<Performance>> = _performances

    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM"
    )

    private val courseApi = apiClient.createService(CoursesApi::class.java)
    private val perfApi = apiClient.createService(PerformancesApi::class.java)
    private val perfObstacleApi = apiClient.createService(PerformanceObstaclesApi::class.java)

    fun fetchObstacles(parkourId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ChronometreViewModel", "Fetching obstacles for parkour $parkourId...")
                val call = courseApi.getCourseObstacles(parkourId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("ChronometreViewModel", " course id: $parkourId")
                        Log.d("ChronometreViewModel", "Obstacles received: $data")
                        _obstacles.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ChronometreViewModel", "Error: $errorMessage")
                        _obstacles.postValue(emptyList())
                    }
                )
            } catch (e: Exception) {
                Log.e("ChronometreViewModel", "Exception: ${e.message}", e)
                _obstacles.postValue(emptyList())
            }
        }
    }

    fun fetchAllPerformances() {
        viewModelScope.launch {
            try {
                Log.d("ChronometreViewModel", "Fetching all performances...")
                val call = perfApi.getAllPerformances()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("ChronometreViewModel", "Performances received: $data")
                        _performances.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("ChronometreViewModel", "Error: $errorMessage")
                        _performances.postValue(emptyList())
                    }
                )
            } catch (e: Exception) {
                Log.e("ChronometreViewModel", "Exception: ${e.message}", e)
                _performances.postValue(emptyList())
            }
        }
    }



    fun addPerformance(performance: PerformanceCreate) {
        viewModelScope.launch {
            try {
                Log.d("ChronometreViewModel", "Adding performance...")
                Log.d("PerformanceCreate", performance.toString())

                val call = perfApi.addPerformance(performance)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ChronometreViewModel", "Performance added: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ChronometreViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ChronometreViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun addPerformanceObstacle(performance: PerformanceObstacleCreate) {
        viewModelScope.launch {
            try {
                Log.d("ChronometreViewModel", "Adding PerformanceObstacle ...")

                val call = perfObstacleApi.createPerformanceObstacle(performance)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ChronometreViewModel", "PerformanceObstacle added: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ChronometreViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ChronometreViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}
