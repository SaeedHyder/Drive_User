package com.ingic.driveuser.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.ingic.driveuser.activities.MainActivity;
import com.ingic.driveuser.entities.RegistrationEnt;
import com.ingic.driveuser.entities.SelectCarEnt;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.entities.UserHomeEnt;
import com.ingic.driveuser.retrofit.GsonFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BasePreferenceHelper extends PreferenceHelper {

    private Context context;

    protected static final String KEY_LOGIN_STATUS = "islogin";
    protected static final String KEY_Term_STATUS = "isAgree";
    private static final String FILENAME = "preferences";
    protected static final String Firebase_TOKEN = "Firebasetoken";
    protected static final String TOKEN = "TOKEN";
    protected static final String USERNAME = "userName";
    protected static final String USERID = "userId";
    protected static final String KEY_USER = "key_user";
    protected static final String KEY_REGISTER = "KEY_REGISTER";
    protected static final String KEY_HOME = "key_home";
    protected static final String KEY_CARTYPES = "KEY_CARTYPES";
    protected static final String DRIVERID = "driverId";
    protected static final String PeakFactor = "PeakFactor";
    protected static final String RIDEINSESSION = "rideinsession";
    protected static final String ScheduleRide = "ScheduleRide";
    protected static final String SCHEDULERIDE = "SCHEDULERIDE";
    protected static final String Is_TRIP_START = "Is_TRIP_START";
    protected static final String Latitude = "Latitude";
    protected static final String Longitutde = "Longitutde";
    protected static final String paymentType = "paymentType";
    protected static final String isFromBottomSheet = "isFromBottomSheet";
    protected static final String isFromBottomSheetLater = "isFromBottomSheetLater";
    protected static final String KEY_DEFAULT_LANG = "keyLanguage";


    public BasePreferenceHelper(Context c) {
        this.context = c;
    }

    public SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean isLogin) {
        putBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS, isLogin);
    }

    public boolean isLogin() {
        return getBooleanPreference(context, FILENAME, KEY_LOGIN_STATUS);
    }

    public void settripStatus(boolean isStarted) {
        putBooleanPreference(context, FILENAME, Is_TRIP_START, isStarted);
    }

    public boolean isStarted() {
        return getBooleanPreference(context, FILENAME, Is_TRIP_START);
    }

    public void setTermStatus(boolean isAgree) {
        putBooleanPreference(context, FILENAME, KEY_Term_STATUS, isAgree);
    }

    public boolean isTermAccepted() {
        return getBooleanPreference(context, FILENAME, KEY_Term_STATUS);
    }

    public void setDriverId(String driverId) {
        putStringPreference(context, FILENAME, DRIVERID, driverId);
    }

    public String getDriverId() {
        return getStringPreference(context, FILENAME, DRIVERID);
    }

    public String getFirebase_TOKEN() {
        return getStringPreference(context, FILENAME, Firebase_TOKEN);
    }

    public void setFirebase_TOKEN(String _token) {
        putStringPreference(context, FILENAME, Firebase_TOKEN, _token);
    }


    public void setUsrName(String _token) {
        putStringPreference(context, FILENAME, USERNAME, _token);
    }

    public String getUserName() {
        return getStringPreference(context, FILENAME, USERNAME);
    }

    public void setUsrId(String userId) {
        putStringPreference(context, FILENAME, USERID, userId);
    }

    public String getUserId() {
        return getStringPreference(context, FILENAME, USERID);
    }

    public UserEnt getUser() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_USER), UserEnt.class);
    }

    public void putUser(UserEnt user) {
        putStringPreference(context, FILENAME, KEY_USER, GsonFactory
                .getConfiguredGson().toJson(user));
    }

    public ArrayList<SelectCarEnt> getCarTypes() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_CARTYPES), new TypeToken<List<SelectCarEnt>>() {
                }.getType());
    }

    public void putCarTypes(ArrayList<SelectCarEnt> CarTypes) {
        putStringPreference(context, FILENAME, KEY_CARTYPES, GsonFactory
                .getConfiguredGson().toJson(CarTypes));
    }

    public UserHomeEnt getUserHome() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_HOME), UserHomeEnt.class);
    }

    public void putUserHome(UserHomeEnt user) {
        putStringPreference(context, FILENAME, KEY_HOME, GsonFactory.getConfiguredGson().toJson(user));
    }

    public void setRideInSession(Boolean RideInSession) {
        putBooleanPreference(context, FILENAME, RIDEINSESSION, RideInSession);
    }

    public Boolean getRideInSession() {
        return getBooleanPreference(context, FILENAME, RIDEINSESSION);
    }


    public void setIsScheduleRide(Boolean IsScheduleRIde) {
        putBooleanPreference(context, FILENAME, SCHEDULERIDE, IsScheduleRIde);
    }

    public Boolean geIsScheduleRide() {
        return getBooleanPreference(context, FILENAME, SCHEDULERIDE);
    }

    public void removeRideSessionPreferences() {
        removePreference(context, FILENAME, KEY_HOME);

    }

    public RegistrationEnt getRegistrationData() {
        return GsonFactory.getConfiguredGson().fromJson(
                getStringPreference(context, FILENAME, KEY_REGISTER), RegistrationEnt.class);
    }

    public void putRegistrationData(RegistrationEnt user) {
        putStringPreference(context, FILENAME, KEY_REGISTER, GsonFactory
                .getConfiguredGson().toJson(user));
    }


    public String get_TOKEN() {
        return getStringPreference(context, FILENAME, TOKEN);
    }

    public void set_TOKEN(String token) {
        putStringPreference(context, FILENAME, TOKEN, token);
    }

    public void setPeakFactor(String peakFactor) {
        putStringPreference(context, FILENAME, PeakFactor, peakFactor);
    }

    public String getPeakFactor() {
        return getStringPreference(context, FILENAME, PeakFactor);
    }

    public void set_current_latitude(String lat) {
        putStringPreference(context, FILENAME, Latitude, lat);
    }

    public String get_latitude() {
        return getStringPreference(context, FILENAME, Latitude);
    }


    public void set_current_longitute(String lng) {
        putStringPreference(context, FILENAME, Longitutde, lng);
    }

    public String get_longitude() {
        return getStringPreference(context, FILENAME, Longitutde);
    }

    public void setPaymentType(String type) {
        putStringPreference(context, FILENAME, paymentType, type);
    }

    public String getPaymentType() {
        return getStringPreference(context, FILENAME, paymentType);
    }

    public void isFromBottomSheet(boolean key) {
        putBooleanPreference(context, FILENAME, isFromBottomSheet, key);
    }

    public Boolean getisFromBottomSheet() {
        return getBooleanPreference(context, FILENAME, isFromBottomSheet);
    }

    public void isFromBottomSheetLater(boolean key) {
        putBooleanPreference(context, FILENAME, isFromBottomSheetLater, key);
    }

    public Boolean getisFromBottomSheetLater() {
        return getBooleanPreference(context, FILENAME, isFromBottomSheetLater);
    }

    public void putLang(Activity activity, String lang) {
        Log.v("lang", "|" + lang);
        Resources resources = context.getResources();

        if (lang.equals("ar")){
            lang = "ar";}
        else{
            lang = "en";}

        putStringPreference(context, FILENAME, KEY_DEFAULT_LANG, lang);
        //Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
//        conf.setLocale(new Locale(lang));
        conf.setLayoutDirection(new Locale(lang));
        conf.locale = new Locale(lang);
        resources.updateConfiguration(conf, dm);
        ((MainActivity) activity).restartActivity();
    }


    public String getLang() {
        return getStringPreference(context, FILENAME, KEY_DEFAULT_LANG);
    }

    public boolean isLanguageArabic() {
        return getLang().equalsIgnoreCase("ar");
    }
}
