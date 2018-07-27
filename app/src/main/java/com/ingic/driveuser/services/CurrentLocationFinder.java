package com.ingic.driveuser.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UpdatedLocationEnt;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.BasePreferenceHelper;
import com.ingic.driveuser.retrofit.WebService;
import com.ingic.driveuser.retrofit.WebServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created on 7/21/2017.
 */

public class CurrentLocationFinder extends Service {
    private static final String TAG = "BroadcastService";
    private final Handler handler = new Handler();
    Intent intent;
    private static double changePositionBy = 0.00005;
    private WebService webService;
    private BasePreferenceHelper preferenceHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"created");
        intent = new Intent(AppConstants.LOCATION_RECIEVED);
        webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptorandheader(CurrentLocationFinder.this,
                WebServiceConstants.SERVICE_URL);
        preferenceHelper = new BasePreferenceHelper(CurrentLocationFinder.this);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG,"Started");
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 15000); // 1 second

        return super.onStartCommand(intent, flags, startId);
    }


    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            broadcastUpdateInfo();
            handler.postDelayed(this, 20000); // broadcast in every 10 seconds
        }
    };

    //call your API in this method
    private void broadcastUpdateInfo() {
        Log.d(TAG, "entered DisplayLoggingInfo");

        //increment position(lat,lng) by 0.000005
        changePositionBy = changePositionBy + changePositionBy;
        Call<ResponseWrapper<UpdatedLocationEnt>>call = webService.getUpdatedLocation(preferenceHelper.getDriverId());
        call.enqueue(new Callback<ResponseWrapper<UpdatedLocationEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UpdatedLocationEnt>> call, Response<ResponseWrapper<UpdatedLocationEnt>> response) {
                if (response!=null&&response.body()!=null&&response.body().getResponse()!=null&&response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)){
                    intent.putExtra("lat",response.body().getResult().getLatitude()+"");
                    intent.putExtra("lon",response.body().getResult().getLongitude()+"");
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UpdatedLocationEnt>> call, Throwable t) {
            Log.e(TAG,t.toString());
            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"Destroyed");
        handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
    }
}


