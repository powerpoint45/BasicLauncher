package com.example.basiclauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class AppClickListener implements OnClickListener {
	Context mContext;
	
	public AppClickListener(Context ctxt){
		mContext = ctxt;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Pac data;
		data= (Pac) v.getTag();
		
		Intent launchIntent = new Intent(Intent.ACTION_MAIN);
		launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		ComponentName cp = new ComponentName(data.packageName,data.name);
		launchIntent.setComponent(cp);
		mContext.startActivity(launchIntent);
		
	}

}
