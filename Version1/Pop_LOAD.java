package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Pop_LOAD {
    private LinearLayout popuplay;
    private View popupview;
    private PopupWindow popup;
    private Context context;

    public Pop_LOAD(Context c){
        context = c;
    }

    public void POP_SHOW() {
        if(popup != null)
            POP_DESTROY();
        popup = new PopupWindow(context);
        popuplay = ((Activity)context).findViewById(R.id.Lay_pop_L);
        popupview = View.inflate(context, R.layout.load_popup_window, null);
        popup.setContentView(popupview);
        popup.showAtLocation(popupview, Gravity.CENTER, 0,0);
        popup.setTouchable(true);
        popup.setFocusable(false);
        popup.setOutsideTouchable(false);
    }

    public void POP_DESTROY(){
        popup.dismiss();
    }

    public void Load_fn(String file_name){
        try {
            StringBuffer r_data = new StringBuffer();
            FileInputStream fis = context.openFileInput(file_name +".txt");
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fis));
            String str = buffer.readLine();
            while(str != null){
                r_data.append(str + "\n");
                str = buffer.readLine();
            }
            ((MainActivity)context).txt_controller.ShowingTxt(r_data.toString());
            buffer.close();
            ((MainActivity)context).txt_controller.ShowingToast("LOAD COMPLETE");
            ((MainActivity)context).txt_controller.ShowingTxt("[LOAD COMPLETE]");
            POP_DESTROY();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

