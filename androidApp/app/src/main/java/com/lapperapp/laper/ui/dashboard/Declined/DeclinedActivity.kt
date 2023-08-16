package com.lapperapp.laper.ui.dashboard.Declined

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class DeclinedActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    private lateinit var requestSentRecyclerView: RecyclerView
    private lateinit var requestReceivedDeclinedRecyclerView: RecyclerView

    private lateinit var requestSentDeclineAdapter: RequestSentDeclineAdapter
    private lateinit var requestReceivedDeclinedAdapter: RequestReceivedDeclinedAdapter

    private lateinit var requestReceivedDeclinedModel: ArrayList<RequestReceivedDeclinedModel>
    private lateinit var requestSentDeclinedModel: ArrayList<RequestSentDeclinedModel>
    lateinit var rId: List<String>
    lateinit var sId: List<String>


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_declined)

        requestReceivedDeclinedRecyclerView =
            findViewById(R.id.request_received_declined_recyclerview)

        requestSentRecyclerView = findViewById(R.id.request_sent_declined_recyclerview)

        requestSentRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        requestSentDeclinedModel = ArrayList<RequestSentDeclinedModel>()
        requestSentDeclineAdapter = RequestSentDeclineAdapter(requestSentDeclinedModel)
        requestSentRecyclerView.adapter = requestSentDeclineAdapter
        requestSentDeclineAdapter.notifyDataSetChanged()

        requestReceivedDeclinedRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        requestReceivedDeclinedModel = ArrayList<RequestReceivedDeclinedModel>()
        requestReceivedDeclinedAdapter =
            RequestReceivedDeclinedAdapter(requestReceivedDeclinedModel)
        requestReceivedDeclinedRecyclerView.adapter = requestReceivedDeclinedAdapter
        requestReceivedDeclinedAdapter.notifyDataSetChanged()

        sId = listOf()
        rId = listOf()

        fetchDeclinedRequestSend()
        fetchDeclinedRequestReceived()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchDeclinedRequestSend() {
        userRef.document(auth.uid.toString())
            .collection("requestSent")
            .whereEqualTo("declined", true)
            .get().addOnSuccessListener { docs ->
                for (doc in docs.documents) {
                    val requestId = doc.id
                    if (!sId.contains(requestId)) {
                        val expertId = doc.getString("expertId").toString()
                        val desc = doc.getString("desc").toString()
                        userRef.document(expertId)
                            .get().addOnSuccessListener { expertDoc ->
                                val expertName = expertDoc.getString("username").toString()
                                val expertImageUrl = expertDoc.getString("userImageUrl").toString()
                                requestSentDeclinedModel.add(
                                    RequestSentDeclinedModel(
                                        expertId,
                                        expertName,
                                        expertImageUrl,
                                        requestId,
                                        desc
                                    )
                                )
                                requestSentDeclineAdapter.notifyDataSetChanged()
                            }
                        sId += requestId
                    }
                }
            }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchDeclinedRequestReceived() {
        userRef.document(auth.uid.toString())
            .collection("requestReceived")
            .whereEqualTo("declined", true)
            .get().addOnSuccessListener { docs ->
                for (doc in docs.documents) {
                    val requestId = doc.id
                    if (!rId.contains(requestId)) {
                        val userId = doc.getString("userId").toString()
                        val desc = doc.getString("desc").toString()
                        userRef.document(userId)
                            .get().addOnSuccessListener { userDoc ->
                                val userName = userDoc.getString("username").toString()
                                val userImageUrl = userDoc.getString("userImageUrl").toString()
                                requestReceivedDeclinedModel.add(
                                    RequestReceivedDeclinedModel(
                                        userId,
                                        userName,
                                        userImageUrl,
                                        requestId,
                                        desc
                                    )
                                )
                                requestReceivedDeclinedAdapter.notifyDataSetChanged()
                            }
                        rId = rId + requestId
                    }
                }
            }
    }


}