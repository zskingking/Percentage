package com.example.administrator.percentage.custom;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.administrator.percentage.R;


/**
 * 电池电量自定义View
 */

public class PercentageView extends View {

    private int mBackLineColor;//底图线段颜色
    private int mLineColor;//线段颜色
    private int mTextColor;//文字颜色

    private float mLineWidth;//线段宽度
    private float mLineHeight;//线段宽度
    private float mTextSize;//文字大小

    private Paint mLinePaint;//线段画笔
    private Paint mTextPaint;//文子画笔

    private int mWidth,mHeight;//宽高
    private int mScreenWidth;//一个屏幕的宽度

    private int mDumpElectric;//当前电量
    private int mElectric;//电量,属性动画动态改变

    private int mScaleToElectric;//当前电量对应的刻度
    private int scale;//配合属性动画使用


    public PercentageView(Context context) {
        super(context);
    }

    public PercentageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PercentageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array =  context.obtainStyledAttributes(attrs,R.styleable.Percentage);
        mBackLineColor = array.getColor(R.styleable.Percentage_backLineColor, Color.GRAY);
        mLineColor = array.getColor(R.styleable.Percentage_lineColor, Color.GREEN);
        mTextColor = array.getColor(R.styleable.Percentage_textColor, Color.BLACK);
        mLineWidth =  array.getDimension(R.styleable.Percentage_lineWidth,7);
        mLineHeight = array.getDimension(R.styleable.Percentage_lineHeight,40);
        mTextSize = array.getDimension(R.styleable.Percentage_textSize,80);

        mLinePaint = new Paint();//线段画笔
        mLinePaint.setAntiAlias(true);//去除锯齿
        mLinePaint.setStrokeCap(Paint.Cap.ROUND);//两头加圆角

        mLinePaint.setStrokeWidth(mLineWidth);//线段宽度

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);//去除锯齿
        mTextPaint.setColor(mTextColor);//文字颜色
        mTextPaint.setTextSize(mTextSize);//文字尺寸

        mScreenWidth = getPhoneW(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //mScreenWidth为最大宽高
        mWidth = resolveSize(mScreenWidth,widthMeasureSpec);
        mHeight = resolveSize(mScreenWidth,heightMeasureSpec);
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight =h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawBackLine(canvas);
        canvas.restore();
        canvas.save();
        drawLine(canvas);
        canvas.restore();
        drawText(canvas);
    }

    //绘制底图线段
    private void drawBackLine(Canvas canvas){
        mLinePaint.setColor(mBackLineColor);
        canvas.translate(getWidth()/2,getHeight()/2);
        for (int i = 0; i < 120; i++) {
            //这里刻度线长度我设置为25
            canvas.drawLine(0,-mHeight/2+10,0,-mHeight/2 + mLineHeight+10, mLinePaint);
            canvas.rotate(3);
        }
    }

    //绘制百分比线段
    private void drawLine(Canvas canvas){
        mLinePaint.setColor(mLineColor);
        canvas.translate(getWidth()/2,getHeight()/2);
        for (int i = 0; i < scale; i++) {
            //这里刻度线长度我设置为25
            canvas.drawLine(0,-mHeight/2+10,0,-mHeight/2 + mLineHeight+10, mLinePaint);
            canvas.rotate(3);
        }
    }

    //绘制文字
    private void drawText(Canvas canvas){
        Rect rect = new Rect();
        String content;
        if(scale == mScaleToElectric){
            //最后一次进行数值矫正，避免四舍五入后产生小偏差
            content = mDumpElectric + "%";
        }else {
            content = mElectric + "%";
        }
        mTextPaint.getTextBounds(content,0,content.length(),rect);
        canvas.drawText(content,mWidth/2 - rect.width()/2,mHeight/2 + rect.height()/2,mTextPaint);
    }


    public int getScale() {
        return scale;
    }

    //属性动画调用方法
    @SuppressLint("ObjectAnimatorBinding")
    public void setScale(int scale) {
        this.scale = scale;
        mElectric = (int) (scale/1.2);
        postInvalidate();
    }

    //设置剩余电量百分比
    public void setCurrentElectric(int electric){
        this.mDumpElectric = electric;
        mScaleToElectric = (int) (mDumpElectric*1.2);
        animator();
    }

    //动画
    private void animator(){
        ObjectAnimator animator = ObjectAnimator.ofInt(this,"scale",0,mScaleToElectric);
        animator.setDuration(1000)
                .start();
    }


    //初始化时一个归零的动画
    public void create(){
        mScaleToElectric = 120;
        ObjectAnimator animator = ObjectAnimator.ofInt(this,"scale",mScaleToElectric,0);
        animator.setDuration(1000)
                .start();
    }

    /**
     * 获取手机分辨率--宽
     * */
    public static int getPhoneW(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int disW = dm.widthPixels;
        return disW;
    }
}
