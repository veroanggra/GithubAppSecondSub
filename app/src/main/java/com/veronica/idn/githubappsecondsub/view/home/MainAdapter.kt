package com.veronica.idn.githubappsecondsub.view.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.veronica.idn.githubappsecondsub.databinding.ItemUserBinding
import com.veronica.idn.githubappsecondsub.domain.data.model.ItemsItem

class MainAdapter(private val listUser : List<ItemsItem?>?) :RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var onClickCallback : OnItemClickCallback? = null

    fun setItemClickCallback (onItemClickCallback: OnItemClickCallback){
        this.onClickCallback = onItemClickCallback
    }


    inner class MainViewHolder(var itemUserBinding: ItemUserBinding) : RecyclerView
    .ViewHolder(itemUserBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemUserBinding = ItemUserBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return MainViewHolder(itemUserBinding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val users = listUser?.get(position)
        holder.itemUserBinding.apply {
            tvItemName.text = users?.login
            tvItemType.text = users?.type
            Glide.with(holder.itemView.context).load(users?.avatarUrl).into(ciItemUser)
            holder.itemView.setOnClickListener{onClickCallback?.onItemClicked(users)}
        }

    }

    override fun getItemCount(): Int = listUser?.size ?: 0
}

interface OnItemClickCallback {
    fun onItemClicked(user: ItemsItem?)

}
