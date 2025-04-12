package com.hillbeater.bookxpert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hillbeater.bookxpert.database.PhoneDataItem
import com.hillbeater.bookxpert.databinding.ItemPhoneDataBinding

class PhoneDataAdapter(
    private var phoneList: List<PhoneDataItem>,
    private val onUpdateClick: (PhoneDataItem) -> Unit,
    private val onDeleteClick: (PhoneDataItem) -> Unit
) : RecyclerView.Adapter<PhoneDataAdapter.PhoneViewHolder>() {

    inner class PhoneViewHolder(val binding: ItemPhoneDataBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhoneViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPhoneDataBinding.inflate(layoutInflater, parent, false)
        return PhoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhoneViewHolder, position: Int) {
        val phoneItem = phoneList[position]

        holder.binding.tvName.text = phoneItem.name

        val summary = buildString {
            phoneItem.color?.takeIf { it.isNotBlank() }?.let { append("Color: $it\n") }
            phoneItem.capacity?.takeIf { it.isNotBlank() }?.let { append("Capacity: $it\n") }
            phoneItem.price?.takeIf { it.isNotBlank() }?.let { append("Price: $it\n") }
            phoneItem.generation?.takeIf { it.isNotBlank() }?.let { append("Generation: $it\n") }
            phoneItem.year?.let { append("Year: $it\n") }
            phoneItem.cpuModel?.takeIf { it.isNotBlank() }?.let { append("CPU model: $it\n") }
            phoneItem.hardDiskSize?.takeIf { it.isNotBlank() }?.let { append("Hard disk size: $it\n") }
            phoneItem.caseSize?.takeIf { it.isNotBlank() }?.let { append("Case Size: $it\n") }
            phoneItem.strapColour?.takeIf { it.isNotBlank() }?.let { append("Strap Colour: $it\n") }
            phoneItem.screenSize?.let { append("Screen size: ${it}\" \n") }
            phoneItem.description?.takeIf { it.isNotBlank() }?.let { append("Description: $it\n") }
        }.trim()

        holder.binding.tvDataSummary.text = if (summary.isNotEmpty()) summary else "No details available"

        holder.binding.btnUpdate.setOnClickListener {
            onUpdateClick(phoneItem)
        }

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(phoneItem)
        }
    }


    override fun getItemCount(): Int = phoneList.size

    fun updateList(newList: List<PhoneDataItem>) {
        phoneList = newList
        notifyDataSetChanged()
    }
}
