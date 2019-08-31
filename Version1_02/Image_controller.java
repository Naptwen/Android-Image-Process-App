package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import java.util.*;
import java.util.ArrayList;

public class Image_controller {


    private String TAG = "Image : ";
    private int Mask_x[][] = {{1,1,1},{1,1,1},{1,1,1}};
    private int Mask_y[][]= {{1,1,1},{1,1,1},{1,1,1}};
    private ArrayList<int[]> Pixel_list; // 5size       [x,y, R,G,B]
    private ArrayList<int[]> Pixel_blur_list; // 5size  [x,y, R,G,B]
    private ArrayList<int[]> Pixel_gray_list; // 3size  [x,y, white strength]
    private ArrayList<int[]> Pixel_mask_list; // 5size  [x,y, G value, Gx Gy]
    private ArrayList<int[]> Pixel_Non_list;  // 3size  [x,y, white strength]
    private Bitmap mbitmap;

    public Bitmap Get_Bitmap_From_camera(TextureView view){
        Matrix m = new Matrix();
        view.setTransform(m);
        Bitmap bitmap = view.getBitmap();
        Log.d(TAG, "Image W[" + view.getWidth() + "] H[" + view.getHeight() + "]");
        return bitmap;
    }

    public void Bitmap_to_Drawing(Bitmap bitmap, ImageView imgview){
        imgview.setVisibility(View.VISIBLE);
        Bitmap new_bitmap = getResizedBitmap(bitmap, 400 );
        mbitmap = new_bitmap;
        imgview.setImageBitmap(new_bitmap);
        Log.d(TAG, "Bitmap is drawn");
    }

    public void Prewitt_mask(){
        Mask_x = new int[][]{{-1, 0, 1}, {-1, 0, 1}, {-1, 0, 1}};
        Mask_y = new int[][]{{-1, -1, -1}, {0, 0, 0}, {1, 1, 1}};
    }

    public void Sobel_mask(){
        Log.d(TAG, "SOBEL MASK");
        Mask_x = new int[][]{{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        Mask_y = new int[][]{{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
    }

    public void Roberts_mask(){
        Mask_x = new int[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, 0}};
        Mask_y = new int[][]{{0, 0, 0}, {0, -1, 0}, {1, 0, 0}};
    }

    public void Laplacian_mask(){
        Mask_x = new int[][]{{0, -1, 0}, {-1, 4,-1}, {0, -1, 0}};
        Mask_y = new int[][]{{0, 1, 0}, {1, 4,1}, {0, 1, 0}};;
    }
    //1 Set Pixel List (col to row)
    public void GetPIxel(Bitmap bitmap){
        Log.d(TAG, "Get Pixel working");
        Pixel_list = new ArrayList<>();
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        for (int j = 0; j < h; j++) {
        for (int i = 0; i < w; i++) {
                int my_pixel = bitmap.getPixel(i, j);
                int[] temp_pixel = {i,j,((my_pixel>>16) & 0xff),((my_pixel>>8) & 0xff),my_pixel & 0xff};
                Pixel_list.add(temp_pixel);
            }}
    }
    //2 Set Pixel_blur_list {Pixel_list need}
    public void Blur() {
        Log.d(TAG, "Blur working");
        if(Pixel_list != null) {
            int[][] Gaussian_matrix = new int[][]{{1, 4, 7, 4, 1}, {4, 16, 26, 16, 4}, {7, 26, 41, 26, 7}, {4, 16, 26, 16, 4}, {1, 4, 7, 4, 1}}; //Gaussian filter
            int size = Pixel_list.size();
            int last_x = Pixel_list.get(size - 1)[0];
            int last_y = Pixel_list.get(size - 1)[1];
            int last_x2 = Pixel_list.get(size - 1)[0] - 1;
            int last_y2 = Pixel_list.get(size - 1)[1] - 1;
            int w = last_x + 1;
            Pixel_blur_list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                int[] sum = new int[]{Pixel_list.get(i)[2],Pixel_list.get(i)[3],Pixel_list.get(i)[4]};
                int avg_R = Pixel_list.get(i)[2];
                int avg_G = Pixel_list.get(i)[3];
                int avg_B = Pixel_list.get(i)[4];
                if (Pixel_list.get(i)[0] != 0 && Pixel_list.get(i)[1] != 0 && Pixel_list.get(i)[0] != last_x && Pixel_list.get(i)[1] != last_y
                        && Pixel_list.get(i)[0] != 1 &&Pixel_list.get(i)[1] != 1 && Pixel_list.get(i)[0] != last_x2 && Pixel_list.get(i)[1] != last_y2) {
                    /*
                    00 |   01   |       02       | 03 | 04
                    10 |   11   |       12       | 13 | 14
                    20 |   21   |     current    | 23 | 24
                    30 |   31   |       32       | 33 | 34
                    40 |   41   |       42       | 43 | 44
                    */
                    int pixel_00 = i - w * 2 - 2;
                    int pixel_01 = i - w * 2 - 1;
                    int pixel_02 = i - w * 2;
                    int pixel_03 = i - w * 2 + 1;
                    int pixel_04 = i - w * 2 + 2;

                    int pixel_10 = i - w - 2;
                    int pixel_11 = i - w - 1;
                    int pixel_12 = i - w;
                    int pixel_13 = i - w + 1;
                    int pixel_14 = i - w + 2;

                    int pixel_20 = i - 2;
                    int pixel_21 = i - 1;
                    // pixel_22 is current pixel position
                    int pixel_23 = i + 1;
                    int pixel_24 = i + 2;

                    int pixel_30 = i + w - 2;
                    int pixel_31 = i + w - 1;
                    int pixel_32 = i + w;
                    int pixel_33 = i + w + 1;
                    int pixel_34 = i + w + 2;

                    int pixel_40 = i + w * 2 - 2;
                    int pixel_41 = i + w * 2 - 1;
                    int pixel_42 = i + w * 2;
                    int pixel_43 = i + w * 2 + 1;
                    int pixel_44 = i + w * 2 + 2;

                    sum[0] = 0;
                    sum[1] = 0;
                    sum[2] = 0;
                    for (int k = 2; k < 5; k++) {
                        int[] matrix_pixel = new int[25];
                        matrix_pixel[0] = Pixel_list.get(pixel_00)[k] * Gaussian_matrix[0][0];
                        matrix_pixel[1] = Pixel_list.get(pixel_01)[k] * Gaussian_matrix[0][1];
                        matrix_pixel[2] = Pixel_list.get(pixel_02)[k] * Gaussian_matrix[0][2];
                        matrix_pixel[3] = Pixel_list.get(pixel_03)[k] * Gaussian_matrix[0][3];
                        matrix_pixel[4] = Pixel_list.get(pixel_04)[k] * Gaussian_matrix[0][4];

                        matrix_pixel[5] = Pixel_list.get(pixel_10)[k] * Gaussian_matrix[1][0];
                        matrix_pixel[6] = Pixel_list.get(pixel_11)[k] * Gaussian_matrix[1][1];
                        matrix_pixel[7] = Pixel_list.get(pixel_12)[k] * Gaussian_matrix[1][2];
                        matrix_pixel[8] = Pixel_list.get(pixel_13)[k] * Gaussian_matrix[1][3];
                        matrix_pixel[9] = Pixel_list.get(pixel_14)[k] * Gaussian_matrix[1][4];

                        matrix_pixel[10] = Pixel_list.get(pixel_20)[k] * Gaussian_matrix[2][0];
                        matrix_pixel[11] = Pixel_list.get(pixel_21)[k] * Gaussian_matrix[2][1];
                        matrix_pixel[12] = Pixel_list.get(i)[k]        * Gaussian_matrix[2][2];
                        matrix_pixel[13] = Pixel_list.get(pixel_23)[k] * Gaussian_matrix[2][3];
                        matrix_pixel[14] = Pixel_list.get(pixel_24)[k] * Gaussian_matrix[2][4];

                        matrix_pixel[15] = Pixel_list.get(pixel_30)[k] * Gaussian_matrix[3][0];
                        matrix_pixel[16] = Pixel_list.get(pixel_31)[k] * Gaussian_matrix[3][1];
                        matrix_pixel[17] = Pixel_list.get(pixel_32)[k] * Gaussian_matrix[3][2];
                        matrix_pixel[18] = Pixel_list.get(pixel_33)[k] * Gaussian_matrix[3][3];
                        matrix_pixel[19] = Pixel_list.get(pixel_34)[k] * Gaussian_matrix[3][4];

                        matrix_pixel[20] = Pixel_list.get(pixel_40)[k] * Gaussian_matrix[4][0];
                        matrix_pixel[21] = Pixel_list.get(pixel_41)[k] * Gaussian_matrix[4][1];
                        matrix_pixel[22] = Pixel_list.get(pixel_42)[k] * Gaussian_matrix[4][2];
                        matrix_pixel[23] = Pixel_list.get(pixel_43)[k] * Gaussian_matrix[4][3];
                        matrix_pixel[24] = Pixel_list.get(pixel_44)[k] * Gaussian_matrix[4][4];
                        for (int j = 0; j < 25; j++)
                            sum[k - 2] += matrix_pixel[j];
                    }
                    avg_R = sum[0] / 273;
                    avg_G = sum[1] / 273;
                    avg_B = sum[2] / 273;
                }
                int[] temp_pixel = {Pixel_list.get(i)[0], Pixel_list.get(i)[1], avg_R, avg_G, avg_B};
                Pixel_blur_list.add(temp_pixel);
            }
            Log.d(TAG, "Blur is finished");
        }
        else
            Log.d(TAG, "need get Bitmap");
    }
    //3. Set the Pixel_gray_list {Pixel_blur_list need}
    public void Bitmap_Gray_scaling() {
        Log.d(TAG, "Gray scale working");
        if (Pixel_blur_list != null) {
            Pixel_gray_list = new ArrayList<>();
            int size = Pixel_blur_list.size();
            for (int i = 0; i < size; i++) {
                int x = Pixel_blur_list.get(i)[0];
                int y = Pixel_blur_list.get(i)[1];
                int R = Pixel_blur_list.get(i)[2];
                int G = Pixel_blur_list.get(i)[3];
                int B = Pixel_blur_list.get(i)[4];
                int Gray = (R+G+B)/3;
                int[] temp_pixel = {x,y,Gray};
                Pixel_gray_list.add(temp_pixel);
                }
            }
    }
    //4. Set the Pixel_mask_list {Pixel_gray_list need}
    public void Bitmap_Change_Mask() {
        Log.d(TAG, "Masking working");
        if (Mask_x != null && Mask_y != null && Pixel_gray_list != null) {
            int Sum_Mask_x = 0;
            int Sum_Mask_y = 0;
            int size = Pixel_gray_list.size();
            int last_x = Pixel_gray_list.get(size-1)[0];
            int last_y = Pixel_gray_list.get(size-1)[1];
            int w = last_x + 1;
            Pixel_mask_list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                Sum_Mask_x = 0;
                Sum_Mask_y = 0;
                if (Pixel_gray_list.get(i)[0] != 0 && Pixel_gray_list.get(i)[1] != 0 && Pixel_gray_list.get(i)[0] != last_x && Pixel_gray_list.get(i)[1] != last_y) {
                    /*
                    00 |         01        | 02
                    10 |     current Pixel | 12
                    20 |         21        | 22
                    */
                    int pixel_00 = i - w - 1;
                    int pixel_01 = i - w;
                    int pixel_02 = i - w + 1;
                    int pixel_10 = i - 1;
                    int pixel_12 = i + 1;
                    int pixel_20 = i + w - 1;
                    int pixel_21 = i + w;
                    int pixel_22 = i + w + 1;

                    int value_00 = Pixel_gray_list.get(pixel_00)[2];
                    int value_01 = Pixel_gray_list.get(pixel_01)[2];
                    int value_02 = Pixel_gray_list.get(pixel_02)[2];
                    int value_10 = Pixel_gray_list.get(pixel_10)[2];
                    int value_11 = Pixel_gray_list.get(i)[2];
                    int value_12 = Pixel_gray_list.get(pixel_12)[2];
                    int value_20 = Pixel_gray_list.get(pixel_20)[2];
                    int value_21 = Pixel_gray_list.get(pixel_21)[2];
                    int value_22 = Pixel_gray_list.get(pixel_22)[2];

                    Sum_Mask_x += Mask_x[0][0]*value_00 + Mask_x[0][1]*value_01 + Mask_x[0][2]*value_02
                            + Mask_x[1][0]*value_10 + Mask_x[1][1]*value_11 + Mask_x[1][2]*value_12
                            + Mask_x[2][0]*value_20 + Mask_x[2][1]*value_21 + Mask_x[2][2]*value_22 ;
                    Sum_Mask_y += Mask_y[0][0]*value_00 + Mask_y[0][1]*value_01 + Mask_y[0][2]*value_02
                            + Mask_y[1][0]*value_10 + Mask_y[1][1]*value_11 + Mask_y[1][2]*value_12
                            + Mask_y[2][0]*value_20 + Mask_y[2][1]*value_21 + Mask_y[2][2]*value_22 ;
                }
                int G_x = Sum_Mask_x;
                int G_y = Sum_Mask_y;
                // (Sum_Mask_x > 0 ? Sum_Mask_x : -Sum_Mask_x) + (Sum_Mask_y > 0 ? Sum_Mask_y : -Sum_Mask_y);
                int G_value = (int) Math.sqrt(Math.pow(G_x,2) + Math.pow(G_y,2));
                int[] temp_pixel = {Pixel_gray_list.get(i)[0],Pixel_gray_list.get(i)[1], G_value, G_x, G_y};
                Pixel_mask_list.add(temp_pixel);
            }
            Log.d(TAG, "Masking is finished ");
        }else
            Log.d(TAG, "Mask set or Gray Scale List is empty ");
     }
    //5, Set the Pixel_Non_list {Pixel_mask_list need}
    public void Non_maximum_suppression() {
        Log.d(TAG, "Non maximum suppression is working");
        if (Pixel_mask_list != null) {

            Pixel_Non_list = new ArrayList<>();
            int size = Pixel_mask_list.size();
            int last_x = Pixel_mask_list.get(size - 1)[0];
            int last_y = Pixel_mask_list.get(size - 1)[1];
            int w = last_x + 1;
            for (int i = 0; i < size; i++) {
                int x = Pixel_mask_list.get(i)[0];
                int y = Pixel_mask_list.get(i)[1];
                int c = Pixel_mask_list.get(i)[2];
                if (Pixel_mask_list.get(i)[0] != 0 && Pixel_mask_list.get(i)[1] != 0 && Pixel_mask_list.get(i)[0] != last_x && Pixel_mask_list.get(i)[1] != last_y) {
                      /* cue the Mask set is double dimension array
                    00 |         01        | 02
                    10 |     current Pixel | 12
                    20 |         21        | 22
                    */
                    int  Gx = Pixel_mask_list.get(i)[3];
                    int  Gy = Pixel_mask_list.get(i)[4];
                    int Angle = (int) Math.abs(Math.atan2(Gy, Gx)/3.14 * 180);
                    //              0 1 2  3 4 5  6 7 8
                    int[] Matrix = {0,0,0, 0,1,0, 0,0,0};
                    //Log.d(TAG,"Angle : "+ Angle);
                    ArrayList<Integer> new_list = new ArrayList<>();
                    new_list.add(Pixel_mask_list.get(i)[2]);
                    if(0 == Angle || Angle == 180){
                            new_list.add(Pixel_mask_list.get(i - 1)[2]);
                            new_list.add(Pixel_mask_list.get(i + 1)[2]);
                    }else if( 0 < Angle && Angle <= 45){
                        new_list.add(Pixel_mask_list.get(i - w + 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + w - 1)[2]);
                    }else if(45 < Angle && Angle <= 90) {
                        new_list.add(Pixel_mask_list.get(i - w)[2]);
                        new_list.add(Pixel_mask_list.get(i + w)[2]);
                    }else if(90 < Angle){
                        new_list.add(Pixel_mask_list.get(i - w - 1)[2]);
                        new_list.add(Pixel_mask_list.get(i + w + 1)[2]);
                    }
                    int  max  = Collections.max(new_list);
                    if(max != Pixel_mask_list.get(i)[2])
                        c = 0;
                    /*

                    if (!max){
                        c = 0;}
                    int pixel_00 = i - w - 1;
                    int pixel_01 = i - w;
                    int pixel_02 = i - w + 1;
                    int pixel_10 = i - 1;
                    int pixel_12 = i + 1;
                    int pixel_20 = i + w - 1;
                    int pixel_21 = i + w;
                    int pixel_22 = i + w + 1;

                    int[] find_max_list = new int[9];
                    find_max_list[0] = Pixel_mask_list.get(pixel_00)[2];
                    find_max_list[1] = Pixel_mask_list.get(pixel_01)[2];
                    find_max_list[2] = Pixel_mask_list.get(pixel_02)[2];
                    find_max_list[3] = Pixel_mask_list.get(pixel_10)[2];
                    find_max_list[4] = Pixel_mask_list.get(i)[2];
                    find_max_list[5] = Pixel_mask_list.get(pixel_12)[2];
                    find_max_list[6] = Pixel_mask_list.get(pixel_20)[2];
                    find_max_list[7] = Pixel_mask_list.get(pixel_21)[2];
                    find_max_list[8] = Pixel_mask_list.get(pixel_22)[2];
                    int k=0;
                    boolean max = true;
                    while(k < 9) {
                        if (find_max_list[4] < find_max_list[k]) {
                            max = false;
                            break;
                        }
                        k++;
                    }
                    */
                }
                int[] temp_pixel = new int[]{x, y, c};
                Pixel_Non_list.add(temp_pixel);
            }
            Log.d(TAG, "Non maximum suppression is finished");
        }else
            Log.d(TAG, "Pixel mask List is empty ");
    }
    //
    public Bitmap Image_Bitmap_applying(Text_Controller txt) {
    if(mbitmap != null){
        long totalTime = System.currentTimeMillis();

        long startTime = System.currentTimeMillis();
        GetPIxel(mbitmap);
        long endTime = System.currentTimeMillis();
        long duration= (endTime - startTime);
        txt.ShowingTxt("Get Pixel : " + duration + "ms");

        startTime = System.currentTimeMillis();
        Blur();
        endTime = System.currentTimeMillis();
        duration= (endTime - startTime);
        txt.ShowingTxt("Set Blur(Gaussian) : " + duration + "ms");

        startTime = System.currentTimeMillis();
        Bitmap_Gray_scaling();
        endTime = System.currentTimeMillis();
        duration= (endTime - startTime);
        txt.ShowingTxt("Set Gray : " + duration + "ms");

        startTime = System.currentTimeMillis();
        Sobel_mask();
        Bitmap_Change_Mask();
        endTime = System.currentTimeMillis();
        duration= (endTime - startTime);
        txt.ShowingTxt("Set Sobel : " + duration + "ms");

        startTime = System.currentTimeMillis();
        Non_maximum_suppression();
        endTime = System.currentTimeMillis();
        duration= (endTime - startTime);
        txt.ShowingTxt("Set Non Max : " + duration + "ms");

        startTime = System.currentTimeMillis();
        Log.d(TAG, "Final Displaying working");
        Bitmap new_bitmap = drawing_bitmap(Pixel_Non_list);
        endTime = System.currentTimeMillis();
        duration= (endTime - startTime);
        txt.ShowingTxt("Set image : " + duration + "ms");
        long totalendTime = System.currentTimeMillis();
        long total_duration = totalendTime - totalTime;

        txt.ShowingTxt("Total time : " + total_duration + "ms");
        Log.d(TAG, "Image is converted");
        txt.ShowingTxt("Completed the process");


        return new_bitmap;
        }else
            return null;
    }
    //
    public void Image_graphi8(Bitmap bitmap) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        ArrayList<Integer> Image_graphic = new ArrayList<Integer>();
        for (int i = 0; i < w; i++) {
        for (int j = 0; j < h; j++) {
            int my_pixel = bitmap.getPixel(i,j);

                Image_graphic.add(my_pixel & 0xFF);
        }}
       Collections.sort(Image_graphic);
        for(int i=0; i<Image_graphic.size(); i++){
            Log.d(TAG, String.valueOf(Image_graphic.get(i).intValue()));
        }
    }
    //
    private Bitmap drawing_bitmap(ArrayList<int[]> list) {
        Bitmap new_bitmap = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), Bitmap.Config.ARGB_8888);
        if (Pixel_gray_list.equals(list) || Pixel_mask_list.equals(list) || Pixel_Non_list.equals(list)) {
            for (int i = 0; i < list.size(); i++) {
                int x = list.get(i)[0];
                int y = list.get(i)[1];
                int color = list.get(i)[2];
                new_bitmap.setPixel(x, y, Color.rgb(color, color, color));
            }
        }else {
            for (int i = 0; i < list.size(); i++) {
                int x = list.get(i)[0];
                int y = list.get(i)[1];
                int r = list.get(i)[2];
                int g = list.get(i)[3];
                int b = list.get(i)[4];
                new_bitmap.setPixel(x, y, Color.rgb(r, g, b));
            }
        }
        return new_bitmap;
    }
    //
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        Log.d(TAG, "Bitmap is resized");
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}
