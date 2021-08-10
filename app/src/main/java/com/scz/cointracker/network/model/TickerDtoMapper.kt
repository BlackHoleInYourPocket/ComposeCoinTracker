package com.scz.cointracker.network.model

import com.scz.cointracker.domain.model.Coin
import com.scz.cointracker.domain.model.Ticker
import com.scz.cointracker.domain.util.DtoMapper

class TickerDtoMapper : DtoMapper<TickerDto, Ticker> {
    override fun mapToDomainModel(model: TickerDto): Ticker {
        return Ticker(
            symbol = model.symbol,
            price = model.price
        )
    }

    override fun mapFromDomainModel(domainModel: Ticker): TickerDto {
        return TickerDto(
            symbol = domainModel.symbol,
            price = domainModel.price
        )
    }

    fun mapFromDtoList(initial: List<TickerDto>): List<Ticker> {
        return initial.map { mapToDomainModel(it) }
    }

    fun mapToDtoList(initial: List<Ticker>): List<TickerDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}