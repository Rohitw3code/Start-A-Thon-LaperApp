package com.lapperapp.laper.Categories

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class ViewAllExpertsActivity : AppCompatActivity() {
    var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var devAdapter: DeveloperAdapter
    var expertRef = db.collection("experts")
    private lateinit var recyclerView: RecyclerView
    var devData = ArrayList<DevModel>()
    private lateinit var toolbar: Toolbar

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_experts)
        recyclerView = findViewById(R.id.view_all_expert_recycler_view)
        toolbar = findViewById(R.id.view_all_expert_toolbar)
        devData = ArrayList()
        devAdapter = DeveloperAdapter(devData)
        recyclerView.layoutManager = GridLayoutManager(baseContext, 3)
        recyclerView.adapter = devAdapter

        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }




        getUsers()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }


    @SuppressLint("NotifyDataSetChanged")
    fun getUsers() {
        expertRef.orderBy(
            "lastActive",
            Query.Direction.DESCENDING
        ).limit(16).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val imageUrl = doc.get("userImageUrl")
                val name = doc.get("username")
                devData.add(DevModel(name as String, imageUrl as String, doc.id, 3))
            }
            devAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
        }

    }


}