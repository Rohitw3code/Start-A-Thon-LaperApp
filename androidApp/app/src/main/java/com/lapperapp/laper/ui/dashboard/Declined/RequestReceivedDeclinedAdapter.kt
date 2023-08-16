package com.lapperapp.laper.ui.dashboard.Declined

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R

class RequestReceivedDeclinedAdapter(private var mList: List<RequestReceivedDeclinedModel>) :
    RecyclerView.Adapter<RequestReceivedDeclinedAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_received_decline_item, parent, false)
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
        val model = mList.get(position)

        holder.userName.text = model.clientName
        holder.ps.text = str(model.desc)
        Glide.with(context).load(model.clientImageUrl).into(holder.userImage)

        holder.deleteBtn.setOnClickListener {
            userRef.document(auth.uid.toString())
                .collection("requestReceived")
                .document(model.requestId)
                .delete()
            Toast.makeText(context, "request deleted", Toast.LENGTH_SHORT).show()
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val userName: TextView = itemView.findViewById(R.id.rrd_client_name)
        val userImage: ImageView = itemView.findViewById(R.id.rrd_client_image)
        val ps: TextView = itemView.findViewById(R.id.rrd_ps)
        val deleteBtn: TextView = itemView.findViewById(R.id.rrd_decline_btn)
    }

}