package com.scz.cointracker.presentation.components.appbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.scz.cointracker.presentation.ui.coinlist.CoinCategory
import com.scz.cointracker.presentation.ui.coinlist.getAllCategories

@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    search: () -> Unit,
    clearQuery: () -> Unit,
    categoryChanged: (CoinCategory) -> Unit,
    focusManager: FocusManager,
    selectedCategory: CoinCategory
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        elevation = 10.dp
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                TextField(
                    value = query,
                    onValueChange = { onQueryChanged(it) },
                    label = {
                        Text(text = "Search")
                    },
                    keyboardActions = KeyboardActions(onSearch = {
                        search()
                        focusManager.clearFocus()
                    }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                search()
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
                                clearQuery()
                                focusManager.clearFocus()
                            })
                    }
                )
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                itemsIndexed(items = getAllCategories()) { index, category ->
                    CategoryChip(category = category.value, onSelected = {
                        categoryChanged(category)
                    }, isSelected = category.value == selectedCategory.value)
                }
            }
        }
    }
}


@Composable
fun CategoryChip(
    category: String,
    isSelected: Boolean = false,
    onSelected: () -> Unit
) {
    Surface(
        modifier = Modifier.padding(16.dp, 8.dp, 8.dp, 8.dp),
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Color.LightGray else MaterialTheme.colors.primary
    ) {
        Row(modifier = Modifier.toggleable(
            value = isSelected,
            onValueChange = {
                onSelected()
            }
        )) {
            Text(
                text = category,
                style = MaterialTheme.typography.body2,
                color = Color.White,
                modifier = Modifier
                    .padding(8.dp, 8.dp, 8.dp, 8.dp)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}