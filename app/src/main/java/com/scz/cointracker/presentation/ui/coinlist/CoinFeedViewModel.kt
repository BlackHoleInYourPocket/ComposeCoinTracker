package com.scz.cointracker.presentation.ui.coinlist

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.model.Ticker
import com.scz.cointracker.repository.CoinRepository
import com.scz.cointracker.room.model.CoinEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinFeedViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    val coinsOnScreen: MutableState<List<Coin>> = mutableStateOf(listOf())
    val coinsFromService: MutableState<List<Coin>> = mutableStateOf(listOf())
    val tickers: MutableState<List<Ticker>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val category = mutableStateOf(CoinCategory.MARKET)
    val loading = mutableStateOf(false)
    val selectedPortfolioCategory = mutableStateOf("")
    private val portfolioCategories: MutableState<List<String>> = mutableStateOf(listOf())
    private val coinsFromPortfolio: MutableState<List<Coin>> = mutableStateOf(listOf())
    private val searchList: MutableState<List<Coin>> = mutableStateOf(listOf())

    init {
        getCoins()
        getTicker()
    }

    fun getCoins() {
        viewModelScope.launch {
            coinsFromService.value = listOf()
            loading.value = true
            repository.getCoins("try").data?.let {
                coinsFromService.value = it
                if (category.value == CoinCategory.MARKET) coinsOnScreen.value = it
                else getPortfolio()
            }
            loading.value = false
        }
    }

    fun getTicker() {
        viewModelScope.launch {
            repository.getTicker().data?.let {
                tickers.value = it
            }
        }
    }

    fun getPortfolio() {
        loading.value = true
        coinsOnScreen.value = listOf()
        var portfolio: List<CoinEntity>
        val portfolioList = ArrayList<Coin>()
        viewModelScope.launch {
            portfolio = repository.getCoins()
            mapPortfolioCategories(portfolio)
            portfolio.forEach { portfolioCoin ->
                coinsFromService.value.find { x -> x.id.lowercase() == portfolioCoin.ids.lowercase() }
                    ?.let { coin ->
                        portfolioCoin.id?.let { entityId ->
                            coin.entityId = entityId
                        }
                        portfolioList.add(
                            Coin(
                                coin.entityId,
                                coin.id,
                                coin.symbol,
                                coin.name,
                                portfolioCoin.boughtPrice,
                                portfolioCoin.boughtUnit,
                                coin.profit,
                                coin.imageUrl,
                                coin.currentPrice,
                                coin.marketCap,
                                coin.high24h,
                                coin.low24h,
                                coin.priceChangePercentage24h,
                                coin.marketCapRank,
                                portfolioCoin.portfolioCategory
                            )
                        )
                    }
            }
            coinsFromPortfolio.value = portfolioList
            coinsOnScreen.value = portfolioList
        }
        loading.value = false
    }

    fun addCoinToPortfolio(coin: CoinEntity) {
        viewModelScope.launch {
            loading.value = true
            repository.insertCoin(coin)
            category.value = CoinCategory.PORTFOLIO
            getPortfolio()
            loading.value = false
        }
    }

    fun deleteCoinFromPortfolio(coin: Coin) {
        viewModelScope.launch {
            repository.deleteCoin(coin.entityId)
            refresh()
        }
    }

    fun getPortfolioCategories(): List<String> {
        return portfolioCategories.value
    }

    private fun mapPortfolioCategories(portfolio: List<CoinEntity>) {
        portfolioCategories.value = portfolio.map { it.portfolioCategory }.distinct()
    }

    fun order(orderType: OrderType, listState: LazyListState, coroutineScope: CoroutineScope) {
        when (orderType) {
            OrderType.PROFIT ->
                if (category.value == CoinCategory.PORTFOLIO) coinsOnScreen.value =
                    coinsOnScreen.value.sortedByDescending { x -> x.profit }
            OrderType.SELLINGPRICE -> coinsOnScreen.value =
                coinsOnScreen.value.sortedByDescending { x -> x.currentPrice }
            OrderType.PERCENTAGE24 -> coinsOnScreen.value =
                coinsOnScreen.value.sortedByDescending { x -> x.priceChangePercentage24h }
            OrderType.MARKETCAP -> coinsOnScreen.value =
                coinsOnScreen.value.sortedBy { x -> x.marketCapRank }

        }
        coroutineScope.launch {
            listState.animateScrollToItem(0)
        }
    }

    fun search() {
        searchList.value = listOf()
        if (category.value.value == CoinCategory.MARKET.value)
            searchList.value = coinsFromService.value
        else {
            searchList.value =
                if (selectedPortfolioCategory.value.isNotEmpty())
                    coinsFromPortfolio.value.filter { x -> x.portfolioCategory == selectedPortfolioCategory.value }
                else
                    coinsFromPortfolio.value
        }

        with(searchList.value) {
            if (size < 1) return
            coinsOnScreen.value = this.filter { coin ->
                coin.id.toLowerCase(Locale.current).contains(query.value) ||
                        coin.symbol.toLowerCase(Locale.current).contains(query.value)
            }
        }
    }

    fun refresh() {
        selectedPortfolioCategory.value = ""
        getCoins()
        getTicker()
    }

    fun categoryChanged(category: CoinCategory) {
        this.category.value = category
        clearQueryText()
        getTicker()
        when (category) {
            CoinCategory.MARKET -> getCoins()
            CoinCategory.PORTFOLIO -> getPortfolio()
        }
    }

    fun onPortfolioCategoryChanged(category: String) {
        selectedPortfolioCategory.value = category
        coinsOnScreen.value =
            coinsFromPortfolio.value.filter { x -> x.portfolioCategory == category }
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun clearQuery() {
        clearQueryText()
        if (category.value.value == CoinCategory.MARKET.value) getCoins()
    }

    fun clearQueryText() {
        query.value = ""
    }
}