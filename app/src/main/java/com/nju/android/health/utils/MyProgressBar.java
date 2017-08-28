package com.nju.android.health.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.nju.android.health.R;

/**
 * Created by chy on 2017/3/17.
 */

public class MyProgressBar extends ProgressBar {
    String text;
    Paint mPaint;

    public MyProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        System.out.println("1");
        initText();
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        System.out.println("2");
        initText();
    }


    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        System.out.println("3");
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
        // TODO Auto-generated method stub
        initText();
        if (this.getProgress() > 80) {
            this.mPaint.setColor(getResources().getColor(R.color.aliwx_white));

        } else {
            this.mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        }
        setText(progress);
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        //this.setText();
        Rect rect = new Rect();
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
//        int x = (getWidth() / 2) - rect.centerX();
//        int y = (getHeight() / 2) - rect.centerY();
        float x = 0, y = 0;
        if (this.getProgress() > 80) {
            this.mPaint.setColor(getResources().getColor(R.color.aliwx_white));
            x = (getWidth() / 2) - rect.centerX();
            y = (getHeight() / 2) - rect.centerY();
        } else {
            x = this.getMeasuredWidth() * this.getProgress() / 100 + mPaint.measureText(text) / 2 - 10;
            y = this.getMeasuredHeight() / 2f - mPaint.getFontMetrics().ascent / 2f - mPaint.getFontMetrics().descent / 2f;
        }

        canvas.drawText(this.text, x, y, this.mPaint);
    }

    //初始化，画笔
    private void initText(){
        this.mPaint = new Paint();
        this.mPaint.setColor(getResources().getColor(R.color.colorPrimary));
        this.mPaint.setTextSize(30f);

    }

    private void setText(){
        setText(this.getProgress());
    }

    //设置文字内容
    private void setText(int progress){
        int i = (progress * 100)/this.getMax();
        this.text = String.valueOf(i) + "%";
    }


}
