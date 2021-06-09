package com.majjane.chefmajjane

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.majjane.chefmajjane.databinding.ItemRowLayoutCommandeBinding
import com.majjane.chefmajjane.responses.Article

class DisplayAllCommandesAdapter() :
    RecyclerView.Adapter<DisplayAllCommandesAdapter.CommandesViewHolder>() {
    var items = mutableListOf<Article>()

    @JvmName("setItems1")
    fun setItems(items: List<Article>) {
        this.items = items as MutableList<Article>
        notifyDataSetChanged()
    }

    class CommandesViewHolder(val binding: ItemRowLayoutCommandeBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandesViewHolder {
        return CommandesViewHolder(
            ItemRowLayoutCommandeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CommandesViewHolder, position: Int) {
        val article = items[position]
        holder.binding.apply {
            quantityTextView.text = article.selectedQuantity.toString()+"x"
            articleName.text = article.name
            articleDescription.text = article.description
            articlePrice.text = article.prixTTC.toString()+" MAD"
        }
    }

    override fun getItemCount(): Int = items.size
}