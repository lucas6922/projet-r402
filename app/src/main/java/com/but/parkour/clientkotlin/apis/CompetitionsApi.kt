package com.but.parkour.clientkotlin.apis

import com.but.parkour.clientkotlin.models.AddCompetitorRequest
import retrofit2.http.*
import retrofit2.Call

import com.but.parkour.clientkotlin.models.Competition
import com.but.parkour.clientkotlin.models.CompetitionCreate
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.Course

interface CompetitionsApi {
    /**
     * POST api/competitions
     * Store a competition, default status set to &#39;not_ready&#39;
     * 
     * Responses:
     *  - 201: Competition successfully created.
     *  - 422: Incorrect body parameters
     *
     * @param competitionCreate Competition&#39;s informations
     * @return [Call]<[Competition]>
     */
    @POST("api/competitions")
    fun addCompetition(@Body competitionCreate: CompetitionCreate): Call<Unit>

    /**
     * DELETE api/competitions/{id}
     * Delete the competition and everything attached to it
     * 
     * Responses:
     *  - 200: Competition deleted.
     *  - 404: Competition not found
     *
     * @param id Competition ID in the database
     * @return [Call]<[Unit]>
     */
    @DELETE("api/competitions/{id}")
    fun deleteCompetition(@Path("id") id: Int): Call<Unit>


    /**
     * DELETE api/competitions/{id}/remove_competitor/{id_competitor}
     * Remove a competitor from the competition
     *
     * Responses:
     *  - 200: Competitor successfully removed.
     *  - 404: Competition or competitor not found
     *  - 422: Incorrect body parameters
     *
     * @param id Competition ID in the database
     * @param idCompetitor Competitor ID in the database
     * @return [Call]<[Unit]>
     */
    @DELETE("api/competitions/{id}/remove_competitor/{id_competitor}")
    fun removeCompetitorFromCompetition(@Path("id") id: Int, @Path("id_competitor") idCompetitor: Int): Call<Unit>

    /**
     * GET api/competitions
     * List of every competitions you have access.
     * 
     * Responses:
     *  - 200: All competitions
     *  - 404: No competitions found.
     *  - 401: Token unauthorized.
     *
     * @return [Call]<[kotlin.collections.List<Competition>]>
     */
    @GET("api/competitions")
    fun getAllCompetitions(): Call<List<Competition>>

    /**
     * GET api/competitions/{id}/courses
     * Get the courses of a given competition
     * Get the courses of a given competition
     * Responses:
     *  - 200: List of the courses
     *  - 404: Competition not found
     *
     * @param id Competition ID in the database
     * @return [Call]<[kotlin.collections.List<Course>]>
     */
    @GET("api/competitions/{id}/courses")
    fun getCompetitionCourses(@Path("id") id: Int): Call<List<Course>>

    /**
     * GET api/competitions/{id}
     * Get the details of a competition
     * Get the details of a competition by his id
     * Responses:
     *  - 200: Competition's details.
     *  - 404: Competition not found
     *
     * @param id Competition ID in the database
     * @return [Call]<[Competition]>
     */
    @GET("api/competitions/{id}")
    fun getCompetitionDetails(@Path("id") id: Int): Call<Competition>

    /**
     * GET api/competitions/{id}/inscriptions
     * Get the competitors registered to the given competition
     * Get the competitors registered to the given competition
     * Responses:
     *  - 200: List of the competitors
     *  - 404: Competition not found
     *
     * @param id Competition ID in the database
     * @return [Call]<[kotlin.collections.List<Competitor>]>
     */
    @GET("api/competitions/{id}/inscriptions")
    fun getCompetitionInscriptions(@Path("id") id: Int): Call<List<Competitor>>

    /**
     * PUT api/competitions/{id}
     * Update the competition
     * 
     * Responses:
     *  - 200: Competition successfully updated.
     *  - 422: Incorrect body parameters
     *  - 404: Competition not found
     *
     * @param id Competition ID in the database
     * @param competitionUpdate Updating competitions informations
     * @return [Call]<[Unit]>
     */
    @PUT("api/competitions/{id}")
    fun updateCompetition(@Path("id") id: Int, @Body competitionUpdate: CompetitionUpdate): Call<Unit>

    /**
     * POST api/competitions/{id}/add_competitor
     * Add a competitor to the competition
     *
     * Responses:
     *  - 201: Competitor successfully added.
     *  - 422: Incorrect body parameters
     *  - 404: Competition or competitor not found
     *  - 409: This competitor is already in the competition.
     *
     * @param id Competition ID in the database
     * @param competitorId Competitor ID in the database
     * @return [Call]<[Unit]>
     */
    @POST("api/competitions/{id}/add_competitor")
    fun addCompetitor(@Path("id") id: Int, @Body competitorId: AddCompetitorRequest): Call<Unit>
}
