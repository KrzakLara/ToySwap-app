package com.example.toyswap

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.toyswap.R
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

data class PostDto(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val condition: Condition

)

enum class Condition {
    NEW,
    USED,
    REFURBISHED
}

class ProductActivity : AppCompatActivity() {
    private lateinit var productImage: ImageView
    private lateinit var productTitle: TextView
    private lateinit var productDescription: TextView
    private lateinit var productPrice: TextView
    private lateinit var addToCartButton: Button
    private lateinit var client: OkHttpClient
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // Inicijalizacija pogleda
        productImage = findViewById(R.id.product_image)
        productTitle = findViewById(R.id.product_title)
        productDescription = findViewById(R.id.product_description)
        productPrice = findViewById(R.id.product_price)
        addToCartButton = findViewById(R.id.add_to_cart_button)

        // Postavljanje podataka o proizvodu
        val productImageResource = R.drawable.toys
        val productTitleText = getString(R.string.product_title)
        val productDescriptionText = getString(R.string.product_description_goes_here)
        val productPriceText = getString(R.string._9_99)

        productImage.setImageResource(productImageResource)
        productTitle.text = productTitleText
        productDescription.text = productDescriptionText
        productPrice.text = productPriceText

        client = OkHttpClient()

        val postId = intent.getLongExtra("postId", 1)

        val request = Request.Builder()
            .url("http://192.168.68.105:8080/api/post/$postId")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val post = Gson().fromJson(responseBody, PostDto::class.java)

                    runOnUiThread {
                        // Ažuriranje korisničkog sučelja s dohvaćenim podacima o postu
                        productTitle.text = post.title
                        productDescription.text = post.description
                        productPrice.text = post.price.toString()
                    }
                } else {

                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // Greška prilikom izvršavanja zahtjeva
                    Toast.makeText(applicationContext, "Greška: " + e.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }}