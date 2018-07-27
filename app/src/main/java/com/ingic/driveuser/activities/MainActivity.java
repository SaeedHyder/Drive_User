package com.ingic.driveuser.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.SelectCarEnt;
import com.ingic.driveuser.fragments.HomeMapFragment;
import com.ingic.driveuser.fragments.LoginFragment;
import com.ingic.driveuser.fragments.SideMenuFragment;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.SideMenuChooser;
import com.ingic.driveuser.global.SideMenuDirection;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.ScreenHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.interfaces.ImageSetter;
import com.ingic.driveuser.interfaces.OnSettingActivateListener;
import com.ingic.driveuser.residemenu.ResideMenu;
import com.ingic.driveuser.retrofit.WebService;
import com.ingic.driveuser.retrofit.WebServiceFactory;
import com.ingic.driveuser.ui.views.TitleBar;
import com.kbeanie.imagechooser.api.ChooserType;
import com.kbeanie.imagechooser.api.ChosenImage;
import com.kbeanie.imagechooser.api.ChosenImages;
import com.kbeanie.imagechooser.api.ImageChooserListener;
import com.kbeanie.imagechooser.api.ImageChooserManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ingic.driveuser.global.AppConstants.Auto_Cancel;
import static com.ingic.driveuser.global.AppConstants.DRIVER_ARRIVED;
import static com.ingic.driveuser.global.AppConstants.KEY_SCHEDULE_CANCEL_RIDE;
import static com.ingic.driveuser.global.AppConstants.PUSH_APPROVE_TYPE;
import static com.ingic.driveuser.global.AppConstants.PUSH_END_TRIP_TYPE;
import static com.ingic.driveuser.global.AppConstants.PUSH_START_TRIP;


public class MainActivity extends DockActivity implements OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ImageChooserListener {
    public final int LocationResultCode = 1;
    public final int WifiResultCode = 2;
    public TitleBar titleBar;
    @BindView(R.id.sideMneuFragmentContainer)
    public FrameLayout sideMneuFragmentContainer;
    @BindView(R.id.header_main)
    TitleBar header_main;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MainActivity mContext;
    private boolean loading;
    private ResideMenu resideMenu;
    private OnSettingActivateListener settingActivateListener;
    private float lastTranslate = 0.0f;
    private ImageChooserManager imageChooserManager;
    private int chooserType;
    private String filePath;
    private String originalFilePath;
    private String thumbnailFilePath;
    private String thumbnailSmallFilePath;
    private boolean isActivityResultOver = false;
    private final static String TAG = "ICA";
    ImageSetter imageSetter;

    private String sideMenuType;
    private String sideMenuDirection;
    private AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        getVehicleTypes();
        titleBar = header_main;
        // setBehindContentView(R.layout.fragment_frame);
        mContext = this;

        setTitleBar();

        Log.i("Screen Density", ScreenHelper.getDensity(this) + "");

        sideMenuType = SideMenuChooser.DRAWER.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();
        settingSideMenu(sideMenuType, sideMenuDirection);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        titleBar.setMenuButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {

                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        UIHelper.hideSoftKeyboard(getDockActivity(), getWindow().getDecorView());
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    UIHelper.hideSoftKeyboard(getApplicationContext(), v);
                    resideMenu.openMenu(sideMenuDirection);
                }


            }
        });

        titleBar.setBackButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    popFragment();
                    UIHelper.hideSoftKeyboard(getApplicationContext(),
                            titleBar);
                }
            }
        });

        if (savedInstanceState == null)
            initFragment(false);

    }

    private void setTitleBar() {
        if (titleBar != null) {
            titleBar.showMenuButton(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.hideSoftKeyboard(getDockActivity(), getWindow().getDecorView());
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
            titleBar.setSubHeading("Home");
        }
    }

    public void getVehicleTypes() {
        WebService webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptorandheader(getDockActivity(), WebServiceConstants.SERVICE_URL);
        Call<ResponseWrapper<ArrayList<SelectCarEnt>>> call = webService.getVehicles();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<SelectCarEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<SelectCarEnt>>> call, Response<ResponseWrapper<ArrayList<SelectCarEnt>>> response) {
                if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putCarTypes(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<SelectCarEnt>>> call, Throwable t) {
                Log.e(LoginFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    public void setImageSetter(ImageSetter imageSetter) {
        this.imageSetter = imageSetter;
    }

    public void setOnSettingActivateListener(OnSettingActivateListener ActivateListener) {
        this.settingActivateListener = ActivateListener;
    }

    public View getDrawerView() {
        return getLayoutInflater().inflate(getSideMenuFrameLayoutId(), null);
    }

    public void removePaddingFromFrame() {
        mainFrameLayout.setPadding(0, 0, 0, 0);
    }

    public void addPaddingFromFrame() {
        mainFrameLayout.setPadding(0, 60, 0, 0);
    }

    public void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }


    public boolean statusCheck() {
        if (isConnected(getApplicationContext())) {
            final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                turnLocationOn(null);
                // buildAlertMessageNoGps(R.string.gps_question, Settings.ACTION_LOCATION_SOURCE_SETTINGS, LocationResultCode);
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps(final int StringResourceID, final String IntentType, final int requestCode) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getDockActivity());
        builder
                .setMessage(getString(StringResourceID))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.gps_yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        if (StringResourceID == R.string.gps_question) {
                            dialog.cancel();
                            turnLocationOn(null);
                            dialog.dismiss();
                        } else {
                            dialog.cancel();
                            startImpIntent(dialog, IntentType, requestCode);
                            dialog.dismiss();
                        }

                        // startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),LocationResultCode);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.gps_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.dismiss();
                        dialog.cancel();

                    }
                });
        if (alert == null) {
            alert = builder.create();

        }

        if (!alert.isShowing())
            alert.show();

    }

    private void startImpIntent(DialogInterface dialog, String IntentType, int requestCode) {
        dialog.dismiss();
        Intent i = new Intent(IntentType);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(i, requestCode);
    }


    /*  private boolean isNetworkAvailable() {
          ConnectivityManager connectivityManager
                  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
          NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
          if (activeNetworkInfo == null ){
              WifiManager wifi = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
              if (!wifi.isWifiEnabled()){
                  buildAlertMessageNoGps(R.string.wifi_question,Settings.ACTION_WIFI_SETTINGS,WifiResultCode);
              return false;
              }
          }else{
              return (activeNetworkInfo.isConnectedOrConnecting());
          }


      }*/

    public boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfoMob = cm.getNetworkInfo(cm.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(cm.TYPE_WIFI);
        if (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) {
            Log.v("TAG", "Mobile Internet connected");
            return true;
        }
        if (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting()) {
            Log.v("TAG", "Wifi Internet connected");
            return true;
        }
        buildAlertMessageNoGps(R.string.wifi_question, Settings.ACTION_WIFI_SETTINGS, WifiResultCode);
        return false;
      /*  ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo drive_mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((drive_mobile != null && drive_mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else {
                //isConnected(getApplicationContext());
                return false;
            }
        } else {
            buildAlertMessageNoGps(R.string.wifi_question, Settings.ACTION_WIFI_SETTINGS, WifiResultCode);
            return false;
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "OnActivityResult");
        Log.i(TAG, "File Path : " + filePath);
        Log.i(TAG, "Chooser Type: " + chooserType);


        if (resultCode == RESULT_OK
                && (requestCode == ChooserType.REQUEST_PICK_PICTURE || requestCode == ChooserType.REQUEST_CAPTURE_PICTURE)) {
            if (imageChooserManager == null) {
                reinitializeImageChooser();
            }
            imageChooserManager.submit(requestCode, data);
        } else {
            // progressBar.setVisibility(View.GONE);
        }


        BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentById(getDockFrameLayoutId());

        if (fragment != null) {
            try {
                fragment.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (requestCode == LocationResultCode) {
            settingActivateListener.onLocationActivateListener();
        }

        if (requestCode == WifiResultCode) {
            settingActivateListener.onNetworkActivateListener();

        }
        if (requestCode == 1000) {
            settingActivateListener.onLocationActivateListener();
        }


    }

    private void settingSideMenu(String type, String direction) {

        if (type.equals(SideMenuChooser.DRAWER.getValue())) {


            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen.x250), (int) DrawerLayout.LayoutParams.MATCH_PARENT);


            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.LEFT;
                sideMneuFragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.RIGHT;
                sideMneuFragmentContainer.setLayoutParams(params);
            }

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

            drawerLayout.closeDrawers();
            lockDrawer();
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.60f);


            setMenuItemDirection(direction);

        }
    }

    public void refreshSideMenu() {

        sideMenuFragment.refreshSideMenuData();

       /* sideMenuFragment = SideMenuFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.remove(sideMenuFragment).commit();

      *//*  resideMenu = new ResideMenu(this);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(getMenuListener());
        resideMenu.setScaleValue(0.52f);
        resideMenu.setPadding(0,0,0,0);
*//*
        setMenuItemDirection(sideMenuDirection);*/
    }

    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {

            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", direction);
            getResideMenu().getLeftMenu().setVisibility(View.GONE);

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);
            getResideMenu().getRightMenu().setVisibility(View.GONE);
        }

    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }

    @Override
    protected void onResume() {
        super.onResume();
        lockDrawer();
    }

    public void initFragment(boolean isRideCancle) {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());
        if (prefHelper.isLogin()) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null && !isRideCancle) {
                String rideID = bundle.getString("rideID");
                String Type = bundle.getString("pushtype");
                // if (!isFragmentVisible(HomeFragment.class.getSimpleName()))
                if (prefHelper.getRideInSession())
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                else if (Type != null && Type.equals(PUSH_START_TRIP)) {
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                } else if (Type != null && Type.equals(PUSH_APPROVE_TYPE)) {
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                } else if (Type != null && Type.equals(PUSH_END_TRIP_TYPE)) {
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                } else if (Type != null && Type.equals(Auto_Cancel)) {
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                } else if (Type != null && Type.equals(KEY_SCHEDULE_CANCEL_RIDE)) {
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                } else if (Type != null && Type.equals(DRIVER_ARRIVED)) {
                    replaceDockableFragment(HomeMapFragment.newInstance(Type, rideID, true), "HomeMapFragment");
                } else
                    replaceDockableFragment(HomeMapFragment.newInstance(), HomeMapFragment.class.getSimpleName());
            } else
                replaceDockableFragment(HomeMapFragment.newInstance(), HomeMapFragment.class.getSimpleName());
        } else {
            replaceDockableFragment(LoginFragment.newInstance(), "LoginFragment");
        }
    }

    public void refreshFragmentbyTag(String tag) {

        BaseFragment currFrag = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        if (currFrag != null) {
            fragTransaction.detach(currFrag);
            fragTransaction.attach(currFrag);
            fragTransaction.commit();
        }

    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();

                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };

        return result;
    }

    public void turnLocationOn(GoogleApiClient apiClient) {
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            apiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            //**************************
            builder.setAlwaysShow(true); //this is the key ingredient
            //**************************

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(apiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            settingActivateListener.onLocationActivateListener();
                            // All drive_location drive_settings are satisfied. The client can initialize drive_location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location drive_settings are not satisfied. But could be fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        getDockActivity(), 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location drive_settings are not satisfied. However, we have no way to fix the
                            // drive_settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onLoadingStarted() {

        if (mainFrameLayout != null) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (progressBar != null) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
        loading = false;

    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @Override
    public void onMenuItemActionCalled(int actionId, String data) {

    }

    @Override
    public void setSubHeading(String subHeadText) {

    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public void hideHeaderButtons(boolean leftBtn, boolean rightBtn) {
    }

    @Override
    public void onBackPressed() {
        if (loading) {
            UIHelper.showLongToastInCenter(getApplicationContext(),
                    R.string.message_wait);
        } else
            super.onBackPressed();

    }

    @Override
    public void onClick(View view) {

    }

    private void notImplemented() {
        UIHelper.showLongToastInCenter(this, "Coming Soon");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, chooserType, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    public void chooseImage() {
        chooserType = ChooserType.REQUEST_PICK_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE, true);
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void takePicture() {
        chooserType = ChooserType.REQUEST_CAPTURE_PICTURE;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_CAPTURE_PICTURE, true);
        imageChooserManager.setImageChooserListener(this);
        try {
            //pbar.setVisibility(View.VISIBLE);
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageChosen(final ChosenImage image) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFileThumbnailSmall();
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");

                    // Toast.makeText(getApplication(),thumbnailFilePath,Toast.LENGTH_LONG).show();
                    imageSetter.setImage(originalFilePath);
//                    imageSetter.setImage(thumbnailFilePath);


                    //loadImage(imageViewThumbnail, image.getFileThumbnail());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }

            }
        });

    }

    @Override
    public void onError(final String reason) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                // pbar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });

    }

}
