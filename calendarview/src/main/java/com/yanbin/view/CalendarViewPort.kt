package com.yanbin.view

import java.lang.IllegalStateException

internal class CalendarViewPort {

    var xOffset = 0f
        private set
    var yOffset = 0f
    var anchorRow = 0

    var viewWidth = 0f
    var viewHeight = 0f

    private var maxViewHeight = 0f
    private var minViewHeight = 0f

    private var onViewPortStateChanged: (ViewPortState) -> Unit = {}
    //TODO need to revisit and refactor it (HORIZONTAL and VERTICAL)
    var state = ViewPortState.IDLE

    fun setDayCellHeight(cellHeight: Float) {
        minViewHeight = cellHeight
        maxViewHeight = cellHeight * WEEKS_OF_ONE_MONTH
        if (viewHeight == 0f) {
            viewHeight = maxViewHeight
        }
    }

    fun setViewPortStateListener(listener: (ViewPortState) -> Unit) {
        onViewPortStateChanged = listener
    }

    fun scrollHorizontally(distance: Float) {
        if (state == ViewPortState.SNAP_HORIZONTAL) {
            return
        }

        if (state != ViewPortState.SCROLL_HORIZONTAL) {
            updateViewPortState(ViewPortState.SCROLL_HORIZONTAL)
        }

        xOffset += distance
    }


    fun resizeVertically(distance: Float) {
        if (state != ViewPortState.SCROLL_VERTICAL) {
            updateViewPortState(ViewPortState.SCROLL_VERTICAL)
        }

        if (ableToExpand(distance) || ableToShrink(distance)) {
            viewHeight += distance
            val anchorHeight = anchorRow * minViewHeight
            val totalDistance = maxViewHeight - viewHeight
            val maxDistance = maxViewHeight - minViewHeight
            yOffset = -anchorHeight * totalDistance / maxDistance
        }
    }

    private fun ableToShrink(distance: Float) =
        distance < 0 && viewHeight + distance >= minViewHeight

    private fun ableToExpand(distance: Float) =
        distance > 0 && viewHeight + distance <= maxViewHeight

    fun snapHorizontally(newOffset: Float) {
        if (state == ViewPortState.SCROLL_HORIZONTAL) {
            updateViewPortState(ViewPortState.SNAP_HORIZONTAL)
        }

        xOffset = newOffset
    }

    fun snapVertically(offset: Float, height: Float) {
        if (state == ViewPortState.SCROLL_VERTICAL) {
            updateViewPortState(ViewPortState.SNAP_VERTICAL)
        }

        yOffset = offset
        viewHeight = height
    }

    fun generateSnapAnimation(): SnapAnimation {
        return when(state) {
            ViewPortState.SCROLL_HORIZONTAL -> {
                val startOffset = xOffset
                val endOffset = calculateSnapOffset()
                HorizontalSnapAnimation(startOffset, endOffset)
            }
            ViewPortState.SCROLL_VERTICAL -> {
                val startOffset = yOffset
                val endOffset = calculateSnapOffset()
                val startHeight = viewHeight
                val endHeight = calculateVerticalSnapHeight()
                VerticalSnapAnimation(startOffset, endOffset, startHeight, endHeight)
            }
            else -> throw IllegalStateException("Impossible!!")
        }

    }

    fun calculateSnapOffset(): Float {
        return when(state) {
            ViewPortState.SCROLL_HORIZONTAL -> calculateHorizontalSnapOffset()
            ViewPortState.SCROLL_VERTICAL -> calculateVerticalSnapOffset()
            else -> Float.NaN
        }
    }

    //TODO need implement
    private fun calculateVerticalSnapOffset(): Float {
        return if (needShrinkVertically()) {
            - anchorRow * minViewHeight
        } else {
            0f
        }
    }

    //TODO need implement
    private fun calculateVerticalSnapHeight(): Float {
        return if (needShrinkVertically()) {
            minViewHeight
        } else {
            maxViewHeight
        }
    }

    private fun needShrinkVertically(): Boolean {
        val totalDistance = maxViewHeight - viewHeight
        return totalDistance > (maxViewHeight - minViewHeight) / 2
    }

    private fun calculateHorizontalSnapOffset(): Float {
        return if (xOffset in 0f..viewWidth / 2 || xOffset in -viewWidth / 2..0f) {
            0f
        } else if (xOffset > viewWidth / 2) {
            viewWidth
        } else {
            -viewWidth
        }
    }

    fun onSnapComplete() {
        if (xOffset == viewWidth) {
            onViewPortStateChanged.invoke(ViewPortState.PREV_VIEW)
        } else if (xOffset == -viewWidth) {
            onViewPortStateChanged.invoke(ViewPortState.NEXT_VIEW)
        }

        xOffset = 0f
        updateViewPortState(ViewPortState.IDLE)
    }

    private fun updateViewPortState(state: ViewPortState) {
        this.state = state
        onViewPortStateChanged.invoke(state)
    }

    companion object {
        const val WEEKS_OF_ONE_MONTH = 5
    }
}

internal enum class ViewPortState {
    IDLE, SCROLL_HORIZONTAL, SCROLL_VERTICAL, SNAP_HORIZONTAL, SNAP_VERTICAL, NEXT_VIEW, PREV_VIEW
}