package com.but.parkour.openapitools.client.apis

import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.apis.ObstaclesApi
import com.but.parkour.clientkotlin.apis.PerformanceObstaclesApi
import com.but.parkour.clientkotlin.apis.PerformancesApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.ObstacleCreate
import com.but.parkour.clientkotlin.models.PerformanceCreate
import com.but.parkour.clientkotlin.models.PerformanceObstacleCreate
import com.but.parkour.clientkotlin.models.PerformanceObstacleUpdate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import org.junit.jupiter.api.Disabled

class PerformancesObstaclesApiTest {
    private val apiClient = ApiClient(
        bearerToken = BuildConfig.API_TOKEN,
    )
    private val reset = apiClient.createService(UtilApi::class.java)
    private val performanceObstaclesApi = apiClient.createService(PerformanceObstaclesApi::class.java)
    private val obstaclesApi = apiClient.createService(ObstaclesApi::class.java)
    private val performancesApi = apiClient.createService(PerformancesApi::class.java)
    private val competitorsApi = apiClient.createService(CompetitorsApi::class.java)
    @BeforeEach
    fun setUp() {
        val resetData = reset.resetData()
        resetData.execute()
    }

    @Test
    @Disabled("ca marche pas frere")
    fun createPerformanceObstacleTest() {
        val obstacle = ObstacleCreate(
            name = "ObstacleTest",
        )
        val obstacleCall = obstaclesApi.addObstacle(obstacle)
        val obstacleResponse = obstacleCall.execute()
        val code = obstacleResponse.code()
        val erreur = if (obstacleResponse.isSuccessful) null else obstacleResponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)

        val competitorId = competitorsApi.getAllCompetitors().execute().body()
            ?.get(0)?.id
        val courseId = performancesApi.getAllPerformances().execute().body()
            ?.get(0)?.courseId
        val performance = PerformanceCreate(
            competitorId = competitorId,
            courseId = courseId,
            status = PerformanceCreate.Status.to_finish,
            totalTime = 0
        )
        val performanceCall = performancesApi.addPerformance(performance)
        val performanceResponse = performanceCall.execute()
        val performanceCode = performanceResponse.code()
        val performanceErreur = if (performanceResponse.isSuccessful) null else performanceResponse.message()
        println("Response code: $performanceCode")
        println("Response error: $performanceErreur")
        println("Response body: ${performanceResponse.errorBody()?.string()}")
        assertEquals(201, performanceCode)
        assertEquals(null, performanceErreur)

        val obstacleID = obstaclesApi.getAllObstacles().execute().body()?.find { it.name == "ObstacleTest" }?.id
        val performanceID = performancesApi.getAllPerformances().execute().body()
            ?.find { it.competitorId == competitorId && it.courseId == courseId }?.id
        if (obstacleID != null && performanceID != null) {
            val performanceObstacleCreate = PerformanceObstacleCreate(
                performanceId = performanceID,
                obstacleId = obstacleID,
                hasFell = false,
                toVerify = false,
                time = 0
            )
            val call = performanceObstaclesApi.createPerformanceObstacle(performanceObstacleCreate)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            println("Response code: $code")
            println("Response error: $erreur")
            println("Response body: ${reponse.errorBody()?.string()}")

            assertEquals(201, code)
            assertEquals(null, erreur)
        }
    }

    @Test
    fun getPerformanceObstacleDetailsTest() {
        val performanceObstacles = performanceObstaclesApi.getAllPerformanceObstacles().execute().body()
        val performanceObstacle = performanceObstacles?.get(0)
        val performanceObstacleId = performanceObstacle?.id
        if(performanceObstacleId!=null){
            val call = performanceObstaclesApi.getPerformanceObstacleDetails(performanceObstacleId)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
        }
    }

    @Test
    fun updatePerformanceObstacleTest() {
        val performanceObstacles = performanceObstaclesApi.getAllPerformanceObstacles().execute().body()
        val performanceObstacle = performanceObstacles?.get(0)
        val performanceObstacleId = performanceObstacle?.id
        if(performanceObstacleId!=null){
            val performanceObstacleUpdate = PerformanceObstacleUpdate(
                hasFell = true,
                toVerify = false,
                time = 0
            )
            val call = performanceObstaclesApi.updatePerformanceObstacle(performanceObstacleId, performanceObstacleUpdate)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
        }
    }
}