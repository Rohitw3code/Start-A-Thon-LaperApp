package com.lapperapp.laper.ui.dashboard.RequestSent

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class RequestSentActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()
    private lateinit var reqSentModelModel: ArrayList<RequestSentModel>
    private lateinit var reqSentAdapter: RequestSentAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_sent)

        recyclerView = findViewById(R.id.request_sent_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        reqSentModelModel = ArrayList()
        reqSentAdapter = RequestSentAdapter(reqSentModelModel)
        recyclerView.adapter = reqSentAdapter
        reqSentAdapter.notifyDataSetChanged()


        getRequestSent()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun getRequestSent() {
        userRef.document(auth.uid.toString())
            .collection("requestSent")
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val reqSentDate = doc.getLong("requestSentDate") as Long
                    val expertId = doc.getString("expertId").toString()
                    val requestId = doc.getString("requestId").toString()
                    val ps = doc.getString("desc").toString()
                    val techId = doc.getString("techId").toString()
                    val cancel = doc.getBoolean("cancelled") as Boolean

                    userRef.document(expertId).get().addOnSuccessListener { documents ->
                        if (documents != null) {
                            val uImageUrl = documents.get("userImageUrl").toString()
                            val uName = documents.get("username").toString()

                            reqSentModelModel.add(
                                RequestSentModel(
                                    reqSentDate,
                                    expertId,
                                    requestId,
                                    uName,
                                    uImageUrl,
                                    ps,
                                    cancel,
                                    techId
                                )
                            )
                            reqSentAdapter.notifyDataSetChanged()
                        }
                        reqSentAdapter.notifyDataSetChanged()
                    }

                }
            }
    }

}