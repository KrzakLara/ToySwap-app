package com.example.toyswap

import com.example.toyswap.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class UserApi {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.68.105:8080/api/profile") // Zamijenite s URL-om vašeg backend-a
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val userService: UserService = retrofit.create(UserService::class.java)

    interface UserService {
        @GET("profile/{userId}")
        fun getProfile(@Path("userId") userId: String): Call<User>
    }

    fun getProfile(userId: String, callback: ProfileCallback) {
        val call = userService.getProfile(userId)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        callback.onSuccess(user)
                    } else {
                        callback.onFailure(Throwable("User not found"))
                    }
                } else {
                    callback.onFailure(Throwable("Failed to get user profile"))
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }

    interface ProfileCallback {
        fun onSuccess(user: User)
        fun onFailure(error: Throwable)
    }
}
