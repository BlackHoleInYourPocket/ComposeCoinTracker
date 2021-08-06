package com.scz.cointracker.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.scz.cointracker.room.model.CoinEntity

@Database(entities = [CoinEntity::class], version = 1)
abstract class CoinDatabase : RoomDatabase() {
    abstract fun coinDao(): CoinDao
}