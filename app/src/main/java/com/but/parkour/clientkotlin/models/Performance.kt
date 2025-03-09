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
import java.time.OffsetDateTime

/**
 * 
 *
 * @param id 
 * @param competitorId 
 * @param courseId 
 * @param status 
 * @param totalTime 
 * @param createdAt 
 * @param updatedAt 
 */


data class Performance (

    @Json(name = "id")
    val id: Int? = null,

    @Json(name = "competitor_id")
    val competitorId: Int? = null,

    @Json(name = "course_id")
    val courseId: Int? = null,

    @Json(name = "status")
    val status: Status? = null,

    @Json(name = "total_time")
    val totalTime: Int? = null,

    @Json(name = "created_at")
    val createdAt: OffsetDateTime? = null,

    @Json(name = "updated_at")
    val updatedAt: OffsetDateTime? = null

) {

    /**
     * 
     *
     * Values: defection,to_finish,to_verify,over
     */
    @JsonClass(generateAdapter = false)
    enum class Status(val value: String) {
        @Json(name = "defection") defection("defection"),
        @Json(name = "to_finish") to_finish("to_finish"),
        @Json(name = "to_verify") to_verify("to_verify"),
        @Json(name = "over") over("over");
    }

}

