package com.scz.cointracker.presentation.components.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.presentation.components.cards.CoinCard
import com.scz.cointracker.presentation.ui.coinlist.CoinCategory

@Composable
fun MarketList(
    coinsOnScreen: List<Coin>,
    category: CoinCategory,
    state: LazyListState
) {
    LazyColumn(state = state) {
        itemsIndexed(items = coinsOnScreen) { _, coin ->
            CoinCard(
                coin = coin,
                onClick = {},
                category = category
            )
        }
    }
}