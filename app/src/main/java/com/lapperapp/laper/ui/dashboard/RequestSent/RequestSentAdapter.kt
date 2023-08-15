package com.lapperapp.laper.ui.dashboard.RequestSent

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import de.hdodenhof.circleimageview.CircleImageView

class RequestSentAdapter(private val mList: List<RequestSentModel>) :
    RecyclerView.Adapter<RequestSentAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_sent_item, parent, false)
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

        holder.reqName.text = model.expName
        holder.reqPs.text = str(model.ps)
        Glide.with(context).load(model.expImage).into(holder.reqImage)

        holder.card.setOnClickListener {
            sendIntent(context, model)
        }

        holder.reqName.setOnClickListener {
            sendIntent(context, model)
        }

        holder.reqPs.setOnClickListener {
            sendIntent(context, model)
        }

        holder.itemView.setOnClickListener {
            sendIntent(context, model)
        }
    }

    fun sendIntent(context: Context, model: RequestSentModel) {
        val intent = Intent(context, RequestSentViewActivity::class.java)
        intent.putExtra("username", model.expName)
        intent.putExtra("userImageUrl", model.expImage)
        intent.putExtra("techId", model.techId)
        intent.putExtra("desc", model.ps)
        intent.putExtra("userId", model.expertId)
        intent.putExtra("requestId", model.reqId)
        intent.putExtra("cancelled", model.cancel)
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var reqImage: CircleImageView = itemView.findViewById(R.id.request_sent_image)
        var reqName: TextView = itemView.findViewById(R.id.request_sent_name)
        var reqPs: TextView = itemView.findViewById(R.id.request_sent_ps)
        var card: CardView = itemView.findViewById(R.id.card_request_sent)
    }

}