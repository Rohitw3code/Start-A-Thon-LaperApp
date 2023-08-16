package com.lapperapp.laper.User

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.ImageViewActivity
import com.lapperapp.laper.R
import com.lapperapp.laper.User.Personal.PersonalFragment
import com.lapperapp.laper.ui.chats.Chat.ChatActivity
import com.lapperapp.laper.utils.TimeAgo
import de.hdodenhof.circleimageview.CircleImageView
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.util.*

class ProfileActivity : AppCompatActivity() {
    val db = Firebase.firestore
    var expertRef = db.collection("experts")

    lateinit var userImage: CircleImageView
    lateinit var userName: TextView
    lateinit var chatSection: ImageView
    lateinit var frameLayout: FrameLayout
    lateinit var bottomBar: AnimatedBottomBar
    lateinit var userId: String
    lateinit var verified:ImageView
    lateinit var title: TextView
    lateinit var lastActive: TextView
    var fragment: Fragment? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        frameLayout = findViewById(R.id.profile_frame_container)
        bottomBar = findViewById(R.id.profile_bottom_bar)
        userName = findViewById(R.id.profile_dev_name)
        userImage = findViewById(R.id.profile_dev_image_view)
        chatSection = findViewById(R.id.profile_chat_section)
        verified = findViewById(R.id.user_profile_verified)
        title = findViewById(R.id.user_profile_title)
        lastActive = findViewById(R.id.user_profile_last_active)

        userId = intent.getStringExtra("userId").toString()
        fragment = ExpertInFragment(userId)
        setFrameLayout(fragment)

        chatSection.setOnClickListener {
            val intent = Intent(baseContext, ChatActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }


        bottomBar.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newTab.id) {
                    R.id.navigation_personal -> {
                        fragment = PersonalFragment(userId)
                    }
                    R.id.navigation_expert_in -> {
                        fragment = ExpertInFragment(userId)
                    }
                }
                if (fragment != null) {
                    setFrameLayout(fragment)
                }
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })

        getExpertData()

    }


    private fun getExpertData() {
        expertRef.document(userId).get().addOnSuccessListener { documents ->
            if (documents != null) {
                val uImageUrl = documents.get("userImageUrl").toString()
                val uName = documents.get("username").toString()
                val uTitle = documents.get("title").toString()

                if (documents.contains("title")){
                    title.text = uTitle
                }
                // Profile Verification
                if (documents.contains("verified")){
                    val verifiedValue = documents.getBoolean("verified") as Boolean
                    if (verifiedValue){
                        verified.visibility = View.VISIBLE
                    }
                    else{
                        verified.setImageResource(R.drawable.notverified)
                    }
                }else{
                    verified.setImageResource(R.drawable.notverified)
                }

                // Last Active
                val la = documents.get("lastActive") as Long
                val timeAgo = TimeAgo()
                val currentDate = timeAgo.getTimeAgo(Date(la), baseContext)
                lastActive.text = currentDate


                userName.text = uName
                Glide.with(this).load(uImageUrl).into(userImage)
                userImage.setOnClickListener {
                    val intent = Intent(baseContext, ImageViewActivity::class.java)
                    intent.putExtra("url", uImageUrl)
                    startActivity(intent)
                }
            }
        }.addOnFailureListener { exception ->
            run {
                Toast.makeText(applicationContext, "" + exception.message, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    fun setFrameLayout(fragment: Fragment?) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        if (fragment != null) {
            fragmentTransaction.replace(frameLayout.id, fragment)
        }
        fragmentTransaction.commit()
    }


}