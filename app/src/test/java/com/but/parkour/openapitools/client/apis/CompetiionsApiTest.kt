package com.but.parkour.openapitools.client.apis

import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.AddCompetitorRequest
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.CompetitionCreate.Gender
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import com.but.parkour.clientkotlin.models.CompetitorCreate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDate


class CompetiionsApiTest {
    private val apiClient = ApiClient(
        bearerToken = BuildConfig.API_TOKEN,
    )
    private val apiService = apiClient.createService(CompetitionsApi::class.java)
    private val competitorApi = apiClient.createService(CompetitorsApi::class.java)
    private val reset = apiClient.createService(UtilApi::class.java)

    @BeforeEach
    fun setUp() {
        val resetData = reset.resetData()
        resetData.execute()
    }

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
    }

    @Test
    fun delCompetitionTest(){
        val allCompet = apiService.getAllCompetitions().execute().body();
        val id = allCompet?.get(0)?.id
        if(id != null){
            val delCompet = apiService.deleteCompetition(id);
            val reponse = delCompet.execute();

            val code = reponse.code();
            val erreur = if(reponse.isSuccessful) null else reponse.message()

            assertEquals(200,code)
            assertEquals(null, erreur)
        }else{
            println("aucune competition")
        }
    }

    @Test
    fun getCompetitionCoursesTest(){
        val allCompet = apiService.getAllCompetitions().execute().body();
        val id = allCompet?.get(0)?.id
        if(id != null){
            val call = apiService.getCompetitionCourses(id)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }else{
            println("aucune competition")
        }
    }

    @Test
    fun getCompetitionInscriptionsTest(){
        val allCompet = apiService.getAllCompetitions().execute().body();
        val id = allCompet?.get(0)?.id
        if(id != null){
            val call = apiService.getCompetitionInscriptions(id)
            val response = call.execute()
            val code = response.code()
            val erreur = if(response.isSuccessful) null else response.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(response.body())
        }else{
            println("aucune competition")
        }
    }

    @Test
    fun updateCompetitionTest(){
        val allCompet = apiService.getAllCompetitions().execute().body()
        println("toutes competition avant modification : " + allCompet)
        val id = allCompet?.get(0)?.id
        if(id != null){
            val competition = CompetitionUpdate(
                name = "Competition modifiée",
                ageMin = 18,
                ageMax = 20,
                gender = CompetitionUpdate.Gender.F,
                hasRetry = false,
                status = CompetitionUpdate.Status.not_started
            )
            val call = apiService.updateCompetition(id, competition)

            val response = call.execute()
            val code = response.code()
            val erreur = if(response.isSuccessful) null else response.message()

            assertEquals(200, code)
            assertEquals(null, erreur)

            val allCompet = apiService.getAllCompetitions().execute().body();
            println("toutes competition après modification : " + allCompet)
        }else{
            println("aucune competition")
        }
    }


    @Test
    fun getCompetitionDetailsTest(){
        val allCompet = apiService.getAllCompetitions().execute().body();
        val id = allCompet?.get(0)?.id
        if(id != null){
            val call = apiService.getCompetitionDetails(id)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }else{
            println("aucune competition")
        }
    }


    @Test
    fun addCompetitorTest() {
        val competitor = CompetitorCreate(
            firstName = "Jean",
            lastName = "Dupont",
            email = "test@test.fr",
            phone = "0606060606",
            gender = CompetitorCreate.Gender.H,
            bornAt = LocalDate.now()
        )
        val call = competitorApi.addCompetitor(competitor)
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else reponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)

        val competitorId = competitorApi.getAllCompetitors().execute().body()
            ?.find { it.firstName == "Jean" && it.lastName == "Dupont" }?.id

        if(competitorId != null){
            val competitorId = AddCompetitorRequest(
                competitorId = competitorId
            )

            val competition = CompetitionCreate(
                name = "Competition de test pour ajout competitor",
                ageMin = 18,
                ageMax = 25,
                gender = Gender.H,
                hasRetry = false
            )
            val callCompet = apiService.addCompetition(competition)
            val reponseCompet = callCompet.execute()
            val codeCompet = reponseCompet.code()
            val erreurCompet = if (reponseCompet.isSuccessful) null else reponseCompet.message()
            assertEquals(201, codeCompet)
            assertEquals(null, erreurCompet)

            val competId = apiService.getAllCompetitions().execute().body()
                ?.find { it.name == "Competition de test pour ajout competitor" }?.id
            println("competId : $competId")
            println("competitorId : $competitorId")
            if (competId != null) {
                val call = apiService.addCompetitor(competId, competitorId)
                val reponse = call.execute()
                val code = reponse.code()
                val erreur = if (reponse.isSuccessful) null else reponse.message()
                println("code : $code")
                println("erreur : $erreur")
                println("body : ${reponse.errorBody()?.string()}")
                assertEquals(201, code)
                assertEquals(null, erreur)
            }
        }


    }

}