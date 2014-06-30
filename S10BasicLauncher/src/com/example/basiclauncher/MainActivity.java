package com.example.basiclauncher;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

public class MainActivity extends Activity {
	DrawerAdapter drawerAdapterObject;
	GridView drawerGrid;
	SlidingDrawer slidingDrawer;
	RelativeLayout homeView;
	class Pac{
		Drawable icon;
		String name;
		String packageName;
		String label;
	}
	Pac[] pacs;
	PackageManager pm;
	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;
	int REQUEST_CREATE_APPWIDGET = 900;
	
	static boolean appLaunchable = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new AppWidgetHost(this, R.id.APPWIDGET_HOST_ID);
		
		drawerGrid = (GridView) findViewById(R.id.content);
		slidingDrawer = (SlidingDrawer) findViewById(R.id.drawer);
		homeView = (RelativeLayout) findViewById(R.id.home_view);
		pm =getPackageManager();
		set_pacs();
		slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			
			@Override
			public void onDrawerOpened() {
				appLaunchable=true;
			}
		});
		
		homeView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				selectWidget();
				return false;
			}
		});
		
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(new PacReceiver(), filter);
	}
	
	void selectWidget() {
	    int appWidgetId = this.mAppWidgetHost.allocateAppWidgetId();
	    Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
	    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	    addEmptyData(pickIntent);
	    startActivityForResult(pickIntent, R.id.REQUEST_PICK_APPWIDGET);
	}
	
	void addEmptyData(Intent pickIntent) {
	    ArrayList customInfo = new ArrayList();
	    pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo);
	    ArrayList customExtras = new ArrayList();
	    pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras);
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_OK ) {
	        if (requestCode == R.id.REQUEST_PICK_APPWIDGET) {
	            configureWidget(data);
	        }
	        else if (requestCode == REQUEST_CREATE_APPWIDGET) {
	            createWidget(data);
	        }
	    }
	    else if (resultCode == RESULT_CANCELED && data != null) {
	        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	        if (appWidgetId != -1) {
	            mAppWidgetHost.deleteAppWidgetId(appWidgetId);
	        }
	    }
	}
	
	private void configureWidget(Intent data) {
	    Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
	    if (appWidgetInfo.configure != null) {
	        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
	        intent.setComponent(appWidgetInfo.configure);
	        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	        startActivityForResult(intent, REQUEST_CREATE_APPWIDGET);
	    } else {
	        createWidget(data);
	    }
	}
	
	public void createWidget(Intent data) {
	    Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
	    AppWidgetHostView hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
	    hostView.setAppWidget(appWidgetId, appWidgetInfo);
	    homeView.addView(hostView);
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    mAppWidgetHost.startListening();
	}
	@Override
	protected void onStop() {
	    super.onStop();
	    mAppWidgetHost.stopListening();
	}
	
	
	
	
	public void set_pacs(){
		Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> pacsList = pm.queryIntentActivities(mainIntent, 0);
		pacs = new Pac[pacsList.size()];
		for(int I=0;I<pacsList.size();I++){
			pacs[I]= new Pac();
			pacs[I].icon=pacsList.get(I).loadIcon(pm);
			pacs[I].packageName=pacsList.get(I).activityInfo.packageName;
			pacs[I].name=pacsList.get(I).activityInfo.name;
			pacs[I].label=pacsList.get(I).loadLabel(pm).toString();
		}
		new SortApps().exchange_sort(pacs);
		drawerAdapterObject = new DrawerAdapter(this, pacs);
		drawerGrid.setAdapter(drawerAdapterObject);
		drawerGrid.setOnItemClickListener(new DrawerClickListener(this, pacs, pm));
		drawerGrid.setOnItemLongClickListener(new DrawerLongClickListener(this, slidingDrawer, homeView,pacs));
	}
	
	public class PacReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			set_pacs();
		}
		
	}

}
