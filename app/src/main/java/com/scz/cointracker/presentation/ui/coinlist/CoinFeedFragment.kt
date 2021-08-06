package com.scz.cointracker.presentation.ui.coinlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.scz.cointracker.presentation.components.*
import com.scz.cointracker.presentation.util.AnimationUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinFeedFragment : Fragment() {

    private val viewModel: CoinFeedViewModel by viewModels()

    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalMaterialApi::class
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val coinsOnScreen = viewModel.coinsOnScreen.value
                val coinsFromService = viewModel.coinsFromService.value
                val query = viewModel.query.value
                val focusManager = LocalFocusManager.current
                val loading = viewModel.loading.value
                val coroutineScope = rememberCoroutineScope()
                val scaffoldState = rememberScaffoldState()
                val itemListState = remember {
                    mutableStateOf(coinsOnScreen)
                }
                Column {
                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                search = viewModel::search,
                                clearQuery = viewModel::clearQuery,
                                categoryChanged = viewModel::categoryChanged,
                                focusManager = focusManager,
                                selectedCategory = viewModel.category.value
                            )
                        },
                        snackbarHost = {
                            scaffoldState.snackbarHostState
                        },
                        scaffoldState = scaffoldState,
                        drawerContent = {
                            BottomDrawer(
                                focusManager,
                                coroutineScope,
                                scaffoldState,
                                coinsFromService,
                                viewModel::addCoinToPortfolio
                            )
                        },
                        drawerElevation = 8.dp,
                        bottomBar = {
                            BottomAppBar()
                        },
                        floatingActionButton = {
                            CustomFloatingActionButton(coroutineScope, scaffoldState)
                        },
                        floatingActionButtonPosition = FabPosition.Center,
                        isFloatingActionButtonDocked = true
                    ) {
                        Box(modifier = Modifier.padding(it)) {
                            FadeInFadeOutAnimatedContent(visible = loading, initialAlpha = 0.7f) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    LoadingCoinShimmer(imageHeight = 150.dp)
                                    CircularIndeterminateProgressBar(isDisplayed = loading)
                                }
                            }

                            ExpandInFadeOutAnimatedContent(visible = !loading) {
                                SwipeRefresh(state = rememberSwipeRefreshState(false),
                                    onRefresh = { viewModel.refresh() }) {
                                    if (viewModel.category.value == CoinCategory.MARKET) LazyColumn {
                                        itemsIndexed(items = coinsOnScreen) { _, coin ->
                                            CoinCard(
                                                coin = coin,
                                                onClick = {},
                                                category = viewModel.category.value
                                            )
                                        }
                                    }
                                    else LazyColumn {
                                        itemsIndexed(
                                            items = coinsOnScreen
                                        ) { _, coin ->
                                            SwipeDismissItem(
                                                content = {
                                                    Column(
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        CoinCard(
                                                            coin = coin,
                                                            onClick = {},
                                                            category = viewModel.category.value
                                                        )
                                                    }
                                                },
                                                onDismissed = { isDismissed ->
                                                    if (isDismissed) {
                                                        itemListState.value =
                                                            itemListState.value.filter { it != coin }
                                                        viewModel.deleteCoinFromPortfolio(coin)
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            DefaultSnackbar(
                                snackbarHostState = scaffoldState.snackbarHostState,
                                onDismiss = {
                                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                                },
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    }
                }
            }
        }
    }
}