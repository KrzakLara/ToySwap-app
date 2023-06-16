package com.example.toyswap

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.toyswap.databinding.ActivityOffersBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class OffersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOffersBinding
    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offers)

        client = OkHttpClient()

        val request = Request.Builder()
            .url("http://192.168.68.105:8080/api/posts") // Replace with your actual backend URL
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {
                    val posts = Gson().fromJson(responseBody, Array<PostDto>::class.java).toList()

                    // Set the retrieved list of posts to the binding variable
                    binding.post = posts.firstOrNull()

                    val handler = Handler(Looper.getMainLooper())
                    handler.post {
                        // Set click listener for the image view
                        binding.imageView.setOnClickListener {
                            val post = binding.post

                            post?.let {
                                // Open ProductActivity with post data
                                val intent = Intent(this@OffersActivity, ProductActivity::class.java)
                                intent.putExtra("postId", post.id)
                                startActivity(intent)
                            }
                        }

                    }
                } else {
                    // Error occurred while retrieving the posts
                    // Here you can handle errors or display an error message
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                // Error occurred while executing the request
                e.printStackTrace()
            }

            fun getFormattedTags(tags: List<String>): String {
                return TextUtils.join(", ", tags)
            }

        })
    }
}
