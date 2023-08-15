package com.lapperapp.laper

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PhotoActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        var image = findViewById<ImageView>(R.id.photo_view)


        val imageUri = intent.getStringExtra("image_uri").toString()

        image.setImageURI(Uri.parse(imageUri))
    }
}