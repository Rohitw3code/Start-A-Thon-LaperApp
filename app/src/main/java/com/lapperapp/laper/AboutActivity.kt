package com.lapperapp.laper

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AboutActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var adminRef = db.collection("admin")
    private lateinit var webview: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        webview = findViewById(R.id.web_view_about)
        webview.settings.javaScriptEnabled = true


        getAbout()

    }


    fun getAbout() {
        adminRef.document("about")
            .get().addOnSuccessListener { doc ->
                val text = doc.getString("html").toString().trim()
                webview.loadData(text, "text/html", "UTF-8")
            }
    }


}