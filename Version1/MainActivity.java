package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener  {
//UI List
    private String TAG = "MAIN : ";
    public static Context mContext;
    ImageButton Enter_Button;
    EditText Input_Text_Field;
    TextView TextViewer;
    Drawing myDrawing;
    Pop_SAVE myPopup_S;
    Pop_LOAD myPopup_L;
    ScrollView scrollview;
    Camera camera;
    ToggleButton Camera_button;
    TextureView mTextureView;
    FrameLayout mDrawingLayout;
    Text_Controller txt_controller;
    ImageView CameraImageVIew;
    Bitmap mBitmap;
    Image_controller mImage_controller;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        Input_Text_Field = findViewById(R.id.InputTextField);
        TextViewer = findViewById(R.id.TextViewer);
        scrollview = findViewById(R.id.Scroll1);
        //Camera
        mTextureView = findViewById(R.id.textureView);
        mTextureView.setOpaque(false);
        camera = new Camera(this, mTextureView);
        //Text
        txt_controller = new Text_Controller(this, TextViewer, scrollview);
        //Drawing
        myDrawing = new Drawing(this);
        mDrawingLayout = (FrameLayout) findViewById(R.id.DrawingLay);
        mDrawingLayout.addView(myDrawing);
        //Image
        CameraImageVIew = (ImageView) findViewById(R.id.CameraImageVIew);
        //Button list
        Camera_button = (ToggleButton) findViewById(R.id.Camera);
        Enter_Button = findViewById(R.id.EnterKey);
        myPopup_S = new Pop_SAVE(this);
        myPopup_L = new Pop_LOAD(this);

        Enter_Button.setOnClickListener(new Button.OnClickListener(){
          @Override
          public void onClick(View view){
            String txt = Input_Text_Field.getText().toString();
            txt_controller.ShowingTxt(txt);
            Input_Text_Field.setText("");
          }
        });
    }

    //popup (Menu)
    public void menu_button(View v){
        Button_Animation((Button)findViewById(R.id.menu));
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
    }
    //popup menu 2 (File)
    public void file_button(View v) {
        Button_Animation((Button)findViewById(R.id.file));
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.document_menu);
        popup.show();
    }
    //popup menu 2 ()
    public void Image_button(View v) {
        Button_Animation((Button)findViewById(R.id.Image));
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.converting_menu);
        popup.show();
    }
    public void Save_1(View v){
        myPopup_S.Save_fn("SAVE_1");
    }
    public void Save_2(View v){
        myPopup_S.Save_fn("SAVE_2");
    }
    public void Save_3(View v){
        myPopup_S.Save_fn("SAVE_3");
    }
    public void Save_exit(View v){
        myPopup_S.POP_DESTROY();
    }
    //LOAD POPUP WINDOW BUTTON FUNCTION LIST
    public void Load_1(View v){
        myPopup_L.Load_fn("SAVE_1");
    }
    public void Load_2(View v){
        myPopup_L.Load_fn("SAVE_2");
    }
    public void Load_3(View v){
        myPopup_L.Load_fn("SAVE_3");
    }
    public void Load_exit(View v){
        myPopup_L.POP_DESTROY();
    }
    //Camera button
    public void Camera_button(View v) {
        Log.d(TAG, "Camera Button pushed");
        if(Camera_button.isChecked()) {
            Camera_button_on();
        }else{
            Camera_button_off();
        }
    }

    private void Camera_button_on(){
        Log.d(TAG, "Camera ON");
        Camera_button.setChecked(true);
        CameraImageVIew.setVisibility(View.INVISIBLE);
        camera.openCamera();
        Camera_button.setTextColor(Color.RED);
        txt_controller.ShowingTxt("[CAMERA ON]");
    }

    private void Camera_button_off(){
        Log.d(TAG, "Camera OFF");
        camera.stopCamera();
        Camera_button.setTextColor(Color.GRAY);
        Camera_button.setChecked(false);
        txt_controller.ShowingTxt("[CAMERA OFF]");
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch(item.getItemId()){
            case R.id.Draw:
                myDrawing.PaintingB = !myDrawing.PaintingB;
                if(myDrawing.PaintingB){
                    txt_controller.ShowingToast("DRAWING ON");
                }
                else{
                    txt_controller.ShowingToast("DRAWING OFF");
                }
                return true;
            case R.id.Clear_Draw:
                Log.d(TAG, "Drawing clear...");
                myDrawing.ScreenClear();
                txt_controller.ShowingToast("CLEAR Drawing");
                txt_controller.ShowingTxt("[DRAWING CLEAR]");
                return true;
            case R.id.Clear_Sc:
                Log.d(TAG, "Screen clear...");
                camera.stopCamera();
                Log.d(TAG, "Camera clear");
                myDrawing.ScreenClear();
                Log.d(TAG, "Drawing canvas clear");
                Camera_button_off();
                CameraImageVIew.setImageBitmap(null);
                CameraImageVIew.setVisibility(View.VISIBLE);
                txt_controller.ShowingToast("CLEAR SCREEN");
                txt_controller.ShowingTxt("[SCREEN CLEAR]");
                return true;
            case R.id.Clear_Txt:
                txt_controller.ShowingToast("CLEAR TEXT");
                TextViewer.setText("");
                return true;
            case R.id.Save:
                myPopup_S.POP_SHOW();
                return true;
            case R.id.Load:
                myPopup_L.POP_SHOW();
                return true;
            case R.id.Import:
                txt_controller.Drawing_point_from_TextViewer();
                return true;
            case R.id.cap:
                Camera_button_off();
                mImage_controller = new Image_controller();
                mBitmap = mImage_controller.GetBitmap(mTextureView);
                mImage_controller.Bitmap_to_Drawing(mBitmap, CameraImageVIew);
                txt_controller.ShowingTxt("[CAPTURE COMPLETE]");
                return true;
            case R.id.blur:
                if(mBitmap != null){
                    long start = System.currentTimeMillis();
                    Bitmap Changed_bitmap =  mImage_controller.Blur(mBitmap);
                    mBitmap = Changed_bitmap;
                    mImage_controller.Bitmap_to_Drawing(mBitmap , CameraImageVIew);
                    long finish = System.currentTimeMillis();
                    long timeElapsed = finish - start;
                    txt_controller.ShowingTxt("[Blue IS FINISHED] " + timeElapsed + "ms");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.gray:
                if(mBitmap != null){
                    long start = System.currentTimeMillis();
                    Bitmap Changed_bitmap =  mImage_controller.Bitmap_Gray_scaling(mBitmap);
                    mBitmap = Changed_bitmap;
                    mImage_controller.Bitmap_to_Drawing(mBitmap , CameraImageVIew);
                    long finish = System.currentTimeMillis();
                    long timeElapsed = finish - start;
                    txt_controller.ShowingTxt("[Gray Scaling IS FINISHED] " + timeElapsed + "ms");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.prewitt:
                if(mBitmap != null){
                    mImage_controller.Prewitt_mask();
                    txt_controller.ShowingTxt("[Prewitt Mask is set]");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.sobel:
                if(mBitmap != null){
                    mImage_controller.Sobel_mask();
                    txt_controller.ShowingTxt("[Sobel Mask is set]");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.robert:
                if(mBitmap != null){
                    mImage_controller.Roberts_mask();
                    txt_controller.ShowingTxt("[Roberts Mask is set]");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.laplac:
                if(mBitmap != null){
                    mImage_controller.Laplacian_mask();
                    txt_controller.ShowingTxt("[MASK FILTER IS APPLIED]");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.analyze:
                if(mBitmap != null){
                    long start = System.currentTimeMillis();
                Bitmap Changed_bitmap =  mImage_controller.Bitmap_Change_Mask(mBitmap);
                mBitmap = Changed_bitmap;
                mImage_controller.Bitmap_to_Drawing(mBitmap , CameraImageVIew);
                    long finish = System.currentTimeMillis();
                    long timeElapsed = finish - start;
                txt_controller.ShowingTxt("[MASK IS FINISHED] " + timeElapsed + "ms");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;
            case R.id.non_max:
                if(mBitmap != null){
                    long start = System.currentTimeMillis();
                    Bitmap Changed_bitmap =  mImage_controller.Non_maximum_suppression(mBitmap);
                    mBitmap = Changed_bitmap;
                    mImage_controller.Bitmap_to_Drawing(mBitmap , CameraImageVIew);
                    long finish = System.currentTimeMillis();
                    long timeElapsed = finish - start;
                    txt_controller.ShowingTxt("[Non Maximum IS FINISHED] " + timeElapsed + "ms");
                }else {
                    txt_controller.ShowingTxt("[NEED CAPTURE THE PIC FIRST]");
                }
                return true;

            default:
                return false;
        }
    }
    //READING AND DRAWING
    public void Web_button(View v){
        Intent intent = new Intent (Intent.ACTION_VIEW, Uri.parse("http://google.com/"));
        startActivity(intent);
    }

    public void Button_Animation(Button button){
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.50, 20);
        myAnim.setInterpolator(interpolator);
        button.startAnimation(myAnim);
    }
}

