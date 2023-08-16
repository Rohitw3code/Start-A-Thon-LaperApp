package com.lapperapp.laper.ui.dashboard.RequestSent

import android.os.Bundle
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
import kotlin.properties.Delegates

class RequestSentViewActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var auth = FirebaseAuth.getInstance()
    var techRef = db.collection("tech")
    var userRef = db.collection("users")

    lateinit var userName: String
    lateinit var techId: String
    lateinit var desc: String
    lateinit var imgUrl: String
    lateinit var userId: String
    lateinit var reqId: String
    var cancelled by Delegates.notNull<Boolean>()

    lateinit var nameView: TextView
    lateinit var techNameView: TextView
    lateinit var descView: TextView
    lateinit var dateView: TextView
    lateinit var userImgView: CircleImageView
    lateinit var cancelBtn: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_sent_view)

        nameView = findViewById(R.id.request_sent_user_name)
        descView = findViewById(R.id.request_sent_desc)
        userImgView = findViewById(R.id.request_sent_user_image)
        dateView = findViewById(R.id.request_sent_user_date)
        techNameView = findViewById(R.id.request_sent_tech)
        cancelBtn = findViewById(R.id.request_sent_cancel_btn)

        userName = intent.getStringExtra("username").toString()
        imgUrl = intent.getStringExtra("userImageUrl").toString()
        techId = intent.getStringExtra("techId").toString()
        desc = intent.getStringExtra("desc").toString()
        userId = intent.getStringExtra("userId").toString()
        reqId = intent.getStringExtra("requestId").toString()
        cancelled = intent.getBooleanExtra("cancelled", false)

        nameView.text = userName
        descView.text = desc
        Glide.with(baseContext).load(imgUrl).into(userImgView)


        cancelBtn.setOnClickListener {
            cancelRequest()
        }

    }


    fun cancelRequest() {
        userRef.document(auth.uid.toString())
            .collection("requestSent")
            .document(reqId)
            .delete()
        userRef.document(userId)
            .collection("requestReceived")
            .document(reqId)
            .delete()

        val obj = NotificationSender(baseContext, userId)
        obj.requestCancelled(desc)

        Toast.makeText(baseContext, "Request Cancelled", Toast.LENGTH_SHORT).show()
    }


}