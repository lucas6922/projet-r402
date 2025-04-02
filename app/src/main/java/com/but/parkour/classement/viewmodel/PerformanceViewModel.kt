package com.but.parkour.classement.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.apis.PerformanceObstaclesApi
import com.but.parkour.clientkotlin.apis.PerformancesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.clientkotlin.models.PerformanceObstacle
import com.but.parkour.concurrents.viewmodel.CompetitorViewModel
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
    val competitorApi = apiClient.createService(CompetitorsApi::class.java)

    init {
        fetchPerformances()
        fetchPerformancesObstacle()
    }

    private fun fetchPerformances() {
        viewModelScope.launch {
            try {
                Log.d("PerformanceViewModel", "Fetching performances...")

                val call = performancesApi.getAllPerformances()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("PerformanceViewModel", "Performances received: $data")
                        _performances.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, _ ->
                        Log.e("PerformanceViewModel", "Error: $errorMessage")
                        _performances.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("PerformanceViewModel", "Exception: ${e.message}", e)
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

    private fun findCompetitorById(id: Int) : Competitor? {
        var competitor : Competitor? = null
        viewModelScope.launch {
            try {
                Log.d("PerformanceViewModel", "Fetching competitor by id...")

                val call = competitorApi.getCompetitorDetails(id)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, _ ->
                        Log.d("PerformanceViewModel", "Competitor received: $data")
                        competitor = data
                    },
                    onError = { errorMessage, _ ->
                        Log.e("PerformanceViewModel", "Error: $errorMessage")
                        competitor = null
                    }
                )

            } catch (e: Exception) {
                Log.e("PerformanceViewModel", "Exception: ${e.message}", e)
                competitor = null
            }
        }
        return competitor
    }


    suspend fun filterCompetitorsWithPerformance(parkour: Course): Map<Competitor, Int> {
        val classement: MutableMap<Competitor, Int> = mutableMapOf()
        val performancesList = performances.value
            ?.filter { it.courseId == parkour.id }
            ?.sortedBy { it.totalTime }
        val competitorViewModel = CompetitorViewModel()

        val competitors = competitorViewModel.fetchAllCompetitors()

        Log.d("PerformanceViewModel", "Competitors received: $competitors")

        performancesList?.forEach {
            val competitor = competitors?.firstOrNull { competitor -> competitor.id == it.competitorId }
            Log.d("PerformanceViewModel", "Competitor received: $competitor")
            if (competitor != null) {
                classement[competitor] = it.totalTime!!
            }
        }

        return classement
    }

    suspend fun filterCompetitorsWithCompetition(listeParkours: List<Course>): Map<Competitor, Int> {

        val classement: MutableMap<Competitor, Int> = mutableMapOf()
        val performancesList : MutableList<Performance> = mutableListOf()
        listeParkours.forEach { parkour ->
            Log.d("PerformanceViewModel", "Parkour: $parkour")
            performancesList.addAll(performances.value?.filter { it.courseId == parkour.id }!!)
        }

        Log.d("PerformanceViewModel", "PerformancesList: $performancesList")

        val totalTimes = mutableMapOf<Int, Int>()


        performancesList.forEach{
            if (totalTimes.containsKey(it.competitorId)) {
                totalTimes[it.competitorId!!] = totalTimes[it.competitorId]!! + it.totalTime!!
            } else {
                totalTimes[it.competitorId!!] = it.totalTime!!
            }
        }

        Log.d("PerformanceViewModel", "Total times: $totalTimes")

        val sortedTimes = totalTimes.toList().sortedBy { (_, value) -> value }.toMap()


        val competitorViewModel = CompetitorViewModel()

        val competitors = competitorViewModel.fetchAllCompetitors()

        Log.d("PerformanceViewModel", "Competitors received: $competitors")

        sortedTimes.forEach() {
            val competitor = competitors?.firstOrNull { competitor -> competitor.id == it.key }
            Log.d("PerformanceViewModel", "Competitor received: $competitor")
            if (competitor != null) {
                classement[competitor] = it.value
            }
        }

        return classement
    }

    suspend fun filterCompetitorsWithObstacle(
        obstacle: CourseObstacle
    ): Map<Competitor, Int> {

        //recuperer les performances obstacles sur un pobstacle donné dans une course donnée

        //pour chaque performances obstacle obtenir le competitor

        //renvoyer le competitor et son temps


        val classement: MutableMap<Competitor, Int> = mutableMapOf()
        val performancesList = performancesObstacle.value
            ?.filter{ it.obstacleId == obstacle.obstacleId }
            ?.sortedBy { it.time }
        val competitorViewModel = CompetitorViewModel()

        val competitors = competitorViewModel.fetchAllCompetitors()

        Log.d("PerformanceViewModel", "Competitors received: $competitors")
        Log.v("Performance Obstacle","List size : ${performancesList?.size}")

        performancesList?.forEach {
            val competitor = competitors?.firstOrNull { competitor -> competitor.id ==  2427}
            Log.d("PerformanceViewModel", "Competitor received: $competitor")
            if (competitor != null) {
                classement[competitor] = it.time!!
            }
        }

        return classement
    }

}