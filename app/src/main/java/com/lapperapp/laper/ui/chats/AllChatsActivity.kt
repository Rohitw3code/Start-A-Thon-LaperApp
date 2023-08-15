package com.lapperapp.laper.ui.chats

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Categories.CategoryModel
import com.lapperapp.laper.Model.ExpertChat
import com.lapperapp.laper.Model.ExpertChatAdapter
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.NewDashboard.NewRequest.NewRequestAdapter

class AllChatsActivity : AppCompatActivity() {

    var db = Firebase.firestore
    var expertRef = db.collection("experts")
    var userRef = db.collection("users")
    val auth = FirebaseAuth.getInstance()
    var chatList = ArrayList<ExpertChat>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ExpertChatAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_chats)

        val toolbar: Toolbar = findViewById(R.id.all_chats_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            val upArrow = resources.getDrawable(R.drawable.ic_baseline_arrow_back_24) // Replace with your own backspace image
            upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            setHomeAsUpIndicator(upArrow)
        }

        recyclerView = findViewById(R.id.experts_chat_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(baseContext)
        chatList = ArrayList()
        chatAdapter = ExpertChatAdapter(chatList)
        recyclerView.adapter = chatAdapter

        getChats()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getChats(){
        userRef.document(auth.uid.toString())
            .collection("expertsChats").get().addOnSuccessListener { docs->
                for(doc in docs){
                    val expertId = doc.id;
                    val timestamp = doc.getLong("timestamp") as Long
                    val freeze = doc.getBoolean("chatFreeze") as Boolean
                    val lastMsg = doc.getString("lastChat") as String
                    expertRef.document(expertId)
                        .get().addOnSuccessListener { doc->
                            val name = doc.getString("username") as String
                            val imageURL = doc.getString("userImageUrl") as String
                            chatList.add(ExpertChat(expertId,name,imageURL,freeze,timestamp,lastMsg))
                            chatAdapter.notifyDataSetChanged()
                        }
                }

            }
    }

}