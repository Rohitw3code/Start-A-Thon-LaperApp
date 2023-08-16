package com.lapperapp.laper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Auth.AuthActivity
import com.lapperapp.laper.Notification.NotificationActivity
import com.lapperapp.laper.databinding.ActivityMainBinding
import com.lapperapp.laper.project.ProjectRequestActivity
import com.lapperapp.laper.settings.SettingsActivity
import com.lapperapp.laper.ui.NewDashboard.NewDashboardFragment
import com.lapperapp.laper.ui.NewHome.NewHomeFragment
import com.lapperapp.laper.ui.home.NotificationCounter
import de.hdodenhof.circleimageview.CircleImageView
import nl.joery.animatedbottombar.AnimatedBottomBar
import ru.nikartm.support.ImageBadgeView

// Start Date : 11 June 2022 , 8:18 AM

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    val auth = FirebaseAuth.getInstance()
    val database = Firebase.database
    var db = Firebase.firestore
    private lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("users")
    val tokenRef = database.getReference("token")
    var firebase = FirebaseAuth.getInstance()
    private lateinit var googleSignClient: GoogleSignInClient
    private lateinit var userImage1: CircleImageView
    private lateinit var notificationBtn: ImageBadgeView
    private lateinit var drawerOpen: ImageView
    private lateinit var headerUserName: TextView
    private lateinit var headerUserEmail: TextView
    private lateinit var headerUserImage: CircleImageView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var tabToAddBadgeAt: AnimatedBottomBar.Tab

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userImage1 = findViewById(R.id.main_user_image1)
        drawerOpen = findViewById(R.id.main_open_drawer)
        notificationBtn = findViewById(R.id.main_notification_btn)
        firebaseAuth = FirebaseAuth.getInstance()

        if (savedInstanceState == null) {
            setFrameLayout(NewHomeFragment())
        }
        binding.appBarMain.bottomBar.setOnTabSelectListener(object :
            AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                var fragment: Fragment? = null
                when (newTab.id) {
                    R.id.navigation_home -> {
                        fragment = NewHomeFragment()
                    }

                    R.id.navigation_dashboard -> {
                        fragment = NewDashboardFragment(binding.appBarMain.bottomBar,tabToAddBadgeAt)
                    }
                }

                if (fragment != null) {
                    setFrameLayout(fragment)
                } else {
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            // An optional method that will be fired whenever an already selected tab has been selected again.
            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
//                Log.d("bottom_bar", "Reselected index: $index, title: ${tab.title}")
            }
        })
//


        // Commented old bottom nav code by Abhay
        drawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        // val navController = findNavController(R.id.frame_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_about, R.id.nav_privacy
            ), drawerLayout
        )
        drawerOpen.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }


        val headerView = navView.getHeaderView(0)
        headerUserName = headerView.findViewById(R.id.drawer_user_name)
        headerUserEmail = headerView.findViewById(R.id.drawer_user_email)
        headerUserImage = headerView.findViewById(R.id.drawer_user_image_view)

        notificationBtn.setOnClickListener {
            val intent = Intent(baseContext, NotificationActivity::class.java)
            startActivity(intent)
        }


        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

        googleSignClient = baseContext?.let { GoogleSignIn.getClient(it, gso) }!!

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_setting -> {
                    val intent = Intent(baseContext, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_project -> {
                    val intent = Intent(baseContext, ProjectRequestActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_about -> {
                    val intent = Intent(baseContext, AboutActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_privacy -> {
                    val intent = Intent(baseContext, PrivacyAndPolicyActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_share -> {
                    shareApp()
                }
                R.id.nav_logout -> {
                    firebase.signOut()
                    googleSignClient.signOut().addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(baseContext, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            return@setNavigationItemSelectedListener true
        }

        tabToAddBadgeAt = binding.appBarMain.bottomBar.tabs[1]
    }


    fun checkDashboardNotification(tabToAddBadgeAt: AnimatedBottomBar.Tab) {
        userRef.document(auth.uid!!)
            .addSnapshotListener(EventListener<DocumentSnapshot?> { snapshot, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    binding.appBarMain.bottomBar.clearBadgeAtTab(tabToAddBadgeAt)
                    return@EventListener
                }
                if (snapshot != null && snapshot.exists()) {
                    if (snapshot.contains("dashboardNotification")){
                        // Check for changes to the "name" field in the document
                        val has = snapshot.getBoolean("dashboardNotification")!!
                        if (has){
                            binding.appBarMain.bottomBar.setBadgeAtTab(
                                tabToAddBadgeAt,
                                AnimatedBottomBar.Badge("1"))
                        }else{
                            binding.appBarMain.bottomBar.clearBadgeAtTab(tabToAddBadgeAt)
                        }
                    }
                } else {
                    binding.appBarMain.bottomBar.clearBadgeAtTab(tabToAddBadgeAt)
                    Log.d(ContentValues.TAG, "Document does not exist")
                }
            })


    }

    override fun onResume() {
        super.onResume()
        val nc = NotificationCounter(notificationBtn)
        nc.fetchNotificationCount()
        checkDashboardNotification(tabToAddBadgeAt)
    }


    override fun onStart() {
        super.onStart()
        fetchUserDetail()
        val nc = NotificationCounter(notificationBtn)
        nc.fetchNotificationCount()
        checkDashboardNotification(tabToAddBadgeAt)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun shareApp() {
        val appPackageName = BuildConfig.APPLICATION_ID
        val appName = baseContext.getString(R.string.app_name)
        val shareBodyText =
            "Instant Programming support by the experts download the app https://play.google.com/store/apps/details?id=$appPackageName"
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TITLE, appName)
            putExtra(Intent.EXTRA_TEXT, shareBodyText)
        }
        sendIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(Intent.createChooser(sendIntent, null))
    }

    fun fetchUserDetail() {
        userRef.document(firebaseAuth.uid as String).get().addOnSuccessListener { documents ->
            if (documents.exists()) {
                val uImageUrl = documents.get("userImageUrl").toString()
                val uUserName = documents.get("username").toString()
                val uEmail = documents.get("email").toString()
                headerUserName.text = uUserName
                headerUserEmail.text = uEmail
                Glide.with(this).load(uImageUrl).into(headerUserImage)
                Glide.with(this).load(uImageUrl).into(userImage1)
            }
        }.addOnFailureListener { exception ->
            run {
                Toast.makeText(baseContext, exception.message, Toast.LENGTH_SHORT)
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
            fragmentTransaction.replace(binding.appBarMain.frameContainer.id, fragment)
        }
        fragmentTransaction.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.frame_container)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
