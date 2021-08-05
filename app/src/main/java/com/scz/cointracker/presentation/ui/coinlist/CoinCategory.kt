package com.scz.cointracker.presentation.ui.coinlist

enum class CoinCategory(val value: String) {
    MARKET("Market"),
    PORTFOLIO("Portfolio")
}

fun getAllCategories(): List<CoinCategory> {
    return listOf(CoinCategory.MARKET, CoinCategory.PORTFOLIO)
}