package com.example.sharememeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    var currentUrl:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    fun loadMeme(){
        progressBar.visibility=View.VISIBLE
        val retrofitBuilder=Retrofit.Builder().baseUrl("https://meme-api.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create()).build().create(API_Service::class.java)
        retrofitBuilder.getMeme().enqueue(object : Callback<MemeData>{
            override fun onResponse(call: Call<MemeData>, response: Response<MemeData>) {
                if(response.isSuccessful){
                    currentUrl= response.body()?.url.toString()
                    Glide.with(applicationContext).load(response.body()?.url).listener(object :RequestListener<Drawable>{
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility=View.GONE
                            return false
                        }

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility=View.GONE
                            return false
                        }
                    }).into(memeImg)
                }
            }

            override fun onFailure(call: Call<MemeData>, t: Throwable) {
                Toast.makeText(applicationContext,t.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun shareMeme(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Checkout this meme $currentUrl")
        val chooser=Intent.createChooser(intent,"Share this meme using...")
        startActivity(intent)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}