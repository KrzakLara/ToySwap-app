package com.example.toyswap

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.toyswap.LoginActivity
import com.example.toyswap.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Tag

object RetrofitClient {
    private const val BASE_URL = "https://localhost:8080/api/register"

    val api: YourApiInterface

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(YourApiInterface::class.java)
    }
}

interface YourApiInterface {
    @POST("register")
    fun register(@Body registerRequest: RegisterRequest): Call<RegisterResponse>

    @GET("posts")
    fun getPosts(): Call<List<Post>>
}

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnRegister.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val firstName = binding.firstName.text.toString().trim()
            val lastName = binding.lastName.text.toString().trim()
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val profilePicture = binding.profilePicture.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || phoneNumber.isEmpty()) {
                // Required fields are empty
                return@setOnClickListener
            }

            val registerRequest = RegisterRequest(email, password, firstName, lastName, phoneNumber, profilePicture)
            performRegistration(registerRequest)
        }

        binding.btnBack.setOnClickListener {
            navigateToLoginActivity()
        }
    }

    private fun performRegistration(registerRequest: RegisterRequest) {
        RetrofitClient.api.register(registerRequest).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        // Registration successful
                        // Handle the response or perform any necessary actions
                        showToast("Registration successful")

                        // Example: Navigate to the login activity after successful registration
                        navigateToLoginActivity()
                    }
                } else {
                    // Registration failed
                    // Handle the error response or perform any necessary actions
                    showToast("Registration failed")
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                // Registration failed
                // Handle the error or perform any necessary actions
                showToast("Network error occurred")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String?,
    val phoneNumber: String,
    val profilePicture: String?
)

data class RegisterResponse(
    val email: String,
    val firstName: String,
    val lastName: String?,
    val phoneNumber: String,
    val profilePicture: String?
)

data class Post(
    val id: Int,
    val title: String,
    val description: String,
    val condition: String,
    val price: Double,
    val tags: List<Tag>,
    val imageIds: List<Int>
)

