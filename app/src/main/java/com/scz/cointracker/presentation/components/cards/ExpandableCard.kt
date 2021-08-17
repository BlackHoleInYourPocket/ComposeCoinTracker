package com.scz.cointracker.presentation.components.cards

import android.os.Bundle
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.scz.cointracker.R
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.model.Ticker
import com.scz.cointracker.extensions.correctCurrentPrice
import com.scz.cointracker.presentation.ui.coinlist.CoinCategory

@ExperimentalMaterialApi
@Composable
fun ExpandableCard(
    title: String,
    titleFontSize: TextUnit = MaterialTheme.typography.body1.fontSize,
    titleFontWeight: FontWeight = FontWeight.Bold,
    shape: Shape = MaterialTheme.shapes.medium,
    padding: Dp = 12.dp,
    coinsOnScreen: List<Coin>,
    navController: NavController,
    tickers: List<Ticker>,
    selectedCategory: CoinCategory,
    itemListState: MutableState<List<Coin>>,
    onDismissed: (Coin) -> Unit,
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .padding(8.dp),
        shape = shape,
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .weight(4f)
                        .padding(8.dp),
                    text = title.capitalize(Locale.current),
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    modifier = Modifier
                        .weight(8f)
                        .padding(8.dp),
                    text = "Profit : ${
                        String.format(
                            "%.3f",
                            calculateGroupProfit(coinsOnScreen, tickers).value
                        )
                    } ",
                    fontSize = titleFontSize,
                    fontWeight = titleFontWeight,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = decideColor(coinsOnScreen, tickers)
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {
                coinsOnScreen.forEach { coin ->
                    SwipeDismissItem(
                        content = {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                CoinCard(
                                    coin = coin.correctCurrentPrice(tickers),
                                    onClick = {
                                        val bundle = Bundle()
                                        bundle.putSerializable("COIN", coin)
                                        navController.navigate(R.id.viewDetail, bundle)
                                    },
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

private fun calculateGroupProfit(
    coinsOnScreen: List<Coin>,
    tickers: List<Ticker>
): MutableState<Double> {
    val profit = mutableStateOf(0.0)
    coinsOnScreen.forEach {
        profit.value += calculateProfit(it.correctCurrentPrice(tickers))
    }
    return profit
}

private fun decideColor(coinsOnScreen: List<Coin>, tickers: List<Ticker>): Color {
    val profit = mutableStateOf(0.0)
    coinsOnScreen.forEach {
        profit.value += calculateProfit(it.correctCurrentPrice(tickers))
    }
    return if (profit.value > 0) Color.Green
    else Color.Red
}

