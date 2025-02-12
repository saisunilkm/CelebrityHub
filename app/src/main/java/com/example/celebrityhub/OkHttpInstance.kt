package com.example.celebrityhub

import okhttp3.OkHttpClient
import okhttp3.Request

object OkHttpInstance {
    private val client = OkHttpClient()

    fun createRequest(url: String): Request {
        return Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJjNWE5OTZkOTBhYzFkMmNmZDdmZDVmOGMyZjQyODM0OCIsIm5iZiI6MTczOTIzNTQzMi41ODQsInN1YiI6IjY3YWFhMDY4NWMwZTY3ZTU2YmJiMTdiMSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.9vxxE9yKKRffbNGIloTxflrtNLMPfu-B24UKvMJqjKE"
            )
            .build()
    }

    fun getClient(): OkHttpClient {
        return client
    }
}