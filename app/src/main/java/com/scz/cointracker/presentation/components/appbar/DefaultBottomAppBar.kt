package com.scz.cointracker.presentation.components.appbar

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.scz.cointracker.room.model.CoinEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DefaultBottomAppBar(
    onClickProfit: () -> Unit,
    onClickSellingPrice: () -> Unit,
    onClickPercentage24h: () -> Unit,
    onClickCapRank: () -> Unit
) {
    BottomAppBar(cutoutShape = CircleShape) {
        Row(horizontalArrangement = Arrangement.Start) {
            Text(text = "Profit", modifier = Modifier
                .padding(4.dp)
                .clickable { onClickProfit() })
            Text(
                text = "Selling",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onClickSellingPrice() })
        }
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            Text(text = "%24h", modifier = Modifier
                .padding(4.dp)
                .clickable {
                    onClickPercentage24h()
                })
            Text(
                text = "Cap Rank",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onClickCapRank() })
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
    var expanded by remember { mutableStateOf(false) }
    val items = coinList.map { it.id.capitalize(Locale.current) }
    var dropDownWidth by remember { mutableStateOf(0) }
    var selectedIndex by remember { mutableStateOf(-1) }
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
            Text(
                if (selectedIndex == -1) "Select Coin" else items[selectedIndex],
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        dropDownWidth = it.width
                    }
                    .clickable(onClick = { expanded = true })
                    .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                style = MaterialTheme.typography.body2
            )
            Icon(
                Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.End)
                    .padding(end = 16.dp, top = 16.dp, bottom = 16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { dropDownWidth.toDp() })
            ) {
                items.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                    }) {
                        Text(text = s)
                    }
                }
            }
        }

        var expanded by remember { mutableStateOf(false) }
        var portfolioCategory by remember { mutableStateOf("") }
        var dropDownWidth by remember { mutableStateOf(0) }

        val icon = if (expanded)
            Icons.Filled.ArrowDropDown
        else
            Icons.Filled.ArrowDropDown

        Box(
            modifier = Modifier
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            OutlinedTextField(
                value = portfolioCategory,
                onValueChange = { portfolioCategory = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        dropDownWidth = it.width
                    },
                label = { Text("Portfolio Category", style = MaterialTheme.typography.body2) },
                trailingIcon = {
                    Icon(icon, "contentDescription", Modifier.clickable { expanded = !expanded })
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { dropDownWidth.toDp() })
            ) {
                portfolioCategories.forEachIndexed { index, s ->
                    DropdownMenuItem(onClick = {
                        portfolioCategory = portfolioCategories[index]
                        expanded = false
                    }) {
                        Text(text = s)
                    }
                }
            }
        }

        Box(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
            CustomTextField(
                query = boughtPrice,
                onQueryChanged = { boughtPrice = it },
                label = "Bought Price",
                focusManager = focusManager,
                onDone = { /*TODO*/ },
                keyboardType = KeyboardType.Number,
                onTrailIconClick = { boughtPrice = "" }
            )
        }

        Box(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
            CustomTextField(
                query = boughtUnit,
                onQueryChanged = { boughtUnit = it },
                label = "Bought Unit",
                focusManager = focusManager,
                onDone = { /*TODO*/ },
                keyboardType = KeyboardType.Number,
                onTrailIconClick = { boughtUnit = "" }
            )
        }

        Box(modifier = Modifier.padding(top = 24.dp, start = 16.dp, end = 16.dp)) {
            Button(
                onClick = {
                    if (selectedIndex == -1 || boughtPrice.isEmpty() || boughtUnit.isEmpty() || portfolioCategory.isEmpty()) {
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
                                items[selectedIndex],
                                boughtPrice.formatDouble(),
                                boughtUnit.formatDouble(),
                                portfolioCategory
                            )
                        )
                        selectedIndex = -1
                        boughtPrice = ""
                        boughtUnit = ""
                        portfolioCategory = ""
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