package com.kaylr.sh_room

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kaylr.sh_room.databinding.ItemHeroBinding
import com.kaylr.sh_room.db.HeroEntity
import com.squareup.picasso.Picasso

class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemHeroBinding.bind(view)
    fun bind(superheroEntity: HeroEntity, navigateToDetailActivity: (String) -> Unit) {
        //recibe los items de la lista
        Picasso.get().load(superheroEntity.image).into(binding.ivSuperHero);
        binding.tvSuperHeroName.text = superheroEntity.name
        binding.root.setOnClickListener { navigateToDetailActivity(superheroEntity.id.toString()) }
        //al ponerle root le decimos que hacemos referencia a cualquier parte del layout. Bien pulsemos el texto o la imagen, hará el setOnClick.
    }
}