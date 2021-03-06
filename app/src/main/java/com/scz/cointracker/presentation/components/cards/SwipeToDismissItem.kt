package com.scz.cointracker.presentation.components.cards

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun SwipeDismissItem(
    content: @Composable () -> Unit,
    onDismissed: (isDismissed: Boolean) -> Unit,
) {

    val hasTriedToDismiss = remember { mutableStateOf(false) }
    var hasConfirmedDismissal: Boolean by remember { mutableStateOf(false) }

    val dismissState = rememberDismissState(
        confirmStateChange = {
            if (it == DismissValue.DismissedToEnd) {
                hasTriedToDismiss.value = true
                hasConfirmedDismissal
            } else {
                hasTriedToDismiss.value = false
                hasConfirmedDismissal
            }
        }
    )
    val dismissedToEnd = dismissState.isDismissed(DismissDirection.StartToEnd)
    val dismissedToStart = dismissState.isDismissed(DismissDirection.EndToStart)
    val isDismissed = (dismissedToEnd || dismissedToStart)

    onDismissed.invoke(isDismissed)

    val contentOffset = if (hasTriedToDismiss.value) {
        48.dp
    } else {
        0.dp
    }

    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(visible = !isDismissed) {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.StartToEnd),
            background = {
                SwipeBackground(
                    onDeleteClicked = {
                        hasConfirmedDismissal = true
                        coroutineScope.launch {
                            dismissState.dismiss(DismissDirection.StartToEnd)
                        }
                    }
                )
            },
        ) {
            Box(
                modifier = Modifier.offset(x = contentOffset),
            ) {
                content()
            }
        }
    }
}

@Composable
fun SwipeBackground(
    onDeleteClicked: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = Color.Red),
    ) {
        IconButton(onClick = onDeleteClicked) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
        }
    }
}