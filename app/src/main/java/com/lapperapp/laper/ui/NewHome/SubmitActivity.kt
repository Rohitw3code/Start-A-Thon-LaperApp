package com.lapperapp.laper.ui.NewHome

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.lapperapp.laper.R

class SubmitActivity : AppCompatActivity() {
    private lateinit var ps: EditText
    private lateinit var selectedCat: TextView
    private lateinit var submitDate: TextView
    private lateinit var submitBtn: RelativeLayout
    private lateinit var submitImage: ImageView

    private lateinit var psValue: String
    private lateinit var categoryValue: String
    private lateinit var dateValue: String
    private lateinit var imageUri: String

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit)
        ps = findViewById(R.id.submit_problem_statement)
        selectedCat = findViewById(R.id.submit_categories)
        submitDate = findViewById(R.id.submit_date)
        submitBtn = findViewById(R.id.submit_btn)
        submitImage = findViewById(R.id.submit_upload_image)

        psValue = intent.getStringExtra("ps_value").toString()
        imageUri = intent.getStringExtra("image_uri").toString()
        categoryValue = intent.getStringExtra("cate_value").toString()

    }


}