package com.scz.cointracker.presentation.components.appbar

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.extensions.formatDouble
import com.scz.cointracker.presentation.components.dropdown.DefaultDropdown
import com.scz.cointracker.presentation.components.dropdown.OutlinedDropdown
import com.scz.cointracker.presentation.components.text.BottomAppBarOrderText
import com.scz.cointracker.presentation.components.text.DefaultNumberTextField
import com.scz.cointracker.presentation.ui.coinlist.OrderType
import com.scz.cointracker.room.model.CoinEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DefaultBottomAppBar(
    onClickOrder: (OrderType, LazyListState, CoroutineScope) -> Unit,
    state: LazyListState,
    coroutineScope: CoroutineScope
) {
    BottomAppBar(cutoutShape = CircleShape) {
        Row(horizontalArrangement = Arrangement.Start) {
            BottomAppBarOrderText(
                text = "Profit",
                onClickOrder = { onClickOrder(OrderType.PROFIT, state, coroutineScope) })

            BottomAppBarOrderText(
                text = "Selling",
                onClickOrder = { onClickOrder(OrderType.SELLINGPRICE, state, coroutineScope) })
        }
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            BottomAppBarOrderText(
                text = "%24h",
                onClickOrder = { onClickOrder(OrderType.PERCENTAGE24, state, coroutineScope) })
            BottomAppBarOrderText(
                text = "Cap Rank",
                onClickOrder = { onClickOrder(OrderType.MARKETCAP, state, coroutineScope) })
        }
    }
}

@Composable
fun DefaultBottomDrawer(
    focusManager: FocusManager,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState,
    coinList: List<Coin>,
    onAdd: (CoinEntity) -> Unit,
    portfolioCategories: List<String>
) {
    val items = coinList.map { it.id.capitalize(Locale.current) }.sorted()
    var selectedCoin by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    var boughtPrice by remember { mutableStateOf("") }
    var boughtUnit by remember { mutableStateOf("") }
    if (items.isEmpty()) return

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

        Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
            Text(
                text = "ADD PORTFOLIO",
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
                .border(1.dp, Color.Gray, RectangleShape)
        ) {
            selectedCoin = DefaultDropdown(items = items)
        }

        Box(
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            selectedCategory = OutlinedDropdown(items = portfolioCategories)
        }

        Box(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
            boughtPrice =
                DefaultNumberTextField(focusManager = focusManager, label = "Bought Price")
        }

        Box(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
            boughtUnit = DefaultNumberTextField(focusManager = focusManager, label = "Bought Unit")
        }

        Box(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
            Button(
                onClick = {
                    if (selectedCoin.isEmpty() || boughtPrice.isEmpty() || boughtUnit.isEmpty() || selectedCategory.isEmpty()) {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Fill All Data!",
                                actionLabel = "OK"
                            )
                        }
                    } else {
                        onAdd(
                            CoinEntity(
                                selectedCoin,
                                boughtPrice.formatDouble(),
                                boughtUnit.formatDouble(),
                                selectedCategory
                            )
                        )
                        selectedCoin = ""
                        boughtPrice = ""
                        boughtUnit = ""
                        selectedCategory = ""
                        coroutineScope.launch {
                            scaffoldState.drawerState.close()
                            focusManager.clearFocus()
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                Text(text = "Add")
            }
        }
    }
}

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {

    SnackbarHost(hostState = snackbarHostState, snackbar = { data ->
        Snackbar(modifier = Modifier.padding(16.dp),
            content = {
                Text(
                    text = data.message,
                    style = MaterialTheme.typography.body2,
                    color = Color.White
                )
            },
            action = {
                data.actionLabel?.let { actionLabel ->
                    TextButton(onClick = { onDismiss() }) {
                        Text(
                            text = actionLabel,
                            style = MaterialTheme.typography.body2,
                            color = Color.White
                        )
                    }
                }
            })
    }, modifier = modifier)

}

@Composable
fun CustomFloatingActionButton(
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    ExtendedFloatingActionButton(
        text = { Text("Add Portfolio") },
        onClick = { coroutineScope.launch { scaffoldState.drawerState.open() } }
    )
}

@Composable
fun CustomTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    label: String,
    focusManager: FocusManager,
    onDone: () -> Unit,
    keyboardType: KeyboardType,
    onTrailIconClick: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = { onQueryChanged(it) },
        label = {
            Text(text = label)
        },
        keyboardActions = KeyboardActions(onDone = {
            onDone()
            focusManager.clearFocus()
        }),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        leadingIcon = {
            Icon(
                Icons.Filled.Done,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onDone()
                    focusManager.clearFocus()
                })
        },
        textStyle = TextStyle(
            color = MaterialTheme.colors.onSurface,
        ),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        modifier = Modifier
            .fillMaxWidth(),
        trailingIcon = {
            Icon(
                Icons.Filled.Cancel,
                contentDescription = null,
                modifier = Modifier.clickable {
                    onTrailIconClick()
                    focusManager.clearFocus()
                })
        }
    )
}
