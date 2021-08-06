package com.scz.cointracker.extensions


fun String.formatDouble(): Double = this.replace(",", ".").toDouble()
