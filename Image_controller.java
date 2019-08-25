package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

public class Image_controller {

    class Dot {
        int x;
        int y;
        int R;
        int G;
        int B;
    };

    private Bitmap bitmap;
    private ArrayList<Dot> Dot_list;
    private String TAG = "Image Converter : ";

    public Bitmap GetBitmap(TextureView view){
        Matrix m = new Matrix();
        view.setTransform(m);
        bitmap = view.getBitmap();
        Log.d(TAG, "Image W[" + view.getWidth() + "] H[" + view.getHeight() + "]");
        return bitmap;
    }

    public void Bitmap_to_Drawing(Bitmap bitmap, ImageView imgview){
        imgview.setVisibility(View.VISIBLE);
        imgview.setImageBitmap(bitmap);
        Log.d(TAG, "Bitmap is drawn");
    }

    public void Bitmap_to_Struct(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[][] mpixel = new int[w][h];

        Dot_list = new ArrayList<Dot>();
        for(int i = 0; i<w; i++)
            for(int j =0; j<h; j++) {
                mpixel[i][j] = bitmap.getPixel(i, j);
                int R = (mpixel[i][j] >> 16) & 0xff;
                int G = (mpixel[i][j] >> 8) & 0xff;
                int B = mpixel[i][j] & 0xff;
                Dot temp = new Dot();
                temp.x = i;
                temp.y = j;
                temp.R = R;
                temp.G = G;
                temp.B = B;
                Dot_list.add(temp);
            }
        Log.d(TAG, "Image is converted to struct");
    }

    public Bitmap Bitmap_Change_BW(Bitmap bitmap) {
        if (bitmap != null){

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[][] my_pixel = new int[w][h];
        Bitmap new_bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++) {
                my_pixel[i][j] = bitmap.getPixel(i, j);
                int R = (my_pixel[i][j] >> 16) & 0xff;
                int G = (my_pixel[i][j] >> 8) & 0xff;
                int B = my_pixel[i][j] & 0xff;
                float[] RGB_factor = {R, G, B};
                float P = (RGB_factor[0] + RGB_factor[1] + RGB_factor[2])/3;
                //float P = RGB_factor[0] * 0.21f+ RGB_factor[1] * 0.72f + RGB_factor[2] * 0.07f;
                int new_pixel = Color.rgb((int)P,(int)P,(int)P);
                new_bitmap.setPixel(i,j,new_pixel);
            }
            Log.d(TAG, "Image is converted");
            return new_bitmap;
        }
        return null;
    }

    public Bitmap Bitmap_Analayzing(Bitmap bitmap){
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[][] my_pixel = new int[w][h];
        Bitmap new_bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);

        float temp_ColorSum = 0;
        int new_pixel = 0;

        for (int i = 0; i < w; i++){
            for (int j = 0; j < h; j++) {
                my_pixel[i][j] = bitmap.getPixel(i, j);
                int R = (my_pixel[i][j] >> 16) & 0xff;
                int G = (my_pixel[i][j] >> 8) & 0xff;
                int B = my_pixel[i][j] & 0xff;
                float ColorSum = R + G + B;
                if(temp_ColorSum != 0){
                    if(temp_ColorSum * 0.95 < ColorSum && temp_ColorSum * 1.05 > ColorSum)
                        new_pixel = Color.rgb(255,255,255);
                    else {
                        temp_ColorSum = ColorSum;
                        new_pixel = Color.rgb(0, 0, 0);
                    }
                }else {
                    temp_ColorSum = ColorSum;
                    new_pixel = Color.rgb(0, 0, 0);
                }
                new_bitmap.setPixel(i,j,new_pixel);
            }
        }
        Log.d(TAG, "Image is Analyzed");
        return new_bitmap;
    }
}
