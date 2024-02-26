package com.kaylr.sh_room

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.room.Room
import com.kaylr.sh_room.HeroActivity.Companion.EXTRA_ID
import com.kaylr.sh_room.HeroActivity.Companion.MY_TOKEN
import com.kaylr.sh_room.databinding.ActivityDetailHeroBinding
import com.kaylr.sh_room.db.DetailsEntity
import com.kaylr.sh_room.db.HeroEntity
import com.kaylr.sh_room.db.SHDB
import com.kaylr.sh_room.db.toDatabase
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class DetailHeroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailHeroBinding
    private lateinit var room: SHDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        room = getRoom()

        setContentView(R.layout.activity_detail_hero)
        binding = ActivityDetailHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var id: String = intent.getStringExtra(EXTRA_ID).orEmpty()
        getSuperheroInformation(id.toInt())//id
    }

    private fun getSuperheroInformation(id: Int) {//id: String
        CoroutineScope(Dispatchers.IO).launch {
            val superheroDetail: Response<HeroDetailResponse> =
                getRetrofit().create(ApiService::class.java).getSuperheroDetail()//(id)
            if (superheroDetail.isSuccessful) {
                val response: HeroDetailResponse? = superheroDetail.body()
                if (response != null) {
                    val list = response.details.map { it.toDatabase() }
                    if(room.getDetailsDao().getAllDetails().isNotEmpty()) {
                        //si no está vacía, busca actualizar los details
                        for (s in list) {
                            val roomDetail = room.getDetailsDao().getDetails(id)
                            if (roomDetail.isNotEmpty()) {
                                if (roomDetail[0].id == id) {
                                    room.getDetailsDao().update(s)
                                }
                            }else{
                                room.getDetailsDao().insert(s)
                            }
                        }
                    }else {
                        //si no encuentra heroes inserta toda la info
                        room.getDetailsDao().insertAll(list)
                    }
                    val listDetails = room.getDetailsDao().getAllDetails()
                    runOnUiThread {
                        createUI(listDetails, id)
                    }
                }
            }
        }
    }

    private fun createUI(listDetails: List<DetailsEntity>, idHero: Int){
        CoroutineScope(Dispatchers.IO).launch {
        for (i in listDetails.listIterator()){
            println("${i.id}--${idHero}")

            if (i.id == idHero){
                println("${idHero}--${i.fullName}")

                val image = Picasso.get().load(room.getDetailsDao().getHeroImage(i.id))
                CoroutineScope(Dispatchers.Main).launch {
                    image.into(binding.ivSuperhero)
                }

                binding.tvSuperheroName.text = room.getDetailsDao().getHeroName(i.id)
                binding.tvSuperheroRealName.text = i.fullName
                binding.tvPublisher.text = i.publisher
                prepareStats(i)//superHero.powerstats
                }
            }
        }
    }
    private fun prepareStats(it: DetailsEntity) {//powerstats: PowerStatsResponse
        updateHeight(binding.viewIntelligence, it.intelligence.orEmpty())
        updateHeight(binding.viewStrength, it.strength.orEmpty())
        updateHeight(binding.viewSpeed, it.speed.orEmpty())
        updateHeight(binding.viewDurability, it.durability.orEmpty())
        updateHeight(binding.viewPower, it.power.orEmpty())
        updateHeight(binding.viewCombat, it.combat.orEmpty())//it.combat?.toString().orEmpty())
    }
    private fun updateHeight(view: View, stat:String){
        val params = view.layoutParams
        if(stat == "null"){
            params.height = 0
        }else {
            params.height = pxToDp(stat.toFloat())
        }
        CoroutineScope(Dispatchers.Main).launch {
            view.layoutParams = params
        }
    }
    private fun pxToDp(px:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, resources.displayMetrics).roundToInt()
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
            .databaseBuilder(this, SHDB::class.java, "BDSuperHeroes")
            .build()
    }
}