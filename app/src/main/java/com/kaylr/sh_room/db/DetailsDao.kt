package com.kaylr.sh_room.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DetailsDao {
    @Query("SELECT * FROM details_table")
    suspend fun getAllDetails():List<DetailsEntity>

    @Query("SELECT image FROM hero_table WHERE id LIKE :id")
    fun getHeroImage(id: Int): String

    @Query("SELECT name FROM hero_table WHERE id LIKE :id")
    suspend fun getHeroName(id: Int): String

    @Query("SELECT * FROM details_table WHERE `full-name` LIKE :query")
    suspend fun getDetails(query:String):List<DetailsEntity>

    @Query("SELECT * FROM details_table WHERE id LIKE :id")
    suspend fun getDetails(id:Int):List<DetailsEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(detailsList: DetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(details:DetailsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(details:List<DetailsEntity>)

    @Query("DELETE FROM details_table")
    suspend fun deleteAllDetails()

}