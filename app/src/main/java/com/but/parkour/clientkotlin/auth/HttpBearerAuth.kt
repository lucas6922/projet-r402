package com.but.parkour.clientkotlin.auth

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class HttpBearerAuth(
    private var schema: String = "",
    var bearerToken: String = ""
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Chain): Response {
        var request = chain.request()

        if (request.header("Authorization") == null && bearerToken.isNotBlank()) {
            request = request.newBuilder()
                .addHeader("Authorization", headerValue())
                .build()
        }
        return chain.proceed(request)
    }

    private fun headerValue(): String {
        return if (schema.isNotBlank()) {
            "${upperCaseBearer()} $bearerToken"
        } else {
            bearerToken
        }
    }

    private fun upperCaseBearer(): String {
        return if (schema.lowercase().equals("bearer")) "Bearer" else schema
    }

}
