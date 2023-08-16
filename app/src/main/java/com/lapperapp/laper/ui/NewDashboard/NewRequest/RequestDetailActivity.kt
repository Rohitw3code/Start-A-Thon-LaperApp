package com.lapperapp.laper.ui.NewDashboard.NewRequest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.ImageViewActivity
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.NewDashboard.TagAdapter
import com.lapperapp.laper.ui.NewDashboard.TagModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class RequestDetailActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var auth = FirebaseAuth.getInstance()
    var expertRef = db.collection("experts")
    var reqRef = db.collection("requests")
    var techRef = db.collection("tech")

    lateinit var userName: String
    lateinit var problemStatement: String
    lateinit var imgUrl: String
    lateinit var expertId: String
    lateinit var reqId: String
    var requestTime by Delegates.notNull<Long>()
    lateinit var tags: ArrayList<String>

    lateinit var nameView: TextView
    lateinit var descView: TextView
    lateinit var dateView: TextView
    lateinit var userImgView: CircleImageView

    lateinit var tagRecyclerview: RecyclerView
    lateinit var tagAdapter: TagAdapter
    lateinit var tagList: ArrayList<TagModel>
    private lateinit var cancelBtn: TextView
    private lateinit var psImage:ImageView

    private lateinit var toolbar: Toolbar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_detail)

        nameView = findViewById(R.id.request_detail_user_name)
        descView = findViewById(R.id.request_detail_desc)
        userImgView = findViewById(R.id.request_detail_user_image)
        dateView = findViewById(R.id.request_detail_user_date)
        tagRecyclerview = findViewById(R.id.request_detail_tech_recyclerview)
        cancelBtn = findViewById(R.id.request_detail_cancel_btn)
        psImage = findViewById(R.id.request_detail_image)

        toolbar = findViewById<Toolbar>(R.id.request_detail_toolbar)
        setSupportActionBar(toolbar)
        var actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        tagList = ArrayList()
        tagAdapter = TagAdapter(tagList)
        val llm = LinearLayoutManager(baseContext)
        llm.orientation = RecyclerView.HORIZONTAL
        tagRecyclerview.layoutManager = llm
        tagRecyclerview.adapter = tagAdapter
        tagAdapter.notifyDataSetChanged()

        cancelBtn.setOnClickListener {
            cancelRequest()
        }


        reqId = intent.getStringExtra("requestId").toString()
        fetchRequestDetail(reqId)


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

    fun fetchTag(tagId: String) {
        techRef.document(tagId)
            .get().addOnSuccessListener { doc ->
                val imageUrl = doc.getString("imageURL").toString()
                val name = doc.getString("name").toString()
                tagList.add(TagModel(tagId, imageUrl, name))
                tagAdapter.notifyDataSetChanged()
            }

    }

    fun cancelRequest() {
        reqRef.document(reqId).delete().addOnSuccessListener {
            Toast.makeText(baseContext, "Request Deleted!", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    fun fetchRequestDetail(reqId: String) {
        reqRef.document(reqId.trim()).get().addOnSuccessListener { doc ->

            expertId = doc.getString("expertId").toString()
            tags = doc.get("requiredTech") as ArrayList<String>
            problemStatement = doc.getString("problemStatement").toString()
            requestTime = doc.getLong("requestTime") as Long
            val imageUrl = doc.getString("imageURL").toString()
            if (!imageUrl.isEmpty()){
                Glide.with(baseContext).load(imageUrl).into(psImage)
            }
            else{
                psImage.visibility = View.GONE
            }
            psImage.setOnClickListener{
                val intent = Intent(baseContext,ImageViewActivity::class.java)
                intent.putExtra("url",imageUrl)
                startActivity(intent)
            }

            for (tag in tags) {
                fetchTag(tag)
            }
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm a")
            val currentDate = sdf.format(requestTime)
            dateView.text = currentDate
            descView.text = problemStatement
            if (expertId.equals("all")) {
                nameView.text = "Expert"
            } else {
                expertRef.document(expertId)
                    .get().addOnSuccessListener { doc1 ->
                        val expertName = doc1.getString("username").toString()
                        val expertImageUrl = doc1.getString("userImageUrl").toString()
                        nameView.text = expertName
                        Glide.with(baseContext).load(expertImageUrl).placeholder(R.drawable.logo)
                            .into(userImgView)
                    }
            }
        }
    }
}