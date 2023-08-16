package com.lapperapp.laper.Categories

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lapperapp.laper.R
import de.hdodenhof.circleimageview.CircleImageView

class CategoryProfileAdapter(private val mList: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryProfileAdapter.ViewHolder>() {
    var selectedArray = ArrayList<String>()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.query_category_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val ItemsViewModel = mList[position]
        holder.txt.text = ItemsViewModel.title
        holder.itemView.setOnClickListener {
            if (!holder.selected) {
                holder.card.setCardBackgroundColor(Color.parseColor("#1273A8"))
                holder.selected = true
                selectedArray.add(ItemsViewModel.id)
            } else {
                holder.card.setCardBackgroundColor(Color.parseColor("#282828"))
                holder.selected = false
                selectedArray.remove(ItemsViewModel.id)
            }
//            val intent = Intent(holder.itemView.context, DeveloperActivity::class.java)
//            intent.putExtra("id", ItemsViewModel.id)
//            context.startActivity(intent)
        }
        Glide.with(context).load(ItemsViewModel.imageUrl).into(holder.img)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val img: CircleImageView = itemView.findViewById(R.id.query_category_image)
        val txt: TextView = itemView.findViewById(R.id.query_category_name)
        val card: CardView = itemView.findViewById(R.id.query_card_view)
        var selected: Boolean = false
    }

}