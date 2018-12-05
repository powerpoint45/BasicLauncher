package com.example.basiclauncher;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout.LayoutParams;

public class AppTouchListener implements OnTouchListener {

	int leftMargine;
	int topMargine;

	public final static int TYPE_APP =0;
	public final static int TYPE_SHORTCUT = 1;

	int appType;
	String UUIDIdentifyer;

	public AppTouchListener(int type, String UUIDIdentifyer){
		this.appType = type;
		this.UUIDIdentifyer = UUIDIdentifyer;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()){
		case MotionEvent.ACTION_MOVE:
			LayoutParams lp = new LayoutParams(v.getWidth(),v.getHeight());
			
			leftMargine = (int) event.getRawX()-v.getWidth()/2;
			topMargine = (int) event.getRawY()-v.getHeight()/2;
			
			if (leftMargine+v.getWidth() > v.getRootView().getWidth())
				leftMargine = v.getRootView().getWidth() - v.getWidth();
			
			if (leftMargine<0)
				leftMargine =0;
			
			if (topMargine+v.getHeight() >((View) v.getParent()).getHeight())
				topMargine = ((View) v.getParent()).getHeight() - v.getHeight();
			
			if (topMargine<0)
				topMargine = 0;
			
			
			lp.leftMargin = leftMargine;
			lp.topMargin = topMargine;
			v.setLayoutParams(lp);
			break;
		case MotionEvent.ACTION_UP:

			HomeView homeView = ((HomeView)v.getParent());

			v.setOnTouchListener(null);
			homeView.hideTrash();

			boolean shouldDeleteItem = homeView.isViewTouchingTrash(v);

			switch (appType){
				case TYPE_APP:
					AppSerializableData appData = SerializationTools.loadSerializedData();
					if (appData!=null){
						Pac p = appData.findPac(UUIDIdentifyer);
						if (p!=null){

						    if (shouldDeleteItem){
						        appData.apps.remove(p);
						        homeView.removeView(v);
                            }else {
                                p.x = leftMargine;
                                p.y = topMargine;
                            }
						}
					}
					SerializationTools.serializeData(appData);
					break;

				case TYPE_SHORTCUT:
					ShortcutSerializableData shortData = SerializationTools.loadSerializedShortcutData();
					if (shortData!=null){
						ShortcutPac p = shortData.findPac(UUIDIdentifyer);
						if (p!=null){
                            if (shouldDeleteItem){
                                shortData.apps.remove(p);
                                homeView.removeView(v);
                            }else {
                                p.x = leftMargine;
                                p.y = topMargine;
                            }
						}
					}
					SerializationTools.serializeShortcutData(shortData);
					break;
			}


			break;
		
		}
		return true;
	}

}
