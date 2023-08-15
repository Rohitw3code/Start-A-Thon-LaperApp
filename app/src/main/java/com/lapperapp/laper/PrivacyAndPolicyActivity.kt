package com.lapperapp.laper

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PrivacyAndPolicyActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var adminRef = db.collection("admin")
    private lateinit var webview: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_and_policy)

        webview = findViewById(R.id.web_view_privacy_policy)
        webview.settings.javaScriptEnabled = true


        getPrivacyPolicy()

    }

    fun getPrivacyPolicy() {
        adminRef.document("privacypolicy")
            .get().addOnSuccessListener { doc ->
                val text = doc.getString("html").toString().trim()
                webview.loadData(text, "text/html", "UTF-8")
            }
    }


}