package com.example.celebrityhub

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class PeopleViewModel : ViewModel() {
    private val repository = PeopleRepository()
    private val _people = MutableLiveData<List<Person>>()
    val people: LiveData<List<Person>> get() = _people
    private var currentPage = 1
    private var totalPages = 1
    private var isSearching = false

    fun loadPopularPeople() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val peopleList = repository.getPopularPeople()
                _people.postValue(peopleList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun searchPeople(query: String) {
        isSearching = true
        currentPage = 1

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val peopleList = repository.searchPeople(query, currentPage)
                _people.postValue(peopleList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun fetchPeople(url: String, append: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = OkHttpInstance.createRequest(url)
                val response = OkHttpInstance.getClient().newCall(request).execute()
                val responseBody = response.body?.string() ?: return@launch

                val jsonObject = JSONObject(responseBody)
                totalPages = jsonObject.optInt("total_pages", 1)
                val resultsArray = jsonObject.getJSONArray("results")

                val newPeopleList = mutableListOf<Person>()

                for (i in 0 until resultsArray.length()) {
                    val personJson = resultsArray.getJSONObject(i)
                    val knownForOverviews = mutableListOf<String>()

                    val knownForArray = personJson.optJSONArray("known_for") ?: JSONArray()
                    for (j in 0 until knownForArray.length()) {
                        val knownForItem = knownForArray.getJSONObject(j)
                        knownForOverviews.add(
                            knownForItem.optString(
                                "overview",
                                "No overview available"
                            )
                        )
                    }

                    val person = Person(
                        id = personJson.getInt("id"),
                        name = personJson.getString("name"),
                        profilePath = personJson.optString("profile_path", null),
                        overview = personJson.getString("overview")
                    )
                    newPeopleList.add(person)
                }

                _people.postValue(if (append) (_people.value.orEmpty() + newPeopleList) else newPeopleList)

            } catch (e: Exception) {
                Log.e("PeopleViewModel", "Error fetching people", e)
            }
        }

    }


    fun loadNextPage() {
        if (currentPage < totalPages) {
            currentPage++
            val url = if (isSearching) {
                "https://api.themoviedb.org/3/search/person?page=$currentPage"
            } else {
                "https://api.themoviedb.org/3/person/popular?page=$currentPage"
            }
            fetchPeople(url, append = true)
        }
    }
}