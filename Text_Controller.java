package com.example.myapplication;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Text_Controller {
    public TextView TextViewer;
    public ScrollView scrollView;
    public Context context;

    public Text_Controller(Context c, TextView v, ScrollView s){
        context = c;
        TextViewer = v;
        scrollView = s;
    }

    public void ShowingTxt(String txt) {
        int sudo_num = txt.indexOf("%sudo ");
        if(sudo_num == 0)
            TextViewer.append("Manager Mode is ON");
        else
            TextViewer.append(txt + "\n");
        sendScroll();
    }

    private void sendScroll(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {Thread.sleep(100);} catch (InterruptedException e) {}
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        }).start();
    }

    public void ShowingToast(String txt) {
        Toast.makeText(context, txt, Toast.LENGTH_SHORT).show();
    }

    public void Drawing_point_from_TextViewer(){
        String str =  TextViewer.getText().toString();
        String new_str = str.replaceAll(" ",""); //remove all space

        String lines[] = new_str.split("\r?\n");
        Log.d("USER LOG","The number of reading lines " + lines.length);
        for(int i = 0; i<lines.length; i++){
            ReadingPos(lines[i]);
        }
    }

    private void ReadingPos(String txt){
        int tx_start,tx_end,ty_start,ty_end,pos_x, pos_y;
        tx_start = txt.indexOf("[");
        tx_end = txt.indexOf("]");
        ty_start = txt.indexOf("[",tx_end);
        ty_end = txt.indexOf("]",tx_end+1);

        if(tx_start != -1 && ty_start != -1&& tx_end!= -1 && ty_end!= -1){
            String txt_x_pos = txt.substring(tx_start+1,tx_end);
            String txt_y_pos = txt.substring(ty_start+1,ty_end);
            Log.d("USER LOG","X= "+txt_x_pos + "| Y = " + txt_y_pos);
            int x_pos = Integer.parseInt(txt_x_pos);
            int y_pos = Integer.parseInt(txt_y_pos);
            ((MainActivity)context).myDrawing.pointlist_ADD(x_pos,y_pos);
            ((MainActivity)context).myDrawing.pointlist_Drawing();
        }
    }
}
