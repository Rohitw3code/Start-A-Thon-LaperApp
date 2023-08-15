package com.lapperapp.laper.Notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.lapperapp.laper.R
import com.lapperapp.laper.utils.TimeAgo
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class NotificationAdapter(private val mList: List<NotificationModel>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return ViewHolder(view)
    }

    fun str(ps: String): String {
        if (ps.length > 80) {
            return ps.subSequence(0, 80).toString() + "..."
        } else {
            return ps
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList.get(position)
        val timeAgo = TimeAgo()
        val currentDate = timeAgo.getTimeAgo(Date(model.time), context)

        holder.title.text = model.title
        holder.text.text = str(model.text)
        holder.time.text = currentDate

        if (model.notificationType == "0" || model.notificationType == "1") {
            holder.title.setTextColor(context.resources.getColor(R.color.blue))
        } else if (model.notificationType == "2" || model.notificationType == "3") {
            holder.title.setTextColor(context.resources.getColor(R.color.green))
        } else if (model.notificationType == "5" || model.notificationType == "6") {
            holder.title.setTextColor(context.resources.getColor(android.R.color.holo_orange_dark))
        }

        Glide.with(context).load(model.imageUrl).into(holder.image)

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image: CircleImageView = itemView.findViewById(R.id.notification_image)
        val title: TextView = itemView.findViewById(R.id.notification_title)
        val text: TextView = itemView.findViewById(R.id.notification_text)
        val time: TextView = itemView.findViewById(R.id.notification_time)
    }


}