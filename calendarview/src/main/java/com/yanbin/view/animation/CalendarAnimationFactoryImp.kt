package com.yanbin.view.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import com.yanbin.view.CalendarViewPort
import com.yanbin.view.ViewRequest

internal class CalendarAnimationFactoryImp : CalendarAnimationFactory {
    override fun empty(): SnapAnimation {
        return EmptySnapAnimation()
    }

    override fun horizontal(startOffset: Float, endOffset: Float): SnapAnimation {
        return HorizontalSnapAnimation(startOffset, endOffset)
    }

    override fun vertical(startOffset: Float, endOffset: Float, startHeight: Float, endHeight: Float): SnapAnimation {
        return VerticalSnapAnimation(startOffset, endOffset, startHeight, endHeight)
    }
}

internal class EmptySnapAnimation: SnapAnimation {
    override fun start(viewPort: CalendarViewPort): AnimationPlayer {
        return AndroidAnimationPlayer(ValueAnimator.ofFloat(0f))
    }
}

internal class HorizontalSnapAnimation(private val startOffset: Float,
                                       private val endOffset: Float): SnapAnimation {

    override fun start(viewPort: CalendarViewPort): AnimationPlayer {
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
        return AndroidAnimationPlayer(animator)
    }
}

internal class VerticalSnapAnimation(private val startOffset: Float,
                                     private val endOffset: Float,
                                     private val startHeight: Float,
                                     private val endHeight: Float): SnapAnimation {

    private var offset = Float.NaN
    private var height = Float.NaN

    override fun start(viewPort: CalendarViewPort): AnimationPlayer {
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
        return AndroidAnimationPlayer(animatorSet)
    }

    private fun tryUpdateViewPort(viewPort: CalendarViewPort) {
        if (offset.isNaN() || height.isNaN()) {
            return
        }
        viewPort.snapVertically(offset, height)
        viewPort.sendViewRequest(ViewRequest.LAYOUT)
    }
}
