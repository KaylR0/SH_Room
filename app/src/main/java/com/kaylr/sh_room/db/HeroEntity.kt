package com.kaylr.sh_room.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kaylr.sh_room.SuperheroItemResponse

@Entity(tableName = "hero_table")
data class HeroEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "idApi") val idApi: String,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "image") val image: String?
)
// Hay que especificarle los campos porque también existe el campo "id"
fun SuperheroItemResponse.toDatabase() = HeroEntity(
    idApi = superheroId,
    name = name,
    image = superheroImage.url
)