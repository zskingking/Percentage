package com.example.administrator.percentage.custom;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.percentage.R;



public class ElectricColumnView extends View{

    private Paint mFramePaint;//边框画笔
    private Paint mFramePaintTop;//边框顶部画笔
    private Paint mElectricPaint;//电量画笔

    private int mLowColor;//低电量颜色颜色
    private int mNormalColor;//正常电量颜色
    private int mMargin;//外边距
    private int mPadding;//内边距
    private int mFrameWidth;//外边框宽度
    private int mRadius;//圆角度
    private int mLineHeight;//正极线段高度

    private int mLineWidth;//正极线段宽度
    private int mWidth,mHeight;//宽高
    private int mTotalElectricHeight;//电池总高度
    private int mCurrentElectricHeight;//当前电池高度
    private float per;
    public ElectricColumnView(Context context) {
        super(context);
    }

    public ElectricColumnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ElectricColumnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array =  context.obtainStyledAttributes(attrs,R.styleable.PerElectric);
        mLowColor = array.getColor(R.styleable.PerElectric_lowColor, Color.RED);
        mNormalColor = array.getColor(R.styleable.PerElectric_normalColor, Color.GREEN);
        mMargin = (int) array.getDimension(R.styleable.PerElectric_margin,15);
        mPadding = (int) array.getDimension(R.styleable.PerElectric_padding,15);
        mFrameWidth = (int) array.getDimension(R.styleable.PerElectric_frameWidth,10);
        mRadius = (int) array.getDimension(R.styleable.PerElectric_radius,20);
        mLineHeight = (int) array.getDimension(R.styleable.PerElectric_perLineHeight,30);
        init();
    }

    private void init(){
        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setColor(mNormalColor);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(mFrameWidth);

        mFramePaintTop = new Paint();//线段画笔
        mFramePaintTop.setAntiAlias(true);//去除锯齿
        mFramePaintTop.setColor(mNormalColor);
        mFramePaintTop.setStyle(Paint.Style.FILL);


        mElectricPaint = new Paint();
        mElectricPaint.setAntiAlias(true);
        mElectricPaint.setColor(mNormalColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //600,1200为最大宽高
        mWidth = resolveSize(600,widthMeasureSpec);
        mHeight = resolveSize(1200,heightMeasureSpec);
        mLineWidth = mWidth/3;
        mTotalElectricHeight = mHeight - (mLineHeight + mMargin*2+mPadding*2);
        mCurrentElectricHeight = mTotalElectricHeight/3;
        setMeasuredDimension(mWidth,mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFrame(canvas);
        drawElectric(canvas);
    }

    //绘制边框
    private void drawFrame(Canvas canvas){
        //绘制正极线
        RectF r = new RectF(mWidth/2-mLineWidth/2,mMargin,mWidth/2+mLineWidth/2,mMargin+mLineHeight);
        canvas.drawRoundRect(r,mRadius,mRadius,mFramePaintTop);
        //canvas.drawLine(mWidth/2-mLineWidth/2,mMargin,mWidth/2+mLineWidth/2,mMargin,mFramePaintTop);
        RectF rect = new RectF(mMargin,mMargin + mLineHeight,mWidth-mMargin,mHeight-mMargin);
        //绘制边框
        canvas.drawRoundRect(rect,mRadius,mRadius,mFramePaint);

    }

    //绘制内部电量
    private void drawElectric(Canvas canvas){
        Rect rect = new Rect(mMargin+ mPadding,mHeight -(mMargin+ mPadding) - mCurrentElectricHeight,
                mWidth-(mMargin+ mPadding),mHeight-(mMargin+ mPadding));
        canvas.drawRect(rect,mElectricPaint);
    }

    //设置电量
    public void setInit(float per){
        if(per>0.2){
            mFramePaint.setColor(mNormalColor);
            mFramePaintTop.setColor(mNormalColor);
            mElectricPaint.setColor(mNormalColor);
        }else {
            mFramePaint.setColor(mLowColor);
            mFramePaintTop.setColor(mLowColor);
            mElectricPaint.setColor(mLowColor);
        }
        animator(per);
    }

    //属性动画
    @SuppressLint("ObjectAnimatorBinding")
    public void setPer(float per){
        mCurrentElectricHeight = (int) (per*mTotalElectricHeight);
        postInvalidate();
    }

    //初始化时一个归零的动画
    public void create(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(this,"per",1,0);
        animator.setDuration(750)
                .start();
    }

    //动画
    private void animator(float per){
        ObjectAnimator animator = ObjectAnimator.ofFloat(this,"per",0,per);
        animator.setDuration(750)
                .start();
    }
}
