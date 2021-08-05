package com.scz.cointracker.presentation.ui.coinlist

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinFeedViewModel @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    val coins: MutableState<List<Coin>> = mutableStateOf(listOf())
    private val initialCoins: MutableState<List<Coin>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val category = mutableStateOf(CoinCategory.MARKET)
    val loading = mutableStateOf(false)

    init {
        getCoins()
    }

    fun getCoins() {
        viewModelScope.launch {
            loading.value = true
            delay(1000)
            repository.getCoins("try").data?.let {
                coins.value = it
                initialCoins.value = it
            }
            loading.value = false
        }
    }

    fun getPortfolio() {
        coins.value = listOf()
        initialCoins.value = listOf()
    }

    fun search() {
        with(initialCoins.value) {
            if (size < 1) return
            coins.value = this.filter { coin ->
                coin.id.toLowerCase(Locale.current).contains(query.value) ||
                        coin.symbol.toLowerCase(Locale.current).contains(query.value)
            }
        }
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun clearQuery() {
        query.value = ""
        getCoins()
    }

    fun categoryChanged(category: CoinCategory) {
        this.category.value = category
        query.value = ""
        when (category) {
            CoinCategory.MARKET -> getCoins()
            CoinCategory.PORTFOLIO -> getPortfolio()
        }
    }
}