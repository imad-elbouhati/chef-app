package com.majjane.chefmajjane.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.majjane.chefmajjane.databinding.ItemRowLayoutMenuTypeBinding
import com.majjane.chefmajjane.responses.menu.MenuResponseItem

class MenuAdapter(val onClick: (MenuResponseItem, Int) -> Unit) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {
    var items = mutableListOf<MenuResponseItem>()

    @JvmName("setItems1")
    fun setItems(items: List<MenuResponseItem>) {

        this.items = items as MutableList<MenuResponseItem>
        notifyDataSetChanged()
    }

    class MenuViewHolder(val binding: ItemRowLayoutMenuTypeBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        return MenuViewHolder(
            ItemRowLayoutMenuTypeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        private var lastChecked: Chip? = null
        private var lastCheckedPos = 0
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menuResponseItem = items[position]
        holder.binding.menuChip.text = menuResponseItem.libelle
        holder.binding.menuChip.isChecked = menuResponseItem.selected
        if (position == 0 && items[0].selected && holder.binding.menuChip.isChecked) {
            lastChecked = holder.binding.menuChip
            lastCheckedPos = 0
        }
        holder.binding.menuChip.setOnClickListener {
            val chip = it as (Chip)
            if (chip.isChecked) {
                if (lastChecked != null) {
                    lastChecked!!.isChecked = false
                    items[lastCheckedPos].selected = false
                }
                lastChecked = chip;
                lastCheckedPos = position
            } else
                lastChecked = null
            items[position].selected = chip.isChecked
            onClick(items[position],position)
        }
    }
    private val TAG = "MenuAdapter"
    override fun getItemCount(): Int = items.size
}