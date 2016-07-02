package com.hs.cv;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.hs.customview.R;

/**
 * 自定义菊花样式的Progress
 * UI线程定义handler定时刷新
 *
 * @author Holy-Spirit
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("DrawAllocation")
public class CustomProgressBar extends View
{

    /*
     * 画笔
     */
    private Paint mPaint = null;

    /*
     * 每个指针的颜色
     */
    private static final int[] colors = new int[]{R.color.color_top,
            R.color.color_right_top, R.color.color_right,
            R.color.color_right_bottom, R.color.color_bottom,
            R.color.color_left_bottom, R.color.color_left,
            R.color.color_left_top};

    /*
     * android res资源对象
     */
    private Resources res = null;

    /*
     * 颜色值轮转指针
     */
    private int i = 0;

    public CustomProgressBar(Context context)
    {
        super(context);

    }

    public CustomProgressBar(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        res = context.getResources();// 获取资源对象，用来获取color值
    }

    public void refreshView()
    {
        invalidate();// 触发onDraw（）方法，重绘Progress
    }

    /*
     * color循环变化
     */
    private int calculateIndex(int in)
    {
        return in % (colors.length - 1);
    }

    /*
     * 设置指针颜色
     */
    public void setLineColor(int index)
    {
        mPaint.setColor(res.getColor(colors[calculateIndex(index)]));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        final float roundWidth;
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();
        roundWidth = width > height ? height : width;
        final float halfRoundWidth = roundWidth / 2;// 获取画布的半长
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(roundWidth / 15);// 设置为画布的十五分之一
        float f1 = halfRoundWidth / 5;
        float f2 = (float) Math.sqrt(f1 * f1 / 2);// 中心点到指针的长度在x，y轴上的投影
        float f3 = (float) Math.sqrt(f1 * f1 * 9) / 2;//指针长在x，y轴上的投影
        float f5 = halfRoundWidth - f3 - f1;//画布四哥点到指针的长度在x，y轴上的投影

        // top
        this.setLineColor(0 + i);
        canvas.drawLine(halfRoundWidth, halfRoundWidth / 5, halfRoundWidth,
                halfRoundWidth / 5 * 4, mPaint);

        // right top
        this.setLineColor(1 + i);
        canvas.drawLine(roundWidth - f5, f5, halfRoundWidth + f2,
                halfRoundWidth - f2, mPaint);

        // right
        this.setLineColor(2 + i);
        canvas.drawLine(halfRoundWidth / 5 * 9, halfRoundWidth,
                halfRoundWidth / 5 * 6, halfRoundWidth, mPaint);

        // right bottom
        this.setLineColor(3 + i);
        canvas.drawLine(roundWidth - f5, roundWidth - f5, halfRoundWidth + f2,
                halfRoundWidth + f2, mPaint);

        // bottom
        this.setLineColor(4 + i);
        canvas.drawLine(halfRoundWidth, halfRoundWidth / 5 * 9, halfRoundWidth,
                halfRoundWidth / 5 * 6, mPaint);

        // left bottom
        this.setLineColor(5 + i);
        canvas.drawLine(f5, roundWidth - f5, halfRoundWidth - f2,
                halfRoundWidth + f2, mPaint);

        // left
        this.setLineColor(6 + i);
        canvas.drawLine(halfRoundWidth / 5, halfRoundWidth,
                halfRoundWidth / 5 * 4, halfRoundWidth, mPaint);
        // // left top
        this.setLineColor(7 + i);
        canvas.drawLine(f5, f5, halfRoundWidth - f2, halfRoundWidth - f2,
                mPaint);

        i++;
    }

}
