package com.seeger.library.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.seeger.library.bean.PieBean;
import com.seeger.library.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Pie Chart
 *
 * @author seeger
 */
public class PieChartView extends View {

    /**
     * view width
     */
    private int mWidth;
    /**
     * view height
     */
    private int mHeight;

    /**
     * 中心坐标
     */
    private Point center;

    /**
     * 偏移量
     */
    private int deviation = 0;

    /**
     * 饼状图半径
     */
    private int mRadius;

    /**
     * 数据集
     */
    private List<PieBean> data;

    private Paint linePaint;
    private Paint txtPaint;

    public PieChartView(Context context) {
        this(context, null);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(4);
        //取消锯齿
        linePaint.setAntiAlias(true);
        txtPaint = new Paint();
        txtPaint.setStyle(Paint.Style.FILL);
        txtPaint.setColor(Color.BLACK);
        txtPaint.setStrokeWidth(4);
        txtPaint.setTextAlign(Paint.Align.CENTER);
        //取消锯齿
        txtPaint.setAntiAlias(true);
        txtPaint.setTextSize(40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        center = new Point(mWidth / 2, mHeight / 2);
        mRadius = mWidth / 2;
        if (mRadius > mHeight / 2) {
            mRadius = mHeight / 2;
        }
        mRadius = mRadius / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (data == null) {
            return;
        }
        if (dataRectF == null) {
            dataRectF = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                dataRectF.add(getData(i));
            }
        }

        int position = 0;
        int percent = 0;
        for (int i = 0; i < data.size(); i++) {
            percent += data.get(i).getPercent();
            if (animationPercent < percent) {
                position = i;
                break;
            }
        }
        for (int i = 0; i < data.size(); i++) {
            int startAngle = 0;
            for (int j = 0; j < i; j++) {
                startAngle += 360 * data.get(j).getPercent() / 100;
            }
            int sweepAngle = 360 * data.get(i).getPercent() / 100;
            if (i == data.size() - 1) {
                sweepAngle = 360 - startAngle;
            }
            if (startAngle + sweepAngle <= 360 * animationPercent / 100) {
                drawPie(canvas, i, position, startAngle, sweepAngle);
            } else {
                sweepAngle = 360 * animationPercent / 100 - startAngle;
                drawPie(canvas, i, position, startAngle, sweepAngle);
                break;
            }
        }
        if (animationPercent > 100) {
            for (int i = 0; i < data.size(); i++) {
                int startAngle = 0;
                for (int j = 0; j < i; j++) {
                    startAngle += 360 * data.get(j).getPercent() / 100;
                }
                int sweepAngle = 360 * data.get(i).getPercent() / 100;
                if (i == data.size() - 1) {
                    sweepAngle = 360 - startAngle;
                }
                drawTitle(canvas, data.get(i), startAngle, sweepAngle);
            }
        }
    }

    private void drawPie(Canvas canvas, int ii, int position, int startAngle, int sweepAngle) {
        //这个是画矩形的画笔，方便大家理解这个圆弧
        Paint mPaint = dataPaint.get(ii);
        RectF oval = dataRectF.get(ii);
        canvas.drawArc(oval, startAngle, sweepAngle, true, mPaint);
    }

    /**
     * 获取圆弧矩形控件，暂未添加偏移量
     *
     * @param i
     * @return
     */
    private RectF getData(int i) {
        int startAngle = 0;
        for (int j = 0; j < i; j++) {
            startAngle += 360 * data.get(j).getPercent() / 100;
        }
        int sweepAngle = 360 * data.get(i).getPercent() / 100;
        int devX = (int) (deviation * Math.sin(startAngle + sweepAngle / 2));
        int devY = (int) (deviation * Math.cos(startAngle + sweepAngle / 2));
        if (startAngle + sweepAngle / 2 < 90) {
            devX = Math.abs(devX);
            devY = Math.abs(devY);
        } else if (startAngle + sweepAngle / 2 < 180) {
            devX = -Math.abs(devX);
            devY = Math.abs(devY);
        } else if (startAngle + sweepAngle / 2 < 270) {
            devX = -Math.abs(devX);
            devY = -Math.abs(devY);
        } else {
            devX = Math.abs(devX);
            devY = -Math.abs(devY);
        }
        RectF oval = new RectF(center.x - mRadius + devX, center.y - mRadius + devY,
                center.x + mRadius + devX, center.y + mRadius + devY);
        return oval;
    }

    /**
     * 展示连接线和文字
     *
     * @param canvas
     * @param item
     * @param startAngle
     * @param sweepAngle
     */
    private void drawTitle(Canvas canvas, PieBean item, int startAngle, int sweepAngle) {
        Paint.FontMetrics fontMetrics = txtPaint.getFontMetrics();
        //为基线到字体上边框的距离,即上图中的top
        float top = fontMetrics.top;
        //为基线到字体下边框的距离,即上图中的bottom
        float bottom = fontMetrics.bottom;
        //计算文字宽度（20 为预留间隔）
        int width = getTextWidth(txtPaint, item.getTitle()) + 20;
        //当前饼块中间角度,用来处理线条方向和文字展示位置
        int angle = startAngle + sweepAngle / 2;
        int lines = 0;
        int dev = 20;
        //线条渐变动画 从101到120为线条动画
        if (animationPercent - 100 <= 4) {
            dev = 20 * (animationPercent - 100) / 4;
        } else {
            lines = 2 * (animationPercent - 104);
        }
        int startX = (int) ((deviation + mRadius) * Math.cos(angle * Math.PI / 180));
        int startY = (int) ((deviation + mRadius) * Math.sin(angle * Math.PI / 180));

        int endX = (int) ((deviation + mRadius + dev) * Math.cos(angle * Math.PI / 180));
        int endY = (int) ((deviation + mRadius + dev) * Math.sin(angle * Math.PI / 180));
        Rect rect;
        int lineX = 0;
        startX = center.x + startX;
        startY = center.y + startY;
        endX = center.x + endX;
        endY = center.y + endY;
        if (angle < 90) {
            //右下角，文字展示方向为右侧
            lineX = endX + lines;
            rect = new Rect(lineX, endY - 25, lineX + width, endY + 25);
        } else if (angle < 180) {
            //左下角，文字展示方向为左侧
            lineX = endX - lines;
            rect = new Rect(lineX - width, endY - 25, lineX, endY + 25);
        } else if (angle < 270) {
            //左上角，文字展示方向为左侧
            lineX = endX - lines;
            rect = new Rect(lineX - width, endY - 25, lineX, endY + 25);
        } else {
            //右上角，文字展示方向为右侧
            lineX = endX + lines;
            rect = new Rect(lineX, endY - 25, lineX + width, endY + 25);
        }
        //100 到 104 画连延伸线
        canvas.drawLine(startX, startY, endX, endY, linePaint);
        //104 到 120 画文字连接线
        if (animationPercent > 104) {
            canvas.drawLine(endX, endY, lineX, endY, linePaint);
        }
        //最终画文字在上面
        if (animationPercent == 120) {
            //基线中间点的y轴计算公式
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
            canvas.drawText(item.getTitle(), rect.centerX(), baseLineY, txtPaint);
        }
    }

    private List<Paint> dataPaint;
    private List<RectF> dataRectF;

    /**
     * 初始化数据
     *
     * @param data
     */
    public void setData(List<PieBean> data) {
        this.data = data;
        dataPaint = new ArrayList<>();
        dataRectF = null;
        for (int i = 0; i < data.size(); i++) {
            //这个是画矩形的画笔，方便大家理解这个圆弧
            Paint mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(ColorUtils.getRandomColor());
            //取消锯齿
            mPaint.setAntiAlias(true);
            dataPaint.add(mPaint);
        }
        startAnimation();
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

    private int animationPercent;

    /**
     * 开始动画操作
     */
    private void startAnimation() {
        animationPercent = 0;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 120);
        valueAnimator.setDuration(2000).start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float obj = ((Float) valueAnimator.getAnimatedValue());
                if ((int) obj > animationPercent) {
                    animationPercent = (int) obj;
                    invalidate();
                }
            }
        });
    }
}
