package com.majjane.chefmajjane.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.majjane.chefmajjane.databinding.ActivityHomeBinding.inflate

@Suppress("UNCHECKED_CAST")
abstract class BaseRecyclerViewAdapter<T : Any, VB : ViewBinding>
    : RecyclerView.Adapter<BaseRecyclerViewAdapter.Companion.BaseViewHolder<VB>>() {

    var items = mutableListOf<T>()

    @JvmName("setItems1")
    fun setItems(items: List<T>) {
        this.items = items as MutableList<T>
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
    var listener: ((view: View, item: T, position: Int) -> Unit)? = null

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
//
//
//        return BaseViewHolder(inflate(LayoutInflater.from(parent.context),parent,false))
//
//    }




    abstract fun getLayout(): Int
    companion object {
        class BaseViewHolder<VB : ViewBinding>(val binding: VB) :
            RecyclerView.ViewHolder(binding.root)
    }
}