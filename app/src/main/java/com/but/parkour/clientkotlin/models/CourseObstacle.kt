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
 * @param courseObstacleId 
 * @param obstacleName 
 * @param position 
 */


data class CourseObstacle (

    @Json(name = "id")
    val courseObstacleId: Int? = null,

    @Json(name = "obstacle_id")
    val obstacleId: Int? = null,

    @Json(name = "obstacle_name")
    val obstacleName: String? = null,

    @Json(name = "position")
    val position: Int? = null

) {


}

