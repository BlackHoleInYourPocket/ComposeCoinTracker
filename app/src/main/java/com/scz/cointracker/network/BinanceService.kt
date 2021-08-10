package com.scz.cointracker.network

import com.scz.cointracker.network.model.TickerDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceService {
    @GET("ticker/price")
    suspend fun getTicker(): Response<List<TickerDto>>
}