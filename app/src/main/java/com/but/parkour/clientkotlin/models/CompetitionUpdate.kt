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
import com.squareup.moshi.JsonClass

/**
 * 
 *
 * @param name 
 * @param ageMin 
 * @param ageMax 
 * @param gender 
 * @param hasRetry 
 * @param status 
 */


data class CompetitionUpdate (

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "age_min")
    val ageMin: Int? = null,

    @Json(name = "age_max")
    val ageMax: Int? = null,

    @Json(name = "gender")
    val gender: Gender? = null,

    @Json(name = "has_retry")
    val hasRetry: Boolean? = null,

    @Json(name = "status")
    val status: Status? = null

) {

    /**
     * 
     *
     * Values: H,F
     */
    @JsonClass(generateAdapter = false)
    enum class Gender(val value: String) {
        @Json(name = "H") H("H"),
        @Json(name = "F") F("F");
    }
    /**
     * 
     *
     * Values: not_ready,not_started,started,finished
     */
    @JsonClass(generateAdapter = false)
    enum class Status(val value: String) {
        @Json(name = "not_ready") not_ready("not_ready"),
        @Json(name = "not_started") not_started("not_started"),
        @Json(name = "started") started("started"),
        @Json(name = "finished") finished("finished");
    }

}

