package app.witwork.boosterlike.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class NonSwipeAbleViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    var swipeLocked = true

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (swipeLocked) return false
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (swipeLocked) return false
        return super.onInterceptTouchEvent(ev)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        if (swipeLocked) return false
        return super.canScrollHorizontally(direction)
    }
}