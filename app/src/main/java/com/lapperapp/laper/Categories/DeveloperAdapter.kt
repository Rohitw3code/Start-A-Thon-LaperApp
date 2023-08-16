package com.lapperapp.laper.Categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lapperapp.laper.R
import com.lapperapp.laper.User.ProfileActivity
import de.hdodenhof.circleimageview.CircleImageView

class DeveloperAdapter(private val mList: List<DevModel>) :
    RecyclerView.Adapter<DeveloperAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.developer_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]

        holder.txt.text = model.name
        Glide.with(context).load(model.userImageUrl).into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            intent.putExtra("userId", model.devId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val img: CircleImageView = itemView.findViewById(R.id.developer_image)
        val txt: TextView = itemView.findViewById(R.id.developer_name)

    }


}