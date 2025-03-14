package com.but.parkour.clientkotlin.apis

import retrofit2.http.*
import retrofit2.Call

import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.CompetitorCoursePerformanceDetails
import com.but.parkour.clientkotlin.models.CompetitorCreate
import com.but.parkour.clientkotlin.models.CompetitorUpdate
import com.but.parkour.clientkotlin.models.Course
import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.clientkotlin.models.PerformanceObstacle

interface CompetitorsApi {
    /**
     * POST api/competitors
     * Store a competitor
     * 
     * Responses:
     *  - 201: Competitor successfully created.
     *  - 422: Incorrect body parameters
     *
     * @param competitorCreate Competitor&#39;s informations
     * @return [Call]<[Competitor]>
     */
    @POST("api/competitors")
    fun addCompetitor(@Body competitorCreate: CompetitorCreate): Call<Unit>

    /**
     * DELETE api/competitors/{id}
     * Delete the competitor and everything attached to him
     * 
     * Responses:
     *  - 200: Competitor deleted.
     *  - 404: Competitor not found
     *
     * @param id Competitor ID in the database
     * @return [Call]<[Unit]>
     */
    @DELETE("api/competitors/{id}")
    fun deleteCompetitor(@Path("id") id: Int): Call<Unit>

    /**
     * GET api/competitors
     * List of every competitors you have access.
     * 
     * Responses:
     *  - 200: All competitors
     *  - 404: No competitors found.
     *  - 401: Token unauthorized.
     *
     * @return [Call]<[kotlin.collections.List<Competitor>]>
     */
    @GET("api/competitors")
    fun getAllCompetitors(): Call<List<Competitor>>

    /**
     * GET api/competitors/{id}/{id_course}/details_performances
     * Retrieves details about an competitor&#39;s performance in a course
     * 
     * Responses:
     *  - 200: Retrieves details about an competitor's performance in a course.
     *  - 404: No competitors found.
     *  - 401: Token unauthorized.
     *
     * @param id Competitor ID in the database
     * @param idCourse Course ID in the database
     * @return [Call]<[kotlin.collections.List<PerformanceObstacle>]>
     */
    @GET("api/competitors/{id}/{id_course}/details_performances")
    fun getCompetitorCoursePerformanceDetails(@Path("id") id: Int , @Path("id_course") idCourse: Int): Call<CompetitorCoursePerformanceDetails>

    /**
     * GET api/competitors/{id}/courses
     * List of every courses a competitor is registered in.
     * 
     * Responses:
     *  - 200: List of every courses a competitor is registered in.
     *  - 404: No competitors found.
     *  - 401: Token unauthorized.
     *
     * @param id Competitor ID in the database
     * @return [Call]<[kotlin.collections.List<Course>]>
     */
    @GET("api/competitors/{id}/courses")
    fun getCompetitorCourses(@Path("id") id: Int ): Call<List<Course>>

    /**
     * GET api/competitors/{id}
     * Get the details of a competitor
     * Get the details of a competitor by his id
     * Responses:
     *  - 200: Competitor's details
     *  - 404: Competitor not found
     *
     * @param id Competitor ID in the database
     * @return [Call]<[Competitor]>
     */
    @GET("api/competitors/{id}")
    fun getCompetitorDetails(@Path("id") id: Int): Call<Competitor>

    /**
     * GET api/competitors/{id}/performances
     * List of every performance for a given competitor.
     * 
     * Responses:
     *  - 200: All performances for a given competitor
     *  - 404: No competitors found.
     *  - 401: Token unauthorized.
     *
     * @param id Competitor ID in the database
     * @return [Call]<[kotlin.collections.List<Performance>]>
     */
    @GET("api/competitors/{id}/performances")
    fun getCompetitorPerformances(@Path("id") id: Int ): Call<CompetitorCoursePerformanceDetails>

    /**
     * PUT api/competitors/{id}
     * Update the competitor
     * 
     * Responses:
     *  - 200: Competitor successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Competitor not found
     *
     * @param id Competitor ID in the database
     * @param competitorUpdate Updating competitors informations
     * @return [Call]<[Unit]>
     */
    @PUT("api/competitors/{id}")
    fun updateCompetitor(@Path("id") id: Int, @Body competitorUpdate: CompetitorUpdate): Call<Unit>

}
