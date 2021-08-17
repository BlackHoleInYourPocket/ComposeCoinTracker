package com.scz.cointracker.presentation.components.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.model.Ticker
import com.scz.cointracker.extensions.correctCurrentPrice
import com.scz.cointracker.presentation.components.cards.CoinCard
import com.scz.cointracker.presentation.ui.coinlist.CoinCategory

@Composable
fun MarketList(
    coinsOnScreen: List<Coin>,
    tickers: List<Ticker>,
    category: CoinCategory,
    state: LazyListState
) {
    LazyColumn(state = state, modifier = Modifier.padding(bottom = 16.dp)) {
        itemsIndexed(items = coinsOnScreen) { _, coin ->
            CoinCard(
                coin = coin.correctCurrentPrice(tickers),
                onClick = {},
                category = category
            )
        }
    }
}