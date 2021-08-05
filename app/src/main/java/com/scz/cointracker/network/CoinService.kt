package com.scz.cointracker.network

import com.scz.cointracker.network.model.CoinDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinService {
    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String,
        @Query("ids") ids: String? = null
    ): Response<List<CoinDto>>
}