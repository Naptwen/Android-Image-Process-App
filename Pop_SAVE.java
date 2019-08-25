package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Pop_SAVE {
    private LinearLayout popuplay;
    private View popupview;
    private PopupWindow popup;
    private Context context;

    public Pop_SAVE(Context c){
        context = c;
    }

    public void POP_SHOW() {
        if(popup != null)
            POP_DESTROY();
        popup = new PopupWindow(context);
        popuplay =  ((Activity)context).findViewById(R.id.Lay_pop);
        popupview = View.inflate(context, R.layout.save_popup_window, null);
        popup.setContentView(popupview);
        popup.showAtLocation(popupview, Gravity.CENTER, 0,0);
        popup.setTouchable(true);
        popup.setFocusable(true);
        popup.setOutsideTouchable(false);
    }

    public void POP_DESTROY(){
        popup.dismiss();
    }

    public void Save_fn(String file_name) {
        String data =  ((MainActivity)context).TextViewer.getText().toString();
        try {
            FileOutputStream fos = context.openFileOutput(file_name+".txt", Context.MODE_PRIVATE);
            PrintWriter out = new PrintWriter(fos);
            out.println(data);
            out.close();
            ((MainActivity)context).txt_controller.ShowingToast("SAVE COMPLETE");
            ((MainActivity)context).txt_controller.ShowingTxt("[SAVE COMPLETE]");
            POP_DESTROY();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
