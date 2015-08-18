package com.example.basiclauncher;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;

public class ThemeTools {
	
	public Pac[] getAllThemes(PackageManager pm) {
		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory("com.anddoes.launcher.THEME");
	    List<ResolveInfo> packs = pm.queryIntentActivities( mainIntent, 0);
	    Pac[] pacs = new Pac[packs.size()];
	    for(int I=0;I<packs.size();I++) {
	    	pacs[I]= new Pac();
	    	pacs[I].icon=packs.get(I).loadIcon(pm);
	    	pacs[I].packageName= packs.get(I).activityInfo.packageName;
	    	pacs[I].name=packs.get(I).activityInfo.name;
	    	pacs[I].label=packs.get(I).loadLabel(pm).toString();			
	    }
		return pacs;
    }

	public static float getScaleFactor(Resources res,String string){
		float scaleFactor=1.0f;
		XmlResourceParser xrp = null;
		XmlPullParser xpp=null;
			try{
			int n;
	        if ((n = res.getIdentifier("appfilter", "xml", string)) != 0) {
	        	xrp = res.getXml(n);
	            System.out.println(n);
	        } else {
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setValidating(false);
	            xpp = factory.newPullParser();
	            InputStream raw = res.getAssets().open("appfilter.xml");
	            xpp.setInput(raw, null);
	        }
	     
	        if (n!=0){
		        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT && scaleFactor==1.0f) {    
		                if (xrp.getEventType()==2){
		                	try{
		                	String s = xrp.getName();
			                if (s.equals("scale")) {
			                	scaleFactor = Float.parseFloat(xrp.getAttributeValue(0));
			                }
		                	}catch(Exception e){}
		                }
		            xrp.next();
		        }
	        }
	        else{
	        	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && scaleFactor==1.0f) {    
	                if (xpp.getEventType()==2){
	                	try{
	                	String s = xpp.getName();
		                if (s.equals("scale")) {
		                	scaleFactor = Float.parseFloat(xpp.getAttributeValue(0));
		                }
	                	}catch(Exception e){}
	                }
	            xpp.next();
	        }
	        }
		}catch(Exception e){
			System.out.println(e);
		}
		return scaleFactor;
	}
	
	
	public static String getResourceName(Resources res,String string,String componentInfo){
		String resource = null;
		XmlResourceParser xrp = null;
		XmlPullParser xpp=null;
			try{
			int n;
	        if ((n = res.getIdentifier("appfilter", "xml", string)) != 0) {
	        	xrp = res.getXml(n);
	        } else {
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setValidating(false);
	            xpp = factory.newPullParser();
	            InputStream raw = res.getAssets().open("appfilter.xml");
	            xpp.setInput(raw, null);
	        }
	     
	        if (n!=0){
		        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT && resource==null) {    
		                if (xrp.getEventType()==2){
		                	try{
		                	String s = xrp.getName();
			                if (s.equals("item")) {
			                	if (xrp.getAttributeValue(0).compareTo(componentInfo)==0){
				                	resource = xrp.getAttributeValue(1);
			                	}
			                }
		                	}catch(Exception e){}
		                }
		            xrp.next();
		        }
	        }
	        else{
	        	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && resource==null) {    
	                if (xpp.getEventType()==2){
	                	try{
	                	String s = xpp.getName();
	                	if (s.equals("item")) {
		                	if (xpp.getAttributeValue(0).compareTo(componentInfo)==0){
			                	resource = xpp.getAttributeValue(1);
		                	}
		                }
	                	}catch(Exception e){}
	                }
	            xpp.next();
	        }
	        }
		}catch(Exception e){
			System.out.println(e);
		}
		return resource;
	}
	
	
	
	public static String [] getIconBackAndMaskResourceName(Resources res,String packageName){
		String[] resource = new String[3];
		XmlResourceParser xrp = null;
		XmlPullParser xpp=null;
			try{
			int n;
	        if ((n = res.getIdentifier("appfilter", "xml", packageName)) != 0) {
	        	xrp = res.getXml(n);
	        } else {
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	            factory.setValidating(false);
	            xpp = factory.newPullParser();
	            InputStream raw = res.getAssets().open("appfilter.xml");
	            xpp.setInput(raw, null);
	        }
	     
	        if (n!=0){
		        while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT && (resource[0]==null || resource[1]==null || resource[2]==null)) {    
		                if (xrp.getEventType()==2){
		                	try{
		                	String s = xrp.getName();
			                if (s.equals("iconback")) {
				                resource[0] = xrp.getAttributeValue(0);
			                }
			                if (s.equals("iconmask")) {
			                	resource[1] = xrp.getAttributeValue(0);
			                }
			                if (s.equals("iconupon")) {
			                	resource[2] = xrp.getAttributeValue(0);
			                }
		                	}catch(Exception e){}
		                }
		            xrp.next();
		        }
	        }
	        else{
	        	while (xpp.getEventType() != XmlPullParser.END_DOCUMENT && (resource[0]==null || resource[1]==null || resource[2]==null)) {    
	                if (xpp.getEventType()==2){
	                	try{
	                	String s = xpp.getName();
	                	if (s.equals("iconback")) {
			                resource[0] = xpp.getAttributeValue(0);
		                }
	                	if (s.equals("iconmask")) {
		                	resource[1] = xpp.getAttributeValue(0);
	                	}
		                if (s.equals("iconupon")) {
		                	resource[2] = xpp.getAttributeValue(0);
		                }
	                	}catch(Exception e){}
	                }
	            xpp.next();
	        }
	        }
		}catch(Exception e){
			System.out.println(e);
		}
		return resource;
	}
}