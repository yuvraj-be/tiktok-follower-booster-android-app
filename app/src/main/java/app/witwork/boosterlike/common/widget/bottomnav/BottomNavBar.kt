package app.witwork.boosterlike.common.widget.bottomnav

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class BottomNavBar(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs), View.OnClickListener {
    companion object {
        const val TAB_EXPLORE = 0
        const val TAB_BY_STARS = 1
        const val TAB_BOOST = 2
        const val TAB_PROFILE = 3
    }

    private lateinit var tabExplore: BottomNavItem
    private lateinit var tabByStars: BottomNavItem
    private lateinit var tabBoost: BottomNavItem
    private lateinit var tabProfile: BottomNavItem

    var listener: OnTabChangedListener? = null

    var currentTabSelected = -1
        set(value) {
            val changed = field != value
            if (changed) {
                if (toggle(value)) {
                    field = value
                }
            } else {
                listener?.reSelected(field)
            }
        }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tabExplore = getChildAt(0) as BottomNavItem
        tabByStars = getChildAt(1) as BottomNavItem
        tabBoost = getChildAt(2) as BottomNavItem
        tabProfile = getChildAt(3) as BottomNavItem

        tabExplore.setOnClickListener(this)
        tabByStars.setOnClickListener(this)
        tabBoost.setOnClickListener(this)
        tabProfile.setOnClickListener(this)

        this.currentTabSelected = TAB_EXPLORE

    }


    override fun onClick(p0: View?) {
        this.currentTabSelected = when (p0) {
            tabExplore -> {
                TAB_EXPLORE
            }
            tabByStars -> {
                TAB_BY_STARS
            }
            tabBoost -> {
                TAB_BOOST
            }
            else -> {
                TAB_PROFILE
            }
        }
    }

    private fun toggle(tabSelected: Int): Boolean {
        val change = listener?.changed(tabSelected)

        if (change == false) {
            return false
        }

        tabExplore.isSelected = tabSelected == TAB_EXPLORE
        tabByStars.isSelected = tabSelected == TAB_BY_STARS
        tabBoost.isSelected = tabSelected == TAB_BOOST
        tabProfile.isSelected = tabSelected == TAB_PROFILE
        return true
    }

    interface OnTabChangedListener {
        fun changed(tabIndex: Int): Boolean
        fun reSelected(tabIndex: Int)
    }

}