package com.but.parkour.clientkotlin.apis

import retrofit2.http.*
import retrofit2.Call


interface UtilApi {
    /**
     * GET api/reset
     * Reset all your datas to a random set.
     * 
     * Responses:
     *  - 200: Datas successfully reseted.
     *
     * @return [Call]<[Unit]>
     */
    @GET("api/reset")
    fun resetData(): Call<Unit>

}
