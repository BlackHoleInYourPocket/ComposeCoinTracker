package com.scz.cointracker.network.model

import java.io.Serializable

data class TickerDto(
    val symbol: String,
    val price: Double
) : Serializable