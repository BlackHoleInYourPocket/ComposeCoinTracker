package com.scz.cointracker.presentation.components.text

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun TwoLineText(
    title: String,
    value: String,
    widthFraction: Float = 1f,
    titleColor: Color = Color.Black,
    valueColor: Color = Color.Black
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Text(
            text = title, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
            color = titleColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Text(
            text = value, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally),
            color = valueColor,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

@Composable
fun OneLineText(text: String, widthFraction: Float = 1f) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}