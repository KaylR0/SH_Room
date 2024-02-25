package com.kaylr.sh_room.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HeroDao {
    @Query("SELECT * FROM hero_table")
    suspend fun getAllSuperheroes():List<HeroEntity>

    @Query("SELECT * FROM hero_table WHERE idApi LIKE :query")
    suspend fun getSuperheroes(query:String):List<HeroEntity>

    @Query("SELECT * FROM hero_table WHERE idApi LIKE :id")
    suspend fun getSuperheroes(id:Int):HeroEntity

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(hero: HeroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(superhero:HeroEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(superheroes:List<HeroEntity>)

    @Query("DELETE FROM hero_table")
    suspend fun deleteAllSuperheroes()

}