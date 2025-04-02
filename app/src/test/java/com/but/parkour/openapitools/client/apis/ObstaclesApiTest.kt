package com.but.parkour.openapitools.client.apis

import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.ObstaclesApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.ObstacleCreate
import com.but.parkour.clientkotlin.models.ObstacleUpdate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class ObstaclesApiTest {
    private val apiClient = ApiClient(
        bearerToken = BuildConfig.API_TOKEN,
    )
    private val reset = apiClient.createService(UtilApi::class.java)
    private val obstacleApi = apiClient.createService(ObstaclesApi::class.java)

    @BeforeEach
    fun setUp() {
        val resetData = reset.resetData()
        resetData.execute()
    }

    @Test
    fun addObstacleTest() {
        val obstacleCreate = ObstacleCreate(
            name = "Obstacle test",
        )
        val call = obstacleApi.addObstacle(obstacleCreate)
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else reponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)
    }

    @Test
    fun deleteObstacleTest() {
        val obstacleCreate = ObstacleCreate(
            name = "Obstacle test",
        )
        val call = obstacleApi.addObstacle(obstacleCreate)
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else reponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)

        val obstacles = obstacleApi.getAllObstacles().execute().body()
        val obstacle = obstacles?.find { it.name == "Obstacle test" }
        val obstacleId = obstacle?.id
        if(obstacleId!=null){
            val callDelete = obstacleApi.deleteObstacle(obstacleId)
            val reponseDelete = callDelete.execute()
            val codeDelete = reponseDelete.code()
            val erreurDelete = if (reponseDelete.isSuccessful) null else reponseDelete.message()
            assertEquals(200, codeDelete)
            assertEquals(null, erreurDelete)
        }
    }

    @Test
    fun getAllObstaclesTest() {
        println("-------------------")
        println("getAllObstaclesTest")
        val call = obstacleApi.getAllObstacles()
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else reponse.message()
        assertEquals(200, code)
        assertEquals(null, erreur)
        println(reponse.body())
        println("-------------------")
    }

    @Test
    fun getObstacleDetailsTest(){
        println("-------------------")
        println("getObstacleDetailsTest")
        val obstacleId = obstacleApi.getAllObstacles().execute()
            .body()?.get(0)?.id
        if(obstacleId!=null){
            val call = obstacleApi.getObstacleDetails(obstacleId)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }else{
            println("Aucun obstacles")
        }
        println("-------------------")
    }

    @Test
    fun updateObstacleTest(){
        println("-------------------")
        println("updateObstacleTest")
        val obstacleCreate = ObstacleCreate(
            name = "Obstacle test",
        )
        val call = obstacleApi.addObstacle(obstacleCreate)
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else reponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)
        println("Obstacle ajouté")

        val obstacles = obstacleApi.getAllObstacles().execute().body()
        val obstacle = obstacles?.find { it.name == "Obstacle test" }
        println("obstacle ava,t modif : " + obstacle)
        val obstacleId = obstacle?.id
        if(obstacleId!=null){

            val obstacleUpdate = ObstacleUpdate(
                name = "Obstacle test update",
            )
            val callUpdate = obstacleApi.updateObstacle(obstacleId, obstacleUpdate)
            val reponseUpdate = callUpdate.execute()
            val codeUpdate = reponseUpdate.code()
            val erreurUpdate = if (reponseUpdate.isSuccessful) null else reponseUpdate.message()
            assertEquals(200, codeUpdate)
            assertEquals(null, erreurUpdate)
            println("Obstacle modifié")
            val obstacleUpdated = obstacleApi.getObstacleDetails(obstacleId).execute().body()
            println("obstacle après modif : " + obstacleUpdated)
        }
        println("-------------------")
    }
}

