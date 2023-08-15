package com.lapperapp.laper.Categories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import java.util.*

class CategoryDeveloperActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var expertRef = db.collection("experts")
    var techRef = db.collection("tech")

    lateinit var techId: String
    val data = ArrayList<DevModel>()
    val adapter = DeveloperAdapter(data)
    private lateinit var devRecyclerView: RecyclerView
    private lateinit var bannerTitle: TextView
    private lateinit var bannerDesc: TextView
    private lateinit var bannerImageView: ImageView
    private lateinit var toolbar: Toolbar


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_developer)

        devRecyclerView = findViewById(R.id.developer_recycler_view)
        bannerTitle = findViewById(R.id.category_developer_banner_title)
        bannerDesc = findViewById(R.id.category_developer_banner_desc)
        bannerImageView = findViewById(R.id.category_developer_banner_image)
        toolbar = findViewById(R.id.category_developer_toolbar)
        devRecyclerView.layoutManager = GridLayoutManager(applicationContext, 3)
        setSupportActionBar(toolbar)

        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }
        techId = intent.getStringExtra("id").toString().trim()
        devRecyclerView.adapter = adapter
        adapter.notifyDataSetChanged()



        getUsers()
        getBanner()
        setLastVisit()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    fun setLastVisit() {
        val hash = hashMapOf(
            "lastVisit" to System.currentTimeMillis()
        )
        techRef.document(techId)
            .update(hash as Map<String, Any>)
    }

    fun getBanner() {
        techRef.document(techId)
            .get()
            .addOnSuccessListener { doc ->
                val bannerImage = doc.getString("bannerImageUrl")
                val name = doc.getString("name")
                val desc = doc.getString("desc")
                bannerTitle.text = name
                if (desc != null) {
                    if (desc.trim().isEmpty()) {
                        bannerDesc.visibility = View.GONE
                    }
                }
                bannerDesc.text = desc
                Glide.with(baseContext).load(bannerImage).into(bannerImageView)
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getUserData(userId: String) {
        expertRef.document(userId.trim())
            .get().addOnSuccessListener { documents ->
                if (documents != null) {
                    val imageUrl = documents.get("userImageUrl").toString()
                    val name = documents.get("username").toString()
                    data.add(DevModel(name, imageUrl, documents.id, 5))
                    adapter.notifyDataSetChanged()
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun getUsers() {
        techRef
            .document(techId)
            .collection("experts")
            .get().addOnSuccessListener { docs ->
                for (doc in docs) {
                    getUserData(doc.id)
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener { exception ->
                {
                }
            }
    }
}