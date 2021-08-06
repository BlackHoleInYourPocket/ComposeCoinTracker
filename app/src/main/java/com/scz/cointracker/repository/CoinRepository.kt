package com.scz.cointracker.repository

import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.util.Resource
import com.scz.cointracker.room.model.CoinEntity

interface CoinRepository {
    suspend fun getCoins(currency: String): Resource<List<Coin>>

    suspend fun getCoins(): List<CoinEntity>

    suspend fun insertCoin(coin: CoinEntity)

    suspend fun deleteCoin(id: Int)
}