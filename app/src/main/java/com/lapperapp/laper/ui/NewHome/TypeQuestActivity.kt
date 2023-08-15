package com.lapperapp.laper.ui.NewHome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import com.lapperapp.laper.R

class TypeQuestActivity : AppCompatActivity() {
    private lateinit var next: Button
    private lateinit var toolbar: Toolbar
    private lateinit var problemStatement: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_type_quest)

        toolbar = findViewById(R.id.type_quest_toolbar)
        problemStatement = findViewById(R.id.addition_detail_type_quest)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        }

        var imageUri = intent.getStringExtra("image_uri")

        next = findViewById(R.id.type_quest_next_btn)
        next.setOnClickListener { v ->
            if (!problemStatement.text.trim().toString().isEmpty()){
                val scIntent = Intent(baseContext, SelectCategoryActivity::class.java)
                scIntent.putExtra("ps_value", problemStatement.text.toString())
                scIntent.putExtra("image_uri", imageUri)
                startActivity(scIntent)
            }
            else{
                Toast.makeText(baseContext,"Problem statement can't be empty",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

}