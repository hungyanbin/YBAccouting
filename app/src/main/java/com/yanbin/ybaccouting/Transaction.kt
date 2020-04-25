package com.yanbin.ybaccouting

data class Transaction(
    val total: Int,
    val deposit: Int,
    val withDraw: Int,
    val name: String,
    val recordTime: String)