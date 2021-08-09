package com.scz.cointracker.presentation.ui.coinlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.navigation.findNavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.scz.cointracker.presentation.components.animation.CircularIndeterminateProgressBar
import com.scz.cointracker.presentation.components.animation.ExpandInFadeOutAnimatedContent
import com.scz.cointracker.presentation.components.animation.FadeInFadeOutAnimatedContent
import com.scz.cointracker.presentation.components.appbar.*
import com.scz.cointracker.presentation.components.cards.LoadingCoinShimmer
import com.scz.cointracker.presentation.components.list.MarketList
import com.scz.cointracker.presentation.components.list.PortfolioList
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
                val portfolioItemListState = remember { mutableStateOf(coinsOnScreen) }
                val rememberLazyListStatePortfolio = rememberLazyListState()
                val rememberLazyListStateMarket = rememberLazyListState()

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
                            DefaultBottomDrawer(
                                focusManager,
                                coroutineScope,
                                scaffoldState,
                                coinsFromService,
                                viewModel::addCoinToPortfolio,
                                viewModel.getPortfolioCategories()
                            )
                        },
                        drawerElevation = 8.dp,
                        bottomBar = {
                            DefaultBottomAppBar(
                                onClickOrder = viewModel::order,
                                if (viewModel.category.value == CoinCategory.PORTFOLIO) rememberLazyListStatePortfolio else rememberLazyListStateMarket,
                                coroutineScope
                            )
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
                                    if (viewModel.category.value == CoinCategory.MARKET) MarketList(
                                        coinsOnScreen = coinsOnScreen,
                                        category = viewModel.category.value,
                                        state = rememberLazyListStateMarket
                                    )
                                    else if (coinsOnScreen.isNotEmpty()) PortfolioList(
                                        selectedCategory = viewModel.category.value,
                                        coinsOnScreen = coinsOnScreen,
                                        itemListState = portfolioItemListState,
                                        onDismissed = viewModel::deleteCoinFromPortfolio,
                                        portfolioCategory = viewModel.getPortfolioCategories(),
                                        onPortfolioCategoryChanged = viewModel::onPortfolioCategoryChanged,
                                        selectedPortolioCategory = viewModel.selectedPortfolioCategory.value,
                                        state = rememberLazyListStatePortfolio,
                                        navController = findNavController()
                                    )
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