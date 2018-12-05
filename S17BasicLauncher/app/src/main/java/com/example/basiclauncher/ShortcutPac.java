package com.example.basiclauncher;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.UUID;

public class ShortcutPac implements Serializable{
	private static final long serialVersionUID = 584968759160131732L;
	transient Bitmap icon;
	String URI;
	String label;
	int x , y;
	String iconLocation;
	String UUIDIdentifyer; //identifyer
	boolean lanscape;

	public void cacheIcon(){
		if (iconLocation==null)
			new File(MainActivity.activity.getApplicationInfo().dataDir+"/cachedShortcuts/").mkdirs();

		if (icon!=null){
            UUIDIdentifyer = UUID.randomUUID().toString();
			iconLocation = MainActivity.activity.getApplicationInfo().dataDir+"/cachedShortcuts/" + UUIDIdentifyer;
			FileOutputStream fos = null;
			try{
				fos = new FileOutputStream(iconLocation);
			}catch(FileNotFoundException e){
				e.printStackTrace();
			}
			
			if (fos!=null){
				icon.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
	
	public void addToHome(Context mContext, final HomeView homeViewForAdapter){
		LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = x;
		lp.topMargin = y;

		LayoutInflater li = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout ll = (LinearLayout) li.inflate(R.layout.drawer_item, null);

		if (icon == null){
			icon = getCachedIcon();
		}

		((ImageView)ll.findViewById(R.id.icon_image)).setImageBitmap(icon);
		((TextView)ll.findViewById(R.id.icon_text)).setText(label);

		ll.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				homeViewForAdapter.showTrash();
				v.setOnTouchListener(new AppTouchListener(AppTouchListener.TYPE_SHORTCUT,UUIDIdentifyer));
				return false;
			}
		});

		ll.setOnClickListener(new ShortcutClickListener(mContext));
		try {
			ll.setTag(Intent.parseUri(URI,0));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		homeViewForAdapter.addView(ll, lp);
	}
	
	public void deleteIcon(){
		if (iconLocation!=null)
			new File(iconLocation).delete();
	}
	
}
