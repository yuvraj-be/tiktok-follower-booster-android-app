package app.witwork.boosterlike.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import app.witwork.boosterlike.R;

public class MyViewWithShadow extends View {

    Paint paint;
    int mainColor;
    int shadowColor;

    // shadow properties
    int offsetX = -25;
    int offsetY = 60;
    int blurRadius = 60;

    public MyViewWithShadow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mainColor = Color.RED;
        shadowColor = ContextCompat.getColor(context, R.color.colorPositiveBackground2);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // Create paint for shadow
        paint.setColor(shadowColor);
        paint.setMaskFilter(new BlurMaskFilter(
                blurRadius /* shadowRadius */,
                BlurMaskFilter.Blur.NORMAL));

        // Draw shadow before drawing object
        canvas.drawRect(0, 20, getWidth(), getHeight() + offsetY, paint);
    }
}