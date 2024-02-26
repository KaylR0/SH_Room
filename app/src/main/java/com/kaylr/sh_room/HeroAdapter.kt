package com.kaylr.sh_room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaylr.sh_room.db.HeroEntity

class HeroAdapter( private var superheroList: List<HeroEntity> = emptyList(), private val navigateToDetailActivity: (String) -> Unit)
    : RecyclerView.Adapter<HeroViewHolder>() {
    fun updateList(list: List<HeroEntity>) {
        this.superheroList = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_hero, parent, false)
        )
    }
    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(superheroList[position],navigateToDetailActivity)
    }
    override fun getItemCount() = superheroList.size
}