package com.but.parkour.openapitools.client.apis

import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.apis.ObstaclesApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.AddCourseObstacleRequest
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.clientkotlin.models.CourseObstacleUpdate
import com.but.parkour.clientkotlin.models.CourseUpdate
import org.junit.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertEquals

class CoursesApiTest {
    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM",
    )
    private val apiService = apiClient.createService(CoursesApi::class.java)
    private val reset = apiClient.createService(UtilApi::class.java)
    private val competition = apiClient.createService(CompetitionsApi::class.java)
    private val obstacleApi = apiClient.createService(ObstaclesApi::class.java)

    @BeforeEach
    fun setUp() {
        val resetData = reset.resetData()
        resetData.execute()
    }
    @Test
    fun addCourseTest(){

        val competitionId = competition.getAllCompetitions()
            .execute().body()?.get(0)?.id

        if(competitionId != null){
            val courseCreate = CourseCreate(
                name = "Course test",
                maxDuration = 12,
                competitionId = competitionId
            )
            val call = apiService.addCourse(courseCreate)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(201, code)
            assertEquals(null, erreur)
        }
    }

    @Test
    fun addCourseObstacleTest(){
        val courseId = apiService.getAllCourses().execute()
            .body()?.get(0)?.id
        if(courseId != null){
            val obstacleId = obstacleApi.getAllObstacles().execute()
                .body()?.get(0)?.id

            if(obstacleId != null) {
                val obstacleId = AddCourseObstacleRequest(
                    obstacleId = obstacleId
                )
                val call = apiService.addCourseObstacle(courseId, obstacleId)
                val reponse = call.execute()
                val code = reponse.code()
                val erreur = if (reponse.isSuccessful) null else reponse.message()
                assertEquals(201, code)
                assertEquals(null, erreur)
            }
        }else{
            println("Aucune course")
        }
    }

    @Test
    fun deleteCourseTest(){
        val courseID = apiService.getAllCourses().execute()
            .body()?.get(0)?.id
        if(courseID != null){
            val call = apiService.deleteCourse(courseID)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()

            assertEquals(200, code)
            assertEquals(null, erreur)
        }else{
            println("pas de courses à supprimer")
        }
    }

    @Test
    fun getAllCoursesTest(){
        println("----------------")
        println("getAllCoursesTest")
        val call = apiService.getAllCourses()
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if(reponse.isSuccessful) null else reponse.message()
        assertEquals(200, code)
        assertEquals(null, erreur)
        println(reponse.body())
        println("----------------")
    }

    @Test
    fun getCourseCompetitorsTest(){
        println("----------------")
        println("getCourseCompetitorsTest")
        val courseId = apiService.getAllCourses().execute()
            .body()?.get(0)?.id

        if(courseId != null){
            val call = apiService.getCourseCompetitors(courseId)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)

            println(reponse.body())
        }
        else{
            println("Aucune course")
        }
        println("----------------")
    }

    @Test
    fun getCourseDetailsTest(){
        println("----------------")
        println("getCourseDetailsTest")
        val courseId = apiService.getAllCourses().execute()
            .body()?.get(0)?.id

        if(courseId != null){
            val courseDetail = apiService.getCourseDetails(courseId)
            val reponse = courseDetail.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)

            println(reponse.body())
        }else{
            println("aucune course")
        }
        println("----------------")
    }

    @Test
    fun getCourseObstaclesTest(){
        println("----------------")
        println("getCourseObstaclesTest")
        val courseId = apiService.getAllCourses().execute()
            .body()?.get(0)?.id

        if(courseId != null){
            val call = apiService.getCourseObstacles(courseId)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)

            println("Obstacles de la course : " + reponse.body())
        }else{
            println("Aucune course")
        }
        println("----------------")
    }

    @Test
    fun updateCourseTest(){
        println("----------------")
        println("updateCourseTest")
        val courses = apiService.getAllCourses().execute()
            .body()

        val courseId = courses?.get(0)?.id
        println("courses avant update : $courses")
        if(courseId != null){
            val competitionID = competition.getAllCompetitions().execute()
                .body()?.get(0)?.id
            val courseUpdate = CourseUpdate(
                name = "Course test",
                maxDuration = 12,
                position = 2,
                isOver = true,
                competitionId = competitionID
            )
            val call = apiService.updateCourse(courseId, courseUpdate)
            val reponse = call.execute()
            val code = reponse.code()
            val erreur = if(reponse.isSuccessful) null else reponse.message()
            assertEquals(200, code)
            assertEquals(null, erreur)
            println("courses après update : ${apiService.getAllCourses().execute().body()}")
        }else{
            println("Aucune course")
        }
        println("----------------")
    }

    @Test
    fun updateCourseObstaclePositionTest(){
        println("----------------")
        println("updateCourseObstaclePositionTest")
        val courseId = apiService.getAllCourses().execute()
            .body()?.get(0)?.id

        if(courseId != null){
            val obstacleId = apiService.getCourseObstacles(courseId).execute()
                .body()?.get(0)?.courseObstacleId

            val obstaclesAvantModif = apiService.getCourseObstacles(courseId).execute().body()
            println("obstacles avant modif : $obstaclesAvantModif")
            if(obstacleId != null){
                val obstacleUpdate = CourseObstacleUpdate(
                    obstacleId = obstacleId,
                    position = 42
                )
                val call = apiService.updateCourseObstaclePosition(courseId, obstacleUpdate)
                val reponse = call.execute()
                val code = reponse.code()
                val erreur = if(reponse.isSuccessful) null else reponse.message()
                assertEquals(200, code)
                assertEquals(null, erreur)
                println("ok")

                println("obstacles après modif : ${apiService.getCourseObstacles(courseId).execute().body()}")
            }else{
                println("Aucun obstacle, en mmême temps si tous les id sont null ca va être complique...")
            }
        }else{
            println("Aucune course")
        }
        println("----------------")
    }
}