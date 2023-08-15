package com.lapperapp.laper.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import de.hdodenhof.circleimageview.CircleImageView

class SettingsActivity : AppCompatActivity() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    val auth = FirebaseAuth.getInstance()

    private lateinit var updateBtn: Button
    private lateinit var username: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var userImage: CircleImageView
    private lateinit var email: TextView
    private lateinit var userId: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        userImage = findViewById(R.id.setting_user_image)
        username = findViewById(R.id.setting_username)
        phoneNumber = findViewById(R.id.setting_phone_number)
        email = findViewById(R.id.setting_user_email)
        userId = findViewById(R.id.setting_user_id)
        updateBtn = findViewById(R.id.settings_update_btn)

        updateBtn.setOnClickListener {
            pushData()
        }
        fetchData()
    }

    private fun fetchData() {
        userRef.document(auth.uid.toString())
            .get().addOnSuccessListener { doc ->
                val name = doc.getString("username") as String
                val pn = doc.getString("phoneNumber") as String
                val _email = doc.getString("email") as String
                val imageUrl = doc.getString("userImageUrl") as String
                username.setText(name)
                email.text = _email
                phoneNumber.setText(pn)
                userId.text = auth.uid.toString()
                Glide.with(baseContext).load(imageUrl).into(userImage)
            }
    }

    private fun pushData() {
        val hash = hashMapOf(
            "username" to username.text.trim().toString(),
            "phoneNumber" to phoneNumber.text.trim().toString()
        )
        userRef.document(auth.uid.toString())
            .update(hash as Map<String, Any>)
        Toast.makeText(baseContext, "updated!", Toast.LENGTH_SHORT).show()
    }


}