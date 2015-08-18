package com.example.basiclauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class Pac implements Serializable{
	private static final long serialVersionUID = 584968759160131712L;
	transient Drawable icon;
	String name;
	String packageName;
	String label;
	int x , y;
	String iconLocation;
	boolean lanscape;

	public void cacheIcon(){
		if (iconLocation==null)
			new File(MainActivity.activity.getApplicationInfo().dataDir+"/cachedApps/").mkdirs();

		if (icon!=null){
			
			iconLocation = MainActivity.activity.getApplicationInfo().dataDir+"/cachedApps/" +packageName + name;
			FileOutputStream fos = null;
			try{
				fos = new FileOutputStream(iconLocation);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			
			if (fos!=null){
				Tools.drawableToBitmap(icon).compress(Bitmap.CompressFormat.PNG, 100, fos);
				try{
					fos.flush();
					fos.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}else
				iconLocation = null;
		}
		
	}
	
	public Bitmap getCachedIcon(){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;
		options.inPreferredConfig = Config.ARGB_8888;
		options.inDither = true;
		
		if (iconLocation !=null){
			File cachedIcon = new File(iconLocation);
			if (cachedIcon.exists()){
				return BitmapFactory.decodeFile(cachedIcon.getAbsolutePath(), options);
			}
		}
		
		return null;
	}
	
	public void addToHome(Context mContext, RelativeLayout homeViewForAdapter){
		LayoutParams lp = new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT,android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;
		
		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) li.inflate(R.layout.drawer_item, null);
		
		if (icon == null){
			icon = new BitmapDrawable(mContext.getResources(),getCachedIcon());
		}
		
		((ImageView)ll.findViewById(R.id.icon_image)).setImageDrawable(icon);
		
		((TextView)ll.findViewById(R.id.icon_text)).setText(label);
		
		ll.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				v.setOnTouchListener(new AppTouchListener());
				return false;
			}
		});
		
		ll.setTag(this);
		ll.setOnClickListener(new AppClickListener(mContext));
		homeViewForAdapter.addView(ll,0, lp);
	}
	
	public void deleteIcon(){
		if (iconLocation!=null)
			new File(iconLocation).delete();
	}
	
}
