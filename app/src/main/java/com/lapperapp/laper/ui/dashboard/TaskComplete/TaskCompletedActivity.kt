package com.lapperapp.laper.ui.dashboard.TaskComplete

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Notification.NotificationSender
import com.lapperapp.laper.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat

class TaskCompletedActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var userRef = db.collection("users")
    var techRef = db.collection("tech")
    val auth = FirebaseAuth.getInstance()

    private lateinit var userId: String
    private lateinit var requestId: String
    private lateinit var techId: String
    private lateinit var ps: String

    lateinit var nameView: TextView
    lateinit var techNameView: TextView
    lateinit var descView: TextView
    lateinit var dateView: TextView
    lateinit var userImgView: CircleImageView
    lateinit var completeBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_complete)

        nameView = findViewById(R.id.project_complete_user_name)
        techNameView = findViewById(R.id.project_complete_tech)
        descView = findViewById(R.id.project_complete_desc)
        dateView = findViewById(R.id.project_complete_user_date)
        userImgView = findViewById(R.id.project_complete_user_image)
        completeBtn = findViewById(R.id.project_complete_accept_btn)

        userId = intent.getStringExtra("userId").toString()
        requestId = intent.getStringExtra("requestId").toString()
        techId = intent.getStringExtra("techId").toString()

        completeBtn.setOnClickListener {
            completeProject()
        }



        fetchRequest()
        fetchUser()
        fetchTech()

    }

    fun completeProject() {
        val obj = NotificationSender(baseContext, userId)
        obj.sendTaskCompleteRequestNotification(techId, requestId, ps)
    }

    fun fetchRequest() {
        userRef.document(userId.trim())
            .collection("requestSent")
            .document(requestId.trim())
            .get().addOnSuccessListener { doc ->
                ps = doc.getString("desc").toString()
                try {
                    val reqDate = doc.getLong("requestSentDate") as Long
                    val sdf = SimpleDateFormat("dd-M-yyyy hh:mm")
                    val currentDate = sdf.format(reqDate)
                    dateView.text = currentDate
                } catch (exception: NumberFormatException) {
                    dateView.text = "Error"
                }
                descView.text = ps
                completeBtn.visibility = View.VISIBLE
            }
    }

    fun fetchTech() {
        techRef.document(techId.trim()).get().addOnSuccessListener { documents ->
            val tName = documents.getString("name").toString().trim()
            val tImageUrl = documents.get("imageURL").toString().trim()
            techNameView.text = tName
            Glide.with(this).load(tImageUrl).into(userImgView)
        }
    }

    fun fetchUser() {
        userRef.document(userId.trim()).get().addOnSuccessListener { documents ->
            if (documents != null) {
                val uImageUrl = documents.get("userImageUrl").toString()
                val uName = documents.get("username").toString()
                nameView.text = uName
                Glide.with(this).load(uImageUrl).into(userImgView)
            }
        }.addOnFailureListener { exception ->
            run {
                Toast.makeText(applicationContext, "" + exception.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}