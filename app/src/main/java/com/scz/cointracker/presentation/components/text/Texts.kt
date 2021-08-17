package com.scz.cointracker.presentation.components.text

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.scz.cointracker.presentation.components.appbar.CustomTextField
import com.scz.cointracker.presentation.ui.coinlist.OrderType
import kotlinx.coroutines.CoroutineScope

@Composable
fun CoinCardTwoLineText(
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
fun CoinCardOneLineText(text: String, widthFraction: Float = 1f) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .fillMaxHeight()
            .wrapContentHeight(Alignment.CenterVertically)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun BottomAppBarOrderText(
    text: String,
    onClickOrder: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(1.dp, Color.White, CircleShape)
            .padding(4.dp)
    ) {
        Text(
            color = Color.White, text = text, modifier = Modifier
                .padding(4.dp)
                .clickable { onClickOrder() }
        )
    }
}

@Composable
fun DefaultNumberTextField(focusManager: FocusManager, label: String): String {
    var query by remember { mutableStateOf("") }
    CustomTextField(
        query = query,
        onQueryChanged = { query = it },
        label = label,
        focusManager = focusManager,
        onDone = { /*TODO*/ },
        keyboardType = KeyboardType.Number,
        onTrailIconClick = { query = "" }
    )
    return query
}

@Composable
fun DetailText(
    title: String,
    value: String?,
    titleColor: Color = Color.Black,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.LightGray, CircleShape)
    ) {
        Text(
            text = title,
            style = TextStyle(fontSize = 21.sp, color = titleColor),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
        Text(
            text = value ?: "",
            style = TextStyle(fontSize = 21.sp, color = valueColor),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }
}

