package com.lapperapp.laper.Auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.MainActivity
import com.lapperapp.laper.R
import nl.joery.animatedbottombar.BuildConfig

class AuthActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    var db = Firebase.firestore
    val Req_Code: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    var userRef = db.collection("users")
    var adminRef = db.collection("admin")
    lateinit var authBtn: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        FirebaseApp.initializeApp(this)

        authBtn = findViewById<LinearLayout>(R.id.auth_with_google_btn)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(this, gso)


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            startActivity(
                Intent(
                    this, MainActivity
                    ::class.java
                )
            )
            finish()
        }
        authBtn.setOnClickListener { _: View? ->
            signInGoogle()
        }
    }

    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                createNewUser(account)
            }
        }
    }

    private fun createNewUser(account: GoogleSignInAccount) {
        val authId = FirebaseAuth.getInstance().uid.toString()

        adminRef.document("constants")
            .get().addOnSuccessListener { doc ->
                val req = doc.getLong("req") as Long

                userRef.document(authId)
                    .get().addOnSuccessListener { documents ->
                        run {
                            if (!documents.exists()) {
                                val user = hashMapOf(
                                    "username" to account.displayName,
                                    "email" to account.email,
                                    "userId" to authId,
                                    "userImageUrl" to account.photoUrl,
                                    "lastActive" to System.currentTimeMillis(),
                                    "token" to "",
                                    "country" to "",
                                    "desc" to "",
                                    "gender" to "male",
                                    "phoneNumber" to "",
                                    "userType" to "user",
                                    "totalRequests" to 0,
                                    "notificationCount" to 0,
                                    "req" to req,
                                    "versionName" to BuildConfig.VERSION_NAME,
                                    "versionCode" to BuildConfig.VERSION_CODE
                                )

                                userRef.document(authId)
                                    .set(user)
                                    .addOnSuccessListener {
                                        adminRef.document("constants")
                                            .update("totalUsers", FieldValue.increment(1))
                                        Toast.makeText(
                                            this,
                                            "Welcome to Laper :)",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                            } else {
                                Toast.makeText(this, "Welcome Back", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }


            }


    }

}