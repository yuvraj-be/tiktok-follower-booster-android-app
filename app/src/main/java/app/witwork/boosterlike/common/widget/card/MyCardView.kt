package app.witwork.boosterlike.common.widget.card

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import app.witwork.boosterlike.R
import app.witwork.boosterlike.common.extension.dp2Px
import app.witwork.boosterlike.common.extension.loadImage
import app.witwork.boosterlike.domain.model.Feed
import kotlinx.android.synthetic.main.my_card_view.view.*

class MyCardView(context: Context) : FrameLayout(context) {
    companion object {
        private const val CARD_ROTATION_DEGREES = 40f
        private const val DURATION = 200L
        const val TOP_LEVEL = 0
        const val MIDDLE_LEVEL = 1
        const val BOTTOM_LEVEL = 2
    }

    interface OnDismissListener {
        fun onDismiss(view: View)
    }

    private var oldX = 0f
    private var oldY = 0f
    private var newX = 0f
    private var newY = 0f
    private var dX = 0f
    private var dY = 0f
    private var defaultX = 0f
    private var defaultY = 0f
    private var rightBoundary = 0f
    private var leftBoundary = 0f
    private var screenWidth = 0
    private var padding = 0

    private val isCardBeyondLeftBoundary: Boolean
        get() {
            return x + width / 2 < leftBoundary
        }

    private val isCardBeyondRightBoundary: Boolean
        get() {
            return x + width / 2 > rightBoundary
        }

    var onDismissListener: OnDismissListener? = null

    var level = TOP_LEVEL
        set(value) {
            field = value
            when (field) {
                TOP_LEVEL -> {
                    if (feed == null) {
                        progress.visibility = View.VISIBLE
                    }
                    rootCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorTopBackground))
                }
                MIDDLE_LEVEL -> {
                    progress.visibility = View.GONE
                    rootCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorMiddleBackground))
                }
                else -> {
                    progress.visibility = View.GONE
                    rootCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorBottomBackground))
                }
            }
            updateUI()
        }

    var feed: Feed? = null
        set(value) {
            field = value
            field?.let {
                progress.visibility = View.GONE
                updateData()
            }
        }

    init {
        View.inflate(context, R.layout.my_card_view, this)
        visibility = View.INVISIBLE
        screenWidth = resources.displayMetrics.widthPixels
        leftBoundary = screenWidth / 6f
        rightBoundary = screenWidth * 5 / 6f
        padding = context.dp2Px(10f)
        level = TOP_LEVEL
        post {
            defaultX = x
            defaultY = y
        }
    }

    private fun updateData() {
        imgAvatar.loadImage(feed?.avatar)
        tvDescription.text = feed?.signature
        tvName.text = context.getString(R.string.display_name, feed?.name)
    }

    private fun updateUI() {
        post {
            val scaleX = (width - padding * level * 4f) / width
            val duration = if (level == BOTTOM_LEVEL) 0 else DURATION
            animate()
                .x(defaultX)
                .y(defaultY + padding * level)
                .scaleX(scaleX)
                .setDuration(duration)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {}

                    override fun onAnimationEnd(p0: Animator?) {
                        visibility = View.VISIBLE
                    }

                    override fun onAnimationCancel(p0: Animator?) {}

                    override fun onAnimationStart(p0: Animator?) {}

                })
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (level != TOP_LEVEL) {
            return false
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                oldX = event.x
                oldY = event.y

                clearAnimation()
                return true
            }

            MotionEvent.ACTION_UP -> {
                when {
                    isCardBeyondLeftBoundary -> {
                        dismissCard(screenWidth * -2f)
                    }
//                    isCardBeyondRightBoundary -> {
//                        dismissCard(screenWidth * 2f)
//                    }
                    else -> {
                        resetCard()
                    }
                }
            }

            MotionEvent.ACTION_MOVE -> {
                newX = event.x
                newY = event.y

                dX = newX - oldX
                dY = newY - oldY

                // Set new position
                x += dX
                y += dY
                setCardRotation(x)
            }
        }

        return super.onTouchEvent(event)
    }

    private fun dismissCard(xPos: Float) {
        animate()
            .x(xPos)
            .y(0f)
            .setInterpolator(AccelerateInterpolator())
            .setDuration(DURATION)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    onDismissListener?.onDismiss(this@MyCardView)
                }

                override fun onAnimationCancel(p0: Animator?) {}

                override fun onAnimationStart(p0: Animator?) {}

            })
    }

    private fun resetCard() {
        animate()
            .x(defaultX)
            .y(defaultY)
            .rotation(0f)
            .setInterpolator(OvershootInterpolator())
            .setDuration(DURATION)
    }

    private fun setCardRotation(posX: Float) {
        val rotation = (CARD_ROTATION_DEGREES * posX) / screenWidth
        val halfCardHeight = height / 2
        if (oldY < halfCardHeight - 2 * padding) {
            this.rotation = rotation * -1
        } else {
            this.rotation = rotation * 1
        }
    }

    fun forceDismiss() {
        dismissCard(screenWidth * -2f)
    }
}