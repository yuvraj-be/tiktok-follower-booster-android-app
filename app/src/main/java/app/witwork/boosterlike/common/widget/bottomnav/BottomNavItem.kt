package app.witwork.boosterlike.common.widget.bottomnav

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import app.witwork.boosterlike.R
import kotlinx.android.synthetic.main.bottom_nav_item.view.*

class BottomNavItem(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private val itemView: View = View.inflate(context, R.layout.bottom_nav_item, this)

    init {
        gravity = Gravity.CENTER
        orientation = VERTICAL
        initAttrs(context, attrs)
    }

    private fun initAttrs(context: Context?, attrs: AttributeSet?) {
        context?.obtainStyledAttributes(attrs, R.styleable.BottomNavItem)
            ?.apply {
                val icon = this.getDrawable(R.styleable.BottomNavItem_nav_icon)
                    ?: ContextCompat.getDrawable(context, R.drawable.ic_nav_explore)

                val title = this.getString(R.styleable.BottomNavItem_nav_title)
                    ?: context.getString(R.string.nav_tab_1)

                addIcon(icon)
                addTitle(title)
            }
            ?.recycle()
    }

    private fun addIcon(icon: Drawable?) {
        itemView.imgIcon.setImageDrawable(icon)
    }

    private fun addTitle(title: String) {
        itemView.tvTitle.text = title
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        val colorRes = if (selected) R.color.colorNavSelected else R.color.colorNavUnSelected
        val color = ContextCompat.getColor(context, colorRes)
        itemView.imgIcon.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        itemView.tvTitle.setTextColor(color)
        itemView.dot.visibility = if (selected) View.VISIBLE else View.GONE
    }
}