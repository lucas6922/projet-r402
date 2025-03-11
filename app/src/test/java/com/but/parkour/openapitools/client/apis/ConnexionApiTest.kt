package com.but.parkour.openapitools.client.apis

import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import org.junit.Test
import org.junit.Assert.*

class ConnexionApiTest {
    private val apiClient = ApiClient()
    private val apiService = apiClient.createService(UtilApi::class.java)
    private val call = apiService.resetData()


    @Test
    fun resetDataTest() {
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else "Erreur: ${reponse.message()}"

        assertEquals(200, code)
        assertEquals(null, erreur)
    }

}