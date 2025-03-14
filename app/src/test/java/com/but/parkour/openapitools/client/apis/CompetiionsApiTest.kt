package com.but.parkour.openapitools.client.apis

import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.ObstaclesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.infrastructure.Serializer
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.CompetitionCreate.Gender
import com.but.parkour.clientkotlin.models.ObstacleCreate
import com.squareup.moshi.Moshi
import junit.framework.TestCase.assertEquals
import org.junit.Test


class CompetiionsApiTest {
    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM",
    )
    private val apiService = apiClient.createService(CompetitionsApi::class.java)
    private val apiService1 = apiClient.createService(ObstaclesApi::class.java)


    @Test
    fun getAllCompetitionsTest(){
        val call = apiService.getAllCompetitions()
        val reponse = call.execute()

        val code = reponse.code()
        val erreur = if(reponse.isSuccessful) null else reponse.message()
        assertEquals(200, code)
        assertEquals(null, erreur)
        println(reponse.body())

        val callCompet = apiService.getCompetitionDetails(1)
        val reponseCompet = callCompet.execute()
        val codeCompet = reponseCompet.code()
        println(reponseCompet.body())
    }

    @Test
    fun addCompetitionTest(){
        val competition = CompetitionCreate(
            name = "Competition de test 2",
            ageMin = 18,
            ageMax = 25,
            gender = Gender.H,
            hasRetry = false
        )
        try{
            val call = apiService.addCompetition(competition)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(201, code)
            assertEquals(null, erreur)
        }catch (E: Exception){
            println(E.message)
        }

//        val obstacle = ObstacleCreate(
//            name = "obstacle de test",
//        )
//
//        try{
//            val call = apiService1.addObstacle(obstacle)
//            val reponse = call.execute()
//            val code = reponse.code()
//            val erreur = if(reponse.isSuccessful) null else reponse.message()
//            assertEquals(201, code)
//            assertEquals(null, erreur)
//        }catch (E: Exception){
//            println(E.message)
//        }
    }
}