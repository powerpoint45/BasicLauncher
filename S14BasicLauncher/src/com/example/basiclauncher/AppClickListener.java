package com.example.basiclauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class AppClickListener implements OnClickListener {
	Pac[] pacsForListener;
	Context mContext;
	
	public AppClickListener(Pac[] pacs, Context ctxt){
		pacsForListener = pacs;
		mContext = ctxt;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String[] data;
		data= (String[]) v.getTag();
		
		Intent launchIntent = new Intent(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cp = new ComponentName(data[0], data[1]);
		launchIntent.setComponent(cp);
		mContext.startActivity(launchIntent);
		
	}

}

