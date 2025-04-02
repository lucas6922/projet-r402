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

class CoursesApiTest : ShouldSpec() {
    init {
        // uncomment below to create an instance of CoursesApi
        //val apiInstance = CoursesApi()

        // to test addCourseObstacle
        should("test addCourseObstacle") {
            // uncomment below to test addCourseObstacle
            //val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
            //val addCourseObstacleRequest : AddCourseObstacleRequest =  // AddCourseObstacleRequest | Obstacle ID to add
            //apiInstance.addCourseObstacle(id, addCourseObstacleRequest)
        }

        // to test deleteCourse
        should("test deleteCourse") {
            // uncomment below to test deleteCourse
            //val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
            //apiInstance.deleteCourse(id)
        }

        // to test getCourseDetails
        should("test getCourseDetails") {
            // uncomment below to test getCourseDetails
            //val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
            //val result : Course = apiInstance.getCourseDetails(id)
            //result shouldBe ("TODO")
        }

        // to test listCourseCompetitors
        should("test listCourseCompetitors") {
            // uncomment below to test listCourseCompetitors
            //val UNKNOWN_PARAMETER_NAME :  =  //  | Course ID in the database
            //val result : kotlin.collections.List<Competitor> = apiInstance.listCourseCompetitors(UNKNOWN_PARAMETER_NAME)
            //result shouldBe ("TODO")
        }

        // to test listCourseObstacles
        should("test listCourseObstacles") {
            // uncomment below to test listCourseObstacles
            //val UNKNOWN_PARAMETER_NAME :  =  //  | Course ID in the database
            //val result : kotlin.collections.List<CourseObstacle> = apiInstance.listCourseObstacles(UNKNOWN_PARAMETER_NAME)
            //result shouldBe ("TODO")
        }

        // to test listCourses
        should("test listCourses") {
            // uncomment below to test listCourses
            //val result : kotlin.collections.List<Course> = apiInstance.listCourses()
            //result shouldBe ("TODO")
        }

        // to test storeCourse
        should("test storeCourse") {
            // uncomment below to test storeCourse
            //val courseCreate : CourseCreate =  // CourseCreate | Store a course as the last one of the competition, the position is automatically computed.
            //apiInstance.storeCourse(courseCreate)
        }

        // to test updateCourse
        should("test updateCourse") {
            // uncomment below to test updateCourse
            //val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
            //val courseUpdate : CourseUpdate =  // CourseUpdate | Updating courses informations
            //apiInstance.updateCourse(id, courseUpdate)
        }

        // to test updateCourseObstaclePosition
        should("test updateCourseObstaclePosition") {
            // uncomment below to test updateCourseObstaclePosition
            //val id : kotlin.Int = 56 // kotlin.Int | Course ID in the database
            //val courseObstacleUpdate : CourseObstacleUpdate =  // CourseObstacleUpdate | Updating courses informations
            //apiInstance.updateCourseObstaclePosition(id, courseObstacleUpdate)
        }

    }
}
