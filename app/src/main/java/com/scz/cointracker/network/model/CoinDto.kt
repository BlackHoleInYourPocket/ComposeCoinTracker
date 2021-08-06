package com.scz.cointracker.network.model

import com.google.gson.annotations.SerializedName

data class CoinDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("image")
    val imageUrl: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("market_cap")
    val marketCap: Long,
    @SerializedName("high_24h")
    val high24h: Double,
    @SerializedName("low_24h")
    val low24h: Double,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int
)