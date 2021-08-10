package com.scz.cointracker.extensions

import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.model.Ticker

fun String.formatDouble(): Double = this.replace(",", ".").toDouble()

fun Coin.correctCurrentPrice(tickers: List<Ticker>): Coin {
    if (tickers.isEmpty()) return this
    this.currentPrice =
        tickers.find { x -> x.symbol == this.symbol.uppercase() + "TRY" }?.price
            ?: this.currentPrice
    return this
}