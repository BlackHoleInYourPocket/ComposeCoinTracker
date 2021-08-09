package com.scz.cointracker.presentation.components.dropdown

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun DefaultDropdown(
    items: List<String>
): String {
    var dropDownWidth by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }
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
        modifier = androidx.compose.ui.Modifier
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
    return if (selectedIndex != -1) items[selectedIndex] else ""
}

@Composable
fun OutlinedDropdown(items: List<String>): String {
    var selectedText by remember { mutableStateOf("") }
    var dropDownWidth by remember { mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.ArrowDropDown
    else
        Icons.Filled.ArrowDropDown
    OutlinedTextField(
        value = selectedText,
        onValueChange = { selectedText = it },
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
        items.forEachIndexed { index, s ->
            DropdownMenuItem(onClick = {
                selectedText = items[index]
                expanded = false
            }) {
                Text(text = s)
            }
        }
    }
    return selectedText
}