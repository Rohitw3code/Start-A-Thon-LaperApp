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

class TaskCompletedReceivedActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()
    lateinit var requestId: String
    lateinit var expertId: String
    lateinit var techId: String
    lateinit var ps: String
    lateinit var expertName: String
    lateinit var expertImageUrl: String

    private lateinit var userName: TextView
    private lateinit var userImage: CircleImageView
    private lateinit var problemStatement: TextView
    private lateinit var taskCompleteBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_completed_receive)

        userName = findViewById(R.id.task_completed_name)
        userImage = findViewById(R.id.task_completed_image)
        problemStatement = findViewById(R.id.task_completed_problem_statement)
        taskCompleteBtn = findViewById(R.id.task_completed_btn)

        requestId = intent.getStringExtra("requestId").toString()
        expertId = intent.getStringExtra("userId").toString()
        techId = intent.getStringExtra("techId").toString()
        expertName = intent.getStringExtra("expertName").toString()
        ps = intent.getStringExtra("ps").toString()
        expertImageUrl = intent.getStringExtra("expertImageUrl").toString()

        taskCompleteBtn.setOnClickListener {
            taskComplete()
        }

        Glide.with(baseContext).load(expertImageUrl).into(userImage)
        userName.text = expertName
        problemStatement.text = ps


    }

    fun fetchDetail() {
        userRef.document(auth.uid.toString())
            .collection("requestSent")
            .document(requestId.trim())
            .get().addOnSuccessListener { doc ->
                techId = doc.getString("techId").toString()
                ps = doc.getString("desc").toString()
                userRef.document(expertId.trim())
                    .get().addOnSuccessListener { udoc ->
                        val expertName = udoc.getString("username").toString().trim()
                        val expertImageURl = doc.getString("userImageUrl").toString().trim()
                        Glide.with(baseContext).load(expertImageURl).into(userImage)
                        userName.text = expertName
                        taskCompleteBtn.visibility = View.VISIBLE
                    }
            }
    }

    fun taskComplete() {
        userRef.document(expertId)
            .collection("availableClient")
            .document(requestId)
            .delete()
        userRef.document(auth.uid.toString())
            .collection("availableExpert")
            .document(requestId)
            .delete()

        userRef.document(auth.uid.toString())
            .collection("requestSent")
            .document(requestId)
            .update("taskCompleted", true)

        userRef.document(expertId)
            .collection("requestReceived")
            .document(requestId)
            .update("taskCompleted", true)

        userRef.document(auth.uid.toString())
            .collection("taskCompletedRequest")
            .document(requestId)
            .update("completed", true)

        val obj = NotificationSender(baseContext, expertId)
        obj.sendTaskCompletedNotification(techId, requestId, ps)

        Toast.makeText(baseContext, "Task Completed Congrats", Toast.LENGTH_SHORT).show()

    }


}