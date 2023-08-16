package com.lapperapp.laper.ui.NewHome
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.lapperapp.laper.R

class SelectCategoryAdapter(
    private val mList: List<SelectCategorymodel>,
    val keys: MutableList<SelectCategorymodel>
) :
    RecyclerView.Adapter<SelectCategoryAdapter.ViewHolder>() {

//    var keys:ArrayList<String> = ArrayList()

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.select_category_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val ItemsViewModel = mList[position]
        holder.txt.text = ItemsViewModel.title

        holder.itemView.setOnClickListener {
            if (holder.clicked) {
                holder.clicked = false
                ViewCompat.setBackgroundTintList(holder.bg, null)
                keys.remove(SelectCategorymodel(ItemsViewModel.title, "", ItemsViewModel.id))
            } else {
                holder.clicked = true
                keys.add(SelectCategorymodel(ItemsViewModel.title, "", ItemsViewModel.id))
                ViewCompat.setBackgroundTintList(
                    holder.bg,
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.green
                        )
                    )
                )
            }
        }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txt: TextView = itemView.findViewById(R.id.select_category_title)
        var bg: RelativeLayout = itemView.findViewById(R.id.select_category_item)
        var clicked: Boolean = false
    }

}