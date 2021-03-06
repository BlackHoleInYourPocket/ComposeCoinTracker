package com.scz.cointracker.domain.model

import java.io.Serializable

data class Coin(
    var entityId: Int = -1,
    val id: String,
    val symbol: String,
    val name: String,
    var boughtPrice: Double = 0.0,
    var boughtUnit: Double = 0.0,
    var profit: Double = 0.0,
    val imageUrl: String,
    var currentPrice: Double = 0.0,
    val marketCap: Long,
    val high24h: Double,
    val low24h: Double,
    val priceChangePercentage24h: Double,
    val marketCapRank: Int,
    var portfolioCategory: String = "",
) : Serializable