package com.scz.cointracker.presentation.ui.coinlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.repository.CoinRepository
import com.scz.cointracker.room.model.CoinEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinFeedViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    val coinsOnScreen: MutableState<List<Coin>> = mutableStateOf(listOf())
    val coinsFromService: MutableState<List<Coin>> = mutableStateOf(listOf())
    val coinsFromPortfolio: MutableState<List<Coin>> = mutableStateOf(listOf())
    private val searchList: MutableState<List<Coin>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val category = mutableStateOf(CoinCategory.MARKET)
    val loading = mutableStateOf(false)

    init {
        getCoins()
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

    fun getPortfolio() {
        loading.value = true
        coinsOnScreen.value = listOf()
        var portfolioIds: List<CoinEntity>
        val portfolioList = ArrayList<Coin>()
        viewModelScope.launch {
            portfolioIds = repository.getCoins()
            portfolioIds.forEach { portfolioCoin ->
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
                                coin.marketCapRank
                            )
                        )
                    }
            }
            val finalList = calcualteProfit(portfolioList)
            coinsFromPortfolio.value = finalList
            coinsOnScreen.value = finalList
        }
        loading.value = false
    }

    private fun calcualteProfit(list: List<Coin>): List<Coin> {
        list.forEach {
            it.profit = it.currentPrice.minus(it.boughtPrice).times(it.boughtUnit)
        }
        return list
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
        }
    }

    fun search() {
        searchList.value = listOf()
        if (category.value.value == CoinCategory.MARKET.value)
            searchList.value = coinsFromService.value
        else searchList.value = coinsFromPortfolio.value

        with(searchList.value) {
            if (size < 1) return
            coinsOnScreen.value = this.filter { coin ->
                coin.id.toLowerCase(Locale.current).contains(query.value) ||
                        coin.symbol.toLowerCase(Locale.current).contains(query.value)
            }
        }
    }

    fun refresh() {
        getCoins()
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

    fun categoryChanged(category: CoinCategory) {
        this.category.value = category
        clearQueryText()
        when (category) {
            CoinCategory.MARKET -> getCoins()
            CoinCategory.PORTFOLIO -> getPortfolio()
        }
    }
}