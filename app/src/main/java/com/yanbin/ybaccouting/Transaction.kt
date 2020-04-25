package com.yanbin.ybaccouting

import com.soywiz.klock.DateTime

data class Transaction(
    val total: Int,
    val deposit: Int,
    val withDraw: Int,
    val name: String,
    val recordTime: DateTime)