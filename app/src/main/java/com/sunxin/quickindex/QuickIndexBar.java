package com.sunxin.quickindex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sunxin on 2016/10/3.
 */
public class QuickIndexBar extends View {

    private static final String TAG = "QuickIndexBar";
    private String[] indexArr = {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z"};

    private Paint paint;
    private int mWidth;
    private int mHeight;

    private float cellHeight;//格子高度

    public QuickIndexBar(Context context) {
        super(context);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//设置抗锯齿
        paint.setColor(Color.WHITE);
        paint.setTextSize(20);
        paint.setTextAlign(Paint.Align.CENTER);

    }

    /**
     * 一般用来获取控件的宽高
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        cellHeight = getMeasuredHeight() * 1.0f / indexArr.length;//格子的高度
    }

    private int lastIndex = -1; //记录上一次的索引

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //获取当前的字母的索引
                float y = event.getY();
                //得到索引
                int index = (int) (y / cellHeight);

                if (index != lastIndex) {

//                    Log.e(TAG, "onTouchEvent: 当前字母是:" + indexArr[index]);
                    //安全性的检查
                    if (index >= 0 && index <= indexArr.length - 1) {
                        if (mOnTouchLetterListener != null) {
                            mOnTouchLetterListener.onTouchLetter(indexArr[index]);
                        }
                    }


                }
                lastIndex = index;
                break;
            case MotionEvent.ACTION_UP:
                //重置索引
                lastIndex = -1;
                break;

        }
        //重绘，会马上去执行onDraw方法。改变点击触摸到的字母的颜色
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //绘制文本的原点是在文字文本框的左下角
        for (int i = 0; i < indexArr.length; i++) {

            float x = mWidth / 2;
            //y坐标就是  格子高度/2+文本高度/2+index*格子的高度

            //获取文本的高度
            int height = getTextHeight(indexArr[i]);

            float y = cellHeight / 2 + height / 2 + i * cellHeight;

            //改变触摸到的字母的颜色
            paint.setColor(lastIndex == i ? Color.BLACK : Color.WHITE);


            canvas.drawText(indexArr[i], x, y, paint);

        }

    }

    /**
     * 获得文本的高度
     *
     * @param text
     */
    private int getTextHeight(String text) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }


    private OnTouchLetterListener mOnTouchLetterListener;

    public void setOnTouchLetterListener(OnTouchLetterListener mOnTouchLetterListener) {
        this.mOnTouchLetterListener = mOnTouchLetterListener;
    }

    /**
     * 当触摸字母的时候的回调
     */
    public interface OnTouchLetterListener {
        void onTouchLetter(String letter);
    }

}
