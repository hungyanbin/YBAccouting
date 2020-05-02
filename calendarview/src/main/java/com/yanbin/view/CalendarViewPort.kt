package com.yanbin.view

internal class CalendarViewPort {

    var xOffset = 0f
    var viewWidth = 0f

    var state = ViewPortState.IDLE

    fun scrollHorizontally(distance: Float) {
        if (state == ViewPortState.SNAP) {
            return
        }

        xOffset += distance
        state = ViewPortState.SCROLLING
    }

    fun calculateSnapOffset(): Float {
        return if (state == ViewPortState.SCROLLING) {
            state = ViewPortState.SNAP

            if (xOffset in 0f..viewWidth / 2 || xOffset in -viewWidth / 2..0f) {
                0f
            } else if (xOffset > viewWidth / 2) {
                viewWidth
            } else {
                -viewWidth
            }
        } else {
            Float.NaN
        }
    }
}

internal enum class ViewPortState {
    IDLE, SCROLLING, SNAP
}