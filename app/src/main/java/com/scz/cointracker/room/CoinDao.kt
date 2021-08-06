package com.scz.cointracker.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.scz.cointracker.room.model.CoinEntity


@Dao
interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoin(coin: CoinEntity)

    @Query("DELETE FROM coins WHERE id = :id")
    suspend fun deleteCoin(id: Int)

    @Query("SELECT * FROM coins")
    suspend fun getCoins(): List<CoinEntity>
}