package com.example.basiclauncher;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SlidingDrawer;
import android.widget.TextView;

public class DrawerLongClickListener implements OnItemLongClickListener {
	
	Context mContext;
	SlidingDrawer drawerForAdapter;
	RelativeLayout homeViewForAdapter;
	Pac[] pacsForListener;
	
	public DrawerLongClickListener(Context ctxt, SlidingDrawer slidingDrawer, RelativeLayout homeView,Pac[] pacs ){
		mContext = ctxt;
		drawerForAdapter = slidingDrawer;
		homeViewForAdapter =homeView;
		pacsForListener = pacs;
	}
	
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View item, int pos,
			long arg3) {
		MainActivity.appLaunchable=false;

		AppSerializableData objectData = SerializationTools.loadSerializedData();
		if (objectData == null)
			objectData = new AppSerializableData();
		
		if (objectData.apps == null)
			objectData.apps = new ArrayList<Pac>();
		
		Pac pacToAdd = pacsForListener[pos];
		pacToAdd.x = (int) item.getX();
		pacToAdd.y = (int) item.getY();
		if (MainActivity.activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			pacToAdd.lanscape = true;
		else
			pacToAdd.lanscape = false;
		
		pacToAdd.cacheIcon();
		objectData.apps.add(pacToAdd);
		SerializationTools.serializeData(objectData);
		
		pacToAdd.addToHome(mContext, homeViewForAdapter);
		drawerForAdapter.animateClose();
		drawerForAdapter.bringToFront();
		return false;
	}

}
