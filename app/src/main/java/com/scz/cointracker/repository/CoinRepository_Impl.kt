package com.scz.cointracker.repository

import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.model.Ticker
import com.scz.cointracker.domain.util.Resource
import com.scz.cointracker.network.BinanceService
import com.scz.cointracker.network.CoinService
import com.scz.cointracker.network.model.CoinDtoMapper
import com.scz.cointracker.network.model.TickerDto
import com.scz.cointracker.network.model.TickerDtoMapper
import com.scz.cointracker.room.CoinDao
import com.scz.cointracker.room.model.CoinEntity

class CoinRepository_Impl(
    private val dao: CoinDao,
    private val coinService: CoinService,
    private val binanceService: BinanceService,
    private val mapper: CoinDtoMapper,
    private val tickerMapper: TickerDtoMapper
) : CoinRepository {

    override suspend fun getCoins(currency: String): Resource<List<Coin>> {
        return try {
            val response = coinService.getCoins(currency)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(mapper.mapFromDtoList(it))
                } ?: Resource.error("Null Data", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("Error", null)
        }
    }

    override suspend fun getTicker(): Resource<List<Ticker>> {
        return try {
            val response = binanceService.getTicker()
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(tickerMapper.mapFromDtoList(it))
                } ?: Resource.error("Null Data", null)
            } else {
                Resource.error("Error", null)
            }
        } catch (e: Exception) {
            Resource.error("Error", null)
        }
    }

    override suspend fun getCoins(): List<CoinEntity> {
        return dao.getCoins()
    }

    override suspend fun insertCoin(coin: CoinEntity) {
        dao.insertCoin(coin)
    }

    override suspend fun deleteCoin(id: Int) {
        dao.deleteCoin(id)
    }
}