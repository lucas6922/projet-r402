package com.but.parkour.clientkotlin.apis

import retrofit2.http.*
import retrofit2.Call

import com.but.parkour.clientkotlin.models.AddCourseObstacleRequest
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.CourseCreate
import com.but.parkour.clientkotlin.models.CourseObstacle
import com.but.parkour.clientkotlin.models.CourseObstacleUpdate
import com.but.parkour.clientkotlin.models.CourseUpdate

interface CoursesApi {
    /**
     * POST api/courses
     * Store a course as the last one of the competition, the position is automatically computed.
     * 
     * Responses:
     *  - 201: Course successfully created.
     *  - 422: Incorrect body parameters
     *  - 404: Competition not found
     *
     * @param courseCreate Store a course as the last one of the competition, the position is automatically computed.
     * @return [Call]<[Unit]>
     */
    @POST("api/courses")
    fun addCourse(@Body courseCreate: CourseCreate): Call<Unit>

    /**
     * POST api/courses/{id}/add_obstacle
     * Add a given obstacle to a course
     * 
     * Responses:
     *  - 201: Obstacle added successfully.
     *  - 422: Incorrect body parameters
     *  - 404: Course or obstacle not found
     *
     * @param id Course ID in the database
     * @param addCourseObstacleRequest Obstacle ID to add
     * @return [Call]<[Unit]>
     */
    @POST("api/courses/{id}/add_obstacle")
    fun addCourseObstacle(@Path("id") id: Int, @Body addCourseObstacleRequest: AddCourseObstacleRequest): Call<Unit>

    /**
     * DELETE api/courses/{id}
     * Delete the course and everything attached to it
     * 
     * Responses:
     *  - 200: Course deleted.
     *  - 404: Course not found
     *
     * @param id Course ID in the database
     * @return [Call]<[Unit]>
     */
    @DELETE("api/courses/{id}")
    fun deleteCourse(@Path("id") id: Int): Call<Unit>

    /**
     * GET api/courses
     * List of every courses you have access.
     * 
     * Responses:
     *  - 200: All courses
     *  - 404: No courses found.
     *  - 401: Token unauthorized.
     *
     * @return [Call]<[kotlin.collections.List<Course>]>
     */
    @GET("api/courses")
    fun getAllCourses(): Call<List<Course>>

    /**
     * GET api/courses/{id}/competitors
     * List of every competitors registered to this course.
     * 
     * Responses:
     *  - 200: List of every competitors registered to this course
     *  - 404: No courses found.
     *  - 401: Token unauthorized.
     *
     * @param id Course ID in the database
     * @return [Call]<[kotlin.collections.List<Competitor>]>
     */
    @GET("api/courses/{id}/competitors")
    fun getCourseCompetitors(@Path("id") id: Int ): Call<List<Competitor>>

    /**
     * GET api/courses/{id}
     * Get the details of a course
     * Get the details of a course by his id
     * Responses:
     *  - 200: Course's details
     *  - 404: Course not found
     *
     * @param id Course ID in the database
     * @return [Call]<[Course]>
     */
    @GET("api/courses/{id}")
    fun getCourseDetails(@Path("id") id: Int): Call<Course>

    /**
     * GET api/courses/{id}/obstacles
     * List of every obstacles of the course.
     * 
     * Responses:
     *  - 200: List of every obstacles of the course
     *  - 404: No courses found.
     *  - 401: Token unauthorized.
     *
     * @param id Course ID in the database
     * @return [Call]<[kotlin.collections.List<CourseObstacle>]>
     */
    @GET("api/courses/{id}/obstacles")
    fun getCourseObstacles(@Path("id") id: Int ): Call<List<CourseObstacle>>

    /**
     * PUT api/courses/{id}
     * Update the course
     * 
     * Responses:
     *  - 200: Course successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Course not found
     *
     * @param id Course ID in the database
     * @param courseUpdate Updating courses informations
     * @return [Call]<[Unit]>
     */
    @PUT("api/courses/{id}")
    fun updateCourse(@Path("id") id: Int, @Body courseUpdate: CourseUpdate): Call<Unit>

    /**
     * POST api/courses/{id}/update_obstacle_position
     * Update the course
     * 
     * Responses:
     *  - 200: Position successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Course or obstacle not found
     *
     * @param id Course ID in the database
     * @param courseObstacleUpdate Updating courses informations
     * @return [Call]<[Unit]>
     */
    @POST("api/courses/{id}/update_obstacle_position")
    fun updateCourseObstaclePosition(@Path("id") id: Int, @Body courseObstacleUpdate: CourseObstacleUpdate): Call<Unit>

    @DELETE("api/courses/{id}/remove_obstacle/{id_obstacle}")
    fun deleteCourseObstacle(@Path("id") id: Int, @Path("id_obstacle") idObstacle: Int): Call<Unit>
}
