package com.but.parkour.clientkotlin.apis

import retrofit2.http.*
import retrofit2.Call

import com.but.parkour.clientkotlin.models.Obstacle
import com.but.parkour.clientkotlin.models.ObstacleCreate
import com.but.parkour.clientkotlin.models.ObstacleUpdate

interface ObstaclesApi {
    /**
     * POST api/obstacles
     * Store a obstacle
     * 
     * Responses:
     *  - 201: Obstacle successfully created.
     *  - 422: Incorrect body parameters
     *
     * @param obstacleCreate Updating obstacles informations
     * @return [Call]<[Unit]>
     */
    @POST("api/obstacles")
    fun addObstacle(@Body obstacleCreate: ObstacleCreate): Call<Unit>

    /**
     * DELETE api/obstacles/{id}
     * Delete the obstacle and everything attached to it, if it was in a course, it&#39;s deleted from the course.
     * 
     * Responses:
     *  - 200: Obstacle deleted.
     *  - 404: Obstacle not found
     *
     * @param id Obstacle ID in the database
     * @return [Call]<[Unit]>
     */
    @DELETE("api/obstacles/{id}")
    fun deleteObstacle(@Path("id") id: Int): Call<Unit>

    /**
     * GET api/obstacles
     * List of every obstacles you have access.
     * 
     * Responses:
     *  - 200: All obstacles
     *  - 401: Token unauthorized.
     *
     * @return [Call]<[kotlin.collections.List<Obstacle>]>
     */
    @GET("api/obstacles")
    fun getAllObstacles(): Call<List<Obstacle>>

    /**
     * GET api/obstacles/{id}
     * Get the details of an obstacle
     * Get the details of an obstacle by his id
     * Responses:
     *  - 200: Obstacle's details
     *  - 404: Obstacle not found
     *
     * @param id Obstacle ID in the database
     * @return [Call]<[Obstacle]>
     */
    @GET("api/obstacles/{id}")
    fun getObstacleDetails(@Path("id") id: Int): Call<Obstacle>

    /**
     * PUT api/obstacles/{id}
     * Update the obstacle
     * 
     * Responses:
     *  - 200: Obstacle successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Obstacle not found
     *
     * @param id Obstacle ID in the database
     * @param obstacleUpdate Updating obstacles informations
     * @return [Call]<[Unit]>
     */
    @PUT("api/obstacles/{id}")
    fun updateObstacle(@Path("id") id: Int, @Body obstacleUpdate: ObstacleUpdate): Call<Unit>

}
