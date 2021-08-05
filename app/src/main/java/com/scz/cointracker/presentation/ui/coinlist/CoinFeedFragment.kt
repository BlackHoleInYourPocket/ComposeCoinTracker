package com.scz.cointracker.presentation.ui.coinlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.scz.cointracker.presentation.components.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinFeedFragment : Fragment() {

    private val viewModel: CoinFeedViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val coins = viewModel.coins.value
                val query = viewModel.query.value
                val focusManager = LocalFocusManager.current
                val loading = viewModel.loading.value

                Column {
                    SearchAppBar(
                        query = query,
                        onQueryChanged = viewModel::onQueryChanged,
                        search = viewModel::search,
                        clearQuery = viewModel::clearQuery,
                        categoryChanged = viewModel::categoryChanged,
                        focusManager = focusManager,
                        selectedCategory = viewModel.category.value
                    )

                    AnimatedVisibility(
                        visible = loading,
                        exit = shrinkOut(animationSpec = getAnimationSpec())
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            LoadingCoinShimmer(imageHeight = 150.dp)
                            CircularIndeterminateProgressBar(isDisplayed = loading)
                        }
                    }

                    AnimatedVisibility(
                        visible = !loading,
                        enter = expandIn(animationSpec = getAnimationSpec()),
                        exit = shrinkOut(animationSpec = getAnimationSpec())
                    ) {
                        SwipeRefresh(state = rememberSwipeRefreshState(false),
                            onRefresh = { viewModel.getCoins() }) {
                            LazyColumn {
                                itemsIndexed(items = coins) { _, coin ->
                                    CoinCard(coin = coin, onClick = {})
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getAnimationSpec(duration: Int = 1000, delay: Int = 300): FiniteAnimationSpec<IntSize> {
    return tween(durationMillis = duration, delayMillis = delay)
}