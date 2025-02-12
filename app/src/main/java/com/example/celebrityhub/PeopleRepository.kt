package com.example.celebrityhub

import android.util.Log
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

class PeopleRepository {

    fun getPopularPeople(): List<Person> {
        val peopleList = mutableListOf<Person>()
        val totalPages = 30

        for (currentPage in 1..totalPages) {
            val url = "https://api.themoviedb.org/3/person/popular?page=$currentPage"

            try {
                val request = OkHttpInstance.createRequest(url)
                val response = OkHttpInstance.getClient().newCall(request).execute()
                val responseBody = response.body?.string() ?: continue

                val resultsArray = JSONObject(responseBody).optJSONArray("results") ?: JSONArray()

                for (i in 0 until resultsArray.length()) {
                    val personJson = resultsArray.getJSONObject(i)
                    val knownForOverviews = mutableListOf<String>()

                    val knownForArray = personJson.optJSONArray("known_for") ?: JSONArray()
                    var knownForItem: JSONObject? = null
                    for (j in 0 until knownForArray.length()) {
                         knownForItem = knownForArray.getJSONObject(j)
                        knownForOverviews.add(knownForItem.optString("overview", "No overview available"))
                    }

                    val person = Person(
                        id = personJson.getInt("id"),
                        name = personJson.getString("name"),
                        profilePath = personJson.optString("profile_path", null),
                        overview = knownForItem!!.getString("overview")
                    )
                    peopleList.add(person)
                }
            } catch (e: Exception) {
                Log.e("PeopleRepository", "Error fetching people from page $currentPage", e)
            }
        }

        return peopleList
    }

    fun searchPeople(query: String, page: Int): List<Person> {
        val peopleList = mutableListOf<Person>()
        val url = "https://api.themoviedb.org/3/search/person?query=$query&page=$page"

        try {
            val request = OkHttpInstance.createRequest(url)
            val response: Response = OkHttpInstance.getClient().newCall(request).execute()
            val responseBody = response.body?.string() ?: return peopleList

            val jsonObject = JSONObject(responseBody)
            val resultsArray = jsonObject.getJSONArray("results")


            for (i in 0 until resultsArray.length()) {
                val personJson = resultsArray.getJSONObject(i)
                val knownForOverviews = mutableListOf<String>()

                val knownForArray = personJson.optJSONArray("known_for") ?: JSONArray()
                var knownForItem: JSONObject? = null
                for (j in 0 until knownForArray.length()) {
                     knownForItem = knownForArray.getJSONObject(j)
                    knownForOverviews.add(knownForItem.optString("overview", "No overview available"))
                }
                val person = Person(
                    id = personJson.getInt("id"),
                    name = personJson.getString("name"),
                    profilePath = personJson.optString("profile_path", null),
                    overview = knownForItem!!.getString("overview")
                )
                peopleList.add(person)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return peopleList
    }

}