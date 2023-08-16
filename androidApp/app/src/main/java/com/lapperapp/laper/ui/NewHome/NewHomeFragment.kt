package com.lapperapp.laper.ui.NewHome

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import com.lapperapp.laper.BuildConfig
import com.lapperapp.laper.Categories.*
import com.lapperapp.laper.ChatGPT.ChatGPTActivity
import com.lapperapp.laper.ImageViewActivity
import com.lapperapp.laper.R
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class NewHomeFragment : Fragment() {

    var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("users")
    var techRef = db.collection("tech")
    val database = Firebase.database
    val tokenRef = database.getReference("token")
    var data = ArrayList<CategoryModel>()

    //    private lateinit var imageSlider: ImageSlider
    private lateinit var userImage2: CircleImageView
    private lateinit var developersCard: CardView
    private lateinit var cameraCard: CardView
    private lateinit var typeCard: CardView
    private lateinit var exploreCard: CardView
    private lateinit var title: TextView

    var imagePicker: ImageView? = null

    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_home_fragment, container, false)

        firebaseAuth = FirebaseAuth.getInstance()

        developersCard = view.findViewById(R.id.home_developers)
        cameraCard = view.findViewById(R.id.home_take_photo)
        typeCard = view.findViewById(R.id.home_type_question)
        exploreCard = view.findViewById(R.id.home_explore)
        title = view.findViewById(R.id.home_title)

        imagePicker = view.findViewById(R.id.image_picker)

        val bottomSheet = BottomSheetDialog(requireContext())
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val cameraBtn = bottomSheetView.findViewById<CardView>(R.id.camera_btn)
        val galleryBtn = bottomSheetView.findViewById<CardView>(R.id.gallery_btn)

        // set the view for the dialog
        bottomSheet.setContentView(bottomSheetView)

        exploreCard.setOnClickListener { v ->
            val explore = Intent(context, AllCategoryActivity::class.java)
            startActivity(explore)
        }

        cameraCard.setOnClickListener {
            bottomSheet.show()
        }

        cameraBtn.setOnClickListener {
            bottomSheet.cancel()
            ImagePicker.with(this)
                .cameraOnly().crop().maxResultSize(400, 400).start()
        }
        galleryBtn.setOnClickListener {
            bottomSheet.cancel()
            ImagePicker.with(this)
                .galleryOnly()    //User can only select image from Gallery
                .start()
        }

        typeCard.setOnClickListener { v ->
            val explore = Intent(context, TypeQuestActivity::class.java)
            explore.putExtra("image_uri","");
            startActivity(explore)
        }

        developersCard.setOnClickListener { v ->
            val explore = Intent(context, ViewAllExpertsActivity::class.java)
            startActivity(explore)
        }

        setToken()
        getUserData()

        return view

    }


    private fun getUserData() {
        userRef.document(firebaseAuth.uid.toString()).get().addOnSuccessListener { documents ->
            if (documents != null) {
                val uName = documents.get("username").toString()
                title.setText("Hello "+uName)
            }
        }.addOnFailureListener { exception ->

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            val typeIntent = Intent(context, TypeQuestActivity::class.java)
            typeIntent.putExtra("image_uri", data?.data.toString())
            startActivity(typeIntent)
        }
    }


    fun setToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { s ->
            val tokenhash = hashMapOf(
                "token" to s
            )
            tokenRef.child(firebaseAuth.uid.toString()).setValue(tokenhash)

            val hashMap = hashMapOf(
                "lastActive" to System.currentTimeMillis(),
                "token" to s,
                "versionName" to BuildConfig.VERSION_NAME,
                "versionCode" to BuildConfig.VERSION_CODE
            )
            userRef.document(firebaseAuth.uid.toString())
                .update(hashMap as Map<String, Any>)
                .addOnSuccessListener {
                }
                .addOnFailureListener { exc ->
                    run {
                    }
                }
        }

    }


    @SuppressLint("SetTextI18n")
    fun fetchUserDetail() {
        userRef.document(firebaseAuth.uid as String).get().addOnSuccessListener { documents ->
            if (documents.exists()) {
                val uImageUrl = documents.get("userImageUrl").toString()
                try {
                    Glide.with(this).load(uImageUrl).into(userImage2)
                } finally {

                }
            }
        }.addOnFailureListener { exception ->
            run {
                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }


}