package com.scz.cointracker.presentation.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.scz.cointracker.R
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.util.loadPicture

@Composable
fun CoinCard(
    coin: Coin,
    onClick: () -> Unit
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
                    OneLineText(text = symbol.uppercase(), 0.33f)

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

                    OneLineText(text = id.capitalize(Locale.current))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
                ) {
                    TwoLineText(
                        title = stringResource(id = R.string.feed_title1),
                        value = "Bought Price",
                        widthFraction = 0.33f
                    )
                    TwoLineText(
                        title = stringResource(id = R.string.feed_title2),
                        value = currentPrice.toString(),
                        widthFraction = 0.33f
                    )
                    TwoLineText(
                        title = stringResource(id = R.string.feed_title3),
                        value = "Profit-Loss"
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(50.dp)
                ) {
                    TwoLineText(
                        title = stringResource(id = R.string.feed_title4),
                        value = low24h.toString(),
                        0.33f
                    )
                    TwoLineText(
                        title = stringResource(id = R.string.feed_title5),
                        value = high24h.toString(),
                        0.33f
                    )
                    TwoLineText(
                        title = stringResource(id = R.string.feed_title6),
                        value = "% $priceChangePercentage24h"
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