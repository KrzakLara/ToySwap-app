package com.example.toyswap



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.toyswap.databinding.ActivityLoginBinding
import com.google.android.material.color.utilities.Score.score
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            // Retrieve email and password from TextInputEditText fields
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            // Call the login API and handle the response
            performLogin(email, password)
        }

        binding.btnRegister.setOnClickListener {
            // Redirect to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }

    private fun performLogin(email: String, password: String) {
        val url = "http://192.168.68.105:8080/api/login"


        val client = OkHttpClient()

        val requestBody = JSONObject()
            .put("email", email)
            .put("password", password)
            .toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val jsonResponse = JSONObject(responseBody)
                    val token = jsonResponse.getString("token")
                    val userJson = jsonResponse.getJSONObject("user")

                    val user = User(
                        email = userJson.getString("email"),
                        firstName = userJson.getString("firstName"),
                        lastName = userJson.getString("lastName"),
                        role = userJson.getString("role"),
                        phoneNumber = userJson.getString("phoneNumber"),
                        enabled = userJson.getBoolean("enabled"),
                        )

                    val loginResponse = LoginResponse(token, user)
                    handleLoginResponse(loginResponse)
                } else {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace() // Dodajte ovo kako biste ispisali informacije o iznimci u konzoli
                    Log.e("LoginActivity", "Error: ${e.message}") // Dodajte ovo za ispisivanje poruke o pogrešci u logu aplikacije
                }
            }

        })
    }


    private fun handleLoginResponse(loginResponse: LoginResponse) {
        val token = loginResponse.token
        val user = loginResponse.user

        // Prikazuje poruku (toast) s tokenom
        val message = "Prijava uspješna. Token: $token"
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        // Preusmjeravanje s tokenom
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("token", token)
        startActivity(intent)

        // Završava trenutnu aktivnost kako bi se spriječilo vraćanje na zaslon za prijavu pritiskom na tipku "back"
        finish()
    }




}

data class User(
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val phoneNumber: String,
    val enabled: Boolean,
)

data class LoginResponse(val token: String, val user: User)

