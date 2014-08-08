package com.example.basiclauncher;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

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
	LauncherAppWidgetHost mAppWidgetHost;
	int REQUEST_CREATE_APPWIDGET = 900;
	int REQUEST_CREATE_SHORTCUT  = 700;
	int numWidgets;
	
	static boolean appLaunchable = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mAppWidgetManager = AppWidgetManager.getInstance(this);
		mAppWidgetHost = new LauncherAppWidgetHost(this, R.id.APPWIDGET_HOST_ID);
		
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
				
				AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
				
				String [] items = {getResources().getString(R.string.widget), getResources().getString(R.string.shortcut)};
				b.setItems(items, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						switch(which){
						case 0:
							selectWidget();
							break;
							
						case 1:
							selectShortcut();
							break;
						}
					}
				});
				AlertDialog d = b.create();
				d.show();
				
				
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
	
	void selectShortcut(){
		Intent intent = new Intent(Intent.ACTION_PICK_ACTIVITY);
		intent.putExtra(Intent.EXTRA_INTENT, new Intent(Intent.ACTION_CREATE_SHORTCUT));
		startActivityForResult(intent, R.id.REQUEST_PICK_SHORTCUT);
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
	        else if (requestCode == R.id.REQUEST_PICK_SHORTCUT){
	        	configureShortcut(data);
	        }
	        else if (requestCode == REQUEST_CREATE_SHORTCUT){
	        	createShortcut(data);
	        }
	        
	    }
	    else if (resultCode == RESULT_CANCELED && data != null) {
	        int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	        if (appWidgetId != -1) {
	            mAppWidgetHost.deleteAppWidgetId(appWidgetId);
	        }
	    }
	}
	
	void configureShortcut(Intent data){
		startActivityForResult(data, REQUEST_CREATE_SHORTCUT);
	}
	
	public void createShortcut(Intent intent){
		Intent.ShortcutIconResource iconResource = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);
		Bitmap icon                              = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
		String shortcutLabel                     = intent.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
		Intent shortIntent                       = intent.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
		
		if (icon==null){
			if (iconResource!=null){
				Resources resources =null;
				try {
					resources = pm.getResourcesForApplication(iconResource.packageName);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				} 
				if (resources != null) {
				    int id = resources.getIdentifier(iconResource.resourceName, null, null); 
				    if(resources.getDrawable(id) instanceof StateListDrawable) {
				    	Drawable d = ((StateListDrawable)resources.getDrawable(id)).getCurrent();
				    	icon = ((BitmapDrawable)d).getBitmap();
				    }else
				    	icon = ((BitmapDrawable)resources.getDrawable(id)).getBitmap();
				}
			}
		}
		

		if (shortcutLabel!=null && shortIntent!=null && icon!=null){
			LayoutParams lp = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.leftMargin = 100;
			lp.topMargin = (int) 100;
				
			LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LinearLayout ll = (LinearLayout) li.inflate(R.layout.drawer_item, null);
				
			((ImageView)ll.findViewById(R.id.icon_image)).setImageBitmap(icon);
			((TextView)ll.findViewById(R.id.icon_text)).setText(shortcutLabel);
				
			ll.setOnLongClickListener(new OnLongClickListener() {
					
				@Override
				public boolean onLongClick(View v) {
					v.setOnTouchListener(new AppTouchListener());
					return false;
				}
			});
				
			ll.setOnClickListener(new ShortcutClickListener(this));
			ll.setTag(shortIntent);
			homeView.addView(ll, lp);
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
	    LauncherAppWidgetHostView hostView = (LauncherAppWidgetHostView) mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo);
	    hostView.setAppWidget(appWidgetId, appWidgetInfo);
	    
	    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(homeView.getWidth()/3, homeView.getHeight()/3);
	    lp.leftMargin = numWidgets * (homeView.getWidth()/3);
	    
	    hostView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				System.out.println("LONG PRESSED WIDGET");
				v.setBackgroundColor(Color.RED);
				return false;
			}
		});
	    
	    homeView.addView(hostView,lp);
	    slidingDrawer.bringToFront();
	    numWidgets ++;
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
