package com.scz.cointracker.presentation.ui.coinlist

enum class CoinCategory(val value: String) {
    MARKET("Market"),
    PORTFOLIO("Portfolio")
}

enum class OrderType(val value: Int) {
    NONE(0),
    PROFIT(1),
    SELLINGPRICE(2),
    PERCENTAGE24(3),
    MARKETCAP(4)
}

fun getAllCategories(): List<CoinCategory> {
    return listOf(CoinCategory.MARKET, CoinCategory.PORTFOLIO)
}