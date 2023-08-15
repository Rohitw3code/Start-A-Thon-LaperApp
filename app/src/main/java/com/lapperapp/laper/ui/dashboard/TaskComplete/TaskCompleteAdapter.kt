package com.lapperapp.laper.ui.dashboard.TaskComplete

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
import de.hdodenhof.circleimageview.CircleImageView

class TaskCompleteAdapter(private val mList: List<TaskCompleteModel>) :
    RecyclerView.Adapter<TaskCompleteAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_completed_item, parent, false)
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

        holder.tcName.text = model.expertName
        Glide.with(context).load(model.expertImageUrl).into(holder.tcImage)

        holder.tcPs.text = str(model.ps)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskCompletedReceivedActivity::class.java)
            intent.putExtra("requestId", model.requestId)
            intent.putExtra("ps", model.ps)
            intent.putExtra("techId", model.techId)
            intent.putExtra("userId", model.expertId)
            intent.putExtra("expertName", model.expertName)
            intent.putExtra("expertImageUrl", model.expertImageUrl)
            context.startActivity(intent)
        }

        holder.tcPs.setOnClickListener {
            val intent = Intent(context, TaskCompletedReceivedActivity::class.java)
            intent.putExtra("requestId", model.requestId)
            intent.putExtra("ps", model.ps)
            intent.putExtra("userId", model.expertId)
            intent.putExtra("techId", model.techId)
            intent.putExtra("expertName", model.expertName)
            intent.putExtra("expertImageUrl", model.expertImageUrl)
            context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var tcImage: CircleImageView = itemView.findViewById(R.id.task_completed_image)
        var tcName: TextView = itemView.findViewById(R.id.task_completed_name)
        var tcPs: TextView = itemView.findViewById(R.id.task_completed_problem_statement)
//        var tcTime: TextView = itemView.findViewById(R.id.task_completed)
    }

}