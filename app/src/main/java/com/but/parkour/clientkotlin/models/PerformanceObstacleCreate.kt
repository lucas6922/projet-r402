/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.but.parkour.clientkotlin.models


import com.squareup.moshi.Json

/**
 * 
 *
 * @param obstacleId 
 * @param performanceId 
 * @param hasFell 
 * @param toVerify 
 * @param time 
 */


data class PerformanceObstacleCreate (

    @Json(name = "obstacle_id")
    val obstacleId: Int? = null,

    @Json(name = "performance_id")
    val performanceId: Int? = null,

    @Json(name = "has_fell")
    val hasFell: Boolean? = null,

    @Json(name = "to_verify")
    val toVerify: Boolean? = null,

    @Json(name = "time")
    val time: Int? = null

) {


}

