package com.jetllama.Rimatyo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jetrii on 6/5/13.
 */

public class PuzzleBase extends View {

    protected int CURRENT_STATE = 0;


    public PuzzleBase(Context context) {
        super(context);
    }

    public PuzzleBase(Context context, AttributeSet attrs) {

        super( context, attrs );
    }


    public PuzzleBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(
                parentWidth, (int) (parentWidth * 1.77));
    }

}