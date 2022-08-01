package com.mmrbd.ktorexample.ui.adapters

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mmrbd.ktorexample.data.models.User
import com.mmrbd.ktorexample.databinding.ItemRcvBinding

class RepoViewAdapter(val context: Context, private val onItemClick: (User) -> Unit) :
    RecyclerView.Adapter<RepoViewAdapter.RecyclerViewHolder>() {

    private var listDataList: List<User> = emptyList()

    inner class RecyclerViewHolder(private val binding: ItemRcvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val orientation = context.resources.configuration.orientation

        fun bind(datum: User) {
            with(datum) {
                with(binding)
                {
                    Glide.with(context).load(owner?.avatar_url).into(ownerAvatar)
                    tvTitle.text = name
                    tvDescription.text = description
                    if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        tvType?.text = owner?.type
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(
            ItemRcvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listDataList[position])

        holder.itemView.setOnClickListener {
            onItemClick.invoke(listDataList[position])
        }
    }

    fun setDataList(listDataList: List<User>) {
        this.listDataList = listDataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listDataList.size
}