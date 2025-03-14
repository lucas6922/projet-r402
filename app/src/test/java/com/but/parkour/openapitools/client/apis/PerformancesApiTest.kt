package com.but.parkour.openapitools.client.apis

import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.apis.PerformancesApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.CompetitorCreate
import com.but.parkour.clientkotlin.models.CompetitorCreate.Gender
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.clientkotlin.models.PerformanceCreate
import com.but.parkour.clientkotlin.models.PerformanceCreate.Status
import com.but.parkour.clientkotlin.models.PerformanceUpdate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.Test
import java.time.LocalDate

class PerformancesApiTest {
    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM",
    )

    private val reset = apiClient.createService(UtilApi::class.java)
    private val performancesApi = apiClient.createService(PerformancesApi::class.java)
    private val competitorsApi = apiClient.createService(CompetitorsApi::class.java)
    private val courseApi = apiClient.createService(CoursesApi::class.java)
    private val competitionApi = apiClient.createService(CompetitionsApi::class.java)

    @BeforeEach
    fun setUp() {
        val resetData = reset.resetData()
        resetData.execute()
    }

    @Test
    fun getAllPerformancesTest() {
        val call = performancesApi.getAllPerformances()
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else reponse.message()
        assertEquals(200, code)
        assertEquals(null, erreur)
    }

    @Test
    fun addPerformancesTest(){
        val competitor = CompetitorCreate(
            firstName = "John",
            lastName = "Doe",
            email = "test@test.com",
            phone = "1234567890",
            gender = Gender.H,
            bornAt = LocalDate.now()
        )
        val call = competitorsApi.addCompetitor(competitor)
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if(reponse.isSuccessful) null else reponse.message()
        assertEquals(201, code)
        assertEquals(null, erreur)

        val competitorId = competitorsApi.getAllCompetitors().execute().body()
            ?.find { it.firstName == "John" }?.id

        val competition = CompetitionCreate(
            name = "Competition de test",
            ageMin = 18,
            ageMax = 25,
            gender = CompetitionCreate.Gender.H,
            hasRetry = false
        )
        val callCompet = competitionApi.addCompetition(competition)
        val reponseCompet = callCompet.execute()
        val codeCompet = reponseCompet.code()
        assertEquals(201, codeCompet)
        val erreurCompet = if(reponseCompet.isSuccessful) null else reponseCompet.message()
        assertEquals(null, erreurCompet)

        val competitionId = competitionApi.getAllCompetitions().execute().body()
            ?.find { it.name == "Competition de test" }?.id

        val course = CourseCreate(
            name = "Course test",
            maxDuration = 10,
            competitionId = competitionId
        )
        val callCourse = courseApi.addCourse(course)
        val reponseCourse = callCourse.execute()
        val codeCourse = reponseCourse.code()
        assertEquals(201, codeCourse)
        val erreurCourse = if(reponseCourse.isSuccessful) null else reponseCourse.message()
        assertEquals(null, erreurCourse)

        val courseId = courseApi.getAllCourses().execute().body()
            ?.find { it.name == "Course test" }?.id

        val performanceCreate = PerformanceCreate(
            competitorId = competitorId,
            courseId = courseId,
            status = Status.over,
            totalTime = 12
        )
        val callPerformance = performancesApi.addPerformance(performanceCreate)
        val reponsePerformance = callPerformance.execute()
        val codePerformance = reponsePerformance.code()
        assertEquals(201, codePerformance)
        val erreurPerformance = if(reponsePerformance.isSuccessful) null else reponsePerformance.message()
        assertEquals(null, erreurPerformance)
    }

    @Test
    fun deletePerformanceTest(){
        val performances = performancesApi.getAllPerformances().execute().body()
        val performanceId = performances?.get(0)?.id

        if(performanceId != null){
            val call = performancesApi.deletePerformance(performanceId)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
        }
    }

    @Test
    fun getPerformanceDetailsTest(){
        val performances = performancesApi.getAllPerformances().execute().body()
        val performanceId = performances?.get(0)?.id

        if(performanceId != null){
            val call = performancesApi.getPerformanceDetails(performanceId)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println(reponse.body())
        }
    }

    @Test
    fun updatePerformanceTest(){
        val performances = performancesApi.getAllPerformances().execute().body()
        val performanceId = performances?.get(0)?.id

        if(performanceId != null){
            val performanceUpdate = PerformanceUpdate(
                status = PerformanceUpdate.Status.defection,
                totalTime = 120
            )
            val call = performancesApi.updatePerformance(performanceId, performanceUpdate)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            println("Response code: $code")
            println("Response error: $erreur")
            println("Response body: ${reponse.errorBody()?.string()}")

            assertEquals(200, code)
            assertEquals(null, erreur)
        }
    }
}