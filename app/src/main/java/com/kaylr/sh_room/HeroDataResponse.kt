package com.kaylr.sh_room

import com.google.gson.annotations.SerializedName

data class HeroDataResponse (
    // @SerializedName("response") val response: String,
    @SerializedName("results") val superheroes: List<SuperheroItemResponse>
    //se puede quitar lo de "@SerializedName("results")" si luego el "val" se llama igual que el indice en la base de datos, "results" en este caso.
)
data class SuperheroItemResponse(
    @SerializedName("id") val superheroId: String,//necesito guardar su ID propio para compararlo con el que ya se ha guardado en la room
    @SerializedName("name") val name: String,
    @SerializedName("image") val superheroImage:SuperheroImageResponse
)
data class SuperheroImageResponse(@SerializedName("url") val url:String)
