package com.njjd.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.njjd.walnuts.R;

/**
 * Created by mrwim on 17/8/8.
 */

public class PopupView extends PopupWindow {
    private View mPopupView;
    private TextView mTextView;
    private CountTimer countTimer;

    public PopupView(Context context, String text, int keep_alive) {
        super(context);
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPopupView = inflater.inflate(R.layout.popupwindow_message, null);
        mTextView = (TextView)mPopupView.findViewById(R.id.popupwindow_messgae_text);
        mTextView.setText(text);
        this.setContentView(mPopupView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
        this.showAsDropDown(mPopupView,0,0);
        countTimer = new CountTimer(keep_alive*1000 + 1000, 500);
        countTimer.start();
//        mPopupView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = mPopupView.findViewById(R.id.popupwindow_messgae_id).getTop();
//                int y=(int) event.getY();
//                if(event.getAction()== MotionEvent.ACTION_UP){
//                    if(y<height){
//                        dismiss();
//                        countTimer.cancel();
//                    }
//                }
//                return true;
//            }
//        });
    }
    public void setText(String text){
        mTextView.setText(text);
    }
    public void cancelTimer(){
        countTimer.cancel();
    }
    private class CountTimer extends CountDownTimer {
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }
        @Override
        public void onFinish() {
            cancel();
        }
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished < 1000 && PopupView.this.isShowing()) {
                PopupView.this.dismiss();
            }
        }
    }
}