package com.lapperapp.laper.User.Personal

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.dashboard.RequestSubmitActivity

class SkillAdapter(private val mList: List<SkillModel>, private var userId: String) :

    RecyclerView.Adapter<SkillAdapter.ViewHolder>() {
    var auth = FirebaseAuth.getInstance()
    var db = Firebase.firestore
    var userRef = db.collection("experts")


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.skill_item, parent, false)
        return ViewHolder(view)
    }

    fun showDialog(context: Context, holder: ViewHolder, ItemsViewModel: SkillModel) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Title")

        val input = EditText(context)
        input.hint = "Enter Description for the Request"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            if (!input.text.isEmpty()) {
                val hashMap = hashMapOf(
                    "requestDate" to 0,
                    "desc" to "" + input.text,
                    "userId" to auth.uid,
                    "techId" to ItemsViewModel.id,
                    "accepted" to false
                )
                val reqMap = hashMapOf(
                    "totalRequests" to FieldValue.increment(1)
                )
                userRef.document(userId).update(reqMap as Map<String, Any>)
                userRef.document(userId).collection("requests")
                    .add(hashMap)
                    .addOnSuccessListener {
                    }
            } else {
                Toast.makeText(context, "Description can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", { dialog, which -> dialog.cancel() })
        builder.show()


    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val ItemsViewModel = mList[position]
        holder.txt.text = ItemsViewModel.title
        Glide.with(holder.itemView.context).load(ItemsViewModel.imageURl).into(holder.img)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, RequestSubmitActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("techId", ItemsViewModel.id)
            intent.putExtra("title", ItemsViewModel.title)
            context.startActivity(intent)
        }

        holder.requestBtn.setOnClickListener {
            val intent = Intent(context, RequestSubmitActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("techId", ItemsViewModel.id)
            intent.putExtra("title", ItemsViewModel.title)
            context.startActivity(intent)
        }

        holder.txt.setOnClickListener {
            val intent = Intent(context, RequestSubmitActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("techId", ItemsViewModel.id)
            intent.putExtra("title", ItemsViewModel.title)
            context.startActivity(intent)
        }

        holder.reqText.setOnClickListener {
            val intent = Intent(context, RequestSubmitActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("techId", ItemsViewModel.id)
            intent.putExtra("title", ItemsViewModel.title)
            context.startActivity(intent)
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txt: TextView = itemView.findViewById(R.id.skill_title)
        var img: ImageView = itemView.findViewById(R.id.skill_image)
        val reqText: TextView = itemView.findViewById(R.id.skill_item_text)
        var requestBtn: RelativeLayout = itemView.findViewById(R.id.skill_request_btn)
    }

}