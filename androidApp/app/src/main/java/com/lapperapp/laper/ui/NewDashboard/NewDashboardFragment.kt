package com.lapperapp.laper.ui.NewDashboard

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.AllRequestsActivity
import com.lapperapp.laper.Categories.ViewAllExpertsActivity
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.NewDashboard.NewAvailableExpert.NewAvailableExpertAdapter
import com.lapperapp.laper.ui.NewDashboard.NewAvailableExpert.NewAvailableExpertModel
import com.lapperapp.laper.ui.NewDashboard.NewRequest.NewRequestAdapter
import com.lapperapp.laper.ui.NewDashboard.NewRequest.NewRequestSentModel
import com.lapperapp.laper.ui.chats.AllChatsActivity
import nl.joery.animatedbottombar.AnimatedBottomBar


class NewDashboardFragment(
    private val bottomBar: AnimatedBottomBar, private val tabToAddBadgeAt: AnimatedBottomBar.Tab
) : Fragment() {
    private lateinit var reqRecyclerView: RecyclerView
    private lateinit var aeRecyclerView: RecyclerView
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var expertsRef = db.collection("experts")
    var reqRef = db.collection("requests")
    var auth = FirebaseAuth.getInstance()
    private lateinit var allChats: CardView
    private lateinit var reqSentModelModel: ArrayList<NewRequestSentModel>
    private lateinit var reqSentAdapter: NewRequestAdapter

    private lateinit var availableExpertModel: ArrayList<NewAvailableExpertModel>
    private lateinit var availableExpertAdapter: NewAvailableExpertAdapter
    private lateinit var uReqIds: ArrayList<String>
    private lateinit var uAvaExpertIds: ArrayList<String>
    private lateinit var findDeveloper: CardView

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_dashboard, container, false)
        uReqIds = ArrayList()
        uAvaExpertIds = ArrayList()

        findDeveloper = view.findViewById(R.id.dash_find_developers)

        reqRecyclerView = view.findViewById(R.id.dashboard_new_request_recycler_view)
        reqRecyclerView.layoutManager = LinearLayoutManager(context)
        reqSentModelModel = ArrayList()
        reqSentAdapter = NewRequestAdapter(reqSentModelModel)
        reqRecyclerView.adapter = reqSentAdapter

        aeRecyclerView = view.findViewById(R.id.dashboard_available_expert_recycler_view)
        aeRecyclerView.layoutManager = LinearLayoutManager(context)
        availableExpertModel = ArrayList()
        availableExpertAdapter = NewAvailableExpertAdapter(availableExpertModel)
        aeRecyclerView.adapter = availableExpertAdapter
        availableExpertAdapter.notifyDataSetChanged()

        allChats = view.findViewById(R.id.dash_all_chats)

        allChats.setOnClickListener {
            val intent = Intent(context, AllChatsActivity::class.java)
            startActivity(intent)
        }

        findDeveloper.setOnClickListener {
            val intent = Intent(context, ViewAllExpertsActivity::class.java)
            startActivity(intent)
        }




        return view
    }

    override fun onStart() {
        super.onStart()
        fetchMyRequests()
        fetchAvailableExpert()
        clearNotification()
    }


    fun clearNotification() {
        userRef.document(auth.uid.toString()).update("dashboardNotification", false)
        bottomBar.clearBadgeAtTab(tabToAddBadgeAt)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchAvailableExpert() {
        val query = userRef.document(auth.uid.toString()).collection("availableExpert")
        query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            for (doc in snapshot!!.documents) {
                Log.d(TAG, "${doc.id} => ${doc.data}")
                if (!uAvaExpertIds.contains(doc.id)) {
                    if (doc.exists()) {
                        if (doc.contains("requestId") && doc.contains("acceptedTime") && doc.contains("expertId")) {
                            val reqId = doc.getString("requestId") as String
                            val acceptTime = doc.getLong("acceptedTime") as Long
                            val expertId = doc.getString("expertId") as String
                            expertsRef.document(expertId).get().addOnSuccessListener { edoc ->
                                    val name = edoc.getString("username") as String
                                    val imageUrl = edoc.getString("userImageUrl") as String
                                    expertsRef.document(expertId).collection("requests")
                                        .document(reqId).get().addOnSuccessListener { doc ->
                                            if (doc.exists()) {
                                                val ps = doc.getString("problemStatement") as String
                                                availableExpertModel.add(
                                                    NewAvailableExpertModel(
                                                        name,
                                                        imageUrl,
                                                        "",
                                                        acceptTime,
                                                        expertId,
                                                        reqId,
                                                        ps
                                                    )
                                                )
                                                availableExpertAdapter.notifyDataSetChanged()
                                                uAvaExpertIds.add(doc.id)
                                            }
                                        }
                                }

                        }
                    } else {
                        uAvaExpertIds.remove(doc.id)
                        availableExpertAdapter.notifyDataSetChanged()
                    }
                }

            }

            // Remove the IDs from the list that don't exist in the snapshot

//            val snapshotIds = snapshot.documents.map { it.id }
//            uReqIds.filter { !snapshotIds.contains(it) }.forEach {
//                val index = reqSentModelModel.indexOfFirst { model -> model.reqId == it }
//                if (index != -1) {
//                    try {
//                        availableExpertModel.removeAt(index)
//                        availableExpertAdapter.notifyItemRemoved(index)
//                    }
//                    finally{
//
//                    }
//                }
//                uAvaExpertIds.remove(it)
//            }

        }


//        userRef.document(auth.uid.toString())
//            .collection("availableExpert")
//            .get()
//            .addOnSuccessListener { docs ->
//                for (doc in docs.documents) {
//                    if (!uAvaExpertIds.contains(doc.id)) {
//                        if (doc.contains("requestId") && doc.contains("acceptTime") && doc.contains(
//                                "expertId"
//                            )
//                        ) {
//                            val reqId = doc.getString("requestId") as String
//                            val acceptTime = doc.getLong("acceptedTime") as Long
//                            val expertId = doc.getString("expertId") as String
//                            expertsRef.document(expertId)
//                                .get().addOnSuccessListener { edoc ->
//                                    val name = edoc.getString("username") as String
//                                    val imageUrl = edoc.getString("userImageUrl") as String
//                                    expertsRef.document(expertId).collection("requests")
//                                        .document(reqId).get().addOnSuccessListener { doc ->
//                                            if (doc.exists()) {
//                                                val ps = doc.getString("problemStatement") as String
//                                                availableExpertModel.add(
//                                                    NewAvailableExpertModel(
//                                                        name,
//                                                        imageUrl,
//                                                        "",
//                                                        acceptTime,
//                                                        expertId,
//                                                        reqId,
//                                                        ps
//                                                    )
//                                                )
//                                                availableExpertAdapter.notifyDataSetChanged()
//                                                uAvaExpertIds.add(doc.id)
//                                            }
//                                        }
//                                }
//
//                        }
//                    }
//                }
//            }
    }

    fun fetchMyRequests() {
        val query = reqRef.whereEqualTo("clientId", auth.uid)
        query.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for (doc in snapshot!!.documents) {
                Log.d(TAG, "${doc.id} => ${doc.data}")
                if (doc.exists()) {
                    if (!uReqIds.contains(doc.id)) {
                        val reqTime = doc.getLong("requestTime") as Long
                        val problemStatement = doc.getString("problemStatement") as String
                        val expertId = doc.getString("expertId") as String
                        val requiredTech = doc.get("requiredTech") as ArrayList<String>
                        if (expertId.equals("all")) {
                            reqSentModelModel.add(
                                NewRequestSentModel(
                                    reqTime,
                                    expertId,
                                    doc.id,
                                    "Laper Experts",
                                    "",
                                    problemStatement,
                                    requiredTech
                                )
                            )
                            uReqIds.add(doc.id)
                        } else {
                            expertsRef.document(expertId).get().addOnSuccessListener { doc1 ->
                                    if (doc1.exists()) {
                                        val expertName = doc1.getString("username").toString()
                                        val expertImageUrl =
                                            doc1.getString("userImageUrl").toString()
                                        reqSentModelModel.add(
                                            NewRequestSentModel(
                                                reqTime,
                                                expertId,
                                                doc.id,
                                                expertName,
                                                expertImageUrl,
                                                problemStatement,
                                                requiredTech
                                            )
                                        )
                                        uReqIds.add(doc.id)
                                        reqSentAdapter.notifyDataSetChanged()
                                    } else {
                                        uReqIds.remove(doc.id)
                                        reqSentAdapter.notifyDataSetChanged()
                                    }
                                }
                        }
                        reqSentAdapter.notifyDataSetChanged()
                    }
                } else {
                    uReqIds.remove(doc.id)
                    reqSentAdapter.notifyDataSetChanged()
                }
            }


            // Remove the IDs from the list that don't exist in the snapshot
            val snapshotIds = snapshot.documents.map { it.id }
            uReqIds.filter { !snapshotIds.contains(it) }.forEach {
                val index = reqSentModelModel.indexOfFirst { model -> model.reqId == it }
                if (index != -1) {
                    reqSentModelModel.removeAt(index)
                    reqSentAdapter.notifyItemRemoved(index)
                }
                uReqIds.remove(it)
            }

        }


    }

//        reqRef.whereEqualTo("clientId", auth.uid).get().addOnSuccessListener { docs ->
//            for (doc in docs.documents) {
//                if (!uReqIds.contains(doc.id)) {
//                    val reqTime = doc.getLong("requestTime") as Long
//                    val problemStatement = doc.getString("problemStatement") as String
//                    val expertId = doc.getString("expertId") as String
//                    val requiredTech = doc.get("requiredTech") as ArrayList<String>
//                    if (expertId.equals("all")) {
//                        reqSentModelModel.add(
//                            NewRequestSentModel(
//                                reqTime,
//                                expertId,
//                                doc.id,
//                                "Laper Experts",
//                                "",
//                                problemStatement,
//                                requiredTech
//                            )
//                        )
//                        uReqIds.add(doc.id)
//                    } else {
//                        expertsRef.document(expertId)
//                            .get().addOnSuccessListener { doc1 ->
//                                val expertName = doc1.getString("username").toString()
//                                val expertImageUrl = doc1.getString("userImageUrl").toString()
//                                reqSentModelModel.add(
//                                    NewRequestSentModel(
//                                        reqTime,
//                                        expertId,
//                                        doc.id,
//                                        expertName,
//                                        expertImageUrl,
//                                        problemStatement,
//                                        requiredTech
//                                    )
//                                )
//                                uReqIds.add(doc.id)
//                                reqSentAdapter.notifyDataSetChanged()
//                            }
//                    }
//                    reqSentAdapter.notifyDataSetChanged()
//                }
//            }
//        }
}

