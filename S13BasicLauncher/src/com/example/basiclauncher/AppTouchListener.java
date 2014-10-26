package com.example.basiclauncher;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout.LayoutParams;

public class AppTouchListener implements OnTouchListener {

	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()){
		case MotionEvent.ACTION_MOVE:
			LayoutParams lp = new LayoutParams(v.getWidth(),v.getHeight());
			lp.leftMargin = (int) event.getRawX()-v.getWidth()/2;
			lp.topMargin = (int) event.getRawY()-v.getHeight()/2;
			v.setLayoutParams(lp);
			break;
		case MotionEvent.ACTION_UP:
			v.setOnTouchListener(null);
			break;
		
		}
		return true;
	}

}
