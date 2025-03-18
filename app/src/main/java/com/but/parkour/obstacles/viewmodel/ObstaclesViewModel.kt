package com.but.parkour.obstacles.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CourseObstacle
import kotlinx.coroutines.launch

class ObstaclesViewModel : ViewModel() {
    private val _obstacles = MutableLiveData<List<CourseObstacle>>()

    val obstacles: LiveData<List<CourseObstacle>> = _obstacles

    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM"
    )

    private val courseApi = apiClient.createService(CoursesApi::class.java)

    fun fetchCoursesObstacles(parkourId : Int) {
        viewModelScope.launch {
            try {
                Log.d("ObstaclesViewModel", "Fetching courses...")

                val call = courseApi.getCourseObstacles(parkourId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ParkourViewModel", "courses received: $data")
                        _obstacles.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ParkourViewModel", "Error: $errorMessage")
                        _obstacles.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Exception: ${e.message}", e)
                _obstacles.postValue(emptyList())
            }
        }
    }
}