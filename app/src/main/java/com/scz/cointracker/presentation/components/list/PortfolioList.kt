package com.scz.cointracker.presentation.components.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.presentation.components.appbar.CategoryChip
import com.scz.cointracker.presentation.components.cards.CoinCard
import com.scz.cointracker.presentation.components.cards.SwipeDismissItem
import com.scz.cointracker.presentation.components.text.TwoLineText
import com.scz.cointracker.presentation.ui.coinlist.CoinCategory
import com.scz.cointracker.presentation.ui.coinlist.getAllCategories

@Composable
fun PortfolioList(
    selectedCategory: CoinCategory,
    coinsOnScreen: List<Coin>,
    itemListState: MutableState<List<Coin>>,
    onDismissed: (Coin) -> Unit,
    portfolioCategory: List<String>,
    onPortfolioCategoryChanged: (String) -> Unit,
    selectedPortolioCategory: String
) {
    Column {
        Row {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = portfolioCategory) { index, category ->
                    CategoryChip(
                        category = category,
                        onSelected = {
                            onPortfolioCategoryChanged(category)
                        },
                        isSelected = category == selectedPortolioCategory
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(8.dp)
                .border(1.dp, Color.Gray, CircleShape)
        ) {
            Text(
                text = "Bought Price : ${
                    String.format(
                        "%.2f",
                        calculateBoughtPrice(coinsOnScreen)
                    )
                }",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .padding(8.dp)
                    .wrapContentWidth(Alignment.Start)
            )

            Text(
                text = "Current Price : ${
                    String.format(
                        "%.2f",
                        calculateCurrentPrice(coinsOnScreen)
                    )
                }",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .wrapContentWidth(Alignment.End),
                color = if (calculateCurrentPrice(coinsOnScreen) > calculateBoughtPrice(
                        coinsOnScreen
                    )
                ) Color.Green else Color.Red
            )
        }

        Row {
            LazyColumn {
                items(
                    coinsOnScreen,
                    { coin: Coin -> coin.entityId }) { coin ->
                    SwipeDismissItem(
                        content = {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CoinCard(
                                    coin = coin,
                                    onClick = {},
                                    category = selectedCategory
                                )
                            }
                        },
                        onDismissed = { isDismissed ->
                            if (isDismissed) {
                                itemListState.value =
                                    itemListState.value.filter { it != coin }
                                onDismissed(coin)
                            }
                        }
                    )
                }
            }
        }
    }
}

fun calculateBoughtPrice(coinsOnScreen: List<Coin>): Double {
    var boughtPrice = 0.0
    coinsOnScreen.forEach { coin ->
        boughtPrice += coin.boughtPrice.plus(coin.boughtUnit)
    }
    return boughtPrice
}

fun calculateCurrentPrice(coinsOnScreen: List<Coin>): Double {
    var currentPrice = 0.0
    coinsOnScreen.forEach { coin ->
        currentPrice += coin.currentPrice.plus(coin.boughtUnit)
    }
    return currentPrice
}