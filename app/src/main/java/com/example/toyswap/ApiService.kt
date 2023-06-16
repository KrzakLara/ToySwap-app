package com.example.toyswap

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {



    @GET("/api/posts") // Replace with the appropriate URL for the posts endpoint
    fun getPosts(): Call<List<Post>>




}