package com.yanbin.ybaccouting.utils

fun String?.isNotNullOrEmpty(): Boolean {
    return this?.isNotEmpty() ?: false
}