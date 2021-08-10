package com.scz.cointracker.domain.model

import java.io.Serializable

class Ticker(
    val symbol: String,
    val price: Double
) : Serializable