package com.example.toyswap

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.toyswap.R
import com.example.toyswap.User
import com.example.toyswap.databinding.ActivityOffersBinding
import com.example.toyswap.databinding.ActivityProfileBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class ProfileActivity : AppCompatActivity() {
    private lateinit var profilePictureImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneTextView: TextView

    private lateinit var client: OkHttpClient
    private lateinit var binding: ActivityProfileBinding
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Inicijalizacija views
        profilePictureImageView = findViewById(R.id.profile_picture)
        nameTextView = findViewById(R.id.name_text)
        emailTextView = findViewById(R.id.email_text)
        phoneTextView = findViewById(R.id.phone_text)

        client = OkHttpClient()

        // Dohvat tokena iz Intent-a
        token = intent.getStringExtra("token")

        // Pokreni API poziv za dohvat podataka profila
        getProfileData()


    }

    private fun getProfileData() {
        val request = Request.Builder()
            .url("http://192.168.68.105:8080/api/profile")
            .header("Authorization", "Bearer $token") // Dodajte token u zaglavlje zahtjeva
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val user = Gson().fromJson(responseBody, User::class.java)

                    runOnUiThread {
                        // Postavljanje korisničkih podataka na odgovarajuće views
                        nameTextView.text = "${user.firstName} "
                        emailTextView.text = user.email
                        phoneTextView.text = user.phoneNumber
                    }
                } else {
                    runOnUiThread {
                        // Obrada greške pri dohvaćanju podataka
                        // Možete prikazati poruku o grešci ili poduzeti druge akcije prema potrebi
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    // Obrada greške pri izvršavanju zahtjeva
                    // Možete prikazati poruku o grešci ili poduzeti druge akcije prema potrebi
                }
            }


        })
    }

    fun openOffersActivity(view: View) {
        val intent = Intent(this, OffersActivity::class.java)
        startActivity(intent)
    }


}

