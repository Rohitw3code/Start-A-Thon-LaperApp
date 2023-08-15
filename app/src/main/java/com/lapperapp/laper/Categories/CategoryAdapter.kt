package com.lapperapp.laper.Categories

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lapperapp.laper.R

class CategoryAdapter(private val mList: List<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val ItemsViewModel = mList[position]
//        holder.img.setImageResource(ItemsViewModel.imageUrl)
        holder.txt.text = ItemsViewModel.title

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CategoryDeveloperActivity::class.java)
            intent.putExtra("id", ItemsViewModel.id)
            holder.itemView.context.startActivity(intent)
        }
        Glide.with(holder.itemView.context).load(ItemsViewModel.imageUrl).into(holder.img)


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val img: ImageView = itemView.findViewById(R.id.category_image_view)
        val txt: TextView = itemView.findViewById(R.id.category_title)

    }

}