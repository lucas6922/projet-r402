package com.but.parkour.clientkotlin.apis

import retrofit2.http.*
import retrofit2.Call

import com.but.parkour.clientkotlin.models.PerformanceObstacle
import com.but.parkour.clientkotlin.models.PerformanceObstacleCreate
import com.but.parkour.clientkotlin.models.PerformanceObstacleUpdate

interface PerformanceObstaclesApi {
    /**
     * POST api/performance_obstacles
     * Store a performance obstacle
     * 
     * Responses:
     *  - 201: Performance obstacle successfully created.
     *  - 422: Incorrect body parameters
     *  - 404: Performance or obstacle not found
     *  - 403: Performance or obstacle don't belong to you.
     *  - 409: This combination already exists.
     *
     * @param performanceObstacleCreate Storing performance obstacles informations
     * @return [Call]<[Unit]>
     */
    @POST("api/performance_obstacles")
    fun createPerformanceObstacle(@Body performanceObstacleCreate: PerformanceObstacleCreate): Call<Unit>

    /**
     * GET api/performance_obstacles
     * List of every performance by obstacle you have access.
     * 
     * Responses:
     *  - 200: All performances by obstacles
     *  - 404: No obstacles found.
     *  - 401: Token unauthorized.
     *
     * @return [Call]<[kotlin.collections.List<PerformanceObstacle>]>
     */
    @GET("api/performance_obstacles")
    fun getAllPerformanceObstacles(): Call<List<PerformanceObstacle>>

    /**
     * GET api/performance_obstacles/{id}
     * Get the details of a obstacle
     * Get the details of a performance obstacle by his id
     * Responses:
     *  - 200: Performance obstacle's details
     *  - 404: Performance obstacle not found
     *
     * @param id Performance obstacle ID in the database
     * @return [Call]<[PerformanceObstacle]>
     */
    @GET("api/performance_obstacles/{id}")
    fun getPerformanceObstacleDetails(@Path("id") id: Int): Call<PerformanceObstacle>

    /**
     * PUT api/performance_obstacles/{id}
     * Update the performance obstacle
     * 
     * Responses:
     *  - 200: Performance obstacle successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Performance obstacle not found
     *
     * @param id Performance obstacle ID in the database
     * @param performanceObstacleUpdate Updating obstacles informations
     * @return [Call]<[Unit]>
     */
    @PUT("api/performance_obstacles/{id}")
    fun updatePerformanceObstacle(@Path("id") id: Int, @Body performanceObstacleUpdate: PerformanceObstacleUpdate): Call<Unit>

}
