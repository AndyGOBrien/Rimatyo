package com.jetllama.Rimatyo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Jetrii on 6/5/13.
 */

public class PuzzleBase extends View {


    protected boolean localView;
    protected int CURRENT_STATE = 0;
    Paint borderPaint;


    public PuzzleBase(Context context) {
        super(context);
        Log.e(MainActivity.TAG, "PuzzleBase initialized without attrs");
    }

    public PuzzleBase(Context context, AttributeSet attrs) {

        super( context, attrs );
        initialize(context, attrs);
    }


    public PuzzleBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context, attrs);
    }


    protected void initialize(Context context, AttributeSet attrs) {

        //Extract info passed through XML layout
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PuzzleBase);
        localView = Boolean.parseBoolean(ta.getString(R.styleable.PuzzleBase_localView));

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(
                parentWidth, (int) (parentWidth * 1.77));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, 0, getWidth()-1, getHeight()-1, borderPaint);
    }
}