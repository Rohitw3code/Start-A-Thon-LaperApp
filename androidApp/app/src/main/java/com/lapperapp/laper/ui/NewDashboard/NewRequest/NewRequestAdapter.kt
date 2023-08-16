package com.lapperapp.laper.ui.NewDashboard.NewRequest

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import com.lapperapp.laper.utils.TimeAgo
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class NewRequestAdapter(private val mList: List<NewRequestSentModel>) :
    RecyclerView.Adapter<NewRequestAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var reqRef = db.collection("requests")
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
        val currentDate = timeAgo.getTimeAgo(Date(model.reqSentDate), context)

        holder.reqDate.text = currentDate
        holder.reqName.text = model.expName
        holder.reqPs.text = str(model.ps)
        Glide.with(context).load(model.expImage).placeholder(R.drawable.logo).into(holder.reqImage)
        holder.itemView.setOnClickListener {
            reqRef.document(model.reqId).get().addOnSuccessListener { doc ->
                if (doc.exists()) {
                    val reqIntent = Intent(context, RequestDetailActivity::class.java)
                    reqIntent.putExtra("requestId", model.reqId)
                    context.startActivity(reqIntent)
                } else {
                    Toast.makeText(context, "Request is Cancelled !", Toast.LENGTH_SHORT).show()
                }
            }
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