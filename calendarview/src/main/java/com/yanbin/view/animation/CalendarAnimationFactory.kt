package com.yanbin.view.animation

internal interface CalendarAnimationFactory {
    fun empty(): SnapAnimation

    fun horizontal(startOffset: Float, endOffset: Float): SnapAnimation

    fun vertical(startOffset: Float,
                 endOffset: Float,
                 startHeight: Float,
                 endHeight: Float): SnapAnimation
}