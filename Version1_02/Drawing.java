package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class Drawing extends View {

    private Paint mPaint;
    public boolean PaintingB = false;
    private int zoom;
    private ArrayList<int[]> pointlist;

    public Drawing(Context context){
        super(context);
        Paint_initate();
    }

    private void Paint_initate(){

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.FILL);
        pointlist =  new ArrayList<int[]>();
        Log.d("USER LOG","PAINT : IMITATING IS FINISHED");
    }

    public void ScreenClear(){
        pointlist =  new ArrayList<int[]>();
        mPaint.setColor(Color.BLACK);
        invalidate();
        Log.d("USER LOG","PAINT : SCREEN CLEARING");
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("USER LOG","DRAWING : START");
        mPaint.setColor(Color.MAGENTA);
        for(int i=0; i<pointlist.size(); i++)
            canvas.drawCircle(pointlist.get(i)[0],pointlist.get(i)[1],3,mPaint);
        Log.d("USER LOG","DRAWING : END");
    }
    //point list controller
    private boolean SecondDimentionalSearching(int x, int y) {
        for(int i=0; i<pointlist.size(); i++)
        {
            if(pointlist.get(i)[0] == x && pointlist.get(i)[1] == y)
                return true;
        }
        return false;
    }

    public void pointlist_ADD(int tx, int ty){

        Log.d("USER LOG","CLICK POS : x["+ tx + "]| y[" +ty + "]");
        if(SecondDimentionalSearching(tx,ty))
            Log.d("USER LOG","IT IS ALREADY IN ARRAY ");
        else
            pointlist.add(new int[] {tx,ty});
    }

    public void pointlist_Drawing(){
        invalidate();
    }

    //Touch
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(PaintingB == true)
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    int x = (int)event.getX();
                    int y = (int)event.getY();
                    String msg = "X[" + x + "] Y[" + y + "]";
                    ((MainActivity)MainActivity.mContext).txt_controller.ShowingTxt(msg);
                    pointlist_ADD(x,y);
                    pointlist_Drawing();
                    break;
                }
                default:
                    break;
            }
        }
        return true;
    }
}
