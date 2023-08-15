package com.lapperapp.laper.Categories

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class AllCategoryActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var techRef = db.collection("tech")
    var data = ArrayList<CategoryModel>()
    private lateinit var adapter: CategoryAdapter
    lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_category)
        toolbar = findViewById<Toolbar>(R.id.all_category_toolbar)
        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.all_category_recycler_view)

        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        adapter = CategoryAdapter(data)
        recyclerview.layoutManager = GridLayoutManager(baseContext, 3)
        recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()

        fetchCats()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
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
            {

            }
        }
    }

}