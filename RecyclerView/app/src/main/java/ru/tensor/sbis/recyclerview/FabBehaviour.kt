package ru.tensor.sbis.recyclerview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val STATE_SCROLLED_DOWN = 1
private const val STATE_SCROLLED_UP = 2

internal class FabBehaviour(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context, attrs) {

    private var currentState: Int? = null
    private var animator: ViewPropertyAnimator? = null

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        super.onNestedScroll(
            coordinatorLayout,
            child,
            target,
            dxConsumed,
            dyConsumed,
            dxUnconsumed,
            dyUnconsumed,
            type
        )
        if (currentState != STATE_SCROLLED_DOWN && dyConsumed > 0) {
            animator?.cancel()
            currentState = STATE_SCROLLED_DOWN
            val layoutParams = child.layoutParams as CoordinatorLayout.LayoutParams
            animator =
                child
                    .animate()
                    .translationY((child.height + layoutParams.bottomMargin).toFloat())
                    .setDuration(300L)
                    .setInterpolator(LinearInterpolator())
                    .apply {
                        start()
                    }
        } else if (currentState != STATE_SCROLLED_UP && dyConsumed < 0) {
            animator?.cancel()
            currentState = STATE_SCROLLED_UP
            animator =
                child
                    .animate()
                    .translationY(0F)
                    .setDuration(300L)
                    .setInterpolator(LinearInterpolator())
                    .apply {
                        start()
                    }
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: FloatingActionButton,
        directTargetChild: View,
        target: View,
        axes: Int,
        type: Int
    ): Boolean = axes == ViewCompat.SCROLL_AXIS_VERTICAL
}