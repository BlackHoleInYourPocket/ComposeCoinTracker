package com.scz.cointracker.presentation.ui.coin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.presentation.components.text.DetailText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CoinDetailFragment : Fragment() {

    private var coin: MutableState<Coin?>? = mutableStateOf(null)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        CoroutineScope(Main).launch {
            coin?.value = arguments?.getSerializable("COIN") as Coin
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    DetailText(
                        title = "Coin Name",
                        value = coin?.value?.id?.capitalize(Locale.current)
                    )
                    DetailText(title = "Bought Price", value = coin?.value?.boughtPrice.toString())
                    DetailText(
                        title = "Current Price",
                        value = coin?.value?.currentPrice.toString()
                    )
                    DetailText(title = "Bought Unit", value = coin?.value?.boughtUnit.toString())
                    DetailText(title = "Total Price", value = calculateTotalPrice(coin?.value))
                    DetailText(
                        title = "Total Current Price",
                        value = calculateTotalCurrentPrice(coin?.value)
                    )
                    coin?.value?.profit?.let {
                        if (it > 0) Color.Green else Color.Red
                    }?.let {
                        DetailText(
                            title = "Profit",
                            value = String.format("%.3f", coin?.value?.profit),
                            valueColor = it
                        )
                    }
                }
            }
        }
    }

    private fun calculateTotalPrice(coin: Coin?): String {
        coin?.let {
            return String.format("%.3f", it.boughtPrice.times(it.boughtUnit))
        }
        return ""
    }

    private fun calculateTotalCurrentPrice(coin: Coin?): String {
        coin?.let {
            return String.format("%.3f", it.currentPrice.times(it.boughtUnit))
        }
        return ""
    }
}