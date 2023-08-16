package com.lapperapp.laper.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Model.DevSkill
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.dashboard.RequestSubmitActivity
import de.hdodenhof.circleimageview.CircleImageView

class DevSkillAdapter(private val mList: List<DevSkill>, private val userId: String) :
    RecyclerView.Adapter<DevSkillAdapter.ViewHolder>() {
    val db = Firebase.firestore
    var techRef = db.collection("tech")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dev_skill, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]
        val context = holder.itemView.context

        holder.title.text = model.skill
        Glide.with(context).load(model.skillImageUrl).into(holder.image)
        holder.reqSend.setOnClickListener {
            val intent = Intent(context, RequestSubmitActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("techId", model.skillId)
            intent.putExtra("title", model.skill)
            context.startActivity(intent)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val title: TextView = itemView.findViewById(R.id.dev_skill_title)
        val image: CircleImageView = itemView.findViewById(R.id.dev_skill_image)
        val reqSend: TextView = itemView.findViewById(R.id.dev_skill_request_btn)

    }

}