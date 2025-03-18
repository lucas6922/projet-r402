package com.but.parkour.concurrents.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.but.parkour.clientkotlin.apis.CompetitionsApi
import com.but.parkour.clientkotlin.apis.CompetitorsApi
import com.but.parkour.clientkotlin.apis.CoursesApi
import com.but.parkour.clientkotlin.infrastructure.ApiClient
import com.but.parkour.clientkotlin.models.AddCompetitorRequest
import com.but.parkour.clientkotlin.models.Competitor
import com.but.parkour.clientkotlin.models.CompetitorCreate
import kotlinx.coroutines.launch

class CompetitorViewModel : ViewModel() {
    private val _competitors = MutableLiveData<List<Competitor>>()
    val competitors: LiveData<List<Competitor>> = _competitors

    private val _unregisteredCompetitors = MutableLiveData<List<Competitor>>()
    val unregisteredCompetitors: LiveData<List<Competitor>> = _unregisteredCompetitors

    private val _competitorsCourse = MutableLiveData<List<Competitor>>()
    val competitorsCourse: LiveData<List<Competitor>> = _competitorsCourse

    private val apiClient = ApiClient(
        bearerToken = "LgJxjdr5uiNa95irSUBNEMqdAz5WxKnxa93b7dbBNOI4V69IgGa6E2dK1KleF5QM"
    )


    val competitionApi = apiClient.createService(CompetitionsApi::class.java)
    val competitorApi = apiClient.createService(CompetitorsApi::class.java)
    val courseApi = apiClient.createService(CoursesApi::class.java)

    fun fetchCompetitorsInscrit(competitionId: Int) {
        viewModelScope.launch {
            try {
                Log.d("CompetitorViewModel", "Fetching competitors...")

                val call = competitionApi.getCompetitionInscriptions(competitionId)

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


    fun fetchUnregisteredCompetitors(competitionId: Int) {
        viewModelScope.launch {
            try {
                apiClient.fetchData(
                    call = competitorApi.getAllCompetitors(),
                    onSuccess = { allCompetitors, _ ->
                        apiClient.fetchData(
                            call = competitionApi.getCompetitionInscriptions(competitionId),
                            onSuccess = { registeredCompetitors, _ ->
                                val unregisteredCompetitors = allCompetitors?.filter { competitor ->
                                    registeredCompetitors?.none { it.id == competitor.id } != false
                                } ?: emptyList()
                                _unregisteredCompetitors.postValue(unregisteredCompetitors)
                                Log.d("CompetitorViewModel", "Competitors unregistred fetch : $unregisteredCompetitors")
                            },
                            onError = { _, _ ->
                                _unregisteredCompetitors.postValue(emptyList())
                            }
                        )
                    },
                    onError = { _, _ ->
                        _unregisteredCompetitors.postValue(emptyList())
                    }
                )
            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _unregisteredCompetitors.postValue(emptyList())
            }
        }
    }

    fun registerCompetitor(competitionId: Int, competitorId: Int) {
        viewModelScope.launch {
            try {
                val competitorId = AddCompetitorRequest(
                    competitorId = competitorId
                )
                val call = competitionApi.addCompetitor(competitionId, competitorId)
                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitor registered: $data")
                        fetchCompetitorsInscrit(competitionId)
                        fetchUnregisteredCompetitors(competitionId)
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

    fun fetchCompetitorsCourse(competitionId : Int){
        viewModelScope.launch {
            try {
                Log.d("CompetitorViewModel", "Fetching competitors for a course... id: $competitionId")

                val call = competitionApi.getCompetitionInscriptions(competitionId)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitors received: $data")
                        _competitorsCourse.postValue(data ?: emptyList())
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                        _competitorsCourse.postValue(emptyList())
                    }
                )

            } catch (e: Exception) {
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
                _competitorsCourse.postValue(emptyList())
            }
        }
    }

    fun addCompetitor(competitor: CompetitorCreate, competitionId: Int){
        viewModelScope.launch {
            try{
                val call = competitorApi.addCompetitor(competitor)

                apiClient.fetchData(
                    call,
                    onSuccess = { data, statusCode ->
                        Log.d("CompetitorViewModel", "Competitor registered: $data")
                        fetchUnregisteredCompetitors(competitionId)
                    },
                    onError = { errorMessage, statusCode ->
                        Log.e("CompetitorViewModel", "Error: $errorMessage")
                    }
                )
            }catch (e: Exception){
                Log.e("CompetitionViewModel", "Exception: ${e.message}", e)
            }
        }
    }

}