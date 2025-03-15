package com.but.parkour.competition.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.Competition
import kotlinx.coroutines.launch

class CompetitionViewModel : ViewModel() {
    private val _competitions = MutableLiveData<List<Competition>>()
    val competitions: LiveData<List<Competition>> = _competitions
    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM"
    )
    init {
        fetchCompetitions()
    }


    private fun fetchCompetitions() {
        viewModelScope.launch {
            try {
                Log.d("CompetitionViewModel", "Fetching competitions...")

                val competitionApi = apiClient.createService(CompetitionsApi::class.java)
                val call = competitionApi.getAllCompetitions()

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitionViewModel", "Competitions received: $data")
                        _competitions.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitionViewModel", "Error: $errorMessage")
                        _competitions.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _competitions.postValue(emptyList())
            }
        }
    }

}