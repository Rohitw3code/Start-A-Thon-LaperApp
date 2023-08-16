package com.lapperapp.laper.ui.chats

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class UserChatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userChatModel: ArrayList<UserChatModel>
    private lateinit var userChatAdapter: UserChatAdapter
    private var auth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private var expertRef = db.collection("experts")
    private lateinit var lottiChat: TextView
    val database = Firebase.database
    val chatRef = database.getReference("chats")
    val userChatListRef = database.getReference("userchatlist")
    lateinit var chatIds: List<String>
    private lateinit var noChat: LottieAnimationView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view = inflater.inflate(R.layout.fragment_user_chat, container, false)

        recyclerView = view.findViewById(R.id.user_chat_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        lottiChat = view.findViewById(R.id.user_chat_lotti)
        noChat = view.findViewById(R.id.no_chat_lotti)
        userChatModel = ArrayList()
        chatIds = ArrayList()
        userChatAdapter = UserChatAdapter(userChatModel)
        recyclerView.adapter = userChatAdapter
        userChatAdapter.notifyDataSetChanged()

        fetchChatList()

        return view

    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchChatList() {
        userChatListRef.child(auth.uid.toString())
            .get().addOnSuccessListener { userChild ->
                if (userChild.childrenCount > 0) {
//                    lottiChat.visibility = View.GONE
                    noChat.visibility = View.GONE
                } else {
//                    lottiChat.visibility = View.VISIBLE
                    noChat.visibility = View.VISIBLE
                }
                for (userKey in userChild.children) {
                    val userId = userKey.key.toString()
                    if (!chatIds.contains(userId)) {
                        chatIds = chatIds + userId
                        val lastChatDate = userKey.child("lastChatDate").value as Long
                        val lastMessage = userKey.child("lastMessage").value.toString()
                        expertRef.document(userId)
                            .get().addOnSuccessListener { doc ->
                                val uImageUrl = doc.get("userImageUrl").toString()
                                val uName = doc.get("username").toString()
                                userChatModel.add(
                                    UserChatModel(
                                        uName,
                                        uImageUrl,
                                        userId,
                                        lastChatDate,
                                        lastMessage
                                    )
                                )
                                userChatAdapter.notifyDataSetChanged()
                            }
                    }
                }
            }
    }
}