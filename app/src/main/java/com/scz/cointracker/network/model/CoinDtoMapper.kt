package com.scz.cointracker.network.model

import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.util.DtoMapper

class CoinDtoMapper : DtoMapper<CoinDto, Coin> {
    override fun mapFromDomainModel(domainModel: Coin): CoinDto {
        return CoinDto(
            id = domainModel.id,
            symbol = domainModel.symbol,
            name = domainModel.name,
            imageUrl = domainModel.imageUrl,
            currentPrice = domainModel.currentPrice,
            marketCap = domainModel.marketCap,
            high24h = domainModel.high24h,
            low24h = domainModel.low24h,
            priceChangePercentage24h = domainModel.priceChangePercentage24h
        )
    }

    override fun mapToDomainModel(model: CoinDto): Coin {
        return Coin(
            id = model.id,
            symbol = model.symbol,
            name = model.name,
            imageUrl = model.imageUrl,
            currentPrice = model.currentPrice,
            marketCap = model.marketCap,
            high24h = model.high24h,
            low24h = model.low24h,
            priceChangePercentage24h = model.priceChangePercentage24h
        )
    }

    fun mapFromDtoList(initial: List<CoinDto>): List<Coin> {
        return initial.map { mapToDomainModel(it) }
    }

    fun mapToDtoList(initial: List<Coin>): List<CoinDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}