package com.ingic.driveuser.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.PickupSelectionactivity;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.interfaces.OnSettingActivateListener;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import DirectionModule.DirectionFinder;
import DirectionModule.DirectionFinderListener;
import DirectionModule.Route;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HomeFragment extends BaseFragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMarkerClickListener,
        DirectionFinderListener,
        OnSettingActivateListener {

    private static final String ORIGIN = "origin";
    private static final String DESTINATION = "destination";
    GoogleMap googleMap;
    GoogleApiClient googleApiClient;

    double latitude;
    double longitude;
    SupportMapFragment map;
    @BindView(R.id.txt_finding_ride)
    AnyTextView txtFindingRide;
    @BindView(R.id.txt_capacity)
    AnyTextView txtCapacity;
    @BindView(R.id.txt_pick_text)
    AnyTextView txtLocationText;
    @BindView(R.id.txt_deslocationText)
    AnyTextView txtDeslocationText;
    @BindView(R.id.ll_destination_selected)
    LinearLayout llDestinationSelected;
    @BindView(R.id.txt_destination_where)
    AnyTextView txtDestinationWhere;
    @BindView(R.id.btn_ridenow)
    Button btnRidenow;
    @BindView(R.id.btn_ridelater)
    Button btnRidelater;
    @BindView(R.id.btn_cancel_ride)
    Button btnCancelRide;
    @BindView(R.id.txt_destination)
    AnyTextView txtDestination;
    private LatLng origin;
    private LatLng destination;
    private MarkerOptions originMarker;
    private MarkerOptions destinationMarker;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getMainActivity().setOnSettingActivateListener(this);
        originMarker = new MarkerOptions().position(new LatLng(0, 0));
        destinationMarker = new MarkerOptions().position(new LatLng(0, 0));
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();

    }



    /* private void setListeners() {
         edtDestination.setAutoCompleteTextListener(new AutoCompleteLocation.AutoCompleteLocationListener() {
             @Override
             public void onTextClear() {

             }

             @Override
             public void onItemSelected(Place selectedPlace) {
                 if (origin == null) {
                     getCurrentLocation();
                 }
                 if (selectedPlace != null) {
                     destination = selectedPlace.getLatLng();
                     setdestinationMarkerOption(destination);
                     InitRideSelection();
                 }


             }
         });
         edtPickup.setAutoCompleteTextListener(new AutoCompleteLocation.AutoCompleteLocationListener() {
             @Override
             public void onTextClear() {

             }

             @Override
             public void onItemSelected(Place selectedPlace) {
                 if (selectedPlace != null) {
                     origin = selectedPlace.getLatLng();
                     setoriginMarkerOption(origin);
                     setRoute();

                 }

             }
         });
     }
 */
    private void InitRideSelection(int i) {
        setRoute();
        txtDestinationWhere.setVisibility(View.GONE);
        llDestinationSelected.setVisibility(View.VISIBLE);
        btnRidelater.setVisibility(View.VISIBLE);
        //edtPickup.setText(drive_address);
    }

    private void setoriginMarkerOption(LatLng latLng) {
        originMarker.position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.set_pickup_location, "asdsdasd")))
                .draggable(true)
                .title("");
        moveMap(latLng);
    }

    private void setdestinationMarkerOption(LatLng latLng) {
        destinationMarker.position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.set_pickup_location, "asdsdasd")))
                .draggable(true)
                .title("");
        moveMap(latLng);
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


    }

    private void sendRequest(String origin, String destination) {
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        titleBar.setSubHeading("Home");

    }

    @Override
    public void onMapReady(GoogleMap googlemap) {
        googleMap = googlemap;
        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapLongClickListener(this);
        googlemap.setOnMarkerClickListener(this);
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                UIHelper.showShortToastInCenter(getDockActivity(),cameraPosition.target.toString());
            }
        });

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
    public void onConnected(@Nullable Bundle bundle) {
        if (origin == null)
            getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    private String getCurrentAddress(double lat, double lng) {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getMainActivity(), Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String country = addresses.get(0).getCountryName();
                return address + ", " + country;
            } else {

                return "Address Not Available";
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //Getting current drive_location
    private void getCurrentLocation() {
        if (googleMap != null)
            googleMap.clear();
        if (ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getMainActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            // origin = new LatLng(latitude, longitude);
            String Address = getCurrentAddress(latitude, longitude);

            if (Address != null) {

                //  edtPickup.setText(Address);
            }
            setoriginMarkerOption(new LatLng(latitude, longitude));
            // moveMap(new LatLng(latitude, longitude));
        }

    }



    private void moveMap(LatLng latLng) {


        googleMap.clear();
        // LatLng latLng = new LatLng(latitude, longitude);


        if (destination != null) {
            googleMap.addMarker(originMarker).setTag(ORIGIN);
            googleMap.addMarker(destinationMarker).setTag(DESTINATION);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(originMarker.getPosition());
            builder.include(destination);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
            googleMap.animateCamera(CameraUpdateFactory.zoomBy(0));
           /* googleMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-1);

                }

                @Override
                public void onCancel() {

                }
            });*/
        } else {
            googleMap.addMarker(originMarker).setTag(ORIGIN);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }

        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

    }

    /*private void getorigin() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (origin == null)
                    getCurrentLocation();
            }
        }, 100);
    }
*/ private void addMarker(LatLng latlng) {
        googleMap.addMarker(new MarkerOptions()
                .position(latlng)
                //.title("asdasd")
                .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_launcher, "asdsdasd")))
                .draggable(true)
                .title(""));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    private void clearMap() {
        googleMap.clear();
    }
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId, String title) {

        View customMarkerView = ((LayoutInflater) getMainActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.img_icon);
        TextView textView = (TextView) customMarkerView.findViewById(R.id.txt_pick_text);
        textView.setText(title);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    private void setRoute() {
        if (origin != null && destination != null) {
            String origin_string = String.valueOf(origin.latitude) + "," + String.valueOf(origin.longitude);
            String destination_string = String.valueOf(destination.latitude) + "," + String.valueOf(destination.longitude);
            sendRequest(origin_string, destination_string);
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //move to current position
        String Address = getCurrentAddress(latitude, longitude);
        setRoute();

        if (Objects.equals(marker.getTag(), ORIGIN)) {
            origin = marker.getPosition();
            setoriginMarkerOption(origin);
            setRoute();
          //  edtPickup.setText(Address != null ? Address : "");

        }
        if (Objects.equals(marker.getTag(), DESTINATION)) {
            destination = marker.getPosition();
            setdestinationMarkerOption(destination);
            setRoute();
          //  edtDestination.setText(Address != null ? Address : "");
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //  googleMap.clear();

        /*googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_pin))
                .draggable(true));*/

       /* latitude = latLng.latitude;
        longitude = latLng.longitude;

        String Address = getCurrentAddress(latLng.latitude, latLng.longitude);

        if (Address != null) {
            edtPickup.setText(Address);
        }

        moveMap(latLng);*/
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        if (marker != null && (Objects.equals(marker.getTag(), ORIGIN))) {
            origin = marker.getPosition();
            StartPickupActivity();
            //edtPickup.setText(getCurrentAddress(marker.getPosition().latitude, marker.getPosition().longitude));
            setRoute();
        }

        return false;
    }


    @Override
    public void onDirectionFinderStart() {

        // progressDialog = ProgressDialog.show(this, "Please wait.","Finding direction..!", true);

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
    public void onDirectionFinderSuccess(List<Route> routes) {


        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLACK).
                    width(15);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            moveMap(null);
            polylinePaths.add(googleMap.addPolyline(polylineOptions));

        }

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route, View view, Object object) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (origin == null) {
            getCurrentLocation();
            getMainActivity().statusCheck();

        }

    }

    @Override
    public void onLocationActivateListener() {
        getCurrentLocation();
    }

    @Override
    public void onNetworkActivateListener() {
        getCurrentLocation();
    }

    private void StartPickupActivity(){
        Intent i = new Intent(getActivity(), PickupSelectionactivity.class);
        startActivityForResult(i,12);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UIHelper.showShortToastInCenter(getDockActivity(),"request code"+requestCode);
    }

    @OnClick({R.id.ll_destination_selected, R.id.txt_destination_where, R.id.btn_ridenow, R.id.btn_ridelater, R.id.btn_cancel_ride})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_destination_selected:
                StartPickupActivity();
                break;
            case R.id.txt_destination_where:
                StartPickupActivity();
                break;
            case R.id.btn_ridenow:
                break;
            case R.id.btn_ridelater:
                break;
            case R.id.btn_cancel_ride:
                break;
        }
    }
}
