package com.lapperapp.laper.ui.dashboard

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
import kotlin.properties.Delegates

class RequestPopActivity : AppCompatActivity() {
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
    var accepted by Delegates.notNull<Boolean>()

    lateinit var nameView: TextView
    lateinit var techNameView: TextView
    lateinit var descView: TextView
    lateinit var dateView: TextView
    lateinit var userImgView: CircleImageView
    lateinit var acceptBtn: TextView
    lateinit var declineBtn: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_pop)

        nameView = findViewById(R.id.request_pop_up_user_name)
        descView = findViewById(R.id.request_pop_up_desc)
        userImgView = findViewById(R.id.request_pop_up_user_image)
        dateView = findViewById(R.id.request_pop_up_user_date)
        techNameView = findViewById(R.id.request_pop_up_tech)
        acceptBtn = findViewById(R.id.request_pop_up_accept_btn)
        declineBtn = findViewById(R.id.request_pop_up_decline_btn)

        userName = intent.getStringExtra("username").toString()
        imgUrl = intent.getStringExtra("userImageUrl").toString()
        techId = intent.getStringExtra("techId").toString()
        desc = intent.getStringExtra("desc").toString()
        userId = intent.getStringExtra("userId").toString()
        reqId = intent.getStringExtra("requestId").toString()
        accepted = intent.getBooleanExtra("accepted", false)

        nameView.text = userName
        descView.text = desc


        Glide.with(baseContext).load(imgUrl).into(userImgView)

        acceptBtn.setOnClickListener {
            setAvailable()
            accepted = true
        }

        declineBtn.setOnClickListener {
            declineUSer()
        }

        if (accepted) {
            acceptBtn.visibility = View.GONE
        }


        getTech()
        checkAva()

    }

    private fun checkAva() {
        userRef.document(auth.uid.toString())
            .collection("requestReceived")
            .document(reqId)
            .get().addOnSuccessListener { doc ->
                val a = doc.getBoolean("accepted") as Boolean
                if (a) {
                    acceptBtn.visibility = View.GONE
                } else {
                    acceptBtn.visibility = View.VISIBLE
                }
            }
    }

    private fun declineUSer() {
        userRef.document(auth.uid.toString())
            .collection("requestReceived")
            .document(reqId)
            .update("declined", true)
        userRef.document(userId)
            .collection("requestSent")
            .document(reqId)
            .update("declined", true)
        userRef.document(auth.uid.toString())
            .collection("availableClient")
            .document(reqId)
            .delete()
        userRef.document(userId)
            .collection("availableExpert")
            .document(reqId)
            .delete()
        Toast.makeText(baseContext, "Declined", Toast.LENGTH_SHORT).show()

        val obj = NotificationSender(baseContext, userId)
        obj.requestDeclined(desc)
    }

    private fun setAvailable() {
        userRef.document(auth.uid.toString())
            .collection("requestReceived")
            .document(reqId)
            .update("accepted", true)
        userRef.document(userId)
            .collection("requestSent")
            .document(reqId)
            .update("accepted", true)
        val avaHash = hashMapOf(
            "techId" to techId,
            "startDate" to System.currentTimeMillis(),
            "endDate" to -1,
            "techId" to techId,
            "expertId" to auth.uid.toString(),
            "requestId" to reqId,
            "problemStatement" to desc
        )
        userRef.document(userId)
            .collection("availableExpert")
            .document(reqId)
            .set(avaHash)
            .addOnSuccessListener {
                Toast.makeText(baseContext, "Request Accepted", Toast.LENGTH_SHORT).show()

                val obj = NotificationSender(baseContext, userId)
                obj.sendRequestAccept(desc)
            }
        val avaExpertHash = hashMapOf(
            "techId" to techId,
            "startDate" to System.currentTimeMillis(),
            "endDate" to -1,
            "techId" to techId,
            "userId" to userId,
            "requestId" to reqId,
            "problemStatement" to desc
        )
        userRef.document(auth.uid.toString())
            .collection("availableClient")
            .document(reqId)
            .set(avaExpertHash)
    }


    fun getTech() {
        techRef.document(techId.trim())
            .get().addOnSuccessListener { doc ->
                val techName = doc.getString("name").toString()
                val techBannerUrl = doc.getString("imageURL").toString()
                techNameView.text = techName
            }
    }

}