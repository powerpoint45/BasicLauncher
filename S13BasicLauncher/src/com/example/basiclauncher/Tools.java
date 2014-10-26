package com.example.basiclauncher;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.TypedValue;

public class Tools {
	public static int numtodp(int in, Activity activity){
		int out = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, in, activity.getResources().getDisplayMetrics());
		return out;
	}
	
	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
	    return resizedBitmap;
	}
	
	public static Matrix getResizedMatrix(Bitmap bm, int newHeight, int newWidth)
	{
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    return matrix;
	}
}
