package dev.ajiex.usergithub.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.ajiex.usergithub.R
import dev.ajiex.usergithub.model.Item

class ItemUserViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var item_click: LinearLayout
    var avatar: ImageView
    var login_name: TextView

    init {
        item_click = itemView.findViewById(R.id.item_click)
        avatar = itemView.findViewById(R.id.avatar_url)
        login_name = itemView.findViewById(R.id.login_name)
    }

    fun bind(itemUser: Item) {
        Glide.with(itemView).load(itemUser.avatar_url).into(avatar)
        login_name.text = itemUser.login
    }

}