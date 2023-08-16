package com.lapperapp.laper.ui.NewDashboard.NewAvailableExpert

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class NewAvailableExpertAdapter(private val mList: List<NewAvailableExpertModel>) :
    RecyclerView.Adapter<NewAvailableExpertAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_request_sent_item, parent, false)
        return ViewHolder(view)
    }

    fun str(ps: String): String {
        if (ps.length > 80) {
            return ps.subSequence(0, 80).toString() + "..."
        } else {
            return ps
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]

        val timeAgo = TimeAgo()
        val currentDate = timeAgo.getTimeAgo(Date(model.date), context)
        holder.reqDate.text = currentDate

        holder.reqName.text = model.name
        holder.reqPs.text = str(model.ps)
        Glide.with(context).load(model.imageUrl).into(holder.reqImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", model.userId)
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var reqImage: CircleImageView = itemView.findViewById(R.id.new_request_sent_image)
        var reqName: TextView = itemView.findViewById(R.id.new_request_sent_name)
        var reqPs: TextView = itemView.findViewById(R.id.new_request_sent_ps)
        var reqDate: TextView = itemView.findViewById(R.id.new_request_date)
    }

}