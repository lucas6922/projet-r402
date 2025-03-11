package com.but.parkour.openapitools.client.apis

import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.infrastructure.Serializer
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.squareup.moshi.Moshi
import org.junit.Test
import kotlin.test.assertEquals

class CompetiionsApiTest {
    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM",
    )
    private val apiService = apiClient.createService(CompetitionsApi::class.java)


    @Test
    fun getAllCompetitionsTest(){
        val call = apiService.getAllCompetitions()
        val reponse = call.execute()

        val code = reponse.code()
        val erreur = if(reponse.isSuccessful) null else reponse.message()
        assertEquals(200, code)
        assertEquals(null, erreur)
        println(reponse.body())
    }

    @Test
    fun addCompetitionTest(){
        val competition = CompetitionCreate(
            name = "Competition de test",
            ageMin = 18,
            ageMax = 25,
            gender = "H",
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
    }
}