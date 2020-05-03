package com.yanbin.view

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
    private var state = ViewPortState.IDLE

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
        if (state == ViewPortState.SNAP) {
            return
        }

        if (state != ViewPortState.SCROLLING) {
            updateViewPortState(ViewPortState.SCROLLING)
        }

        xOffset += distance
    }


    fun resizeVertically(distance: Float) {
        if (state != ViewPortState.SCROLLING) {
            updateViewPortState(ViewPortState.SCROLLING)
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
        if (state == ViewPortState.SCROLLING) {
            updateViewPortState(ViewPortState.SNAP)
        } else if (state != ViewPortState.SNAP) {
            return
        }

        xOffset = newOffset
    }

    fun calculateSnapOffset(): Float {
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
    IDLE, SCROLLING, SNAP, NEXT_VIEW, PREV_VIEW
}