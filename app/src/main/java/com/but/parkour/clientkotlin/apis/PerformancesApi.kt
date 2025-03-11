package com.but.parkour.clientkotlin.apis

import retrofit2.http.*
import retrofit2.Call

import com.but.parkour.clientkotlin.models.Performance
import com.but.parkour.clientkotlin.models.PerformanceCreate
import com.but.parkour.clientkotlin.models.PerformanceUpdate

interface PerformancesApi {
    /**
     * POST api/performances
     * Store a performance
     * 
     * Responses:
     *  - 201: Performance successfully created.
     *  - 422: Incorrect body parameters
     *  - 404: Competitor or course not found
     *  - 403: Competitor or course don't belong to you.
     *  - 409: This combination already exists.
     *
     * @param performanceCreate Storing performance informations
     * @return [Call]<[Unit]>
     */
    @POST("api/performances")
    fun addPerformance(@Body performanceCreate: PerformanceCreate): Call<Unit>

    /**
     * DELETE api/performances/{id}
     * Delete the performance and everything attached to it.
     * 
     * Responses:
     *  - 200: Performance deleted.
     *  - 404: Performance not found
     *
     * @param id Performance ID in the database
     * @return [Call]<[Unit]>
     */
    @DELETE("api/performances/{id}")
    fun deletePerformance(@Path("id") id: Int): Call<Unit>

    /**
     * GET api/performances
     * List of every performance you have access.
     * 
     * Responses:
     *  - 200: All performances
     *  - 401: Token unauthorized.
     *
     * @return [Call]<[kotlin.collections.List<Performance>]>
     */
    @GET("api/performances")
    fun getAllPerformances(): Call<List<Performance>>

    /**
     * GET api/performances/{id}
     * Get the details of a performance
     * Get the details of a performance by his id
     * Responses:
     *  - 200: Performance's details
     *  - 404: Performance not found
     *
     * @param id Performance&#39;s ID in the database
     * @return [Call]<[Performance]>
     */
    @GET("api/performances/{id}")
    fun getPerformanceDetails(@Path("id") id: Int): Call<Performance>

    /**
     * PUT api/performances/{id}
     * Update the performance
     * 
     * Responses:
     *  - 200: Performance successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Performance not found
     *
     * @param id Performance ID in the database
     * @param performanceUpdate Updating performance informations
     * @return [Call]<[Unit]>
     */
    @PUT("api/performances/{id}")
    fun updatePerformance(@Path("id") id: Int, @Body performanceUpdate: PerformanceUpdate): Call<Unit>

}
