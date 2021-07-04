package com.example.sharememeapp

import retrofit2.Call
import retrofit2.http.GET

interface API_Service {
    @GET("gimme")
    fun getMeme():Call<MemeData>
}