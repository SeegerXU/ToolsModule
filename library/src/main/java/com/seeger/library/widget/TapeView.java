package com.seeger.library.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;

import com.seeger.library.utils.MathUtils;

import java.math.BigDecimal;

/**
 * 薄荷健康的滑动卷尺效果
 *
 * @author seeger
 */
public class TapeView extends View {

    /**
     * label画笔
     */
    private Paint labelPaint;
    private Paint scaleTextPaint;
    private Paint baseLinePaint;
    /**
     * 宽
     */
    private int mWidth;
    /**
     * 高
     */
    private int mHeight;

    private int rulerHeight;
    private Paint bgPaint;
    private Rect bgRect;

    private Paint scalePaint;

    private double maxScale = 100;
    private double minScale = 0;
    private double currentScale = 55.5;
    private int scaleSpacing = 20;
    private int scaleHeight = 50;
    private int scaleHeightL = 80;
    private int currentX;
    private boolean isTouch = false;

    private int moveX;
    private int deviationX;
    private double originalScale;

    public TapeView(Context context) {
        this(context, null);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TapeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        labelPaint = new Paint();
        labelPaint.setColor(Color.BLUE);
        labelPaint.setTextSize(60);
        labelPaint.setTextAlign(Paint.Align.CENTER);

        baseLinePaint = new Paint();
        baseLinePaint.setColor(Color.BLUE);
        baseLinePaint.setStrokeWidth(5);

        scaleTextPaint = new Paint();
        scaleTextPaint.setColor(Color.GRAY);
        scaleTextPaint.setTextSize(40);
        scaleTextPaint.setTextAlign(Paint.Align.CENTER);

        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#EEEEEE"));
        scalePaint = new Paint();
        scalePaint.setColor(Color.GRAY);
        scalePaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = labelPaint.getFontMetrics();
        //为基线到字体上边框的距离,即上图中的top
        float top = fontMetrics.top;
        //为基线到字体下边框的距离,即上图中的bottom
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (mHeight / 6 - top / 2 - bottom / 2);
        if (isTouch) {
            int step = moveX / scaleSpacing;
            currentScale = originalScale - MathUtils.mul(step, 0.1);
            currentScale = MathUtils.round(currentScale, 1, BigDecimal.ROUND_HALF_UP);
            deviationX = moveX - step * scaleSpacing;
        } else {
            moveX = 0;
            deviationX = 0;
        }
        canvas.drawText(String.valueOf(currentScale), mWidth / 2, baseLineY, labelPaint);
        canvas.drawRect(bgRect, bgPaint);

        for (int i = 0; i <= mWidth / scaleSpacing / 2 + 1; i++) {
            double currentL = currentScale - 0.1 * i;

            int startX = mWidth / 2 + deviationX - i * scaleSpacing;
            int startY = rulerHeight;
            if (currentL == (int) currentL) {
                Paint.FontMetrics font = labelPaint.getFontMetrics();
                //为基线到字体上边框的距离,即上图中的top
                int baseLineL = (int) (rulerHeight + scaleHeightL + 20 - font.top / 2 - font.bottom / 2);
                canvas.drawText(String.valueOf(currentL), startX, baseLineL, scaleTextPaint);
                canvas.drawLine(startX, startY, startX, startY + scaleHeightL, scalePaint);
            } else {
                canvas.drawLine(startX, startY, startX, startY + scaleHeight, scalePaint);
            }

            double currentR = currentScale + 0.1 * i;
            int startX1 = mWidth / 2 + deviationX + i * scaleSpacing;
            if (currentR == (int) currentR) {
                Paint.FontMetrics font = labelPaint.getFontMetrics();
                //为基线到字体上边框的距离,即上图中的top
                int baseLineL = (int) (rulerHeight + scaleHeightL + 20 - font.top / 2 - font.bottom / 2);
                canvas.drawText(String.valueOf(currentR), startX1, baseLineL, scaleTextPaint);
                canvas.drawLine(startX1, startY, startX1, startY + scaleHeightL, scalePaint);
            } else {
                canvas.drawLine(startX1, startY, startX1, startY + scaleHeight, scalePaint);
            }
        }
        canvas.drawLine(mWidth / 2, rulerHeight, mWidth / 2, rulerHeight + scaleHeightL, baseLinePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        rulerHeight = getMeasuredHeight() / 3;
        bgRect = new Rect(0, rulerHeight, mWidth, mHeight);
    }

    /**
     * 获取文字精准宽度
     *
     * @param paint
     * @param str
     * @return
     */
    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }

    private int downX;
    private float XVelocity;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                downX = (int) event.getX();
                originalScale = currentScale;
                break;
            case MotionEvent.ACTION_MOVE:
                moveX = (int) event.getX() - downX;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouch = false;
                setCurrentScale(currentScale);
                break;
            case MotionEvent.ACTION_CANCEL:
                isTouch = false;
                setCurrentScale(currentScale);
                break;
        }
        return true;
    }

    public void setCurrentScale(double currentScale) {
        this.currentScale = currentScale;
        if (!isTouch) {
            invalidate();
        }
    }
}
