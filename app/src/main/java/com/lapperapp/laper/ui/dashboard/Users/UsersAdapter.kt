package com.lapperapp.laper.ui.dashboard.Users

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lapperapp.laper.R
import com.lapperapp.laper.TimeAgo
import com.lapperapp.laper.User.ProfileActivity
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class UsersAdapter(private val mList: List<UsersModel>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_client_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]

        Glide.with(context).load(model.userImageUrl).into(holder.img)
        holder.txt.text = model.userName

        val ta = TimeAgo()
        val result = Date(model.userLastActive)
        val tam = ta.getTimeAgo(result, context)
        holder.tma.text = tam

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val img: CircleImageView = itemView.findViewById(R.id.user_image)
        val txt: TextView = itemView.findViewById(R.id.user_name)
        val tma: TextView = itemView.findViewById(R.id.user_time_ago)

    }


}