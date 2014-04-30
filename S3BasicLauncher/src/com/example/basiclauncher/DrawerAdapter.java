package com.example.basiclauncher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class DrawerAdapter extends BaseAdapter{
	Context mContext;
	MainActivity.Pac[] pacsForAdapter;
	
	public DrawerAdapter (Context c, MainActivity.Pac pacs[]){
		mContext =c;
		pacsForAdapter = pacs;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pacsForAdapter.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ImageView imageView = new ImageView(mContext);
		imageView.setImageDrawable(pacsForAdapter[pos].icon);
		imageView.setLayoutParams(new GridView.LayoutParams(65,65));
		imageView.setPadding(3, 3, 3, 3);
		return imageView;
	}

}
