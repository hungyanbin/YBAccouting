package com.yanbin.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator

internal interface SnapAnimation {
    fun start(viewPort: CalendarViewPort): Animator
}

internal class EmptySnapAnimation: SnapAnimation {
    override fun start(viewPort: CalendarViewPort): Animator {
        return ValueAnimator.ofFloat(0f)
    }
}

internal class HorizontalSnapAnimation(private val startOffset: Float,
                                       private val endOffset: Float): SnapAnimation {

    override fun start(viewPort: CalendarViewPort): Animator {
        val animator = ValueAnimator.ofFloat(startOffset, endOffset)
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator?.animatedValue as Float
            viewPort.snapHorizontally(value)
            viewPort.sendViewRequest(ViewRequest.DRAW)
        }
        animator.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                viewPort.onSnapComplete()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })

        animator.start()
        return animator
    }
}

internal class VerticalSnapAnimation(private val startOffset: Float,
                                     private val endOffset: Float,
                                     private val startHeight: Float,
                                     private val endHeight: Float): SnapAnimation {

    private var offset = Float.NaN
    private var height = Float.NaN

    override fun start(viewPort: CalendarViewPort): Animator {
        val animatorSet = AnimatorSet()
        val offsetAnimator = ValueAnimator.ofFloat(startOffset, endOffset)
        offsetAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator?.animatedValue as Float
            offset = value
            tryUpdateViewPort(viewPort)
        }

        val heightAnimator = ValueAnimator.ofFloat(startHeight, endHeight)
        heightAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator?.animatedValue as Float
            height = value
            tryUpdateViewPort(viewPort)
        }

        animatorSet.playTogether(offsetAnimator, heightAnimator)
        animatorSet.duration = 300
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.addListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                viewPort.onSnapComplete()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationStart(animation: Animator?) {}
        })

        animatorSet.start()
        return animatorSet
    }

    private fun tryUpdateViewPort(viewPort: CalendarViewPort) {
        if (offset.isNaN() || height.isNaN()) {
            return
        }
        viewPort.snapVertically(offset, height)
        viewPort.sendViewRequest(ViewRequest.LAYOUT)
    }
}
