package com.lapperapp.laper.Notification

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class NotificationActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private var auth = FirebaseAuth.getInstance()
    var userRef = db.collection("users")
    private var lastRes: DocumentSnapshot? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationList: ArrayList<NotificationModel>
    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var nestedScrollView: NestedScrollView
    private lateinit var loadMore: TextView
    private lateinit var uniqueId: List<String>
    private lateinit var noNoti: LottieAnimationView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        recyclerView = findViewById(R.id.notification_recycler_view)
        nestedScrollView = findViewById(R.id.notification_nest_scroll_view)
        loadMore = findViewById(R.id.notification_load_more)
        noNoti = findViewById(R.id.no_notification_lotti)

        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        notificationList = ArrayList()
        uniqueId = ArrayList()


        notificationAdapter = NotificationAdapter(notificationList)
        recyclerView.adapter = notificationAdapter
        notificationAdapter.notifyDataSetChanged()

        loadMore.setOnClickListener {
            fetchNotification()
        }


        fetchNotification()
        resetNotification()

    }

    fun resetNotification() {
        userRef.document(auth.uid.toString())
            .update("notificationCount", 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchNotification() {
        var query: Query
        if (lastRes == null) {
            query = userRef.document(auth.uid.toString()).collection("notification")
                .orderBy("time", Query.Direction.DESCENDING).limit(20)
        } else {
            query = userRef.document(auth.uid.toString()).collection("notification")
                .orderBy("time", Query.Direction.DESCENDING).startAfter(lastRes).limit(5)
        }

        query.get()
            .addOnSuccessListener { docs ->
                if (docs.documents.count() < 1) {
                    noNoti.visibility = View.VISIBLE
                }
                for (doc in docs.documents) {
                    val type = doc.getString("type").toString()
                    val title = doc.getString("title").toString()
                    val text = doc.getString("text").toString()
                    val time = doc.getLong("time") as Long
                    val userId = doc.getString("userId").toString()
                    if (!uniqueId.contains(doc.id)) {
                        userRef.document(userId)
                            .get().addOnSuccessListener { udoc ->
                                val userImageUrl = udoc.getString("userImageUrl").toString()
                                notificationList.add(
                                    NotificationModel(
                                        userImageUrl,
                                        title,
                                        text,
                                        type,
                                        time
                                    )
                                )
                                notificationAdapter.notifyDataSetChanged()
                                if (docs.size() > 0) {
                                    lastRes = docs.documents.get(docs.size() - 1)
                                }
                            }
                        uniqueId = uniqueId + doc.id
                    }
                }
            }


    }


}