package com.scz.cointracker.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String,
    val currentPrice: Double,
    val marketCap: Long,
    val high24h: Double,
    val low24h: Double,
    val priceChangePercentage24h: Double
) : Parcelable