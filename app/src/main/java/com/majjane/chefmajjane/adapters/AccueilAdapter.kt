package com.majjane.chefmajjane.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.majjane.chefmajjane.R
import com.majjane.chefmajjane.databinding.ItemRowAccueilLayoutBinding
import com.majjane.chefmajjane.responses.AccueilResponseItem

class AccueilAdapter(val onClick: (AccueilResponseItem, Int) -> Unit) : RecyclerView.Adapter<AccueilAdapter.AccueilViewHolder>() {
    var items = mutableListOf<AccueilResponseItem>()

    @JvmName("setItems1")
    fun setItems(items: List<AccueilResponseItem>) {
        this.items = items as MutableList<AccueilResponseItem>
        notifyDataSetChanged()
    }

    class AccueilViewHolder(val binding: ItemRowAccueilLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccueilViewHolder {

        return AccueilViewHolder(
            ItemRowAccueilLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    private val TAG = "AccueilAdapter"
    override fun onBindViewHolder(holder: AccueilViewHolder, position: Int) {
        val accueilResponseItem = items[position]

        holder.binding.apply {
            categoryName.text = accueilResponseItem.name
            Glide.with(this.root)
                .load(accueilResponseItem.image)
                .placeholder(R.drawable.place_holder_image)
                .into(imageViewAccueil)
            root.setOnClickListener {
                onClick(accueilResponseItem,position)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}