package com.but.parkour.api

import com.but.parkour.data.Competition
import com.but.parkour.data.Competitor
import com.but.parkour.data.Course
import com.but.parkour.data.Obstacle
import com.but.parkour.data.Performance
import com.but.parkour.data.PerformanceObstacle
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //Util
    @GET("reset")
    suspend fun reset(): Response<Unit>

    //Competitions

    @GET("competitions")
    suspend fun getCompetitions(): Response<List<Competition>>

    @POST
    /**
     * CompetitionCreate{
     * name	string
     * age_min	integer
     * age_max	integer
     * gender	stringEnum: [ H, F ]
     * has_retry	boolean
     * }
     */
    suspend fun addCompetition(@Body competition: Competition): Response<Unit>

    @GET("competitions/{id}")
    suspend fun getCompetition(@Path("id") id: Int): Response<Competition>

    @PUT("competitions/{id}")
    suspend fun updateCompetition(@Path("id") id: Int, @Body competition: Competition): Response<Unit>

    @DELETE("competitions/{id}")
    suspend fun deleteCompetition(@Path("id") id: Int): Response<Unit>

    @GET("competitions/{id}/inscription")
    suspend fun getInscriptions(@Path("id") id: Int): Response<List<Competitor>>

    @GET("competitions/{id}/courses")
    suspend fun getCoursesCompetition(@Path("id") id: Int): Response<List<Course>>

    //Competitors

    @GET("competitors")
    suspend fun getCompetitors(): Response<List<Competitor>>

    @POST("competitors")
    /**
     * CompetitorCreate{
     * first_name	string
     * last_name	string
     * email	string
     * phone	string
     * gender	stringEnum: [ H, F ]
     * born_at	string($date)
     * }
     */
    suspend fun addCompetitor(@Body competitor: Competitor): Response<Unit>

    @GET("competitors/{id}")
    suspend fun getCompetitor(@Path("id") id: Int): Response<Competitor>

    @PUT("competitors/{id}")
    suspend fun updateCompetitor(@Path("id") id: Int, @Body competitor: Competitor): Response<Unit>

    @DELETE("competitors/{id}")
    suspend fun deleteCompetitor(@Path("id") id: Int): Response<Unit>

    @GET("competitors/{id}/performances")
    suspend fun getPerformances(@Path("id") id: Int): Response<List<Performance>>

    @GET("competitors/{id}/courses")
    suspend fun getCoursesCompetitor(@Path("id") id: Int): Response<List<Course>>

    @GET("competitors/{id}/{id_course}/details_performances")
    suspend fun getDetailsPerformances(@Path("id") id: Int, @Path("id_course") id_course: Int): Response<List<PerformanceObstacle>>

    //Courses

    @GET("courses")
    suspend fun getCourses(): Response<List<Course>>

    @POST
    /**
     * CourseCreate{
     * name	string
     * max_duration	integer
     * competition_id	integer
     * }
     */
    suspend fun addCourse(@Body course: Course): Response<Unit>

    @GET("courses/{id}/competitors")
    suspend fun getCompetitors(@Path("id") id: Int): Response<List<Competitor>>

    @GET("courses/{id}/obstacles")
    suspend fun getObstacles(@Path("id") id: Int): Response<List<Obstacle>>

    @GET("courses/{id}")
    suspend fun getCourse(@Path("id") id: Int): Response<Course>

    @PUT("courses/{id}")
    suspend fun updateCourse(@Path("id") id: Int, @Body course: Course): Response<Unit>

    @DELETE("courses/{id}")
    suspend fun deleteCourse(@Path("id") id: Int): Response<Unit>

    @POST("courses/{id}/add_obstacle")
    /**
     *
     * id*	integer
     *
     * {
     * obstacle_id*	integer
     * example: 1
     * }
     */
    suspend fun addObstacle(@Path("id") id: Int, @Body obstacle: Obstacle): Response<Unit>

    @POST("courses/{id}/update_obstacle_position")
    /**
     *
     * id*	integer
     *
     * CourseObstacleUpdate{
     * obstacle_id	integer
     * position	integer
     * }
     */
    suspend fun updateObstaclePosition(@Path("id") id: Int, @Body obstacle: Obstacle): Response<Unit>

    //Obstacles
    @GET("obstacles")
    suspend fun getObstacles(): Response<List<Obstacle>>

    @POST("obstacles")
    /**
     * ObstacleCreate{
     * name	string
     * }
     */
    suspend fun addObstacle(@Body obstacle: Obstacle): Response<Unit>

    @GET("obstacles/{id}")
    suspend fun getObstacle(@Path("id") id: Int): Response<Obstacle>

    @PUT("obstacles/{id}")
    /**
     * ObstacleUpdate{
     * name	string
     * }
     */
    suspend fun updateObstacle(@Path("id") id: Int, @Body obstacle: Obstacle): Response<Unit>

    @DELETE("obstacles/{id}")
    suspend fun deleteObstacle(@Path("id") id: Int): Response<Unit>


    //Performances obstacles
    @GET("performances_obstacles")
    suspend fun getPerformancesObstacles(): Response<List<PerformanceObstacle>>

    @POST("performances_obstacles")
    /**
     * PerformanceObstacleCreate{
     * obstacle_id	integer
     * performance_id	integer
     * has_fell	boolean
     * to_verify	boolean
     * time	integer
     * }
     */
    suspend fun addPerformanceObstacle(@Body performanceObstacle: PerformanceObstacle): Response<Unit>

    @GET("performances_obstacles/{id}")
    suspend fun getPerformanceObstacle(@Path("id") id: Int): Response<PerformanceObstacle>

    @PUT("performances_obstacles/{id}")
    /**
     * PerformanceObstacleUpdate{
     * has_fell	boolean
     * to_verify	boolean
     * time	integer
     * }
     */
    suspend fun updatePerformanceObstacle(@Path("id") id: Int, @Body performanceObstacle: PerformanceObstacle): Response<Unit>

    //Performances
    @GET("performances")
    suspend fun getPerformances(): Response<List<Performance>>

    @POST("performances")
    /**
     * PerformanceCreate{
     * competitor_id	integer
     * course_id	integer
     * status	stringEnum:
     * [ defection, to_finish, to_verify, over ]
     * total_time	integer
     * }
     */
    suspend fun addPerformance(@Body performance: Performance): Response<Unit>

    @GET("performances/{id}")
    suspend fun getPerformance(@Path("id") id: Int): Response<Performance>

    @PUT("performances/{id}")
    /**
     * PerformanceUpdate{
     * status	stringEnum:
     * [ defection, to_finish, to_verify, over ]
     * total_time	integer
     * }
     */
    suspend fun updatePerformance(@Path("id") id: Int, @Body performance: Performance): Response<Unit>

    @DELETE("performances/{id}")
    suspend fun deletePerformance(@Path("id") id: Int): Response<Unit>
}