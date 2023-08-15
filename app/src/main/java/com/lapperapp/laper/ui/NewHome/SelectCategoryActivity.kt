package com.lapperapp.laper.ui.NewHome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class SelectCategoryActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var techRef = db.collection("tech")
    var data = ArrayList<SelectCategorymodel>()
    private lateinit var adapter: SelectCategoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var next: Button
    lateinit var keys: MutableList<SelectCategorymodel>
    private lateinit var psValue: String
    private lateinit var imageUri: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_category)

        psValue = intent.getStringExtra("ps_value").toString()
        imageUri = intent.getStringExtra("image_uri").toString()

        toolbar = findViewById<Toolbar>(R.id.select_category_toolbar)
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        next = findViewById(R.id.type_quest_next_btn)
        recyclerView = findViewById(R.id.select_category_recycler_view)

        keys = mutableListOf<SelectCategorymodel>()

        adapter = SelectCategoryAdapter(data, keys)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

        next.setOnClickListener { v ->
            if(!keys.isEmpty()){
                val srintent = Intent(baseContext, SendRequestActivity::class.java)
                srintent.putExtra("ps_value", psValue)
                srintent.putExtra("image_uri", imageUri)
                srintent.putParcelableArrayListExtra("tags", ArrayList(keys))
                startActivity(srintent)
            }
            else{
                Toast.makeText(baseContext,"select atleast one", Toast.LENGTH_SHORT).show()
            }
        }


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
                data.add(SelectCategorymodel(name as String, imageUrl as String, doc.id))
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            {

            }
        }
    }


}