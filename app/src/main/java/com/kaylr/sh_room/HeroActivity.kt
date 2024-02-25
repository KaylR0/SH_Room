package com.kaylr.sh_room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.kaylr.sh_room.databinding.ActivityHeroBinding
import com.kaylr.sh_room.db.SHDB
import com.kaylr.sh_room.db.toDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HeroActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "extra_id"
        const val MY_TOKEN = "7038753112847970"
    }
    private lateinit var binding: ActivityHeroBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: HeroAdapter
    private lateinit var room: SHDB
    private fun navigateToDetail(id: String) {
        val intent = Intent(this, DetailHeroActivity::class.java)
        intent.putExtra(EXTRA_ID, id)
        startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room = getRoom()

        binding = ActivityHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetrofit()
        fillDatabase()
        initUI()
    }

    private fun initUI() {
      /*  binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }
            override fun onQueryTextChange(newText: String?) = false
        })*/
        adapter = HeroAdapter{superheroId -> navigateToDetail(superheroId)}
        binding.rvSuperHero.setHasFixedSize(true)
        binding.rvSuperHero.layoutManager = LinearLayoutManager(this)
        binding.rvSuperHero.adapter = adapter
    }

    private fun searchByName(query: String) {//query: String
        binding.progressBar.isVisible = true

        //.IO es para hilos secundarios
        //.MAIN es para el hilo principal
        CoroutineScope(Dispatchers.IO).launch {
            //usamos corrutinas para que use otro hilo y que no se atasque el programa principal
            val myResponse: Response<HeroDataResponse> =
                retrofit.create(ApiService::class.java).getSuperheroes()//(query)
            if (myResponse.isSuccessful) {
                Log.i("Consulta", "Funciona :)")
                val response: HeroDataResponse? = myResponse.body()
                if (response != null) {
                    Log.i("Cuerpo de la consulta", response.toString())
                    runOnUiThread {
                        //adapter.updateList(response.superheroes)
                        //binding.progressBar.isVisible = false
                    }
                }
            } else {
                Log.i("Consulta", "No funciona :(")
            }
        }
    }
    private fun fillDatabase() {
        binding.progressBar.isVisible = true

        //.IO es para hilos secundarios
        //.MAIN es para el hilo principal
        CoroutineScope(Dispatchers.IO).launch {
            //usamos corrutinas para que use otro hilo y que no se atasque el programa principal
            val myResponse: Response<HeroDataResponse> =
                retrofit.create(ApiService::class.java).getSuperheroes()//(query)
            if (myResponse.isSuccessful) {
                Log.i("Consulta", "Funciona :)")
                val response: HeroDataResponse? = myResponse.body()
                if (response != null) {
                    Log.i("Cuerpo de la consulta", response.toString())
                    val list = response.superheroes.map { it.toDatabase() }
                    if(room.getHeroDao().getAllSuperheroes().isNotEmpty()) {
                        //si no está vacía busca actualizar los heroes
                        for (s in list) {
                            val roomHero = room.getHeroDao().getSuperheroes(s.idApi)
                            if(roomHero.isNotEmpty()){
                                if (roomHero[0].idApi == s.idApi) {
                                    room.getHeroDao().update(s)
                                    //room.getHeroDao().deleteAllSuperheroes()
                                    //room.getHeroDao().update(list)
                                    // if (room.getHeroDao().getAllSuperheroes().isEmpty()) {
                                    /* val heroes = room.getHeroDao().getAllSuperheroes()
                                for (i in heroes.indices) {
                                    println(heroes[i])
                                }
                                Log.i("BASE DE DATOS", room.getHeroDao().getSuperheroes(1).toString())*/
                                } else {
                                    room.getHeroDao().insert(s)
                                }
                            }
                        }
                    }else {
                        //si no encuentra heroes inserta toda la info
                        room.getHeroDao().insertAll(list)
                    }
                    adapter.updateList(room.getHeroDao().getAllSuperheroes())
                    runOnUiThread{
                        binding.progressBar.isVisible = false
                    }
                }
            } else {
                Log.i("Consulta", "No funciona :(")
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://superheroapi.com/api/$MY_TOKEN/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun getRoom(): SHDB{
        return Room
            .databaseBuilder(this, SHDB::class.java, "SuperheroesBD")
            .build()
    }
}