package com.scz.cointracker.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun TwoLineText(title: String, value: String, widthFraction: Float = 1f) {
    Column(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
    ) {
        Text(
            text = title, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Text(
            text = value, modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
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