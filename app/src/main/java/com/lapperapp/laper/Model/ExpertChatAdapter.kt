package com.lapperapp.laper.Model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.chats.Chat.ChatActivity
import de.hdodenhof.circleimageview.CircleImageView

class ExpertChatAdapter(private val mList: List<ExpertChat>) :
    RecyclerView.Adapter<ExpertChatAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_chat_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]

        holder.name.text = model.name
        holder.lastChat.text = model.lastMessage
        Glide.with(context).load(model.imageUrl).into(holder.img)

        holder.relative.setOnClickListener {v->
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", model.expertId)
            intent.putExtra("freeze",true)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val relative:RelativeLayout = itemView.findViewById(R.id.user_chat_relative)
        val img: CircleImageView = itemView.findViewById(R.id.user_chat_image)
        val name: TextView = itemView.findViewById(R.id.user_chat_name)
        val date: TextView = itemView.findViewById(R.id.user_chat_date)
        val lastChat: TextView = itemView.findViewById(R.id.user_chat_last_chat)

    }


}