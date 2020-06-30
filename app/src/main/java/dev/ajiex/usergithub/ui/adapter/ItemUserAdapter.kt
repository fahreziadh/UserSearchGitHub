package dev.ajiex.usergithub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.ajiex.usergithub.R
import dev.ajiex.usergithub.model.Item

class ItemUserAdapter(private var list_item: List<Item>) :
    RecyclerView.Adapter<ItemUserViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ItemUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list_item.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: ItemUserViewHolder, position: Int) {
        holder.bind(list_item.get(position))
    }

}