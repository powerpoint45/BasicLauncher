package com.example.basiclauncher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

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
	
	public void deleteIcon(){
		if (iconLocation!=null)
			new File(iconLocation).delete();
	}
	
}

