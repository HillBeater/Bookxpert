package com.hillbeater.bookxpert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hillbeater.bookxpert.databinding.ItemViewBinding
import com.hillbeater.bookxpert.model.MenuItem
import com.hillbeater.bookxpert.R

class ItemAdapter(
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    private val menuList = listOf(
        MenuItem(
            id = 1,
            title = "View PDF Report",
            subtitle = "Display balance sheet using a PDF viewer",
            iconResId = R.drawable.pdf_icon
        ),
        MenuItem(
            id = 2,
            title = "Capture/Select Image",
            subtitle = "Capture from camera or pick image from gallery",
            iconResId = R.drawable.camera_icon
        ),
        MenuItem(
            id = 3,
            title = "View API Data",
            subtitle = "Fetch, store, update & delete API data",
            iconResId = R.drawable.list_icon
        ),
        MenuItem(
            id = 4,
            title = "Setting",
            subtitle = "Enable/Disable push notifications",
            iconResId = R.drawable.setting
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = menuList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    class ViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem) {
            binding.titleText.text = item.title
            binding.subtitleText.text = item.subtitle
            binding.IvIcon.setImageResource(item.iconResId)
        }
    }
}
