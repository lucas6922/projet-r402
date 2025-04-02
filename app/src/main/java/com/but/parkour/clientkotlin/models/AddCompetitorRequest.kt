package com.but.parkour.clientkotlin.models

import com.squareup.moshi.Json

data class AddCompetitorRequest(
    @Json(name = "competitor_id")
    val competitorId: Int
)
