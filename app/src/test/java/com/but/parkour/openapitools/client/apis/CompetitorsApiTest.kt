package com.but.parkour.openapitools.client.apis

import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CompetitorCreate
import com.but.parkour.clientkotlin.models.CompetitorUpdate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDate

class CompetitorsApiTest {
    private val apiClient = ApiClient(
        bearerToken = BuildConfig.API_TOKEN,
    )
    private val apiService = apiClient.createService(CompetitorsApi::class.java)
    private val reset = apiClient.createService(UtilApi::class.java)

    @BeforeEach
    fun setUp() {
        val resetData = reset.resetData()
        resetData.execute()
    }
    @Test
    fun getAllCompetitorsTest(){
        val call = apiService.getAllCompetitors()
        val reponse = call.execute()

        val code = reponse.code()
        val erreur = if(reponse.isSuccessful) null else reponse.message()
        assertEquals(200, code)
        assertEquals(null, erreur)
        println(reponse.body())
    }


    @Test
    fun addCompetitorTest(){
        val competitor = CompetitorCreate(
            firstName = "John",
            lastName = "Doe",
            email = "test@test.com",
            phone = "1234567890",
            gender = CompetitorCreate.Gender.H,
            bornAt = LocalDate.now()
        )
        val call = apiService.addCompetitor(competitor)
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if(reponse.isSuccessful) null else reponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)
    }

    @Test
    fun deleteCompetitorTest(){
        val allCompetitor = apiService.getAllCompetitors().execute().body();
        val id = allCompetitor?.get(0)?.id
        if(id != null) {
            val call = apiService.deleteCompetitor(id)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
        }else{
            println("Aucun competitor")
        }
    }

    /**
     * Attention renvoi le competiteur et une liste de performance, une perf (perf obstacle) par obstacle
     */
    @Test
    fun getCompetitorCoursePerformanceDetailsTest(){
        val allCompetitor = apiService.getAllCompetitors().execute().body();
        val idCompetitor = allCompetitor?.get(0)?.id
        println(idCompetitor)
        if(idCompetitor != null) {
            val courses = apiService.getCompetitorCourses(idCompetitor).execute().body()
            val idCourse = courses?.get(0)?.id
            println(idCourse)
            if (idCourse != null) {
                val call = apiService.getCompetitorCoursePerformanceDetails(idCompetitor, idCourse)
                val reponse = call.execute()
                val code = reponse.code()
                val erreur = if (reponse.isSuccessful) null else reponse.message()
                assertEquals(200, code)
                assertEquals(null, erreur)
                println(reponse.body()?.performances)
            } else {
                println("Aucun course")
            }
        }
    }

    @Test
    fun getCompetitorCoursesTest(){
        val allCompetitor = apiService.getAllCompetitors().execute().body();
        val idCompetitor = allCompetitor?.get(0)?.id
        if(idCompetitor != null) {
            val call = apiService.getCompetitorCourses(idCompetitor)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }else{
            println("Aucun competitor")
        }
    }

    @Test
    fun getCompetitorDetailsTest(){
        val allCompetitor = apiService.getAllCompetitors().execute().body();
        val idCompetitor = allCompetitor?.get(0)?.id
        if(idCompetitor != null) {
            val call = apiService.getCompetitorDetails(idCompetitor)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }else{
            println("Aucun competitor")
        }
    }

    /**
     * Comme pour getCompetitorCoursePerformanceDetails :
     * Attention renvoi le competiteur et une liste de performance, une perf par course
     */
    @Test
    fun getCompetitorPerformancesTest(){
        val allCompetitor = apiService.getAllCompetitors().execute().body();
        val idCompetitor = allCompetitor?.get(0)?.id
        if(idCompetitor != null) {
            println(idCompetitor)
            val call = apiService.getCompetitorPerformances(idCompetitor)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }else{
            println("Aucun competitor")
        }
    }

    @Test
    fun updateCompetitorTest(){
        val allCompetitor = apiService.getAllCompetitors().execute().body();
        val id = allCompetitor?.get(0)?.id
        println("Competiteur avant modif : " + allCompetitor?.get(0))
        if(id != null) {
            val competitor = CompetitorUpdate(
                firstName = "John",
                lastName = "Doe",
                email = "email.modif@test.test",
                phone = "1234567890",
                bornAt = LocalDate.now()
            )
            val call = apiService.updateCompetitor(id, competitor)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if (reponse.isSuccessful) null else reponse.message()

            assertEquals(200, code)
            assertEquals(null, erreur)
            val allCompetitorAfter = apiService.getAllCompetitors().execute().body();
            println("Competiteur après modif : " + allCompetitorAfter?.get(0))
        }else{
            println("Aucun competitor")
        }
    }
}