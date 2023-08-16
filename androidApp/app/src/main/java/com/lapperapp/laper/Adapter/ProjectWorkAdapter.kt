package com.lapperapp.laper.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lapperapp.laper.Model.ProjectWorkModel
import com.lapperapp.laper.R

class ProjectWorkAdapter(private var mList: List<ProjectWorkModel>) :
    RecyclerView.Adapter<ProjectWorkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_work_profile_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = mList[position]

        holder.category.text = model.cat
        holder.subCategory.text = model.cat
        holder.desc.text = model.desc
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val category: TextView = itemView.findViewById(R.id.project_work_category_profile)
        val subCategory: TextView = itemView.findViewById(R.id.project_work_sub_category_profile)
        val tags: TextView = itemView.findViewById(R.id.project_work_tags_profile)
        val desc: TextView = itemView.findViewById(R.id.project_work_desc_profile)

    }

}