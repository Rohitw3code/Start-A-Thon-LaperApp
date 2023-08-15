package com.lapperapp.laper.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.MainActivity
import com.lapperapp.laper.Notification.NotificationSender
import com.lapperapp.laper.R
import com.lapperapp.laper.service.PushNotification
import java.text.SimpleDateFormat
import java.util.*

class RequestSubmitActivity : AppCompatActivity() {
    var auth = FirebaseAuth.getInstance()
    var db = Firebase.firestore
    var pushRef = db.collection("requests")
    var userRef = db.collection("users")
    var expertRef = db.collection("experts")
    private lateinit var expertId: String
    private lateinit var techId: String
    private lateinit var title: String
    private lateinit var tech: TextView
    private lateinit var desc: TextView
    private lateinit var date: TextView
    private lateinit var submit: TextView

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_submit)
        tech = findViewById(R.id.request_submit_tech_title)
        desc = findViewById(R.id.request_submit_description)
        date = findViewById(R.id.request_submit_date)
        submit = findViewById(R.id.request_submit_btn)

        expertId = intent.getStringExtra("userId").toString()
        techId = intent.getStringExtra("techId").toString()
        title = intent.getStringExtra("title").toString()

        submit.setOnClickListener {
            submitNewRequest()
        }


        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")
        val currentDate = sdf.format(System.currentTimeMillis())

        date.text = currentDate
        tech.text = title


    }


    fun submitNewRequest() {
        if (!desc.text.isEmpty()) {
            val docId = System.currentTimeMillis()
            val pst = desc.text.toString()
            val tag = ArrayList<String>()
            tag.add(techId)
            val reqHash = hashMapOf(
                "clientId" to auth.uid,
                "requestTime" to docId,
                "problemStatement" to pst,
                "accepted" to false,
                "expertId" to expertId,
                "problemSolved" to false,
                "requiredTech" to tag,
            )

            pushRef.document(docId.toString()).set(reqHash).addOnCompleteListener {
                val ps = PushNotification(baseContext)
                ps.sendNotification(expertId, "New request", pst.toString(), "0")
                Toast.makeText(baseContext, "Request Sent!", Toast.LENGTH_SHORT).show()
                desc.text = ""
                finish()
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
            }


        }

    }



    fun submitRequest() {
        if (!desc.text.isEmpty()) {
            val docId = userRef.document().id
            val ps = desc.text.toString()

            val requestReceiveHash = hashMapOf(
                "requestDate" to System.currentTimeMillis(),
                "desc" to "" + ps,
                "userId" to auth.uid,
                "techId" to techId,
                "accepted" to false,
                "declined" to false,
                "deleted" to false,
                "taskCompleted" to false
            )

            val reqSentMap = hashMapOf(
                "requestSentDate" to System.currentTimeMillis(),
                "expertId" to expertId,
                "requestId" to docId,
                "desc" to "" + ps,
                "techId" to techId,
                "accepted" to false,
                "declined" to false,
                "deleted" to false,
                "cancelled" to false,
                "taskCompleted" to false
            )

            val reqMap = hashMapOf(
                "totalRequests" to FieldValue.increment(1)
            )

            userRef.document(expertId).update(reqMap as Map<String, Any>)

            userRef.document(auth.uid.toString())
                .collection("requestSent")
                .document(docId)
                .set(reqSentMap)

            expertRef.document(expertId).collection("requestReceived")
                .document(docId)
                .set(requestReceiveHash)
                .addOnSuccessListener {
                    desc.text = ""
                    Toast.makeText(baseContext, "Request Submitted", Toast.LENGTH_SHORT).show()
                    val obj = NotificationSender(baseContext, expertId)
                    obj.sendRequestSentNotification(ps)
                    expertRef.document(auth.uid.toString())
                        .update("req", FieldValue.increment(-1))
                }
        } else {
            Toast.makeText(baseContext, "Description can't be empty", Toast.LENGTH_SHORT).show()
        }
    }
}