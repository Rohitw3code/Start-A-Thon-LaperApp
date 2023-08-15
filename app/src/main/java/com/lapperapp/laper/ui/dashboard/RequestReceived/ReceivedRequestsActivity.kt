package com.lapperapp.laper.ui.dashboard.RequestReceived

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Model.RequestReceivedModel
import com.lapperapp.laper.R

class ReceivedRequestsActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    private lateinit var requestRecycler: RecyclerView
    private lateinit var reqReceivedModel: ArrayList<RequestReceivedModel>
    private lateinit var reqReceivedAdapter: RequestReceivedAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_requests)

        requestRecycler = findViewById(R.id.all_requests_recycler_view)
        requestRecycler.layoutManager = LinearLayoutManager(baseContext)

        reqReceivedModel = ArrayList()
        reqReceivedAdapter = RequestReceivedAdapter(reqReceivedModel)
        requestRecycler.adapter = reqReceivedAdapter
        reqReceivedAdapter.notifyDataSetChanged()
        fetchRequests()


    }

    fun fetchRequests() {
        userRef.document(auth.uid.toString())
            .collection("requestReceived")
            .whereEqualTo("declined", false)
            .get().addOnSuccessListener { docu ->
                for (doc in docu.documents) {
                    val rUserId = doc.get("userId").toString()
                    val rDesc = doc.get("desc").toString()
                    val rTechId = doc.get("techId").toString()
                    val rDate = doc.getLong("requestDate") as Long
                    val accepted = doc.getBoolean("accepted")
                    reqReceivedModel.add(
                        RequestReceivedModel(
                            rDesc,
                            doc.id,
                            rUserId,
                            rTechId,
                            rDate,
                            accepted as Boolean
                        )
                    )
                    reqReceivedAdapter.notifyDataSetChanged()
                }
            }
    }

}