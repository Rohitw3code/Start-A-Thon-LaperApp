package com.lapperapp.laper.ui.dashboard.AvailableClient

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.chats.Chat.ChatActivity
import com.lapperapp.laper.ui.dashboard.TaskComplete.TaskCompletedActivity
import de.hdodenhof.circleimageview.CircleImageView

class AvailableClientAdapter(private val mList: List<AvailableClientModel>) :
    RecyclerView.Adapter<AvailableClientAdapter.ViewHolder>() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.available_client_item, parent, false)
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

        holder.reqName.text = model.userName
        Glide.with(context).load(model.userImageUrl).into(holder.reqImage)

        holder.reqPs.text = str(model.ps)

        holder.chat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("userId", model.userId)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, TaskCompletedActivity::class.java)
            intent.putExtra("userId", model.userId)
            intent.putExtra("requestId", model.requestId)
            intent.putExtra("techId", model.techId)
            context.startActivity(intent)
        }

        holder.reqPs.setOnClickListener {
            val intent = Intent(context, TaskCompletedActivity::class.java)
            intent.putExtra("userId", model.userId)
            intent.putExtra("requestId", model.requestId)
            intent.putExtra("techId", model.techId)
            context.startActivity(intent)
        }

        holder.reqName.setOnClickListener {
            val intent = Intent(context, TaskCompletedActivity::class.java)
            intent.putExtra("userId", model.userId)
            intent.putExtra("requestId", model.requestId)
            intent.putExtra("techId", model.techId)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            showDialog(context, model)
            return@setOnLongClickListener true
        }

        holder.reqPs.setOnLongClickListener {
            showDialog(context, model)
            return@setOnLongClickListener true
        }

    }

    fun showDialog(context: Context, modelc: AvailableClientModel) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ps_dialog_item)

        val ps = dialog.findViewById<TextView>(R.id.ps_dialog_problem)
        ps.text = modelc.ps
        val okay = dialog.findViewById<TextView>(R.id.ps_dialog_okay)
        okay.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialog1(context: Context, modelc: AvailableClientModel) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Problem Statement")
        val input = TextView(context)
        input.text = modelc.ps
        builder.setView(input)
        builder.setPositiveButton("Okay") { dialog, which -> }
        builder.show()
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        var reqImage: CircleImageView = itemView.findViewById(R.id.availabe_client_request_image)
        var reqName: TextView = itemView.findViewById(R.id.availabe_client_request_name)
        var reqPs: TextView = itemView.findViewById(R.id.availabe_client_request_problem_statement)
        var chat: ImageView = itemView.findViewById(R.id.availabe_client_request_chat)
    }

}