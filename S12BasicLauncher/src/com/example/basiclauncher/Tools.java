package com.example.basiclauncher;

import android.app.Activity;
import android.util.TypedValue;

public class Tools {
	public static int numtodp(int in, Activity activity){
		int out = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, in, activity.getResources().getDisplayMetrics());
		return out;
	}
}
