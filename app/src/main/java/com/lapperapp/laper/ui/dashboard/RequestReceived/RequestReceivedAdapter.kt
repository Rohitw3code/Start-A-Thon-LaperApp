package com.lapperapp.laper.ui.dashboard.RequestReceived

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Model.RequestReceivedModel
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.dashboard.RequestPopActivity
import de.hdodenhof.circleimageview.CircleImageView

class RequestReceivedAdapter(private val mList: List<RequestReceivedModel>) :
    RecyclerView.Adapter<RequestReceivedAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.request_item, parent, false)
        return ViewHolder(view)
    }

    fun str(ps: String): String {
        if (ps.length > 80) {
            return ps.subSequence(0, 80).toString() + "..."
        } else {
            return ps
        }
    }

    private fun showDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_CONTEXT_MENU)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.request_pop_up)
        dialog.show()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val model = mList[position]

        holder.reqDesc.text = str(model.desc)
        userRef.document(model.userId)
            .get().addOnSuccessListener { documents ->
                val uName = documents.get("username").toString()
                val uImageUrl = documents.get("userImageUrl").toString()
                holder.reqName.text = uName
                Glide.with(context).load(uImageUrl).into(holder.reqImage)

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, RequestPopActivity::class.java)
                    intent.putExtra("username", uName)
                    intent.putExtra("userImageUrl", uImageUrl)
                    intent.putExtra("techId", model.techId)
                    intent.putExtra("desc", model.desc)
                    intent.putExtra("userId", model.userId)
                    intent.putExtra("requestId", model.reqId)
                    intent.putExtra("accepted", model.accepted)
                    context.startActivity(intent)
                }

                holder.reqDesc.setOnClickListener {
                    val intent = Intent(context, RequestPopActivity::class.java)
                    intent.putExtra("username", uName)
                    intent.putExtra("userImageUrl", uImageUrl)
                    intent.putExtra("techId", model.techId)
                    intent.putExtra("desc", model.desc)
                    intent.putExtra("userId", model.userId)
                    intent.putExtra("requestId", model.reqId)
                    intent.putExtra("accepted", model.accepted)
                    context.startActivity(intent)
                }

            }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var reqImage: CircleImageView = itemView.findViewById(R.id.request_user_image)
        var reqName: TextView = itemView.findViewById(R.id.request_user_name)
        var reqDesc: TextView = itemView.findViewById(R.id.request_description)
    }

}