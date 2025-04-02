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
 * @param name 
 * @param maxDuration 
 * @param position 
 * @param isOver 
 * @param competitionId 
 */


data class CourseUpdate (

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "max_duration")
    val maxDuration: Int? = null,

    @Json(name = "position")
    val position: Int? = null,

    @Json(name = "is_over")
    val isOver: Boolean? = null,

    @Json(name = "competition_id")
    val competitionId: Int? = null

) {


}

