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

package com.but.parkour.openapitools.client.apis.genere

import io.kotlintest.specs.ShouldSpec

class PerformanceObstaclesApiTest : ShouldSpec() {
    init {
        // uncomment below to create an instance of PerformanceObstaclesApi
        //val apiInstance = PerformanceObstaclesApi()

        // to test getPerformanceObstacleDetails
        should("test getPerformanceObstacleDetails") {
            // uncomment below to test getPerformanceObstacleDetails
            //val id : kotlin.Int = 56 // kotlin.Int | Performance obstacle ID in the database
            //val result : PerformanceObstacle = apiInstance.getPerformanceObstacleDetails(id)
            //result shouldBe ("TODO")
        }

        // to test listPerformanceObstacles
        should("test listPerformanceObstacles") {
            // uncomment below to test listPerformanceObstacles
            //val result : kotlin.collections.List<PerformanceObstacle> = apiInstance.listPerformanceObstacles()
            //result shouldBe ("TODO")
        }

        // to test storePerformanceObstacle
        should("test storePerformanceObstacle") {
            // uncomment below to test storePerformanceObstacle
            //val performanceObstacleCreate : PerformanceObstacleCreate =  // PerformanceObstacleCreate | Storing performance obstacles informations
            //apiInstance.storePerformanceObstacle(performanceObstacleCreate)
        }

        // to test updatePerformanceObstacle
        should("test updatePerformanceObstacle") {
            // uncomment below to test updatePerformanceObstacle
            //val id : kotlin.Int = 56 // kotlin.Int | Performance obstacle ID in the database
            //val performanceObstacleUpdate : PerformanceObstacleUpdate =  // PerformanceObstacleUpdate | Updating obstacles informations
            //apiInstance.updatePerformanceObstacle(id, performanceObstacleUpdate)
        }

    }
}
