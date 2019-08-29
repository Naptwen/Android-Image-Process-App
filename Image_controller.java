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

    public Bitmap bitmap;
    private String TAG = "Image Converter : ";
    private int Mask_x[][] = {{1,1,1},{1,1,1},{1,1,1}};
    private int Mask_y[][]= {{1,1,1},{1,1,1},{1,1,1}};

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

    private int gray_scaling (int RGB_pixel) {
        return (( ((RGB_pixel>>16) & 0xff) + ((RGB_pixel >> 8) & 0xff) + (RGB_pixel & 0xff))/3);
    }

    public void Prewitt_mask(){
        Mask_x = new int[][]{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};
        Mask_y = new int[][]{{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};
    }

    public void Sobel_mask(){
        Mask_x = new int[][]{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        Mask_y = new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    }

    public void Roberts_mask(){
        Mask_x = new int[][]{{-1, 0, 0}, {0, 1, 0}, {0, 0, 0}};
        Mask_y = new int[][]{{0, 0, 0}, {0, 2, 0}, {1, 0, 0}};
    }

    public void Laplacian_mask(){
        Mask_x = new int[][]{{0, -1, 0}, {-1, 4,-1}, {0, -1, 0}};
        Mask_y = new int[][]{{0, 1, 0}, {1, 4,1}, {0, 1, 0}};;
    }

    public Bitmap Bitmap_Gray_scaling(Bitmap bitmap) {
        if (bitmap != null) {
            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int my_pixel = bitmap.getPixel(i,j);
                int gray_value = gray_scaling(my_pixel);
                bitmap.setPixel(i, j, Color.rgb(gray_value, gray_value, gray_value));
            }}
            return bitmap;
        }
        return null;
    }

    public Bitmap Bitmap_Change_Mask(Bitmap bitmap) {
        if (bitmap != null && Mask_x != null && Mask_y == null) {

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();


            int Sum_Mask_x = 0;
            //int[] point_list = new int[w*h];
            Bitmap new_bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

            for (int i = 1; i < w - 1; i++) {
                for (int j = 1; j < h - 1; j++) {
                    Sum_Mask_x = 0;
                    for (int i2 = 0; i2 < 3; i2++) {
                        for (int j2 = 0; j2 < 3; j2++) {
                            int my_pixel = bitmap.getPixel((i - 1) + i2, (j - 1) + j2);
                            int gray_pixel = gray_scaling(my_pixel);
                            Sum_Mask_x += Mask_x[i2][j2] * gray_pixel;
                        }
                    }
                    int value = (Sum_Mask_x > 0 ? Sum_Mask_x : -Sum_Mask_x);
                    new_bitmap.setPixel(i, j, Color.rgb(value, value, value));
                }
            }
            Log.d(TAG, "Image is converted");

            return new_bitmap;
        }
        else if (bitmap != null && Mask_x != null && Mask_y != null) {

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            int Sum_Mask_x = 0;
            int Sum_Mask_y = 0;

            //int[] point_list = new int[w*h];
            Bitmap new_bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            int[] pixel_list = new int[w * h];
            for (int i = 1; i < w - 1; i++) {
            for (int j = 1; j < h - 1; j++) {
                Sum_Mask_x = 0;
                Sum_Mask_y = 0;
                for (int i2 = 0; i2 < 3; i2++) {
                    for (int j2 = 0; j2 < 3; j2++) {
                        int my_pixel = bitmap.getPixel((i - 1) + i2, (j - 1) + j2);
                        int B_pixel = my_pixel & 0xff;
                        Sum_Mask_x += Mask_x[i2][j2] * B_pixel;
                        Sum_Mask_y += Mask_y[i2][j2] * B_pixel;
                    }
                }
                int value = (Sum_Mask_x > 0 ? Sum_Mask_x : -Sum_Mask_x) + (Sum_Mask_y > 0 ? Sum_Mask_y : -Sum_Mask_y);
                new_bitmap.setPixel(i, j, Color.rgb(value, value, value));
            }}
            Log.d(TAG, "Image is converted ");

            return new_bitmap;
        }
        return null;
    }

    public Bitmap Blur(Bitmap bitmap) {
        if(bitmap != null){

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            for (int i = 3; i < w-3; i++) {
            for (int j = 3; j < h-3; j++) {
                int sum_R = 0;
                int sum_G = 0;
                int sum_B = 0;
                for (int i2 = 0; i2 < 5; i2++) {
                for (int j2 = 0; j2 < 5; j2++) {
                        int my_pixel = bitmap.getPixel((i - 2) + i2, (j - 2) + j2);
                        sum_R += my_pixel>>16 & 0xff;
                        sum_G += my_pixel >> 8 & 0xff;
                        sum_B += my_pixel & 0xff;
                 }}
                int avg_R = sum_R/25;
                int avg_G = sum_G/25;
                int avg_B = sum_B/25;
                bitmap.setPixel(i,j, Color.rgb(avg_R, avg_G, avg_B));
            }}
            return bitmap;
        }
        else
            return null;
        }

    public Bitmap Non_maximum_suppression(Bitmap bitmap) {
        if(bitmap != null){

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            ArrayList<Integer> non_max_list= new ArrayList<Integer>();

            for (int i = 1; i < w-1; i++) {
            for (int j = 1; j < h-1; j++) {
                    int sum = 0;
                    for (int i2 = 0; i2 < 3; i2++) {
                    for (int j2 = 0; j2 < 3; j2++) {
                            int my_pixel = bitmap.getPixel((i - 1) + i2, (j - 1) + j2);
                            int new_sum  = my_pixel & 0xff;
                            if(new_sum >= sum)
                                sum = new_sum;
                        }}
                int current_pixel = bitmap.getPixel(i, j);
                int current_sum  = current_pixel & 0xff;
                     if (sum != current_sum ) {
                         non_max_list.add(i);
                         non_max_list.add(j);
                     }
            }}
            int size = non_max_list.size()/2;
            for(int k=0; k<size; k++){
                int x = non_max_list.get(k*2);
                int y = non_max_list.get((k*2)+1);
                bitmap.setPixel(x,y,Color.BLACK);
            }
            return bitmap;
        }
        else
            return null;
    }
    }