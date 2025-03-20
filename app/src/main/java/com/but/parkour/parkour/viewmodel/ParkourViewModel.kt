package com.but.parkour.parkour.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseCreate
import kotlinx.coroutines.launch

class ParkourViewModel : ViewModel() {
    private val _parkours = MutableLiveData<List<Course>>()

    val parkours: LiveData<List<Course>> = _parkours

    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM"
    )

    private val competitionApi = apiClient.createService(CompetitionsApi::class.java)
    private val courseApi = apiClient.createService(CoursesApi::class.java)

    fun fetchCourses(competitionId : Int) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Fetching courses...")

                val call = competitionApi.getCompetitionCourses(competitionId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ParkourViewModel", "courses received: $data")
                        _parkours.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ParkourViewModel", "Error: $errorMessage")
                        _parkours.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Exception: ${e.message}", e)
                _parkours.postValue(emptyList())
            }
        }
    }

    fun addCourse(course: CourseCreate) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Adding course...")

                val call = courseApi.addCourse(course)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ParkourViewModel", "course added: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ParkourViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun removeCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                Log.d("ParkourViewModel", "Removing course...")

                val call = courseApi.deleteCourse(courseId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("ParkourViewModel", "course removed: $data")
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("ParkourViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("ParkourViewModel", "Exception: ${e.message}", e)
            }
        }
    }
}