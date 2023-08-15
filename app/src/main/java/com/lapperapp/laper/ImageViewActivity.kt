package com.lapperapp.laper

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageViewActivity : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var url: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)
        imageView = findViewById(R.id.image_view_show)

        url = intent.getStringExtra("url").toString()
        Glide.with(baseContext).load(url).into(imageView)


    }

}