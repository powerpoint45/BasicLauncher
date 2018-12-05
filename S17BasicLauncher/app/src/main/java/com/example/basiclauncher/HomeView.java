package com.example.basiclauncher;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class HomeView extends RelativeLayout {

    public HomeView(Context context) {
        super(context);
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showTrash(){
        findViewById(R.id.trash).setVisibility(VISIBLE);
    }

    public void hideTrash(){
        findViewById(R.id.trash).setVisibility(GONE);
    }

    public boolean isViewTouchingTrash(View v){
        RectF trashRect = Tools.viewToRect(findViewById(R.id.trash));
        RectF vRect = Tools.viewToRect(v);

        return trashRect.intersect(vRect);
    }


}
