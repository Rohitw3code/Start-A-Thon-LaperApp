package com.lapperapp.laper.ui.dashboard.Users

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class UsersActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    private lateinit var userAdapter: UsersAdapter
    private lateinit var userModel: ArrayList<UsersModel>
    private lateinit var recyclerView: RecyclerView
    lateinit var usersId: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        recyclerView = findViewById(R.id.user_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        userModel = ArrayList<UsersModel>()
        userAdapter = UsersAdapter(userModel)
        recyclerView.adapter = userAdapter
        userAdapter.notifyDataSetChanged()

        usersId = listOf()

        fetchUsers()

    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchUsers() {
        userRef.orderBy(
            "lastActive",
            Query.Direction.DESCENDING
        ).limit(12).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                if (!usersId.contains(doc.id)) {
                    val imageUrl = doc.get("userImageUrl").toString()
                    val name = doc.get("username").toString()
                    val timeago = doc.getLong("lastActive") as Long
                    userModel.add(UsersModel(imageUrl, name, timeago, doc.id))
                    usersId = usersId + doc.id
                }
            }
            userAdapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->

        }
    }


}