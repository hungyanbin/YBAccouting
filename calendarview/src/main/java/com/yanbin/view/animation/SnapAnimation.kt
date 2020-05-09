package com.yanbin.view.animation

import com.yanbin.view.CalendarViewPort

internal interface SnapAnimation {
    fun start(viewPort: CalendarViewPort): AnimationPlayer
}

