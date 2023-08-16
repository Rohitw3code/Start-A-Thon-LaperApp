package com.lapperapp.laper.ui.chats.Chat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import com.lapperapp.laper.User.ProfileActivity
import com.lapperapp.laper.service.PushNotification
import de.hdodenhof.circleimageview.CircleImageView


class ChatActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private var auth = FirebaseAuth.getInstance()
    var userRef = db.collection("users")
    var expertRef = db.collection("experts")

    val database = Firebase.database
    val chatRef = database.getReference("chats")
    val userChatListRef = database.getReference("userchatlist")
    val messageIdList = arrayListOf<String>()

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatModel: ArrayList<ChatModel>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var msgEdit: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var receiverUserId: String
    private lateinit var userImage: CircleImageView
    private lateinit var userName: TextView
    private lateinit var appBar: AppBarLayout
    private lateinit var taskDone: ImageView
    private lateinit var noMessage: LottieAnimationView
    var freeze:Boolean = false
    private lateinit var freezeText:TextView
    private lateinit var chatRelative:RelativeLayout
    private lateinit var backBtn:ImageView


    @SuppressLint("NotifyDataSetChanged", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatRecyclerView = findViewById(R.id.chat_recycler_view)
        userImage = findViewById(R.id.chat_user_image_app_bar)
        userName = findViewById(R.id.chat_user_name_app_bar)
        msgEdit = findViewById(R.id.text_message_chat)
        appBar = findViewById(R.id.chat_app_bar_layout)
        sendBtn = findViewById(R.id.send_msg_btn_chat)
        taskDone = findViewById(R.id.user_chat_done)
        noMessage = findViewById(R.id.no_message_lotti)
        freezeText = findViewById(R.id.freeze_text_chat)
        chatRelative = findViewById(R.id.chat_relative)
        backBtn = findViewById(R.id.back_chat)

        chatRecyclerView.layoutManager = LinearLayoutManager(baseContext)
        chatModel = ArrayList()

        receiverUserId = intent.getStringExtra("userId").toString()
        freeze = intent.getBooleanExtra("freeze",false)

        if (freeze){
            freezeText.visibility = View.VISIBLE
            chatRelative.visibility = View.GONE
        }



        chatAdapter = ChatAdapter(chatModel, receiverUserId)
        chatRecyclerView.adapter = chatAdapter
        chatAdapter.notifyDataSetChanged()

        sendBtn.setOnClickListener {
            sendMessage()
        }

        appBar.setOnClickListener {
            val intent = Intent(baseContext, ProfileActivity::class.java)
            intent.putExtra("userId", receiverUserId)
            startActivity(intent)
        }

        fetchMessages()
        addUser()
        fetchUser()

    }

    fun fetchUser() {
        expertRef.document(receiverUserId)
            .get().addOnSuccessListener { doc ->
                val username = doc.getString("username")
                val userimageUrl = doc.getString("userImageUrl")
                userName.text = username
                Glide.with(baseContext).load(userimageUrl).into(userImage)
            }
    }


    fun addUser() {
        val userChat = hashMapOf(
            "lastChatDate" to System.currentTimeMillis()
        )

        userChatListRef.child(auth.uid.toString())
            .child(receiverUserId)
            .setValue(userChat)
    }

    fun markedAsRead(id:String){
        val database = FirebaseDatabase.getInstance().reference
        val updates = HashMap<String, Any>()
        updates["read"] = true
        database.child("chats").child(auth.uid.toString())
            .child(receiverUserId).child(id).updateChildren(updates)
        database.child("chats").child(receiverUserId)
            .child(auth.uid.toString()).child(id).updateChildren(updates)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchMessages() {
        chatRef.child(auth.uid.toString())
            .child(receiverUserId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.hasChildren()){
                        noMessage.visibility = View.VISIBLE
                    }
                    for (child in snapshot.children) {
                        if (!messageIdList.contains(child.key.toString())) {
                            val message = child.child("text").value.toString()
                            val sendDate = child.child("sentDate").value as Long
                            val receiverId = child.child("receiverId").value.toString()
                            val type = child.child("type").value as Long
                            chatModel.add(ChatModel(message, sendDate, receiverId, type))
                            chatAdapter.notifyDataSetChanged()
                            messageIdList.add(child.key.toString())
                            if (receiverId.equals(auth.uid.toString())){
                                markedAsRead(child.key.toString())
                            }
                        }
                    }
                    chatRecyclerView.layoutManager?.scrollToPosition(snapshot.children.count() - 1)


                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun sendMessage() {
        val msg = msgEdit.text.toString().trim()

        if (msg.isEmpty()) {
            return
        }

        val childId = chatRef.push().key.toString()

        val senderHashMap = hashMapOf(
            "sentDate" to System.currentTimeMillis(),
            "receiverId" to receiverUserId,
            "text" to msg,
            "type" to 0,
            "read" to false
        )
        chatRef.child(auth.uid.toString())
            .child(receiverUserId)
            .child(childId).setValue(senderHashMap)

        val receiverHashMap = hashMapOf(
            "sentDate" to System.currentTimeMillis(),
            "receiverId" to receiverUserId,
            "text" to msg,
            "type" to 0,
            "read" to false
        )
        chatRef.child(receiverUserId)
            .child(auth.uid.toString())
            .child(childId).setValue(receiverHashMap)

        msgEdit.setText("")
        val userHash = hashMapOf(
            "lastChatDate" to System.currentTimeMillis(),
            "lastMessage" to msg
        )

        userChatListRef.child(auth.uid.toString())
            .child(receiverUserId)
            .setValue(userHash)

        userChatListRef.child(receiverUserId)
            .child(auth.uid.toString())
            .setValue(userHash)

        chatRecyclerView.layoutManager?.scrollToPosition(chatAdapter.itemCount - 1)

        val pn = PushNotification(baseContext)
        pn.sendNotification(receiverUserId, "New Message", msg, "2")

    }


}