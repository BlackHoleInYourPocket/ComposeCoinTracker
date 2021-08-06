package com.scz.cointracker.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Coins")
data class CoinEntity(
    var ids: String,
    var boughtPrice: Double,
    var boughtUnit: Double,
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
)