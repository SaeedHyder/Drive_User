package com.ingic.driveuser;

import android.app.Application;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import io.fabric.sdk.android.Fabric;

public class BaseApplication extends Application {

	private static Bus bus;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MultiDex.install(this);
		Fabric.with(this, new Crashlytics());
		initImageLoader();


		bus = new Bus(ThreadEnforcer.MAIN);
	}

	public static Bus getBus(){
		return bus;
	}
	
	public void initImageLoader() {
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri( R.color.black )
				.showImageOnFail( R.color.black ).resetViewBeforeLoading( true )
				.cacheInMemory( true ).cacheOnDisc( true )
				
				.imageScaleType( ImageScaleType.IN_SAMPLE_POWER_OF_2 )
				.displayer( new FadeInBitmapDisplayer( 850 ) )
				.bitmapConfig( Bitmap.Config.RGB_565 ).build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext() ).defaultDisplayImageOptions( options )
				.build();
		
		ImageLoader.getInstance().init( config );
		L.disableLogging();
	}
	
}
