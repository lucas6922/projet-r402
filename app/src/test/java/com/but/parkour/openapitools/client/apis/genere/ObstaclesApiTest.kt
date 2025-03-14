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

class ObstaclesApiTest : ShouldSpec() {
    init {
        // uncomment below to create an instance of ObstaclesApi
        //val apiInstance = ObstaclesApi()

        // to test deleteObstacle
        should("test deleteObstacle") {
            // uncomment below to test deleteObstacle
            //val id : kotlin.Int = 56 // kotlin.Int | Obstacle ID in the database
            //apiInstance.deleteObstacle(id)
        }

        // to test getObstacleDetails
        should("test getObstacleDetails") {
            // uncomment below to test getObstacleDetails
            //val id : kotlin.Int = 56 // kotlin.Int | Obstacle ID in the database
            //val result : Obstacle = apiInstance.getObstacleDetails(id)
            //result shouldBe ("TODO")
        }

        // to test listObstacles
        should("test listObstacles") {
            // uncomment below to test listObstacles
            //val result : kotlin.collections.List<Obstacle> = apiInstance.listObstacles()
            //result shouldBe ("TODO")
        }

        // to test storeObstacle
        should("test storeObstacle") {
            // uncomment below to test storeObstacle
            //val obstacleCreate : ObstacleCreate =  // ObstacleCreate | Updating obstacles informations
            //apiInstance.storeObstacle(obstacleCreate)
        }

        // to test updateObstacle
        should("test updateObstacle") {
            // uncomment below to test updateObstacle
            //val id : kotlin.Int = 56 // kotlin.Int | Obstacle ID in the database
            //val obstacleUpdate : ObstacleUpdate =  // ObstacleUpdate | Updating obstacles informations
            //apiInstance.updateObstacle(id, obstacleUpdate)
        }

    }
}
