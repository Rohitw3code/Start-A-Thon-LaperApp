package com.lapperapp.laper.query

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Categories.CategoryModel
import com.lapperapp.laper.Categories.CategoryProfileAdapter
import com.lapperapp.laper.R
import com.lapperapp.laper.service.PushNotification


class QuerySubActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var pushRef = db.collection("requests")
    var userRef = db.collection("users")
    var techRef = db.collection("tech")
    var auth = FirebaseAuth.getInstance()
    var data = ArrayList<CategoryModel>()

    private lateinit var adapter: CategoryProfileAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var submit: RelativeLayout
    private lateinit var queryEdit: EditText

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_query_submit)

        recycler = findViewById(R.id.query_category_recycler_view)
        queryEdit = findViewById(R.id.query_edit)
        submit = findViewById(R.id.query_submit)

        adapter = CategoryProfileAdapter(data)
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL)
        recycler.layoutManager = staggeredGridLayoutManager
        recycler.adapter = adapter
        adapter.notifyDataSetChanged()

        submit.setOnClickListener {
            if (!queryEdit.text.toString().trim().isEmpty()) {
                pushRequest()
            } else {
                Toast.makeText(
                    baseContext,
                    "Can not send Empty problem statement request",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


        fetchCats()

    }

    fun pushRequest() {
        val retime = System.currentTimeMillis()
        val pst = queryEdit.text.toString()
        val reqHash = hashMapOf(
            "clientId" to auth.uid,
            "requestTime" to retime,
            "problemStatement" to pst,
            "accepted" to false,
            "expertId" to "all",
            "problemSolved" to false,
            "requiredTech" to adapter.selectedArray
        )

        pushRef.document(retime.toString()).set(reqHash).addOnCompleteListener {
            val ps = PushNotification(baseContext)
            ps.sendNotification("experts", "New request", pst, "0")
            Toast.makeText(baseContext, "Request Sent!", Toast.LENGTH_SHORT).show()
            queryEdit.setText("")
        }


    }

    fun fetchCats() {
        techRef.orderBy(
            "lastVisit",
            Query.Direction.DESCENDING
        ).limit(12).get().addOnSuccessListener { documents ->
            for (doc in documents) {
                val name = doc.get("name")
                val imageUrl = doc.get("imageURL")
                data.add(CategoryModel(imageUrl as String, name as String, doc.id))
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
        }
    }

}