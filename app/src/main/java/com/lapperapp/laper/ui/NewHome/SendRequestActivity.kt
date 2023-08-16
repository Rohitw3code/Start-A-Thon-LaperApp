package com.lapperapp.laper.ui.NewHome

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.github.tommykw.tagview.TagView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lapperapp.laper.MainActivity
import com.lapperapp.laper.PhotoActivity
import com.lapperapp.laper.R
import com.lapperapp.laper.service.PushNotification

class SendRequestActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var pushRef = db.collection("requests")
    var userRef = db.collection("users")
    var techRef = db.collection("tech")
    var auth = FirebaseAuth.getInstance()

    private lateinit var ps: TextView
    private lateinit var tags: TextView
    private lateinit var image: ImageView

    private lateinit var toolbar: Toolbar
    private lateinit var psVale: String
    private lateinit var imageUri: String
    private lateinit var category: List<SelectCategorymodel>

    private lateinit var askBtn: Button
    private lateinit var progress: ProgressBar


    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_request)
        toolbar = findViewById(R.id.send_request_toolbar)
        askBtn = findViewById(R.id.ask_btn_send_request)
        progress = findViewById(R.id.progress_send_request)

        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        ps = findViewById(R.id.ps_send_request)
        tags = findViewById(R.id.category_send_request)
        image = findViewById(R.id.image_send_request)

        psVale = intent.getStringExtra("ps_value").toString()
        imageUri = intent.getStringExtra("image_uri").toString()
        category = intent.getParcelableArrayListExtra<SelectCategorymodel>("tags")!!

        ps.text = psVale
        var tag = ""
        for (cat in category) {
            tag += "#" + cat.title + " "
        }
        tags.text = tag

        image.setImageURI(Uri.parse(imageUri))

        image.setOnClickListener { v ->
            val pintent = Intent(baseContext, PhotoActivity::class.java)
            pintent.putExtra("image_uri", imageUri)
            startActivity(pintent)
        }

        askBtn.setOnClickListener { v ->
            Toast.makeText(baseContext,"clicked",Toast.LENGTH_SHORT).show()
            if (!psVale.trim().isEmpty()) {
                progress.visibility = View.VISIBLE
                if (imageUri.trim().isEmpty()) {
                    pushRequest("")
                } else {
                    uploadImg()
                }
            } else {
                Toast.makeText(
                    baseContext,
                    "Can not send Empty problem statement request",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    fun getExtensionFromUri(uri: Uri, context: Context): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            ?: return null // Return null if extension not found

        return mimeTypeMap.getExtensionFromMimeType(context.contentResolver.getType(uri))
            ?: return extension // Return original extension if MIME type not found
    }


    fun getFileNameFromUri(context: Context, uri: Uri): String {
        var filename = ""

        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            it.moveToFirst()
            filename =
                "image_${System.currentTimeMillis()}." + getExtensionFromUri(uri, baseContext)
        }
        return filename
    }

    fun uploadImg() {
        // Get the URI of the local file you want to upload
        val fileUri = Uri.parse(imageUri)

        val sd = getFileNameFromUri(applicationContext, fileUri!!)

        // Get a reference to the Firebase Storage root
        val storageRef = Firebase.storage.reference

        // Create a reference to the image file you want to upload
        val imageRef = storageRef.child("images/$sd")

        // Upload the file to Firebase Storage
        imageRef.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image upload succeeded, get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Do something with the download URL
                    val downloadUrl = uri.toString()
                    pushRequest(downloadUrl)
                    Log.d(TAG, "Download URL: $downloadUrl")
                }.addOnFailureListener { exception ->
                    // Handle any errors in getting the download URL
                    Log.e(TAG, "Error getting download URL: $exception")
                    progress.visibility = View.GONE
                    Toast.makeText(baseContext, "Fail downloading ", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .addOnFailureListener { exception ->
                // Handle any errors in uploading the image
                Log.e(TAG, "Error uploading image: $exception")
                progress.visibility = View.GONE
                Toast.makeText(baseContext, "Error uploading image ", Toast.LENGTH_SHORT)
                    .show()
            }

    }

    fun sendNotificationToAllExperts(pst:String){
        val ps = PushNotification(baseContext)
        val db = Firebase.firestore
        val expertRef = db.collection("experts")
        expertRef.limit(10).get().addOnSuccessListener { docs->
            for (doc in docs.documents){
                ps.sendNotification(doc.id, "New request", pst, "0")
            }
        }

    }

    fun pushRequest(url: String) {
        val retime = System.currentTimeMillis()
        val pst = psVale
        val reqHash = hashMapOf(
            "clientId" to auth.uid,
            "requestTime" to retime,
            "problemStatement" to pst,
            "accepted" to false,
            "imageURL" to url,
            "expertId" to "all",
            "problemSolved" to false,
            "requiredTech" to category.map { it.id }.toMutableList()
        )

        pushRef.document(retime.toString()).set(reqHash).addOnCompleteListener {
            progress.visibility = View.GONE
            sendNotificationToAllExperts(pst)
            Toast.makeText(baseContext, "Request Sent!", Toast.LENGTH_SHORT).show()
            askBtn.isEnabled = false
            val mrintent = Intent(baseContext, MainActivity::class.java)
            startActivity(mrintent)
            finish()
        }


    }

}