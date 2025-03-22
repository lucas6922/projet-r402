package com.but.parkour.concurrents.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Competitor
import kotlinx.coroutines.launch

class GestionConcurrentViewModel: ViewModel() {
    private val _competitors = MutableLiveData<List<Competitor>>()
    val competitors: LiveData<List<Competitor>> = _competitors

    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM"
    )

    private val competitorApi = apiClient.createService(CompetitorsApi::class.java)

    fun fetchAllCompetitors(){
        viewModelScope.launch {
            try {
                Log.d("CompetitorViewModel", "Fetching all competitors...")

                val call = competitorApi.getAllCompetitors()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitors received: $data")
                        _competitors.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                        _competitors.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _competitors.postValue(emptyList())
            }
        }
    }
}