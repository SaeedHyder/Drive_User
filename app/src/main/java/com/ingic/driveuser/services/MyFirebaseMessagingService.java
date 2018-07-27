package com.ingic.driveuser.services;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.MainActivity;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.helpers.BasePreferenceHelper;
import com.ingic.driveuser.helpers.NotificationHelper;
import com.ingic.driveuser.retrofit.WebService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private WebService webservice;
    private BasePreferenceHelper preferenceHelper;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferenceHelper = new BasePreferenceHelper(getApplicationContext());
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            buildNotification(remoteMessage);


        }
    }

    private void buildNotification(RemoteMessage messageBody) {
        String title = getString(R.string.app_name);
        String message = messageBody.getData().get("message");
        String rideID = messageBody.getData().get("ride_id");
        String Type = messageBody.getData().get("type");
        if (Type!=null&& Type.equals(AppConstants.PUSH_END_TRIP_TYPE)){
            preferenceHelper.setRideInSession(false);
            preferenceHelper.removeRideSessionPreferences();
            preferenceHelper.settripStatus(false);
        }
        if (Type!=null&& Type.equals(AppConstants.PUSH_START_TRIP)){
           preferenceHelper.settripStatus(true);
        }
        Log.e(TAG, "message: " + message);
        Intent resultIntent = new Intent(MyFirebaseMessagingService.this, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        resultIntent.putExtra("message", message);
        resultIntent.putExtra("tapped", true);
        resultIntent.putExtra("rideID", rideID);
        resultIntent.putExtra("pushtype", Type);
        Intent pushNotification = new Intent(AppConstants.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("rideID", rideID);
        pushNotification.putExtra("pushtype", Type);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(pushNotification);
        if (Type!=null&&!Type.equals(AppConstants.PUSH_START_TRIP))
        showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);
        else {
            showNotificationMessage(MyFirebaseMessagingService.this, title, message, "", resultIntent);
        }
    }

    private void SendNotification(int count, JSONObject json) {

    }

    /**
     * Showing drive_notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        NotificationHelper.getInstance().showNotification(context,
                R.drawable.android_icon,
                title,
                message,
                timeStamp,
                intent);
    }


}
