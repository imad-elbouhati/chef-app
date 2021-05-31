package com.majjane.chefmajjane.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.majjane.chefmajjane.Sushi
import com.majjane.chefmajjane.databinding.ItemRowLayoutSushiBinding


class SushiAdapter : RecyclerView.Adapter<SushiAdapter.MyViewHolder>() {
    var items = mutableListOf<Sushi>()

    @JvmName("setItems1")
    fun setItems(items: List<Sushi>){
        this.items = items as MutableList<Sushi>
    }
    class MyViewHolder(val binding: ItemRowLayoutSushiBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemRowLayoutSushiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val sushi = items[position]
        holder.binding.sushiTitle.text  = sushi.title
        holder.binding.sushiDescription.text  = sushi.desc
        holder.binding.sushiPrice.text  = sushi.price
    }

    override fun getItemCount(): Int = items.size
}