package com.lapperapp.laper.ui.chats

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.chats.Chat.ChatActivity
import com.lapperapp.laper.utils.TimeAgo
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class UserChatAdapter(private val mList: List<UserChatModel>) :
    RecyclerView.Adapter<UserChatAdapter.ViewHolder>() {

    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_chat_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]

        holder.ucName.text = model.name
        if (!model.lastChatText.equals("null")) {
            holder.ucLastChat.text = model.lastChatText
        }
        Glide.with(context).load(model.imageUrl).into(holder.ucImage)

        val timeAgo = TimeAgo()
        val currentDate = timeAgo.getTimeAgo(Date(model.lastChatDate), context)
        holder.ucDate.text = currentDate

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", model.userId)
            context.startActivity(intent)
        }

        holder.card.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", model.userId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var ucImage: CircleImageView = itemView.findViewById(R.id.user_chat_image)
        var ucName: TextView = itemView.findViewById(R.id.user_chat_name)
        var ucLastChat: TextView = itemView.findViewById(R.id.user_chat_last_chat)
        var ucDate: TextView = itemView.findViewById(R.id.user_chat_date)
        var card: RelativeLayout = itemView.findViewById(R.id.user_chat_relative)
    }

}