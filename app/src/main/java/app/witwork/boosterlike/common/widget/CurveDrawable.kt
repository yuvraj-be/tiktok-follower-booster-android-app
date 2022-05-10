package app.witwork.boosterlike.common.widget

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.IntRange

class CurveDrawable(private val marginTop: Int = 0, @ColorInt private val color: Int) : Drawable() {
    private val paint: Paint by lazy {
        return@lazy Paint()
            .apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                color = this@CurveDrawable.color
            }
    }

    private val path = Path()

    override fun draw(canvas: Canvas) {
        path.reset()
        val bounds = bounds
        val horizontalOffset = bounds.width() * .8f
        val top = -bounds.height() * .8f
        val bottom = marginTop * 1f

        val ovalRect = RectF(-horizontalOffset, top, bounds.width() + horizontalOffset, bottom)
        path.lineTo(ovalRect.left, top)
        path.arcTo(ovalRect, 0f, 180f, false)
        path.fillType = Path.FillType.INVERSE_EVEN_ODD
        canvas.drawPath(path, paint);
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) {
        paint.setAlpha(alpha)
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.setColorFilter(colorFilter)
    }
}