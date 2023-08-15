package com.lapperapp.laper.User.Personal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import com.lapperapp.laper.utils.TimeAgo
import java.util.*

class PersonalFragment(private var userId: String) : Fragment() {
    private val db = Firebase.firestore
    private var docsref = db.collection("experts")
    private var techRef = db.collection("tech")
    lateinit var userEmail: TextView
    lateinit var userPhone: TextView
    lateinit var userAbout: TextView
    lateinit var userCountry: TextView
    lateinit var lastActive: TextView
    lateinit var gender:TextView
    lateinit var userType: String


    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_personal, container, false)

        userEmail = view.findViewById(R.id.profile_about_email)
        userPhone = view.findViewById(R.id.profile_about_phone)
        userAbout = view.findViewById(R.id.profile_personal_about)
        userCountry = view.findViewById(R.id.profile_about_country)
        lastActive = view.findViewById(R.id.profile_last_active)
        gender = view.findViewById(R.id.profile_about_gender)

        getUserData()

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getUserData() {
        docsref.document(userId).get().addOnSuccessListener { documents ->
            val uEmail = documents.get("email").toString()
            val uPhone = documents.get("phoneNumber").toString()
            val desc = documents.get("desc").toString()
            val country = documents.get("country").toString()
            userType = documents.get("userType").toString()
            val la = documents.get("lastActive") as Long
            if (documents.contains("gender")){
                val gend = documents.get("gender").toString()
                gender.text = gend
            }

            val timeAgo = TimeAgo()
            val currentDate = timeAgo.getTimeAgo(Date(la), context)

            lastActive.text = currentDate
            userEmail.text = uEmail
            userPhone.text = uPhone
            userAbout.text = desc
            userCountry.text = country

        }.addOnFailureListener { exception ->
            run {
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


}