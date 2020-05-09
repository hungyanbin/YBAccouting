package com.yanbin.view.animation

import android.animation.Animator

interface AnimationPlayer {
    fun cancel()
}

class AndroidAnimationPlayer(private val animator: Animator): AnimationPlayer {
    override fun cancel() {
        animator.cancel()
    }
}