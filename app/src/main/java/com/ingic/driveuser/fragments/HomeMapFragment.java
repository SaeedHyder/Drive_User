package com.ingic.driveuser.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.PickupSelectionactivity;
import com.ingic.driveuser.entities.AllDriversHomeEnt;
import com.ingic.driveuser.entities.CancelReasonEnt;
import com.ingic.driveuser.entities.CreateRideEnt;
import com.ingic.driveuser.entities.DriverEnt;
import com.ingic.driveuser.entities.EstimateFareEnt;
import com.ingic.driveuser.entities.LocationEnt;
import com.ingic.driveuser.entities.PromoCodeEnt;
import com.ingic.driveuser.entities.RideDriverEnt;
import com.ingic.driveuser.entities.RideEnt;
import com.ingic.driveuser.entities.RideIdEnt;
import com.ingic.driveuser.entities.RideTrackEnt;
import com.ingic.driveuser.entities.SelectCarEnt;
import com.ingic.driveuser.entities.UserHomeEnt;
import com.ingic.driveuser.entities.VehicleDetail;
import com.ingic.driveuser.entities.getSettingeEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.BottomSheetDialogHelper;
import com.ingic.driveuser.helpers.DateHelper;
import com.ingic.driveuser.helpers.DatePickerHelper;
import com.ingic.driveuser.helpers.DialogHelper;
import com.ingic.driveuser.helpers.HomeServiceHelper;
import com.ingic.driveuser.helpers.TokenUpdater;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.interfaces.OnSettingActivateListener;
import com.ingic.driveuser.interfaces.webServiceResponseLisener;
import com.ingic.driveuser.ui.adapters.ReasonCancelListViewAdapter;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DirectionModule.DirectionFinder;
import DirectionModule.DirectionFinderListener;
import DirectionModule.Route;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static com.ingic.driveuser.global.AppConstants.CURRENT_RATING;
import static com.ingic.driveuser.global.AppConstants.LAST_RATING;
import static com.ingic.driveuser.global.AppConstants.PUSH_APPROVE_TYPE;
import static com.ingic.driveuser.global.AppConstants.PUSH_END_TRIP_TYPE;
import static com.ingic.driveuser.global.AppConstants.PUSH_START_TRIP;
import static com.ingic.driveuser.global.WebServiceConstants.APPROVE_DRIVER;
import static com.ingic.driveuser.global.WebServiceConstants.CANCELREASON;
import static com.ingic.driveuser.global.WebServiceConstants.CANCEL_RIDE;
import static com.ingic.driveuser.global.WebServiceConstants.CREATE;
import static com.ingic.driveuser.global.WebServiceConstants.ESTIMATEFARE;
import static com.ingic.driveuser.global.WebServiceConstants.ESTIMATEFARELATER;
import static com.ingic.driveuser.global.WebServiceConstants.GETSetting;
import static com.ingic.driveuser.global.WebServiceConstants.HOMEDRIVERS;
import static com.ingic.driveuser.global.WebServiceConstants.NEARBY;
import static com.ingic.driveuser.global.WebServiceConstants.PROMOCODE;
import static com.ingic.driveuser.global.WebServiceConstants.PROMOCODELATER;
import static com.ingic.driveuser.global.WebServiceConstants.PeakFactor;
import static com.ingic.driveuser.global.WebServiceConstants.RIDE_END_TRIP;
import static com.ingic.driveuser.global.WebServiceConstants.RIDE_LAST_RATING;
import static com.ingic.driveuser.global.WebServiceConstants.RIDE_LATER;
import static com.ingic.driveuser.global.WebServiceConstants.RIDE_RATING_current;
import static com.ingic.driveuser.global.WebServiceConstants.RIDE_RATING_last;
import static com.ingic.driveuser.global.WebServiceConstants.START_RIDE;
import static com.ingic.driveuser.global.WebServiceConstants.STATUS_RIDELATER;
import static com.ingic.driveuser.global.WebServiceConstants.STATUS_RIDENOW;
import static com.ingic.driveuser.global.WebServiceConstants.isRideIsGoing;
import static com.ingic.driveuser.global.WebServiceConstants.rideTracking;

;

/**
 * Created on 6/29/2017.
 */

public class HomeMapFragment extends BaseFragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DirectionFinderListener,
        OnSettingActivateListener,
        webServiceResponseLisener {

    public static String Notfication_RIDEID = "Notfication_RIDEID";
    public static String Notfication_TYPE = "Notfication_TYPE";
    public static String IS_FROM_NOTIFICATION = "pending_rides_detail";

    protected BroadcastReceiver broadcastReceiver;
    @BindView(R.id.txt_locationtype)
    TextView txtLocationtype;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.txt_pick_text)
    AnyTextView txtPickText;
    @BindView(R.id.txt_destination_text)
    AnyTextView txtDestinationText;
    @BindView(R.id.ll_destination)
    LinearLayout llDestination;
    @BindView(R.id.btn_ridenow)
    Button btnRidenow;
    @BindView(R.id.btn_ridelater)
    Button btnRidelater;
    @BindView(R.id.btn_cancel_ride)
    Button btnCancelRide;
    @BindView(R.id.btn_done_selection)
    Button btndoneselection;
    @BindView(R.id.custom_marker_view)
    RelativeLayout customMarkerView;
    @BindView(R.id.iv_tick)
    ImageView ivTick;
    @BindView(R.id.layout_pick)
    RelativeLayout layoutpick;
    @BindView(R.id.layout_destination)
    RelativeLayout layoutdestination;
    @BindView(R.id.container_finding_ride)
    RelativeLayout findingRide;
    @BindView(R.id.txt_Schedule_text)
    AnyTextView txtScheduleText;
    @BindView(R.id.circle)
    TextView circle;
    @BindView(R.id.txt_marker)
    TextView txtMarker;
    @BindView(R.id.layout_schedule)
    RelativeLayout layoutSchedule;
    @BindView(R.id.btn_location)
    CircleImageView btnLocation;
    @BindView(R.id.Main_frame)
    CoordinatorLayout Main_frame;
    View viewParent;
    @BindView(R.id.ll_source_destination)
    LinearLayout llSourceDestination;
    @BindView(R.id.laterbookingline)
    ImageView laterbookingline;
    @BindView(R.id.txt_peak_factor)
    AnyTextView txtPeakFactor;
    @BindView(R.id.ll_peakFactor)
    LinearLayout llPeakFactor;


    GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    SupportMapFragment map;
    String duration = "2 min";
    String peakFactor = "";
    @BindView(R.id.pulsator)
    PulsatorLayout pulsator;
    Unbinder unbinder;

    private double latitude;
    private double longitude;
    private Location Mylocation;
    private LocationEnt origin;
    private LocationEnt destination;
    private LocationEnt driverPosition;

    private LocationEnt originScheduleRide;
    private LocationEnt destinationScheduleRide;
    private double distance = 1;
    private double time_duration = 1;

    private Date DateSelected;
    private Date TimeSelected;
    private String cancelTimeValue;

    private TitleBar titleBar;
    private boolean mIsTitleBarChanged = false;

    private boolean isCurrentLocationMove;
    private ArrayList<SelectCarEnt> carTypeList;

    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private LocationListener listener;

    private HomeServiceHelper serviceHelper;

    private PromoCodeEnt promoCodeEnt;
    private SelectCarEnt selectCarEnt;
    private CreateRideEnt rideEnt;
    private DriverEnt driverDetail;
    private boolean isActivityResult = false;

    private Marker pickupMarker;
    private Marker DriverMarker;
    private Marker DestinationMarker;
    private boolean isRideinSession;
    private boolean isScheduleRide;
    private RideDriverEnt rideDriverEnt;
    private String rideID;
    private String type;
    private boolean isFromNotification;
    private String RideIdFeedBack;
    private String DriverIdFeedback;
    private String DriverId;

    private BottomSheetDialogHelper rideReaching;
    private BottomSheetDialogHelper estimateFareDialog;
    private BottomSheetDialogHelper setupRideDialoge;
    private EstimateFareEnt estimateFareEnt;
    private BottomSheetDialogHelper ratingDialog;

    private static String rideTrackKey = "rideTrackKey";
    private String trackKey;
    private RideTrackEnt rideTrackEntity;

    private static String isScheduleRideKey = "isScheduleRide";
    private boolean isScheduleKey = false;
    private String paymentType;


    public static HomeMapFragment newInstance() {
        return new HomeMapFragment();
    }

    public static HomeMapFragment newInstance(RideTrackEnt rideTrackEnt) {
        Bundle args = new Bundle();
        args.putString(rideTrackKey, new Gson().toJson(rideTrackEnt));
        HomeMapFragment fragment = new HomeMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeMapFragment newInstance(boolean isScheduleRide) {
        Bundle args = new Bundle();
        args.putBoolean(isScheduleRideKey, isScheduleRide);
        HomeMapFragment fragment = new HomeMapFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static HomeMapFragment newInstance(String type, String rideID, boolean isFromNotification) {

        if (type != null && type.equals(AppConstants.PUSH_APPROVE_TYPE)) {
            Bundle args = new Bundle();
            args.putString(Notfication_RIDEID, rideID);
            args.putString(Notfication_TYPE, type);
            args.putBoolean(IS_FROM_NOTIFICATION, isFromNotification);
            HomeMapFragment fragment = new HomeMapFragment();
            fragment.setArguments(args);
            return fragment;
        } else if (type != null && type.equals(AppConstants.PUSH_START_TRIP)) {
            Bundle args = new Bundle();
            args.putString(Notfication_RIDEID, rideID);
            args.putString(Notfication_TYPE, type);
            args.putBoolean(IS_FROM_NOTIFICATION, isFromNotification);
            HomeMapFragment fragment = new HomeMapFragment();
            fragment.setArguments(args);
            return fragment;
        } else if (type != null && type.equals(AppConstants.PUSH_END_TRIP_TYPE)) {
            Bundle args = new Bundle();
            args.putString(Notfication_RIDEID, rideID);
            args.putString(Notfication_TYPE, type);
            args.putBoolean(IS_FROM_NOTIFICATION, isFromNotification);
            HomeMapFragment fragment = new HomeMapFragment();
            fragment.setArguments(args);
            return fragment;
        } else {
            return new HomeMapFragment();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceHelper = new HomeServiceHelper(this, getDockActivity(), webService, prefHelper);
        origin = null;
        if (getArguments() != null) {
            rideID = getArguments().getString(Notfication_RIDEID);
            type = getArguments().getString(Notfication_TYPE);
            isFromNotification = getArguments().getBoolean(IS_FROM_NOTIFICATION);
            trackKey = getArguments().getString(rideTrackKey);
            isScheduleKey = getArguments().getBoolean(isScheduleRideKey);
            //   prefHelper.setRideInSession(true);
        }
        if (trackKey != null) {
            rideTrackEntity = new Gson().fromJson(trackKey, RideTrackEnt.class);
        }
        prefHelper.isFromBottomSheet(false);
        prefHelper.isFromBottomSheetLater(false);

        //BaseApplication.getBus().register(this);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (viewParent != null) {
            ViewGroup parent = (ViewGroup) viewParent.getParent();
            if (parent != null)
                parent.removeView(viewParent);
        }
        try {
            viewParent = inflater.inflate(R.layout.fragment_home_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }

        getMainActivity().setOnSettingActivateListener(this);
       /* originMarker = new MarkerOptions().position(new LatLng(0, 0));
        destinationMarker = new MarkerOptions().position(new LatLng(0, 0));*/
        if (viewParent != null)
            ButterKnife.bind(this, viewParent);
        unbinder = ButterKnife.bind(this, viewParent);
        return viewParent;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                AppConstants.Device_Type,
                prefHelper.getFirebase_TOKEN());
        getMainActivity().refreshSideMenu();

        onNotificationReceived();
        if (map == null)
            initMap();


    }

    private void initMap() {
        map = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        map.getMapAsync(this);


        googleApiClient = new GoogleApiClient.Builder(getMainActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)

                .addApi(LocationServices.API)
                .build();
        txtLocationtype.setText(getResources().getString(R.string.set_pickup_location));


    }

    boolean isOneTime;

    @SuppressWarnings("deprecation")
    @Override
    public void onMapReady(GoogleMap googlemap) {
        googleMap = googlemap;
        googlemap.getUiSettings().setCompassEnabled(false);

        startMoveMap();

        if (!prefHelper.getRideInSession()) {

            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    String address = getCurrentAddress(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                    if (address != null) {
                        if (!prefHelper.getRideInSession() && origin != null) {
                            origin = new LocationEnt(address, googleMap.getCameraPosition().target);

                            // serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude)), HOMEDRIVERS);
                            serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(googleMap.getCameraPosition().target.latitude), String.valueOf(googleMap.getCameraPosition().target.longitude)), HOMEDRIVERS, false);

                        }

                    } else {
                        serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(googleMap.getCameraPosition().target.latitude), String.valueOf(googleMap.getCameraPosition().target.longitude)), HOMEDRIVERS, false);

                    }
                    //UIHelper.showShortToastInCenter(getDockActivity(), cameraPosition.drive_target.toString());
                }
            });

       /*  googlemap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
             @Override
             public void onCameraMoveStarted(int i) {
                 String address = getCurrentAddress(googleMap.getCameraPosition().target.latitude, googleMap.getCameraPosition().target.longitude);
                 if (address != null) {
                     if (!prefHelper.getRideInSession() && origin != null) {
                         origin = new LocationEnt(address, googleMap.getCameraPosition().target);

                         // serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude)), HOMEDRIVERS);
                         serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(googleMap.getCameraPosition().target.latitude), String.valueOf(googleMap.getCameraPosition().target.longitude)), HOMEDRIVERS, false);

                     }

                 } else {
                     serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(googleMap.getCameraPosition().target.latitude), String.valueOf(googleMap.getCameraPosition().target.longitude)), HOMEDRIVERS, false);

                 }
             }
         });*/

        }

        if (rideTrackEntity == null) {
            serviceHelper.enqueueCall(headerWebService.isRideOnGoing(AppConstants.user), isRideIsGoing, false);
        }

        if (isFromNotification) {
            if (type.equals(PUSH_APPROVE_TYPE)) {
                serviceHelper.enqueueCall(headerWebService.getApproveDriver(rideID + ""), APPROVE_DRIVER);
            } else if (type.equals(PUSH_START_TRIP)) {

                serviceHelper.enqueueCall(headerWebService.getApproveDriver(rideID + ""), START_RIDE);
            } else if (type.equals(PUSH_END_TRIP_TYPE)) {
                serviceHelper.enqueueCall(headerWebService.getApproveDriver(rideID + ""), RIDE_END_TRIP);

            } else if (type != null && type.equals(AppConstants.Auto_Cancel)) {
                // UIHelper.showLongToastInCenter(getDockActivity(), getResources().getString(R.string.driver_not_found_error));
                prefHelper.setRideInSession(false);
                prefHelper.removeRideSessionPreferences();
                getMainActivity().initFragment(true);
            } else if (type != null && type.equals(AppConstants.KEY_SCHEDULE_CANCEL_RIDE)) {
                prefHelper.setIsScheduleRide(false);
            } else if (type != null && type.equals(AppConstants.DRIVER_ARRIVED)) {
                driverArrivedDialoge();

            }
        }
        if (rideTrackEntity != null)
            RestoreState(rideTrackEntity);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //  if (origin == null || origin.getLatlng().equals(new LatLng(0, 0)))
        serviceHelper.enqueueCall(headerWebService.isRideOnGoing(AppConstants.user), isRideIsGoing, false);
        // getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, String title, int ColorID) {

        View customMarkerView = ((LayoutInflater) getMainActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.img_icon);
        TextView textView = (TextView) customMarkerView.findViewById(R.id.txt_pick_text);
        TextView txtCircle = (TextView) customMarkerView.findViewById(R.id.txt_circle);
        txtCircle.setVisibility(View.GONE);
        textView.setText(title);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setBackground(getResources().getDrawable(R.drawable.drive_14min));
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    boolean isApprovedRide = false;

    private void onNotificationReceived() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(AppConstants.REGISTRATION_COMPLETE)) {
                    System.out.println("registration complete");
                    System.out.println(prefHelper.getFirebase_TOKEN());

                } else if (intent.getAction().equals(AppConstants.PUSH_NOTIFICATION)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String rideID = bundle.getString("rideID");
                        String Type = bundle.getString("pushtype");
                        if (Type != null && Type.equals(AppConstants.PUSH_END_TRIP_TYPE)) {
                            serviceHelper.enqueueCall(headerWebService.getApproveDriver(rideID + ""), RIDE_END_TRIP);
                        } else if (Type != null && Type.equals(AppConstants.PUSH_APPROVE_TYPE)) {
                            isApprovedRide = true;
                            serviceHelper.enqueueCall(headerWebService.getApproveDriver(rideID + ""), APPROVE_DRIVER);
                        } else if (Type != null && Type.equals(AppConstants.DRIVER_ARRIVED)) {
                            driverArrivedDialoge();

                        } else if (Type != null && Type.equals(AppConstants.PUSH_START_TRIP)) {
                            if (!isApprovedRide) {
                                serviceHelper.enqueueCall(headerWebService.getApproveDriver(rideID + ""), START_RIDE);
                            }
                            if (rideReaching != null) {
                                Button cancelButton = rideReaching.getDesiredButton(R.id.btn_cancel_ride);
                                if (cancelButton != null) {
                                    cancelButton.setEnabled(false);
                                    cancelButton.setVisibility(View.GONE);

                                }
                            }
                        } else if (Type != null && Type.equals(AppConstants.Auto_Cancel)) {
                            //UIHelper.showLongToastInCenter(getDockActivity(), getResources().getString(R.string.driver_not_found_error));
                            prefHelper.setRideInSession(false);
                            prefHelper.removeRideSessionPreferences();
                            /*getMainActivity().getIntent().replaceExtras(bundle);
                            getDockActivity().popBackStackTillEntry(0);*/
                            getMainActivity().initFragment(true);
                        } else if (Type != null && Type.equals(AppConstants.KEY_SCHEDULE_CANCEL_RIDE)) {
                            prefHelper.setIsScheduleRide(false);
                        } else if (Type != null && Type.equals(AppConstants.PUSH_NOT_FOUND)) {
                            UIHelper.showLongToastInCenter(getDockActivity(), getResources().getString(R.string.driver_not_found_error));
                            prefHelper.setRideInSession(false);
                            prefHelper.removeRideSessionPreferences();
                            getMainActivity().getIntent().replaceExtras(bundle);
                            getDockActivity().popBackStackTillEntry(0);
                            getMainActivity().initFragment(false);
                        }
                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Notification Data is Empty");
                    }
                } else if (intent.getAction().equals(AppConstants.LOCATION_RECIEVED)) {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        String lat = bundle.getString("lat");
                        String lon = bundle.getString("lon");
                        LatLng latLng = new LatLng(Double.parseDouble(lat + ""), Double.parseDouble(lon + ""));
                        driverPosition = new LocationEnt("", latLng);
                        if (DriverMarker != null)
                            animateMarker(DriverMarker.getPosition(), latLng, false);


                    } else {
                        //UIHelper.showShortToastInCenter(getDockActivity(), "Notification Data is Empty");
                    }
                }
            }
        };
    }

    private void driverArrivedDialoge() {

        if (rideReaching != null) {
            Button cancelButton = rideReaching.getDesiredButton(R.id.btn_cancel_ride);
            if (cancelButton != null) {
                cancelButton.setEnabled(false);
                cancelButton.setVisibility(View.GONE);

            }
        }

        if (rideDriverEnt != null) {
            final DialogHelper dialoge = new DialogHelper(getDockActivity());
            dialoge.initDriverArrivedDialoge(R.layout.driver_arrived_dialoge, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialoge.hideDialog();
                }
            }, rideDriverEnt);

            dialoge.showDialog();

        }

        if (rideTrackEntity != null)
            rideTrackEntity.setStatus("4");


        driverArrivedMarkers();
    }

    private void driverArrivedMarkers() {
        if (destination != null && driverPosition != null) {

            BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.drive_location);
            DestinationMarker = googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(icon1));

            pickupMarker.setVisible(false);
            moveMapBetweenMarker(destination.getLatlng(), driverPosition.getLatlng());
        }
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getDockActivity()).unregisterReceiver(broadcastReceiver);
        //SaveCurrentState();
        super.onPause();
        UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity()
                .getWindow().getDecorView());
    }

    @Override
    public void onResume() {
        super.onResume();
        UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity()
                .getWindow().getDecorView());
        if (origin == null || origin.getLatlng().equals(new LatLng(0, 0))) {
            customMarkerView.setVisibility(View.GONE);
            llDestination.setVisibility(View.GONE);
            getMainActivity().statusCheck();
            // getCurrentLocation();
        }

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.REGISTRATION_COMPLETE));

        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.PUSH_NOTIFICATION));
        LocalBroadcastManager.getInstance(getDockActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter(AppConstants.LOCATION_RECIEVED));


    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        this.titleBar = titleBar;


        if (mIsTitleBarChanged) {
            adjustTitleBar();
        } else if (prefHelper.getisFromBottomSheet()) {
            titleBar.hideButtons();
            titleBar.showBackButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartPickupActivity(10);
                }
            });
            titleBar.setBackgroundColor(getResources().getColor(R.color.appBlackColor));
            titleBar.setSubHeading("Home");
            if (setupRideDialoge != null)
                setupRideDialoge.hideDialog();
            setupRideNowDialog();
        } else if (prefHelper.getisFromBottomSheetLater()) {
            titleBar.hideButtons();
            titleBar.showBackButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StartPickupActivity(10);
                }
            });
            titleBar.setBackgroundColor(getResources().getColor(R.color.appBlackColor));
            titleBar.setSubHeading("Home");
            if (setupRideDialoge != null)
                setupRideDialoge.hideDialog();
            setupScheduleDialog();
        } else {
            titleBar.hideButtons();
            titleBar.showMenuButton();
            titleBar.setBackgroundColor(getResources().getColor(R.color.appBlackColor));
            titleBar.setSubHeading("Home");
        }

    }

    public void adjustTitleBar() {
        if (titleBar != null) {
            titleBar.hideButtons();
            titleBar.showMenuButton();
            titleBar.showMessageButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDockActivity().addDockableFragment(MessagesFragment.newInstance(driverDetail.getPhoneNo()), MessagesFragment.class.getSimpleName());
                }
            });
            titleBar.showCallButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel: " + driverDetail.getPhoneNo()));
                    startActivity(intent);
                }
            });
            if (isAdded() && titleBar != null) {
                titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.your_ride));
            }
        }
    }

    private void sendRequest(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void setRoute() {
        if (origin != null && destination != null) {
            LatLng pick = origin.getLatlng();
            LatLng destinat = destination.getLatlng();
            String origin_string = String.valueOf(pick.latitude) + "," + String.valueOf(pick.longitude);
            String destination_string = String.valueOf(destinat.latitude) + "," + String.valueOf(destinat.longitude);

            sendRequest(origin_string, destination_string);
        }
    }

    @Override
    public void onDirectionFinderStart() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

   /*    *//* googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.set_pickup_location,
                        "14 min", R.color.black))));
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.destination_icon);
<<<<<<< HEAD
        movemap(origin.getLatlng());
        googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(icon));
=======

        googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(icon));*//*
>>>>>>> f89e593cd6182c6ba33fabb11b9f8726c1c4d18b*/


        for (Route routesingle : route) {
           /* PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLACK).
                    width(15);

            if (originScheduleRide == null) {
                pickupMarker = googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.drive_pin,
                                routesingle.duration.text, R.color.black))));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.drive_location);
                googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(icon));
            } else {
                pickupMarker = googleMap.addMarker(new MarkerOptions().position(originScheduleRide.getLatlng())
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.drive_pin,
                                routesingle.duration.text, R.color.black))));
                BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.drive_location);
                googleMap.addMarker(new MarkerOptions().position(destinationScheduleRide.getLatlng()).icon(icon));

            }
            moveRouteMap();
            for (int i = 0; i < routesingle.points.size(); i++)
                polylineOptions.add(routesingle.points.get(i));

            //moveMap(null);
            polylinePaths.add(googleMap.addPolyline(polylineOptions));*/
            distance = (double) routesingle.distance.value / 1000;
            time_duration = routesingle.duration.value / 60;

        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route, View view, Object object) {

    }

    @Override
    public void onLocationActivateListener() {
        if (origin == null || origin.getLatlng().equals(new LatLng(0, 0)))
            getCurrentLocation();
    }

    @Override
    public void onNetworkActivateListener() {
        if (origin == null) {
            // getMainActivity().statusCheck();
            //  getCurrentLocation();
        }
    }

    private void moveRouteMap() {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (originScheduleRide == null) {
            builder.include(origin.getLatlng());
            builder.include(destination.getLatlng());
        } else {
            builder.include(originScheduleRide.getLatlng());
            builder.include(destinationScheduleRide.getLatlng());
        }
        LatLngBounds bounds = builder.build();

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
        googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

                //   CameraUpdate zout = CameraUpdateFactory.zoomBy(-3.0f);
                //  googleMap.animateCamera(zout);
                CameraUpdate zout = CameraUpdateFactory.zoomBy(-0.5f);
                googleMap.animateCamera(zout);

            }

            @Override
            public void onCancel() {

            }
        });
    }

    private void movemap(LatLng latlng) {


      /*  CameraUpdate center = CameraUpdateFactory.newLatLng(latlng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
>>>>>>> f89e593cd6182c6ba33fabb11b9f8726c1c4d18b

        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);*/

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latlng.latitude, latlng.longitude))
                .zoom(14)
                .bearing(0)
                .tilt(45)
                .build();
        if (isCurrentLocationMove) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            // googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            isCurrentLocationMove = false;
        } else {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
        /*if (isCurrentLocationMove) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    customMarkerView.setVisibility(View.VISIBLE);
                    llDestination.setVisibility(View.VISIBLE);
                    isCurrentLocationMove = false;
                }

                @Override
                public void onCancel() {

                }
            });
        }else
        {*/
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        // }
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void animateMarker(final LatLng startPosition, final LatLng toPosition, final boolean hideMarker) {




        /*final Marker marker = googleMap.addMarker(new MarkerOptions()
                .position(startPosition)
                .title(mCarParcelableListCurrentLation.get(position).mCarName)
                .snippet(mCarParcelableListCurrentLation.get(position).mAddress)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drive_car1)));*/
        if (DriverMarker != null) {

            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();

            final long duration = 3000;
            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * toPosition.longitude + (1 - t)
                            * startPosition.longitude;
                    double lat = t * toPosition.latitude + (1 - t)
                            * startPosition.latitude;


                    double dLon = (toPosition.longitude - startPosition.longitude);
                    double y = Math.sin(dLon) * Math.cos(toPosition.latitude);
                    double x = Math.cos(startPosition.latitude) * Math.sin(toPosition.latitude) -
                            Math.sin(startPosition.latitude) * Math.cos(toPosition.latitude) * Math.cos(dLon);
                    double brng = Math.toDegrees((Math.atan2(y, x)));
                    brng = (360 - ((brng + 360) % 360));
                    DriverMarker.setPosition(new LatLng(lat, lng));
                    // carMarker.setRotation((float) brng);

                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        if (hideMarker) {
                            DriverMarker.setVisible(false);
                        } else {
                            DriverMarker.setVisible(true);
                        }
                    }
                }
            });


            if (rideTrackEntity != null && rideTrackEntity.getStatus().equals("1")) {
                if (origin != null)
                    moveMapBetweenMarker(origin.getLatlng(), toPosition);
                else
                    movemap(toPosition);
            } else {
                if (destination != null)
                    moveMapBetweenMarker(destination.getLatlng(), toPosition);
                else
                    movemap(toPosition);
            }


        }

    }

    private void getCurrentLocation() {


        if (googleMap != null && !prefHelper.getRideInSession()) {
            googleMap.clear();
        }
        if (getMainActivity() != null) {
            if (ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        }
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

      /*  if (Mylocation == null) {
            locationRequest.setInterval(1000);
        }*/
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location mlocation) {
                if (mlocation != null) {
                    Mylocation = mlocation;
                    LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, listener);

                    if (Mylocation != null) {
                        //Getting longitude and latitude
                        latitude = Mylocation.getLatitude();
                        longitude = Mylocation.getLongitude();

                        prefHelper.set_current_latitude(latitude + "");
                        prefHelper.set_current_longitute(longitude + "");
                        // origin = new LatLng(latitude, longitude);
                        String Address = getCurrentAddress(latitude, longitude);
                        if (!prefHelper.getRideInSession()) {
                            customMarkerView.setVisibility(View.VISIBLE);
                            llDestination.setVisibility(View.VISIBLE);
                        }
                        serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(latitude), String.valueOf(longitude)), HOMEDRIVERS, false);


                        if (Address != null) {

                            origin = new LocationEnt(Address, new LatLng(latitude, longitude));
                        } else {
                            origin = new LocationEnt("Un Named Street", new LatLng(latitude, longitude));
                        }
                        isCurrentLocationMove = true;
                        movemap(origin.getLatlng());
                        // moveMap(new LatLng(latitude, longitude));
                        //   serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude)), HOMEDRIVERS,false);

                    } else {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Can't get your Location Try getting using Location Button");
                    }
                }
            }
        };
        if (googleApiClient.isConnected() && getActivity() != null)
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, listener);

                //drive_location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

            }
    }

    private String getCurrentAddress(double lat, double lng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getDockActivity(), Locale.ENGLISH);
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String Province = addresses.get(0).getAddressLine(1);
                String country = addresses.get(0).getCountryName();
                return address + ", " + Province + ", " + country;
            } else {

                return "Address Not Available";
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public LatLng translateCoordinates(final double distance, final LatLng origpoint, final double angle) {
        final double distanceNorth = Math.sin(angle) * distance;
        final double distanceEast = Math.cos(angle) * distance;

        final double earthRadius = 6371000;

        final double newLat = origpoint.latitude + (distanceNorth / earthRadius) * 180 / Math.PI;
        final double newLon = origpoint.longitude + (distanceEast / (earthRadius * Math.cos(newLat * 180 / Math.PI))) * 180 / Math.PI;

        return new LatLng(newLat, newLon);
    }

    private void StartPickupActivity(int Id) {

        Intent i = new Intent(getActivity(), PickupSelectionactivity.class);
        Bundle args = new Bundle();
        args.putString("origin", new Gson().toJson(origin));
        args.putString("destination", new Gson().toJson(destination));
        i.putExtra("route", args);
        // i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(i, Id);
    }

    @OnClick({R.id.txt_Schedule_text, R.id.txt_pick_text, R.id.btn_done_selection, R.id.txt_destination_text, R.id.btn_location, R.id.ll_destination, R.id.btn_ridenow, R.id.btn_ridelater, R.id.btn_cancel_ride, R.id.txt_locationtype})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_pick_text:

                StartPickupActivity(10);

                break;
            case R.id.txt_destination_text:
                StartPickupActivity(10);
                break;
            case R.id.ll_destination:
                StartPickupActivity(10);
                break;

            case R.id.txt_locationtype:
                StartPickupActivity(10);
                break;
            case R.id.btn_ridenow:
                // movemap(origin.getLatlng());
                //  setupRideNowDialog();

                SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                Date rideTime = new Date();
                String time = "";
                customMarkerView.setVisibility(View.GONE);

                if (rideTrackEntity != null && rideTrackEntity.getTime() != null) {
                    String getTime = rideTrackEntity.getTime();
                    try {
                        date = parser.parse(getTime);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        int currentMinute = calendar.get(Calendar.MINUTE);
                        calendar.set(Calendar.MINUTE, currentMinute - 30);
                        rideTime = calendar.getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    time = parser.format(rideTime);
                }
                if (time != null && !time.equals("")) {
                    if (!(DateHelper.timeComparision(time, rideTrackEntity.getDate()))) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "You cannot create a ride\nYou have a scheduled ride");
                    } else {
                        showAllDrivers(null, false);
                        serviceHelper.enqueueCall(headerWebService.getLastFeedback(), RIDE_LAST_RATING);
                    }
                } else {
                    showAllDrivers(null, false);
                    serviceHelper.enqueueCall(headerWebService.getLastFeedback(), RIDE_LAST_RATING);
                }
                break;
            case R.id.btn_ridelater:
                movemap(origin.getLatlng());
                setupScheduleDialog();
                break;
            case R.id.btn_cancel_ride:
                serviceHelper.enqueueCall(headerWebService.getSetting("cancel_time"), GETSetting);

                break;

            case R.id.btn_done_selection:
                initRideStatus();
                break;
            case R.id.btn_location:
                if (getMainActivity().statusCheck())
                    getCurrentLocation();
                break;
            case R.id.txt_Schedule_text:
                setupScheduleDialog();
                break;
        }
    }

    private void setupRatingDialog(final RideDriverEnt result, int RatingType) {
        if (rideReaching != null) {
            rideReaching.hideDialog();
        }
        if (btnRidenow != null && btnRidelater != null) {
            btnRidenow.setVisibility(View.GONE);
            btnRidelater.setVisibility(View.GONE);
        }
        llSourceDestination.setVisibility(View.GONE);
        customMarkerView.setVisibility(View.GONE);

        showRatingBottomSheet(result, RatingType);


    }

    private void showRatingBottomSheet(final RideDriverEnt result, final int RatingType) {
        ratingDialog = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_submit_rating);
        ratingDialog.initRatingDialog(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Rating = (int) Math.ceil(ratingDialog.getRatingScore());
                RideIdFeedBack = result.getRideDetail().getId() + "";
                DriverIdFeedback = result.getDriverDetail().getId() + "";

                Rating = Rating + 0;
                if (RatingType == LAST_RATING) {
                    if (ratingDialog != null)
                        ratingDialog.hideDialog();
                    serviceHelper.enqueueCall(headerWebService.submitRideFeedback(
                            result.getRideDetail().getId() + "",
                            result.getDriverDetail().getId() + "",
                            Rating + ""
                    ), RIDE_RATING_last);
                } else if (RatingType == CURRENT_RATING) {
                    if (ratingDialog != null)
                        ratingDialog.hideDialog();
                    serviceHelper.enqueueCall(headerWebService.submitRideFeedback(
                            result.getRideDetail().getId() + "",
                            result.getDriverDetail().getId() + "",
                            Rating + ""
                    ), RIDE_RATING_current);
                }

            }
        }, result);
        if (ratingDialog != null)
            ratingDialog.showDialog();
        titleBar.hideButtons();
        if (RatingType == LAST_RATING) {
            titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.submit_rating_last));
        } else if (RatingType == CURRENT_RATING) {
            getDockActivity().StopDriverLocationService();
            titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.rate_title));
        }
    }

    private void setupRideNowDialog() {
        btnRidenow.setVisibility(View.GONE);
        btnRidelater.setVisibility(View.GONE);
        llSourceDestination.setVisibility(View.GONE);
        if (ratingDialog != null)
            ratingDialog.hideDialog();
        String org = "";
        String des = "";

        paymentType = "Cash";
        if (prefHelper.getPaymentType() != null && !(prefHelper.getPaymentType().equals(""))) {
            paymentType = prefHelper.getPaymentType();
        }



     /*   if (!(prefHelper.getCarTypes() == null)) {
            carTypeList = prefHelper.getCarTypes();
        } else {
            serviceHelper.enqueueCall(headerWebService.getVehicles(), WebServiceConstants.getVehicles);
        }*/

        if (destination != null) {
            des = destination.getAddress();
            if (destination.getAddress().contains("null")) {
                des = destination.getAddress().replace("null,", "");
            }

        }
        if (origin != null) {
            org = origin.getAddress();
            if (origin.getAddress().contains("null")) {
                org = origin.getAddress().replace("null,", "");
            }

        }

        if (setupRideDialoge != null) {
            setupRideDialoge.hideDialog();
        }

        setupRideDialoge = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_ride_now);
        setupRideDialoge.initNewSelectRideBottomSheet(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectCarDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPromoCodeDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().addDockableFragment(PaymentMethodFragment.newInstance(AppConstants.bottomsheet), "PaymentMethodFragment");
                //  UIHelper.showShortToastInCenter(getDockActivity(), "will be implemented");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (selectCarEnt != null)
                // selectCarEnt = setupRideDialoge.getSelectedType();
                String percentage = "";
                String estimatedFare = "";
                if (promoCodeEnt != null)
                    percentage = promoCodeEnt.getPercentage();
                if (estimateFareEnt != null) {
                    estimatedFare = estimateFareEnt.getEstimateFare();
                }

                if (selectCarEnt == null) {
                    UIHelper.showShortToastInCenter(getDockActivity(), "Please select car first");
                } else {

                    setupRideDialoge.hideDialog();

                    serviceHelper.enqueueCall(headerWebService.createNewRide(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude),
                            origin.getAddress(), origin.getAddress(), String.valueOf(destination.getLatlng().latitude), String.valueOf(destination.getLatlng().longitude),
                            destination.getAddress(), destination.getAddress(), selectCarEnt.getId() + "", paymentType, percentage + "", "", "", STATUS_RIDENOW, estimatedFare, time_duration + "", distance + "", checkForNullOREmpty(peakFactor), prefHelper.getUser().getUserPreference()), CREATE);

                }

            }
        }, prefHelper, org, des, selectCarEnt, promoCodeEnt, estimateFareEnt, peakFactor);

        setupRideDialoge.showDialog();
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRidenow.setVisibility(View.VISIBLE);
                btnRidelater.setVisibility(View.VISIBLE);
                titleBar.showMenuButton();
                llSourceDestination.setVisibility(View.VISIBLE);
                setupRideDialoge.hideDialog();
                StartPickupActivity(10);


            }
        });

          /* serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                        checkForNullOREmpty(percentage), distance + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARE);*/

       /* carTypeList = new ArrayList<>();onNo

        carTypeList.add(new SelectCarEnt("drawable://" + R.drawable.economy, R.drawable.circle_unactive, "Economy", R.color.button_color, R.color.gray_dark, "drawable://" + R.drawable.economy_active, R.drawable.circle_blue));
        carTypeList.add(new SelectCarEnt("drawable://" + R.drawable.business_unactive, R.drawable.circle_unactive, "Business", R.color.button_color, R.color.gray_dark, "drawable://" + R.drawable.business_active, R.drawable.circle_blue));
        carTypeList.add(new SelectCarEnt("drawable://" + R.drawable.vip_unactive, R.drawable.circle_unactive, "Vip", R.color.button_color, R.color.gray_dark, "drawable://" + R.drawable.vip_active, R.drawable.circle_blue));
*/
       /* setupRideDialoge = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottomsheet_selectride);
        setupRideDialoge.initSelectRideBottomSheet(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPromoCodeDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCarEnt = setupRideDialoge.getSelectedType();
                String percentage = "";
                if (promoCodeEnt != null)
                    percentage = promoCodeEnt.getPercentage();
                serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                        checkForNullOREmpty(percentage), distance + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARE);
                setupRideDialoge.hideDialog();

            }
        }, carTypeList, prefHelper, setupRideDialoge);
        setupRideDialoge.showDialog();*/


    }

    private void setupScheduleDialog() {

        layoutSchedule.setVisibility(View.GONE);
        laterbookingline.setVisibility(View.GONE);
        btnCancelRide.setVisibility(View.GONE);
        btnRidenow.setVisibility(View.GONE);
        btnRidelater.setVisibility(View.GONE);
        llSourceDestination.setVisibility(View.GONE);


        String org = "";
        String des = "";

        paymentType = "Cash";
        if (prefHelper.getPaymentType() != null && !(prefHelper.getPaymentType().equals(""))) {
            paymentType = prefHelper.getPaymentType();
        }

        String percentage = "";
        String estimatedFare = "";
        if (promoCodeEnt != null)
            percentage = promoCodeEnt.getPercentage();
        if (estimateFareEnt != null) {
            estimatedFare = estimateFareEnt.getEstimateFare();
        }

        final String finalPercentage = percentage;
        final String finalEstimatedFare = estimatedFare;


        if (destination != null) {
            des = destination.getAddress();
            if (destination.getAddress().contains("null")) {
                des = destination.getAddress().replace("null,", "");
            }

        }
        if (origin != null) {
            org = origin.getAddress();
            if (origin.getAddress().contains("null")) {
                org = origin.getAddress().replace("null,", "");
            }

        }

        setupRideDialoge = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_ride_now);

        setupRideDialoge.initNewSelectRideLaterBottomSheet(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initSelectCarLaterDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPromoCodeLaterDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker(setupRideDialoge.getDateEditText());

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker(setupRideDialoge.getTimeEditText());
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().addDockableFragment(PaymentMethodFragment.newInstance(AppConstants.bottomsheetLater), "PaymentMethodFragment");
                //  UIHelper.showShortToastInCenter(getDockActivity(), "will be implemented");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SelectedDate = "", SelectedTime = "";
                if (DateSelected != null && TimeSelected != null) {
                    SelectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(DateSelected.getTime());
                    SelectedTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(TimeSelected.getTime());
                }

                if (DateSelected == null) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_pickup_date));
                } else if (TimeSelected == null) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getDockActivity().getResources().getString(R.string.select_time_pickup));
                } else {
                    if (!(prefHelper.getCarTypes() == null)) {
                        carTypeList = prefHelper.getCarTypes();

                        if (selectCarEnt == null) {
                            UIHelper.showShortToastInCenter(getDockActivity(), "Please select car first");
                        } else {
                            serviceHelper.enqueueCall(headerWebService.createNewRide(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude),
                                    origin.getAddress(), origin.getAddress(), String.valueOf(destination.getLatlng().latitude), String.valueOf(destination.getLatlng().longitude),
                                    destination.getAddress(), destination.getAddress(), selectCarEnt.getId() + "", paymentType, finalPercentage + "", SelectedDate, SelectedTime, STATUS_RIDELATER, finalEstimatedFare, time_duration + "", distance + "", checkForNullOREmpty(peakFactor), prefHelper.getUser().getUserPreference()), RIDE_LATER);

                        }
                    } else {
                        serviceHelper.enqueueCall(headerWebService.getVehicles(), WebServiceConstants.getVehiclesLaterRide);
                    }

                }

            }
        }, prefHelper, org, des, selectCarEnt, promoCodeEnt, estimateFareEnt, peakFactor, DateSelected, TimeSelected);

        setupRideDialoge.showDialog();
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRidenow.setVisibility(View.VISIBLE);
                btnRidelater.setVisibility(View.VISIBLE);
                hideScheduleViews();
                DateSelected = null;
                TimeSelected = null;
                titleBar.showMenuButton();
                llSourceDestination.setVisibility(View.VISIBLE);
                setupRideDialoge.hideDialog();
                StartPickupActivity(10);


            }
        });

     /*   final BottomSheetDialog dialog = new BottomSheetDialog(getDockActivity());
        dialog.setContentView(R.layout.bottom_sheet_scheduled_picker);
        Button cancelbutton = (Button) dialog.findViewById(R.id.SubmitButton);
        final AnyTextView date_pick = (AnyTextView) dialog.findViewById(R.id.txt_datepicker);
        final AnyTextView time_pick = (AnyTextView) dialog.findViewById(R.id.txt_timepicker);
        date_pick.setPaintFlags(Typeface.BOLD);
        time_pick.setPaintFlags(Typeface.BOLD);
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DateSelected == null) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.select_pickup_date));
                } else if (TimeSelected == null) {
                    UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.select_time_pickup));
                } else {
                    dialog.dismiss();
                    if (!(prefHelper.getCarTypes() == null)) {
                        carTypeList = prefHelper.getCarTypes();
                        setupScheduleComfirmDialog(date_pick.getText().toString() + " at " + time_pick.getText().toString());
                    } else {
                        serviceHelper.enqueueCall(headerWebService.getVehicles(), WebServiceConstants.getVehiclesLaterRide);
                    }

                }
            }
        });
        // dialog.setCanceledOnTouchOutside(false);
        //dialog.setCancelable(false);

        date_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatePicker(date_pick);
            }
        });

        time_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker(time_pick);
            }
        });
        dialog.show();*/
    }

    private void setupScheduleComfirmDialog(String s) {
        layoutSchedule.setVisibility(View.VISIBLE);
        laterbookingline.setVisibility(View.VISIBLE);
        txtScheduleText.setText(s);
        btnCancelRide.setVisibility(View.GONE);
        btnRidenow.setVisibility(View.GONE);
        btnRidelater.setVisibility(View.GONE);
        llSourceDestination.setVisibility(View.VISIBLE);

        carTypeList = prefHelper.getCarTypes();
      /*  carTypeList.add(new SelectCarEnt("drawable://" + R.drawable.economy, R.drawable.circle_unactive, "Economy", R.color.button_color, R.color.gray_dark, "drawable://" + R.drawable.economy_active, R.drawable.circle_blue));
        carTypeList.add(new SelectCarEnt("drawable://" + R.drawable.business_unactive, R.drawable.circle_unactive, "Business", R.color.button_color, R.color.gray_dark, "drawable://" + R.drawable.business_active, R.drawable.circle_blue));
        carTypeList.add(new SelectCarEnt("drawable://" + R.drawable.vip_unactive, R.drawable.circle_unactive, "Vip", R.color.button_color, R.color.gray_dark, "drawable://" + R.drawable.vip_active, R.drawable.circle_blue));
*/

        setupRideDialoge = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottomsheet_selectride);
        setupRideDialoge.initSelectRideBottomSheet(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPromoCodeDialog();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectCarEnt = setupRideDialoge.getSelectedType();
                String percentage = "";
                if (promoCodeEnt != null)
                    percentage = promoCodeEnt.getPercentage();

                String SelectedDate = "", SelectedTime = "";
                if (DateSelected != null && TimeSelected != null) {
                    SelectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(DateSelected.getTime());
                    SelectedTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(TimeSelected.getTime());
                }

               /* serviceHelper.enqueueCall(headerWebService.createNewRide(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude),
                        origin.getAddress(), origin.getAddress(), String.valueOf(destination.getLatlng().latitude), String.valueOf(destination.getLatlng().longitude),
                        destination.getAddress(), destination.getAddress(), selectCarEnt.getId() + "", percentage + "", SelectedDate, SelectedTime, STATUS_RIDELATER, "00", distance + ""), RIDE_LATER);
*/


                serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                        checkForNullOREmpty(percentage), distance + "", "", checkForNullOREmpty(peakFactor)), ESTIMATEFARELATER);

                setupRideDialoge.hideDialog();
            }
        }, R.string.schedule_ride, carTypeList, prefHelper, setupRideDialoge);
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.schedule_new_trip));
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideScheduleViews();
                DateSelected = null;
                TimeSelected = null;
                setupRideDialoge.hideDialog();


            }
        });
        setupRideDialoge.showDialog();
    }

    private void setcanceldialog(String CancelTime) {
        final DialogHelper canceldialog = new DialogHelper(getDockActivity());
        canceldialog.cancelRide(R.layout.cancel_ride_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canceldialog.hideDialog();
                serviceHelper.enqueueCall(headerWebService.getCancelReasons(), CANCELREASON);

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canceldialog.hideDialog();
            }
        }, CancelTime);
        canceldialog.setCancelable(false);
        canceldialog.showDialog();
    }

    private void ShowrideReachingDialog(RideDriverEnt result) {

        //rideDriverEnt = result;

        if (result != null) {
            DriverId = result.getDriverDetail().getId() + "";
            googleMap.clear();
            if (customMarkerView != null)
                customMarkerView.setVisibility(View.GONE);
            if (findingRide != null)
                findingRide.setVisibility(View.GONE);
            if (rideReaching != null) {
                rideReaching.hideDialog();
            }
            startMoveMap();
            googleMap.getUiSettings().setScrollGesturesEnabled(true);
            googleMap.setMyLocationEnabled(true);
            if (pulsator != null) {
                pulsator.stop();
            }
            if (btnCancelRide != null) {
                btnCancelRide.setVisibility(View.GONE);
            }
            if (llDestination != null) {
                llDestination.setVisibility(View.GONE);
            }
            if (btnLocation != null) {
                btnLocation.setVisibility(View.GONE);
            }
            if (btndoneselection != null) {
                btndoneselection.setVisibility(View.GONE);
            }
            if (layoutdestination != null) {
                layoutdestination.setVisibility(View.GONE);
            }
            if (layoutpick != null) {
                layoutpick.setVisibility(View.GONE);
            }
            if (btnRidenow != null) {
                btnRidenow.setVisibility(View.GONE);
            }
            if (btnRidelater != null) {
                btnRidelater.setVisibility(View.GONE);
            }
            prefHelper.setDriverId(result.getDriverDetail().getId() + "");

            rideReaching = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_dialog_ride_detail);
            rideReaching.initRideDetailBottomSheet(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancelTimeValue != null) {
                        setcanceldialog(cancelTimeValue);
                    } else {
                        serviceHelper.enqueueCall(headerWebService.getSetting("cancel_time"), GETSetting);
                    }
                }
            }, result, prefHelper.isStarted());
            rideReaching.showDialog();
            mIsTitleBarChanged = true;
            adjustTitleBar();

            driverPosition = new LocationEnt("", new LatLng(Double.parseDouble(
                    result.getDriverDetail().getLatitude()),
                    Double.parseDouble(result.getDriverDetail().getLongitude())));

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carwithcircle);
            DriverMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(
                    result.getDriverDetail().getLatitude()),
                    Double.parseDouble(result.getDriverDetail().getLongitude()))).icon(icon));

            if (origin != null && origin.getLatlng() != null) {
               /* pickupMarker = googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                        .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.drive_pin,
                                duration, R.color.black))));*/
                BitmapDescriptor pickupMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.profilecircle);
                pickupMarker = googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                        .icon(pickupMarkerIcon));
            }
              /* BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.drive_location);
            DestinationMarker=googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(icon1));*/


            if (rideTrackEntity != null && rideTrackEntity.getStatus().equals("1")) {

                LatLng driverLatLng = new LatLng(Double.parseDouble(result.getDriverDetail().getLatitude()), Double.parseDouble(result.getDriverDetail().getLongitude()));
                if (origin != null && driverLatLng != null) {
                    moveMapBetweenMarker(origin.getLatlng(), driverLatLng);
                }

            } else if ((rideTrackEntity != null && rideTrackEntity.getStatus().equals("4")) || (rideTrackEntity != null && rideTrackEntity.getStatus().equals("5"))) {
                if (destination != null && driverPosition != null) {

                    BitmapDescriptor icon1 = BitmapDescriptorFactory.fromResource(R.drawable.drive_location);
                    DestinationMarker = googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(icon1));

                    pickupMarker.setVisible(false);
                    moveMapBetweenMarker(destination.getLatlng(), driverPosition.getLatlng());
                }
                if (rideReaching != null) {
                    Button cancelButton = rideReaching.getDesiredButton(R.id.btn_cancel_ride);
                    if (cancelButton != null) {
                        cancelButton.setEnabled(false);
                        cancelButton.setVisibility(View.GONE);

                    }
                }

            }
        }

        getDockActivity().StartDriverLocationService();


     /*   if (isScheduleRide) {
            setScheduleRideRoute(rideDriverEnt);
        } else {
            //    setRoute();
        }*/



        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final BottomSheetDialogHelper ratingDialog = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_submit_rating);
                ratingDialog.initRatingDialog(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rideReaching.hideDialog();
                        ratingDialog.hideDialog();

                    }
                });
                ratingDialog.showDialog();

            }
        }, 10000);*/
    }

    private void initPromoCodeDialog() {
        final DialogHelper promodialog = new DialogHelper(getDockActivity());
        promodialog.promoCode(R.layout.promo_code_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity()
                        .getWindow().getDecorView());
                promodialog.hideDialog();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promodialog.getEditText(R.id.txt_promoCode).equals("")) {

                } else {
                    UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity()
                            .getWindow().getDecorView());
                    promodialog.hideDialog();
                    serviceHelper.enqueueCall(headerWebService.getPromoCode(promodialog.getEditText(R.id.txt_promoCode)), PROMOCODE);
                }

            }
        });
        promodialog.setCancelable(false);
        promodialog.showDialog();
    }

    private void initPromoCodeLaterDialog() {
        final DialogHelper promodialog = new DialogHelper(getDockActivity());
        promodialog.promoCode(R.layout.promo_code_dialog, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promodialog.hideDialog();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (promodialog.getEditText(R.id.txt_promoCode).equals("")) {

                } else {
                    promodialog.hideDialog();
                    serviceHelper.enqueueCall(headerWebService.getPromoCode(promodialog.getEditText(R.id.txt_promoCode)), PROMOCODELATER);
                }

            }
        });
        promodialog.setCancelable(false);
        promodialog.showDialog();
    }

    private void initSelectCarDialog() {

        if (!(prefHelper.getCarTypes() == null)) {
            carTypeList = prefHelper.getCarTypes();
        } else {
            serviceHelper.enqueueCall(headerWebService.getVehicles(), WebServiceConstants.getVehicles);
        }

        final DialogHelper selecCarDialoge = new DialogHelper(getDockActivity());

        selecCarDialoge.selectCar(R.layout.dialoge_select_car, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity().getWindow().getDecorView());
                selecCarDialoge.hideDialog();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity().getWindow().getDecorView());

                try {
                    if (selecCarDialoge.getSelectedType() != null) {
                        selecCarDialoge.hideDialog();
                        selectCarEnt = selecCarDialoge.getSelectedType();

                        String percentage = "";
                        if (promoCodeEnt != null)
                            percentage = promoCodeEnt.getPercentage();

                        if (selectCarEnt.getId() != null) {
                            serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                                    checkForNullOREmpty(percentage), distance + "", time_duration + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARE);
                        } else {
                            selecCarDialoge.hideDialog();
                        }


                    } else {
                        selecCarDialoge.hideDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    selecCarDialoge.hideDialog();
                }

            }
        }, carTypeList, selectCarEnt);
        selecCarDialoge.setCancelable(false);
        selecCarDialoge.showDialog();
    }

    private void initSelectCarLaterDialog() {

        if (!(prefHelper.getCarTypes() == null)) {
            carTypeList = prefHelper.getCarTypes();
        } else {
            serviceHelper.enqueueCall(headerWebService.getVehicles(), WebServiceConstants.getVehiclesLaterRide);
        }

        final DialogHelper selecCarDialoge = new DialogHelper(getDockActivity());

        selecCarDialoge.selectCar(R.layout.dialoge_select_car, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity().getWindow().getDecorView());
                selecCarDialoge.hideDialog();

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity().getWindow().getDecorView());

                try {
                    if (selecCarDialoge.getSelectedType() != null) {
                        selecCarDialoge.hideDialog();
                        selectCarEnt = selecCarDialoge.getSelectedType();

                        String percentage = "";
                        if (promoCodeEnt != null)
                            percentage = promoCodeEnt.getPercentage();

                        if (selectCarEnt.getId() != null) {
                            serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                                    checkForNullOREmpty(percentage), distance + "", time_duration + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARELATER);
                        } else {
                            selecCarDialoge.hideDialog();
                        }


                    } else {
                        selecCarDialoge.hideDialog();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    selecCarDialoge.hideDialog();
                }

               /* selecCarDialoge.hideDialog();
                selectCarEnt = selecCarDialoge.getSelectedType();

                String percentage = "";
                if (promoCodeEnt != null)
                    percentage = promoCodeEnt.getPercentage();

                if (selectCarEnt.getId() != null) {
                    serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                            checkForNullOREmpty(percentage), distance + "", time_duration + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARELATER);
                } else {
                    selecCarDialoge.hideDialog();
                }
*/
            }
        }, carTypeList, selectCarEnt);
        selecCarDialoge.setCancelable(false);
        selecCarDialoge.showDialog();
    }

    private void setRequestCancelDialog(ArrayList<CancelReasonEnt> arrayList) {
        final Dialog dialog = new Dialog(getDockActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.request_for_cancellation_dialog);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_ok);
        ListView listView = (ListView) dialog.findViewById(R.id.lv_listview);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_cancel);


     /*   for (int i = 1; i <= 8; i++) {
            arrayList.add("I'm getting late");
        }*/


        final ReasonCancelListViewAdapter adapter = new ReasonCancelListViewAdapter(getDockActivity(), arrayList, true);
        listView.setAdapter(adapter);

        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                serviceHelper.enqueueCall(headerWebService.ChangeRideStatus(String.valueOf(rideEnt.getId()), adapter.getSelectedItem()), CANCEL_RIDE);
            }
        });

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

    }

    private void initEstimateFareBottomSheet(final EstimateFareEnt result) {
        estimateFareDialog = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_dialog_estimate_fare);
        estimateFareDialog.initEstimateFareBottomSheet(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               // estimateFareDialog.hideDialog();

                                                               String percentage = "";
                                                               if (promoCodeEnt != null)
                                                                   percentage = promoCodeEnt.getPercentage();

                                                               serviceHelper.enqueueCall(headerWebService.createNewRide(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude),
                                                                       origin.getAddress(), origin.getAddress(), String.valueOf(destination.getLatlng().latitude), String.valueOf(destination.getLatlng().longitude),
                                                                       destination.getAddress(), destination.getAddress(), selectCarEnt.getId() + "", "Cash", percentage + "", "", "", STATUS_RIDENOW, result.getEstimateFare(), time_duration + "", distance + "", checkForNullOREmpty(peakFactor), prefHelper.getUser().getUserPreference()), CREATE);

                                                           }
                                                       }, selectCarEnt.getType(),
                selectCarEnt.getCapacity() + "",
                selectCarEnt.getVehicleImageOne() + "", Integer.parseInt(result.getEstimateFare() + ""));
        estimateFareDialog.showDialog();
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estimateFareDialog.hideDialog();
                setupRideNowDialog();
            }
        });
    }

    private void hideRideSelectionViews() {

        googleMap.clear();
        customMarkerView.setVisibility(View.GONE);
        llDestination.setVisibility(View.GONE);
        btndoneselection.setVisibility(View.GONE);
        layoutdestination.setVisibility(View.GONE);
        layoutpick.setVisibility(View.GONE);
        btnRidenow.setVisibility(View.GONE);
        btnRidelater.setVisibility(View.GONE);

    }

    private void showFindRideViews(ArrayList<DriverEnt> result) {
        if (titleBar != null) {
            titleBar.hideButtons();
            titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));

        }
        final Circle mCircle;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carwithcircle);
        double lat = origin.getLatlng().latitude;
        double lng = origin.getLatlng().longitude;

        for (DriverEnt ent : result
                ) {
            googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ent.getLatitude()), Double.parseDouble(ent.getLongitude())))
                    .icon(icon));
        }

        googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.drive_circle)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin.getLatlng(), 17));
        googleMap.animateCamera(zoom);

        /* mCircle = googleMap.addCircle(new CircleOptions()
                .center(origin.getLatlng())
                .radius(6000)
                .strokeColor(0xFF1A1F23)
                .fillColor(0xFF82858C));
       ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setIntValues(0, 100);
        valueAnimator.setDuration(3000);
        valueAnimator.setEvaluator(new IntEvaluator());
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                mCircle.setRadius(animatedFraction * 100);
            }
        });*/


        pulsator.start();
        stopMovingMap();


        // valueAnimator.start();
        findingRide.setVisibility(View.VISIBLE);
        btnCancelRide.setVisibility(View.VISIBLE);


    }

    private void hideScheduleViews() {
        btnRidenow.setVisibility(View.VISIBLE);
        btnRidelater.setVisibility(View.VISIBLE);
        layoutSchedule.setVisibility(View.GONE);
        llSourceDestination.setVisibility(View.VISIBLE);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));
    }

    private void initdestinationLocationSelect() {
        googleMap.clear();
        showAllDrivers(null, false);
        llSourceDestination.setVisibility(View.VISIBLE);
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartPickupActivity(10);
            }
        });
        // googleMap.setMyLocationEnabled(false);
        btnLocation.setVisibility(View.GONE);
        customMarkerView.setVisibility(View.VISIBLE);
        imgIcon.setVisibility(View.VISIBLE);
        circle.setVisibility(View.GONE);
        ivTick.setVisibility(View.GONE);
        txtMarker.setVisibility(View.GONE);

        // txtMarker.setText(duration);
        imgIcon.setImageResource(R.drawable.drive_location);
        txtLocationtype.setVisibility(View.GONE);
        llDestination.setVisibility(View.GONE);
        layoutdestination.setVisibility(View.VISIBLE);
        layoutpick.setVisibility(View.VISIBLE);
        btndoneselection.setVisibility(View.VISIBLE);
        btnRidenow.setVisibility(View.GONE);
        btnRidelater.setVisibility(View.GONE);
        if (destination == null) {
            destination = origin;
        }
        movemap(destination.getLatlng());
        if (destination != null) {
            String des = destination.getAddress();
            if (destination.getAddress().contains("null")) {
                des = destination.getAddress().replace("null,", "");
            }
            txtDestinationText.setText(des);
        }
        if (origin != null) {
            String org = origin.getAddress();
            if (origin.getAddress().contains("null")) {
                org = origin.getAddress().replace("null,", "");
            }
            txtPickText.setText(org);
        }

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                String address = getCurrentAddress(cameraPosition.target.latitude, cameraPosition.target.longitude);
                // serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(cameraPosition.target.latitude), String.valueOf(cameraPosition.target.longitude)), HOMEDRIVERS);

                if (address != null) {
                    destination = new LocationEnt(address,
                            cameraPosition.target);
                } else {
                    destination = new LocationEnt("Un Named Street",
                            cameraPosition.target);
                }
                String des = destination.getAddress();
                if (destination != null && destination.getLatlng() != null && destination.getAddress().contains("null")) {
                    des = destination.getAddress().replace("null,", "");
                }
                txtDestinationText.setText(des);

            }
        });
    }

    private void initRideStatus() {
        googleMap.clear();
        customMarkerView.setVisibility(View.GONE);
        showAllDrivers(null, false);
        btnLocation.setVisibility(View.GONE);
        llSourceDestination.setVisibility(View.VISIBLE);
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartPickupActivity(10);
            }
        });
        //  titleBar.showMenuButton();
        //googleMap.setMyLocationEnabled(false);

        llDestination.setVisibility(View.GONE);
        btndoneselection.setVisibility(View.GONE);
        layoutdestination.setVisibility(View.VISIBLE);
        layoutpick.setVisibility(View.VISIBLE);
        btnRidenow.setVisibility(View.VISIBLE);
        btnRidelater.setVisibility(View.VISIBLE);

        if (origin != null && origin.getLatlng() != null)
            serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(origin.getLatlng().latitude + "", origin.getLatlng().longitude + ""), PeakFactor, false);

      /*  pickupMarker = googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.drive_pin,
                        duration, R.color.black))));*/
        BitmapDescriptor pickupMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.profilecircle);
        if (origin != null && origin.getLatlng() != null) {
            pickupMarker = googleMap.addMarker(new MarkerOptions().position(origin.getLatlng())
                    .icon(pickupMarkerIcon));
        }
        BitmapDescriptor DestinationIcon = BitmapDescriptorFactory.fromResource(R.drawable.drive_location);
        if (destination != null && destination.getLatlng() != null) {
            googleMap.addMarker(new MarkerOptions().position(destination.getLatlng()).icon(DestinationIcon));
        }
        if (origin != null && origin.getLatlng() != null && destination != null && destination.getLatlng() != null) {
            moveMapBetweenMarker(origin.getLatlng(), destination.getLatlng());
        }
        setRoute();


        if (destination != null) {
            String des = destination.getAddress();
            if (destination.getAddress().contains("null")) {
                des = destination.getAddress().replace("null,", "");
            }
            txtDestinationText.setText(des);
        }
        if (origin != null) {
            String org = origin.getAddress();
            if (origin.getAddress().contains("null")) {
                org = origin.getAddress().replace("null,", "");
            }
            txtPickText.setText(org);
        }
        googleMap.setOnCameraChangeListener(null);


    }

    private void moveMapBetweenMarker(LatLng origin, LatLng destination) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (origin != null && destination != null) {
            builder.include(origin);
            builder.include(destination);

            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {

                    //   CameraUpdate zout = CameraUpdateFactory.zoomBy(-3.0f);
                    //  googleMap.animateCamera(zout);
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-0.5f);
                    googleMap.animateCamera(zout);

                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle b = data.getBundleExtra("route");
            if (b != null) {
                origin = new Gson().fromJson(b.getString("origin"), LocationEnt.class);
                destination = new Gson().fromJson(b.getString("destination"), LocationEnt.class);
                boolean setEmpty = b.getBoolean("backPressed");
                boolean setonMap = b.getBoolean("setonMap");

                isActivityResult = true;

                customMarkerView.setVisibility(View.GONE);
                llDestination.setVisibility(View.GONE);
                btnLocation.setVisibility(View.GONE);
                findingRide.setVisibility(View.GONE);

                selectCarEnt = null;
                promoCodeEnt = null;
                estimateFareEnt = null;

                if (setonMap) {
                    // serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude)), HOMEDRIVERS);
                    showAllDrivers(null, false);
                    initdestinationLocationSelect();
                } else if (setEmpty) {
                    getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(), "HomeMapFragment");
                } else
                    initRideStatus();

            }
        }
    }

    @Override
    public void ResponseSuccess(Object result, String Tag, String message) throws ClassCastException {


        switch (Tag) {
            case CANCELREASON:
                setRequestCancelDialog((ArrayList<CancelReasonEnt>) result);
                break;
            case PROMOCODE:
                // UIHelper.showShortToastInCenter(getDockActivity(), "Promo code added");
                promoCodeEnt = (PromoCodeEnt) result;
                if (selectCarEnt != null) {
                    serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                            checkForNullOREmpty(promoCodeEnt.getPercentage()), distance + "", time_duration + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARE);
                } else {
                    setupRideDialoge.hideDialog();
                    setupRideNowDialog();
                }


                break;

            case PROMOCODELATER:
                // UIHelper.showShortToastInCenter(getDockActivity(), "Promo code added");
                promoCodeEnt = (PromoCodeEnt) result;
                if (selectCarEnt != null) {
                    serviceHelper.enqueueCall(headerWebService.getRideEstimate(String.valueOf(selectCarEnt.getId()),
                            checkForNullOREmpty(promoCodeEnt.getPercentage()), distance + "", time_duration + "", checkForNullOREmpty(peakFactor)), ESTIMATEFARELATER);
                } else {
                    setupRideDialoge.hideDialog();
                    setupScheduleDialog();
                }


                break;
            case ESTIMATEFARE:
                setupRideDialoge.hideDialog();
                estimateFareEnt = (EstimateFareEnt) result;
                setupRideNowDialog();


                //  initEstimateFareBottomSheet((EstimateFareEnt) result);
                break;

            case ESTIMATEFARELATER:
                setupRideDialoge.hideDialog();

                estimateFareEnt = (EstimateFareEnt) result;
                setupScheduleDialog();

                //initEstimateFareBottomSheetLater((EstimateFareEnt) result);
                break;

            case CREATE:
                rideEnt = (CreateRideEnt) result;
                if (message.equals("Ride has been created successfully")) {
                    setupRideDialoge.hideDialog();
                    serviceHelper.enqueueCall(headerWebService.getNearbyDrivers(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude)), NEARBY);
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), message);
                }
                break;
            case NEARBY:
                if (setupRideDialoge != null)
                    setupRideDialoge.hideDialog();
                hideRideSelectionViews();
                AllDriversHomeEnt entityNearByDrivers = (AllDriversHomeEnt) result;
                showFindRideViews(entityNearByDrivers.getDriverList());
                break;

            case HOMEDRIVERS:

                AllDriversHomeEnt entityHome = (AllDriversHomeEnt) result;
                showAllDrivers(entityHome.getDriverList(), true);

                if (entityHome.getPeakFactor() != null && !entityHome.getPeakFactor().equals("")) {

                    peakFactor = entityHome.getPeakFactor() + "";
                    prefHelper.setPeakFactor(peakFactor);
                    if (txtPeakFactor != null && peakFactor != null)
                        txtPeakFactor.setText(peakFactor);

                } else {
                    peakFactor = "";
                    prefHelper.setPeakFactor(peakFactor);

                }
                if (customMarkerView != null)
                    customMarkerView.setVisibility(View.VISIBLE);
                if (entityHome.getDriverList().size() > 0) {
                    duration = entityHome.getDriverList().get(0).getDuration();
                } else {
                    duration = "No Car";
                }
                if (circle != null && txtMarker != null) {
                    circle.setText(duration);
                    txtMarker.setText(duration);
                }

                if (isScheduleKey) {
                    isScheduleKey = false;
                    final DialogHelper dialogHelper = new DialogHelper(getDockActivity());
                    dialogHelper.scheduleDialoge(R.layout.schedule_ride_dialoge, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogHelper.hideDialog();
                        }
                    });

                    dialogHelper.showDialog();
                }
                break;

            case PeakFactor:

                AllDriversHomeEnt entityHome1 = (AllDriversHomeEnt) result;
                //showAllDrivers(entityHome1.getDriverList(),true);

                if (entityHome1.getPeakFactor() != null && !entityHome1.getPeakFactor().equals("")) {

                    peakFactor = entityHome1.getPeakFactor() + "";
                    prefHelper.setPeakFactor(peakFactor);

                } else {
                    peakFactor = "";
                    prefHelper.setPeakFactor(peakFactor);

                }

                break;


            case APPROVE_DRIVER:
                if (result != null) {

                    prefHelper.setRideInSession(true);
                    driverDetail = ((RideDriverEnt) result).getDriverDetail();
                    rideDriverEnt = (RideDriverEnt) result;
                    findingRide.setVisibility(View.GONE);
                    startMoveMap();
                    btnLocation.setVisibility(View.VISIBLE);

                    pulsator.stop();
                    pulsator.clearAnimation();
                    pulsator.setVisibility(View.GONE);
                    btnCancelRide.setVisibility(View.GONE);

                    if (rideTrackEntity != null) {
                        rideTrackEntity.setStatus("1");
                        ShowrideReachingDialog(rideDriverEnt);
                       /* if (isFromNotification && isScheduleRide) {
                            setScheduleRideRoute(rideDriverEnt);
                        } else if (isFromNotification && !isScheduleRide) {
                            setRoute();
                        } else {

                        }*/
                    } else {
                        serviceHelper.enqueueCall(headerWebService.rideTracking(rideDriverEnt.getRideId() + ""), rideTracking, false);
                    }

                }
                break;

            case START_RIDE:
                if (result != null) {

                    if (rideTrackEntity != null)
                        rideTrackEntity.setStatus("5");
                    rideDriverEnt = (RideDriverEnt) result;
                    driverDetail = ((RideDriverEnt) result).getDriverDetail();
                    prefHelper.setRideInSession(true);
                    findingRide.setVisibility(View.GONE);
                    startMoveMap();
                    googleMap.getUiSettings().setScrollGesturesEnabled(true);
                    pulsator.stop();
                    btnCancelRide.setVisibility(View.GONE);

                    if (rideDriverEnt.getRideDetail().getType().equals("later") && rideDriverEnt.getRideDetail().getStatus().equals("4") || rideDriverEnt.getRideDetail().getStatus().equals("5")) {
                        prefHelper.setIsScheduleRide(true);
                        isScheduleRide = true;
                    }


                    if (rideReaching != null) {
                        Button cancelButton = rideReaching.getDesiredButton(R.id.btn_cancel_ride);
                        if (cancelButton != null) {
                            cancelButton.setEnabled(false);
                            cancelButton.setVisibility(View.GONE);

                        }
                    }

                    ShowrideReachingDialog(rideDriverEnt);

                  /*  if (isFromNotification && isScheduleRide) {
                        setScheduleRideRoute(rideDriverEnt);
                    }*//* else if (isFromNotification && !isScheduleRide) {
                        setRoute();
                    }*//* else {
                        ShowrideReachingDialog(rideDriverEnt);
                    }
                    //  setScheduleRideRoute(rideDriverEnt);*/

                }
                break;
            case RIDE_END_TRIP:
                driverDetail = ((RideDriverEnt) result).getDriverDetail();
                rideDriverEnt = (RideDriverEnt) result;
                prefHelper.settripStatus(false);

                if (rideTrackEntity != null)
                    rideTrackEntity.setStatus("6");

                if (!rideDriverEnt.getRideDetail().getType().equals("later")) {

                    if (prefHelper.geIsScheduleRide()) {
                        prefHelper.setRideInSession(false);
                        prefHelper.removeRideSessionPreferences();
                        prefHelper.setIsScheduleRide(true);
                    } else {
                        prefHelper.setRideInSession(false);
                        prefHelper.removeRideSessionPreferences();
                    }

                    getDockActivity().replaceDockableFragment(DriverRatingFragment.newInstance(rideDriverEnt), "DriverRatingFragment");
                } else {
                    prefHelper.setRideInSession(false);
                    prefHelper.setIsScheduleRide(false);
                    isScheduleRide = false;
                    prefHelper.removeRideSessionPreferences();
                    driverDetail = ((RideDriverEnt) result).getDriverDetail();
                    rideDriverEnt = (RideDriverEnt) result;
                    getDockActivity().replaceDockableFragment(DriverRatingFragment.newInstance(rideDriverEnt), "DriverRatingFragment");
                    // setupRatingDialog(rideDriverEnt, CURRENT_RATING);
                }
                break;
            case CANCEL_RIDE:
                prefHelper.setRideInSession(false);
                prefHelper.removeRideSessionPreferences();
                getDockActivity().popBackStackTillEntry(0);
                getMainActivity().initFragment(true);

                break;
            case RIDE_LATER:
                prefHelper.setIsScheduleRide(true);
                isScheduleRide = true;
//                estimateFareDialog.hideDialog();
                getDockActivity().popBackStackTillEntry(0);
                //  getDockActivity().replaceDockableFragment(TripsFragment.newInstance(true), TripsFragment.class.getSimpleName());
                getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(true), "HomeMapFragment");
                break;
            case RIDE_RATING_current:
                prefHelper.setRideInSession(false);
                prefHelper.removeRideSessionPreferences();
                getDockActivity().popBackStackTillEntry(0);
                getDockActivity().replaceDockableFragment(RideFeedbackFragment.newInstance(RideIdFeedBack, DriverIdFeedback), RideFeedbackFragment.class.getSimpleName());

                break;
            case RIDE_RATING_last:
                if (ratingDialog != null)
                    ratingDialog.hideDialog();
                setupRideNowDialog();
                break;
            case WebServiceConstants.getVehicles:

                prefHelper.putCarTypes((ArrayList<SelectCarEnt>) result);
                carTypeList = prefHelper.getCarTypes();
                setupRideNowDialog();
                break;

            case WebServiceConstants.getVehiclesLaterRide:
                prefHelper.putCarTypes((ArrayList<SelectCarEnt>) result);
                carTypeList = prefHelper.getCarTypes();
                setupScheduleDialog();
                break;


            case RIDE_LAST_RATING:
                RideDriverEnt ent = (RideDriverEnt) result;

                if (ent == null) {
                    setupRideNowDialog();
                } else if (ent.getMessage() != null && ent.getMessage().equals("Please first pay last ride charges")) {
                    final DialogHelper lastRidePayment = new DialogHelper(getDockActivity());
                    lastRidePayment.LastRidePayment(R.layout.dialog_last_ride_payment, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lastRidePayment.hideDialog();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            lastRidePayment.hideDialog();
                            getDockActivity().replaceDockableFragment(CreditCardDetailFragment.newInstance(), CreditCardDetailFragment.class.getSimpleName());
                        }
                    });
                    lastRidePayment.showDialog();
                } else {
                    setupRatingDialog(ent, LAST_RATING);
                }

                break;

            case isRideIsGoing:
                RideIdEnt rideEnt = (RideIdEnt) result;

                if (rideEnt.getId() != null) {
                    prefHelper.setRideInSession(true);
                    if (llDestination != null)
                        llDestination.setVisibility(View.GONE);

                    if (!isActivityResult) {
                        serviceHelper.enqueueCall(headerWebService.rideTracking(rideEnt.getId() + ""), rideTracking, false);
                        if (btnLocation != null)
                            btnLocation.setVisibility(View.VISIBLE);
                    }
                    // serviceHelper.enqueueCall(headerWebService.rideTracking(rideEnt.getId() + ""), rideTracking, false);

                } else {
                    if (origin == null || origin.getLatlng().equals(new LatLng(0, 0))) {
                        getCurrentLocation();
                        if (llDestination != null && btnLocation != null) {
                            llDestination.setVisibility(View.VISIBLE);
                            btnLocation.setVisibility(View.VISIBLE);
                        }
                    }
                }

                break;

            case rideTracking:
                RideTrackEnt rideTrackingEnt = (RideTrackEnt) result;

                if (rideTrackingEnt != null) {
                    rideTrackEntity = rideTrackingEnt;
                    RestoreState(rideTrackingEnt);

                }

                break;

            case GETSetting:
                getSettingeEnt getSettinge = (getSettingeEnt) result;
                cancelTimeValue = getSettinge.getValue();
                setcanceldialog(getSettinge.getValue());


                break;


        }
    }

    private void stopMovingMap() {
        googleMap.getUiSettings().setScrollGesturesEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(false);
    }

    private void startMoveMap() {
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
    }


    private void setScheduleRideRoute(RideDriverEnt result) {

        if (result != null) {

            LatLng pick = new LatLng(Double.parseDouble(result.getRideDetail().getPickupLatitude()), Double.parseDouble(result.getRideDetail().getPickupLongitude()));
            LatLng destinat = new LatLng(Double.parseDouble(result.getRideDetail().getDestinationLatitude()), Double.parseDouble(result.getRideDetail().getDestinationLongitude()));
            String origin_string = String.valueOf(pick.latitude) + "," + String.valueOf(pick.longitude);
            String destination_string = String.valueOf(destinat.latitude) + "," + String.valueOf(destinat.longitude);
            originScheduleRide = new LocationEnt(result.getRideDetail().getPickupAddress(), pick, result.getRideDetail().getPickupPlace());
            destinationScheduleRide = new LocationEnt(result.getRideDetail().getDestinationAddress(), destinat, result.getRideDetail().getDestinationPlace());
            sendRequest(origin_string, destination_string);
        }


    }

    private void initEstimateFareBottomSheetLater(final EstimateFareEnt result) {

        estimateFareDialog = new BottomSheetDialogHelper(getDockActivity(), Main_frame, R.layout.bottom_dialog_estimate_fare);
        estimateFareDialog.initEstimateFareBottomSheet(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               estimateFareDialog.hideDialog();
                                                               String percentage = "";
                                                               if (promoCodeEnt != null)
                                                                   percentage = promoCodeEnt.getPercentage();

                                                               String SelectedDate = "", SelectedTime = "";
                                                               if (DateSelected != null && TimeSelected != null) {
                                                                   SelectedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(DateSelected.getTime());
                                                                   SelectedTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(TimeSelected.getTime());
                                                               }

                                                               serviceHelper.enqueueCall(headerWebService.createNewRide(String.valueOf(origin.getLatlng().latitude), String.valueOf(origin.getLatlng().longitude),
                                                                       origin.getAddress(), origin.getAddress(), String.valueOf(destination.getLatlng().latitude), String.valueOf(destination.getLatlng().longitude),
                                                                       destination.getAddress(), destination.getAddress(), selectCarEnt.getId() + "", "Cash", percentage + "", SelectedDate, SelectedTime, STATUS_RIDELATER, result.getEstimateFare(), time_duration + "", distance + "", checkForNullOREmpty(peakFactor), prefHelper.getUser().getUserPreference()), RIDE_LATER);


                                                           }
                                                       }, selectCarEnt.getType(),
                selectCarEnt.getCapacity() + "",
                selectCarEnt.getVehicleImageOne() + "", Integer.parseInt(result.getEstimateFare() + ""));
        estimateFareDialog.showDialog();
        titleBar.hideButtons();
        titleBar.setSubHeading(getDockActivity().getResources().getString(R.string.home));
        titleBar.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estimateFareDialog.hideDialog();
                setupRideNowDialog();
            }
        });


    }

    private void showAllDrivers(ArrayList<DriverEnt> result, boolean show) {

        if (show) {
            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.carwithcircle);
            for (DriverEnt ent : result) {
                if (ent.getLatitude() != null && !ent.getLatitude().equals(""))
                    googleMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(ent.getLatitude()), Double.parseDouble(ent.getLongitude())))
                            .icon(icon));
            }
        }

    }

    @Override
    public void ResponseFailure(String tag) {
        loadingFinished();
    }

    private void initTimePicker(final TextView textView) {
        if (DateSelected != null) {
            TimePickerDialog dialog = new TimePickerDialog(getDockActivity(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Date date = new Date();
                    Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);
                    c.set(year, month, day, hourOfDay, minute);
                    Calendar calcurrent = Calendar.getInstance();
                    calcurrent.setTime(date);
                    int currentHour = calcurrent.get(Calendar.HOUR_OF_DAY);
                    int currentMinute = calcurrent.get(Calendar.MINUTE);
                    calcurrent.set(Calendar.MINUTE, currentMinute + 30);
                    int addedHalfhour = calcurrent.get(Calendar.HOUR_OF_DAY);
                    int addedHalfhourMinute = calcurrent.get(Calendar.MINUTE);


                    if (DateHelper.isSameDay(DateSelected, date) && !DateHelper.isTimeAfter(currentHour, currentMinute, hourOfDay, minute)) {
                        UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.less_time_error));

                    } else if (DateHelper.isSameDay(DateSelected, date) && !DateHelper.isTimeAfter(addedHalfhour, addedHalfhourMinute, hourOfDay, minute)) {
                        UIHelper.showShortToastInCenter(getDockActivity(), "Select Time 30 min after current time");

                    } else {

                        TimeSelected = c.getTime();
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, month, day, hourOfDay, minute + 15);
                        String preTime = new SimpleDateFormat("HH:mm a").format(c.getTime()) + "-" +
                                new SimpleDateFormat("HH:mm a").format(cal.getTime());
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(preTime);
                        textView.setPaintFlags(Typeface.BOLD);
                    }
                }
            }, DateSelected.getHours(), DateSelected.getMinutes(), false);

            dialog.show();
        } else {
            UIHelper.showShortToastInCenter(getDockActivity(), getResources().getString(R.string.select_pickup_date_first));
        }
    }

    private boolean timeComparision(int hour, int min) {

        SimpleDateFormat parser = new SimpleDateFormat("HH:mm");

        try {

            Date currentTime = new Date();
            String time = hour + ":" + min;
            Date userDate = parser.parse(time);

            if (userDate.before(currentTime) && userDate.after(currentTime)) {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void initDatePicker(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        final DatePickerHelper datePickerHelper = new DatePickerHelper();
        datePickerHelper.initDateDialog(
                getDockActivity(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
                , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Date date = new Date();
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

// and get that as a Date
                        Date dateSpecified = c.getTime();
                        if (dateSpecified.before(date)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.date_before_error));
                        } else {
                            DateSelected = dateSpecified;
                            String predate = new SimpleDateFormat("EEE,MMM d").format(c.getTime());
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(predate);
                            textView.setPaintFlags(Typeface.BOLD);
                        }

                    }
                }, "PreferredDate");

        datePickerHelper.showDate();
    }

    private void SaveCurrentState() {
        if (prefHelper.getRideInSession()) {
            UserHomeEnt home = new UserHomeEnt();
            home.setLatitude(latitude);
            home.setLongitude(longitude);
            home.setScheduleRide(isScheduleRide);
            home.setOrigin(origin);
            home.setDestination(destination);
            home.setDistance(distance);
            home.setPromoCodeEnt(promoCodeEnt);
            home.setSelectCarEnt(selectCarEnt);
            home.setRideEnt(rideEnt);
            home.setDriverDetail(driverDetail);
            home.setRideinSession(isRideinSession);
            home.setRideDriverEnt(rideDriverEnt);
            home.setOriginScheduleRide(originScheduleRide);
            home.setDestinationScheduleRide(destinationScheduleRide);
            home.setDriverId(prefHelper.getDriverId());
            prefHelper.putUserHome(home);
        }

    }

    private void RestoreState(RideTrackEnt rideTrackEntity) {


        if (rideTrackEntity != null && rideTrackEntity.getDriverInfo()!=null && rideTrackEntity.getVehicleInfo()!=null && rideTrackEntity.getType().equals("current")) {
            if ((rideTrackEntity != null && rideTrackEntity.getStatus().equals("1")) || (rideTrackEntity != null && rideTrackEntity.getStatus().equals("4")) || (rideTrackEntity != null && rideTrackEntity.getStatus().equals("5"))) {

                prefHelper.setRideInSession(true);

                DriverId = rideTrackEntity.getDriverInfo().getId() + "";
                isScheduleRide = rideTrackEntity.getType().equals("current") ? false : true;
                distance = Double.parseDouble(rideTrackEntity.getDistance());
                driverDetail = rideTrackEntity.getDriverInfo();
                isRideinSession = prefHelper.getRideInSession();

                origin = new LocationEnt(rideTrackEntity.getPickupAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getPickupLatitude()), Double.parseDouble(rideTrackEntity.getPickupLongitude())));
                destination = new LocationEnt(rideTrackEntity.getDestinationAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getDestinationLatitude()), Double.parseDouble(rideTrackEntity.getDestinationLongitude())));
                originScheduleRide = new LocationEnt(rideTrackEntity.getPickupAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getPickupLatitude()), Double.parseDouble(rideTrackEntity.getPickupLongitude())));
                destinationScheduleRide = new LocationEnt(rideTrackEntity.getDestinationAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getDestinationLatitude()), Double.parseDouble(rideTrackEntity.getDestinationLongitude())));

                // latitude = prefHelper.getUserHome().getLatitude();
                //  longitude = prefHelper.getUserHome().getLongitude();
                //promoCodeEnt = prefHelper.getUserHome().getPromoCodeEnt();
                //selectCarEnt = prefHelper.getUserHome().getSelectCarEnt();
                rideEnt = new CreateRideEnt();
                setRideEntData(rideTrackEntity);
                rideDriverEnt = new RideDriverEnt();
                setRideDriverEntData(rideTrackEntity);

                if (rideDriverEnt != null) {
                    if (rideTrackEntity.getStatus().equals("1") || rideTrackEntity.getStatus().equals("4") || rideTrackEntity.getStatus().equals("5")) {
                        ShowrideReachingDialog(rideDriverEnt);
                    }

                }
            } else {
                prefHelper.setRideInSession(false);
            }
        } else if (rideTrackEntity != null && rideTrackEntity.getDriverInfo()!=null && rideTrackEntity.getVehicleInfo()!=null && rideTrackEntity.getType().equals("later")) {
            if (!(DateHelper.timeComparision(rideTrackEntity.getTime(), rideTrackEntity.getDate()))) {
                if (rideTrackEntity.getStatus().equals("1") || rideTrackEntity.getStatus().equals("4") || rideTrackEntity.getStatus().equals("5")) {
                    prefHelper.setRideInSession(true);

                    DriverId = rideTrackEntity.getDriverInfo().getId() + "";
                    isScheduleRide = rideTrackEntity.getType().equals("current") ? false : true;
                    distance = Double.parseDouble(rideTrackEntity.getDistance());
                    driverDetail = rideTrackEntity.getDriverInfo();
                    isRideinSession = prefHelper.getRideInSession();

                    origin = new LocationEnt(rideTrackEntity.getPickupAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getPickupLatitude()), Double.parseDouble(rideTrackEntity.getPickupLongitude())));
                    destination = new LocationEnt(rideTrackEntity.getDestinationAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getDestinationLatitude()), Double.parseDouble(rideTrackEntity.getDestinationLongitude())));
                    originScheduleRide = new LocationEnt(rideTrackEntity.getPickupAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getPickupLatitude()), Double.parseDouble(rideTrackEntity.getPickupLongitude())));
                    destinationScheduleRide = new LocationEnt(rideTrackEntity.getDestinationAddress(), new LatLng(Double.parseDouble(rideTrackEntity.getDestinationLatitude()), Double.parseDouble(rideTrackEntity.getDestinationLongitude())));

                    rideEnt = new CreateRideEnt();
                    setRideEntData(rideTrackEntity);
                    rideDriverEnt = new RideDriverEnt();
                    setRideDriverEntData(rideTrackEntity);

                    ShowrideReachingDialog(rideDriverEnt);
                } else {
                    getCurrentLocation();

                }
            } else {
                getCurrentLocation();
                if (llDestination != null && btnLocation != null) {
                    if (!isActivityResult) {
                        llDestination.setVisibility(View.VISIBLE);
                        btnLocation.setVisibility(View.VISIBLE);

                    } else {
                        llDestination.setVisibility(View.GONE);
                        btnLocation.setVisibility(View.GONE);
                    }
                }
            }
        }


        /*if (prefHelper.getUserHome() != null && prefHelper.getRideInSession()) {
            DriverId = prefHelper.getUserHome().getDriverId();
            latitude = prefHelper.getUserHome().getLatitude();
            longitude = prefHelper.getUserHome().getLongitude();
            isScheduleRide = prefHelper.getUserHome().isScheduleRide();
            destination = prefHelper.getUserHome().getDestination();
            distance = prefHelper.getUserHome().getDistance();
            promoCodeEnt = prefHelper.getUserHome().getPromoCodeEnt();
            selectCarEnt = prefHelper.getUserHome().getSelectCarEnt();
            rideEnt = prefHelper.getUserHome().getRideEnt();
            driverDetail = prefHelper.getUserHome().getDriverDetail();
            isRideinSession = prefHelper.getRideInSession();
            rideDriverEnt = prefHelper.getUserHome().getRideDriverEnt();
            origin = prefHelper.getUserHome().getOrigin();
            originScheduleRide = prefHelper.getUserHome().getOriginScheduleRide();
            destinationScheduleRide = prefHelper.getUserHome().getDestinationScheduleRide();

            if (prefHelper.getRideInSession()) {
                ShowrideReachingDialog(rideDriverEnt);

            }
        }*/
    }

    private void setRideDriverEntData(RideTrackEnt rideTrackEntity) {

        rideDriverEnt.setRideId(rideTrackEntity.getId());
        rideDriverEnt.setDriverId(rideTrackEntity.getDriverInfo().getId());
        rideDriverEnt.setDriverDetail(rideTrackEntity.getDriverInfo());

        rideDriverEnt.setRideStatus(1);
        rideDriverEnt.setTripStatus(1);


        VehicleDetail vehicleDetail = new VehicleDetail();
        vehicleDetail.setId(rideTrackEntity.getVehicleInfo().getId());
        vehicleDetail.setDriverId(rideTrackEntity.getVehicleInfo().getDriverId());
        vehicleDetail.setUserId(rideTrackEntity.getUserInfo().getId());
        vehicleDetail.setVehicleName(rideTrackEntity.getVehicleInfo().getVehicleName());
        vehicleDetail.setVehicleColor(rideTrackEntity.getVehicleInfo().getVehicleColor());
        vehicleDetail.setVehicleImage(rideTrackEntity.getVehicleInfo().getVehicleImage());
        vehicleDetail.setVehicleModel(rideTrackEntity.getVehicleInfo().getVehicleModel());
        vehicleDetail.setVehicleNumber(rideTrackEntity.getVehicleInfo().getVehicleNumber());
        vehicleDetail.setVehiclePicture(rideTrackEntity.getVehicleInfo().getVehiclePicture());
        rideDriverEnt.setVehicleDetail(vehicleDetail);

        RideEnt rideEnt = new RideEnt();
        rideEnt.setId(rideTrackEntity.getId());
        rideEnt.setUserId(rideTrackEntity.getUserId());
        rideEnt.setCancelId(rideTrackEntity.getCancelId());
        rideEnt.setPickupAddress(rideTrackEntity.getPickupAddress());
        rideEnt.setPickupLatitude(rideTrackEntity.getPickupLatitude());
        rideEnt.setPickupLongitude(rideTrackEntity.getPickupLongitude());
        rideEnt.setPickupPlace(rideTrackEntity.getPickupPlace());
        rideEnt.setDestinationAddress(rideTrackEntity.getDestinationAddress());
        rideEnt.setDestinationLatitude(rideTrackEntity.getDestinationLatitude());
        rideEnt.setDestinationLongitude(rideTrackEntity.getDestinationLongitude());
        rideEnt.setDestinationPlace(rideTrackEntity.getDestinationPlace());
        rideEnt.setVehicleId(Integer.valueOf(rideTrackEntity.getVehicleId()));
        rideEnt.setDate(rideTrackEntity.getDate());
        rideEnt.setTime(rideTrackEntity.getTime());
        rideEnt.setStatus(rideTrackEntity.getStatus());
        rideEnt.setEstimateFare(rideTrackEntity.getEstimateFare());
        rideEnt.setPaymentType(rideTrackEntity.getPaymentType());
        rideEnt.setTotalAmount(rideTrackEntity.getTotalAmount());
        rideEnt.setPaid(rideTrackEntity.getPaid());
        rideEnt.setDistance(rideTrackEntity.getDistance());
        rideEnt.setType(rideTrackEntity.getType());
        rideEnt.setRideStatus("1");
        rideEnt.setPromoId(1);
        rideDriverEnt.setRideDetail(rideEnt);


    }

    private void setRideEntData(RideTrackEnt rideTrackEntity) {

        rideEnt.setPickupLatitude(rideTrackEntity.getPickupLatitude());
        rideEnt.setPickupLongitude(rideTrackEntity.getPickupLongitude());
        rideEnt.setPickupAddress(rideTrackEntity.getPickupAddress());
        rideEnt.setPickupPlace(rideTrackEntity.getPickupPlace());
        rideEnt.setDestinationAddress(rideTrackEntity.getDestinationAddress());
        rideEnt.setDestinationLatitude(rideTrackEntity.getDestinationLatitude());
        rideEnt.setDestinationLongitude(rideTrackEntity.getDestinationLongitude());
        rideEnt.setDestinationPlace(rideTrackEntity.getDestinationPlace());
        rideEnt.setVehicleId(rideTrackEntity.getVehicleId());
        rideEnt.setType(rideTrackEntity.getType());
        rideEnt.setEstimateFare(rideTrackEntity.getEstimateFare());
        rideEnt.setDistance(rideTrackEntity.getDistance());
        rideEnt.setDate(rideTrackEntity.getDate());
        rideEnt.setTime(rideTrackEntity.getTime());
        rideEnt.setUserId(Integer.parseInt(rideTrackEntity.getUserId()));
        rideEnt.setImageUrl(rideTrackEntity.getImageUrl());
        rideEnt.setCreatedAt(rideTrackEntity.getCreatedAt());
        rideEnt.setUpdatedAt(rideTrackEntity.getUpdatedAt());
        rideEnt.setId(rideTrackEntity.getId());
    }

}
