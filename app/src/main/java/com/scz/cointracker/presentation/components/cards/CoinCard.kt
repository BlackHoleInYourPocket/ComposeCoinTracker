package com.scz.cointracker.presentation.components.cards

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.scz.cointracker.R
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.presentation.components.text.CoinCardOneLineText
import com.scz.cointracker.presentation.components.text.CoinCardTwoLineText
import com.scz.cointracker.presentation.ui.coinlist.CoinCategory
import com.scz.cointracker.util.loadPicture

@Composable
fun CoinCard(
    coin: Coin,
    onClick: () -> Unit,
    category: CoinCategory
) {
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(
                start = 8.dp,
                bottom = 8.dp,
                top = 8.dp,
                end = 8.dp
            )
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp
    ) {
        with(coin) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
                ) {
                    CoinCardOneLineText(text = symbol.uppercase(), 0.33f)

                    getImage(imageUrl = imageUrl)?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.33f)
                                .requiredHeight(50.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    CoinCardOneLineText(text = id.capitalize(Locale.current))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
                ) {
                    CoinCardTwoLineText(
                        title = if (category.value == CoinCategory.MARKET.value) stringResource(id = R.string.feed_title7)
                        else stringResource(id = R.string.feed_title1),
                        value = if (category.value == CoinCategory.MARKET.value) marketCap.toString()
                        else boughtPrice.toString(),
                        widthFraction = 0.33f
                    )
                    CoinCardTwoLineText(
                        title = stringResource(id = R.string.feed_title2),
                        value = currentPrice.toString(),
                        widthFraction = 0.33f
                    )
                    CoinCardTwoLineText(
                        title = if (category.value == CoinCategory.MARKET.value) stringResource(id = R.string.feed_title8)
                        else stringResource(id = R.string.feed_title3),
                        value = if (category.value == CoinCategory.MARKET.value) marketCapRank.toString()
                        else String.format("%.3f", profit),
                        valueColor = if (category.value == CoinCategory.PORTFOLIO.value && profit > 0) Color.Green
                        else if (category.value == CoinCategory.PORTFOLIO.value) Color.Red else Color.Black
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
                ) {
                    CoinCardTwoLineText(
                        title = stringResource(id = R.string.feed_title4),
                        value = low24h.toString(),
                        0.33f
                    )
                    CoinCardTwoLineText(
                        title = stringResource(id = R.string.feed_title5),
                        value = high24h.toString(),
                        0.33f
                    )
                    CoinCardTwoLineText(
                        title = stringResource(id = R.string.feed_title6),
                        value = "% ${String.format("%.3f", priceChangePercentage24h)}"
                    )
                }
            }
        }
    }
}

@Composable
fun getImage(imageUrl: String): Bitmap? {
    return loadPicture(
        url = imageUrl,
        defaultImage = R.drawable.ic_launcher_background
    ).value
}