package com.but.parkour.openapitools.client.apis

import com.but.parkour.BuildConfig
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.UtilApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.CompetitionUpdate
import org.junit.Test
import org.junit.Assert.*

class ConnexionApiTest {
    private val apiClient = ApiClient(
        bearerToken = BuildConfig.API_TOKEN,
    )
    private val apiService = apiClient.createService(UtilApi::class.java)
    private val call = apiService.resetData()

    private val competitionApi = apiClient.createService(CompetitionsApi::class.java)


    @Test
    fun resetDataTest() {
        val reponse = call.execute()
        val code = reponse.code()
        val erreur = if (reponse.isSuccessful) null else "Erreur: ${reponse.message()}"

        assertEquals(200, code)
        assertEquals(null, erreur)

        //mettre une competition en finished

        val competitions = competitionApi.getAllCompetitions().execute().body()

        val competition0 = competitions?.get(0)

        val competitionUpdate = CompetitionUpdate(
            status = CompetitionUpdate.Status.finished
        )

        competitionApi.updateCompetition(competition0?.id!!, competitionUpdate).execute()

        val competition1 = competitions.get(1)

        val competitionUpdate1 = CompetitionUpdate(
            status = CompetitionUpdate.Status.not_started
        )

        competitionApi.updateCompetition(competition1.id!!, competitionUpdate1).execute()

        val competition2 = competitions.get(2)

        val competitionUpdate2 = CompetitionUpdate(
            status = CompetitionUpdate.Status.started
        )

        competitionApi.updateCompetition(competition2.id!!, competitionUpdate2).execute()


    }

}