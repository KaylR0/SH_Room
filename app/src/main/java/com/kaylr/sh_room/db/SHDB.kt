package com.kaylr.sh_room.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HeroEntity::class, DetailsEntity::class], version = 1)
abstract class SHDB: RoomDatabase() {
    abstract fun getHeroDao(): HeroDao
    abstract fun getDetailsDao(): DetailsDao
}