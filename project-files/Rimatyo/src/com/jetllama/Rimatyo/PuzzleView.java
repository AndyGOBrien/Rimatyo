package com.jetllama.Rimatyo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by Jetrii on 6/4/13.
 */
public class PuzzleView extends PuzzleBase{

    private Shape[] shapeArray;

    public PuzzleView(Context context) {
        super(context);

        initPuzzle(context);
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPuzzle(context);
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        initPuzzle(context);
    }

    private void initPuzzle(Context context){

        shapeArray = new Shape[5];

        shapeArray[0] = new Shape(Color.RED, 0);
        shapeArray[1] = new Shape(Color.GREEN, 1);
        shapeArray[2] = new Shape(Color.CYAN, 2);
        shapeArray[3] = new Shape(Color.YELLOW, 3);

        shapeArray[4] = new Shape(Color.RED, 4);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        Paint p = new Paint();
        int shapeSize = (int)(width * .42);


        //Draw the 4 shapes
        shapeArray[0].drawSelf(canvas, p, new Rect(width / 20, height / 5, width / 20 + shapeSize, height/5 + shapeSize));
        shapeArray[1].drawSelf(canvas, p, new Rect((int)(width * .53), height / 5,(int)(width * .53) + shapeSize, height/5 + shapeSize));
        shapeArray[2].drawSelf(canvas, p, new Rect(width / 20, (int)(height * .55), width / 20 + shapeSize, (int)(height * .55)+ shapeSize));
        shapeArray[3].drawSelf(canvas, p, new Rect((int)(width * .53), (int)(height * .55), (int)(width * .53) + shapeSize, (int)(height * .55)+ shapeSize));

    }





    private class Shape{
        public int color;
        public int type;   // 0 = triangle, 1 = trapezoid, 2 = cross, 3 = triangle, 4 = kite

        public Shape(int c, int t){
            color = c;
            type = t;
        }

        //We are going to just do squares for now and implement shapes later
        public void drawSelf(Canvas c, Paint p, Rect location){
            p.setColor(color);
            c.drawRect(location, p);



        }  //End drawSelf
    }//End Shape class


}
