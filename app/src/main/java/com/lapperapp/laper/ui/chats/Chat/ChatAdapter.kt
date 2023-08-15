package com.lapperapp.laper.ui.chats.Chat

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.lapperapp.laper.R
import com.lapperapp.laper.utils.TimeAgo
import java.util.*

class ChatAdapter(private val mList: List<ChatModel>, private val receiverUserId: String) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)

        return ViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]
        val timeAgo = TimeAgo()
        val currentDate = timeAgo.getTimeAgo(Date(model.date), context)


        if (model.reciverId.trim().equals(auth.uid.toString())) {
            // RECEIVER USER
            holder.recLinear.visibility = View.VISIBLE
            holder.recText.text = model.text
            holder.recDate.text = currentDate

            holder.sendLinear.visibility = View.GONE
        } else {
            // SENDER USER
            holder.sendLinear.visibility = View.VISIBLE
            holder.sendText.text = model.text
            holder.sendDate.text = currentDate

            holder.recLinear.visibility = View.GONE
        }
        holder.itemView.setOnLongClickListener{
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", model.text)
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context,"copied!",Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        // RECEIVER ItemView
        val recText: TextView = itemView.findViewById(R.id.receiver_text_message_item)
        val recDate: TextView = itemView.findViewById(R.id.receiver_date_message_item)
        val recLinear: LinearLayout = itemView.findViewById(R.id.receiver_linear_message_item)

        // SENDER ItemView
        val sendText: TextView = itemView.findViewById(R.id.sender_text_message_item)
        val sendDate: TextView = itemView.findViewById(R.id.sender_date_message_item)
        val sendLinear: LinearLayout = itemView.findViewById(R.id.sender_linear_message_item)
    }


}