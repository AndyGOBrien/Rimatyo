package com.jetllama.Rimatyo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Jetrii on 6/4/13.
 */
public class PuzzleView extends PuzzleBase{

    private Shape[] shapeArray;

    public PuzzleView(Context context) {
        super(context);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        int width = getWidth();
        int height = getHeight();
        Paint p = new Paint();
        p.setColor(Color.RED);


        canvas.drawRect(new Rect(width/4,height / 4, width/4 + width/5 ,height/4 + height/5 ), p);
       // canvas.drawRect(new Rect(width/4 + width /4 ,height / 4, width/4 + width/5 ,height/4 + height/5 ), p);

       // canvas.drawRect(new Rect(width/4,height / 4, width/4 + width/5 ,height/4 + height/5 ), p);
       // canvas.drawRect(new Rect(width/4,height / 4, width/4 + width/5 ,height/4 + height/5 ), p);


        //canvas.drawRect(new Rect(width/2,200, 500,500), p);
        Log.d(MainActivity.TAG, "WIDTH: " + width / 2);
    }





    private class Shape{
        public int color;
        public int type;   // 0 = triangle, 1 = trapezoid, 2 = cross, 3 = triangle, 4 = kite

        public Shape(int c, int t){
            color = c;
            type = t;
        }

        public void drawSelf(Canvas c){


        }
    }
}
