package com.ingic.driveuser.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.LocationEnt;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.RideEnt;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.interfaces.OnReceivePlaceListener;
import com.ingic.driveuser.retrofit.WebService;
import com.ingic.driveuser.retrofit.WebServiceFactory;
import com.ingic.driveuser.ui.adapters.ArrayListAdapter;
import com.ingic.driveuser.ui.adapters.AutoCompleteListAdapter;
import com.ingic.driveuser.ui.viewbinder.NearestPlacesBinder;
import com.ingic.driveuser.ui.viewbinder.RecentPlaceViewBinder;
import com.ingic.driveuser.ui.viewbinders.abstracts.AutocompleteBinder;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.ExpandedListView;
import com.ingic.driveuser.ui.views.TitleBar;

import java.util.ArrayList;

import NearbyLocation.NearByPlaces;
import NearbyLocation.PlacesTask;
import NearbyLocation.entities.PlacesEnt;
import NearbyLocation.entities.Result;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickupSelectionactivity extends DockActivity implements
        View.OnFocusChangeListener,
        OnReceivePlaceListener,
        AdapterView.OnItemClickListener, NearByPlaces {

    private static final String TAG = PickupSelectionactivity.class.getSimpleName();
    @BindView(R.id.iv_pickIcon)
    ImageView ivPickIcon;
    @BindView(R.id.edt_pickup)
    EditText edtPickup;
    @BindView(R.id.input_layout_pickup)
    TextInputLayout inputLayoutPickup;
    @BindView(R.id.ll_pickup)
    LinearLayout llPickup;
    @BindView(R.id.iv_destinationIcon)
    ImageView ivDestinationIcon;
    @BindView(R.id.edt_destination)
    EditText edtDestination;
    @BindView(R.id.input_layout_destination)
    TextInputLayout inputLayoutDestination;
    @BindView(R.id.ll_destination)
    RelativeLayout llDestination;
    @BindView(R.id.new_places)
    ExpandedListView newPlaces;
    @BindView(R.id.recent_places)
    ExpandedListView recentPlaces;
    @BindView(R.id.header_main)
    TitleBar headerMain;
    @BindView(R.id.layout_map_location)
    RelativeLayout maplayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.dividerView)
    View dividerView;
    ArrayListAdapter<LocationEnt> recentPlacesAdapter;
    ArrayList<LocationEnt> userCollection;
    @BindView(R.id.nearest_places)
    ExpandedListView nearestPlaces;
    ArrayListAdapter<Result> nearestPlacesAdapter;
    @BindView(R.id.txt_recent_places)
    AnyTextView txtRecentPlaces;
    @BindView(R.id.recent_places_view)
    View recentPlacesView;
    @BindView(R.id.txt_nearBy)
    AnyTextView txtNearBy;
    @BindView(R.id.view_nearBy)
    View viewNearBy;
    @BindView(R.id.rl_mainFrame)
    LinearLayout rlMainFrame;
    private AutoCompleteListAdapter madapter;
    private GoogleApiClient mGoogleApiClient;
    private LocationEnt origin;
    private LocationEnt destination;
    private String currentFocus = "";
    private WebService webService;
    PlacesEnt nearByPlaces;
    private double latitude;
    private double longitude;
    private Location Mylocation;
    private LocationListener listener;
    private PlacesTask placesTask;


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pickup_location);


        recentPlacesAdapter = new ArrayListAdapter<LocationEnt>(this, new RecentPlaceViewBinder());
        nearestPlacesAdapter = new ArrayListAdapter<Result>(this, new NearestPlacesBinder());
        webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptorandheader(getDockActivity(), WebServiceConstants.SERVICE_URL);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            Bundle b = getIntent().getBundleExtra("route");
            if (b != null) {
                origin = new Gson().fromJson(b.getString("origin"), LocationEnt.class);
                destination = new Gson().fromJson(b.getString("destination"), LocationEnt.class);
            }
        }


        placesTask = new PlacesTask(this);

        StringBuilder sbValue = new StringBuilder(sbMethod(Double.parseDouble(prefHelper.get_latitude()), Double.parseDouble(prefHelper.get_longitude())));
        placesTask.execute(sbValue.toString());

        getHistory();
        bindData();
        setAutocomplete();
        setListeners();
        settitlebar();
    }

    private void getHistory() {
        Call<ResponseWrapper<ArrayList<RideEnt>>> call = webService.getUserRideHistory();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<RideEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<RideEnt>>> call, Response<ResponseWrapper<ArrayList<RideEnt>>> response) {
                if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    setRecentPlacesData(response.body().getResult());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<RideEnt>>> call, Throwable t) {
                Log.e(TAG, t.toString());

            }
        });
    }

    private void setRecentPlacesData(ArrayList<RideEnt> recentData) {

        userCollection = new ArrayList<>();

        for (RideEnt ent : recentData
                ) {
            userCollection.add(new LocationEnt(ent.getPickupAddress(), new LatLng(Double.parseDouble(ent.getPickupLatitude()), Double.parseDouble(ent.getPickupLongitude()))));
        }

        if (userCollection.size() <= 0) {
            txtRecentPlaces.setVisibility(View.GONE);
            recentPlacesView.setVisibility(View.GONE);
        } else {
            txtRecentPlaces.setVisibility(View.VISIBLE);
            recentPlacesView.setVisibility(View.GONE);
        }
       /* userCollection.add(new LocationEnt("Iqra University, Karachi 75500, Pakistan", new LatLng(24.837734199999996, 67.0812502)));
        userCollection.add(new LocationEnt("Axact House,82B Khayban-e-Ittehad Rd, Karachi 75500, Pakistan", new LatLng(24.8276911, 67.0742583)));
        userCollection.add(new LocationEnt("Ocean Mall,Khayaban-e-Iqbal, Karachi 75500, Pakistan", new LatLng(24.823853300000003, 67.0358956)));*/

        bindRecentPlacesData(userCollection);
    }

    private void bindRecentPlacesData(ArrayList<LocationEnt> userCollection) {

        recentPlacesAdapter.clearList();
        recentPlaces.setAdapter(recentPlacesAdapter);
        recentPlacesAdapter.addAll(userCollection);
        recentPlacesAdapter.notifyDataSetChanged();
        //  setListViewHeightBasedOnChildren(recentPlaces);

        recentPlaces.setOnTouchListener(null);
        recentPlaces.setScrollContainer(false);
        recentPlaces.setExpanded(true);
        recentPlaces.setOnItemClickListener(this);
    }


    private void bindData() {
        if (origin != null && origin.getAddress() != null) {
            edtPickup.setText(origin.getAddress());
        }
        if (destination != null && destination.getAddress() != null) {
            edtDestination.setText(destination.getAddress());
        }

    }

    private void settitlebar() {
        headerMain.hideButtons();
        headerMain.setSubHeading("Home");
        headerMain.setBackgroundColor(getResources().getColor(R.color.appBlackColor));
        headerMain.showBackButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent(PickupSelectionactivity.this,HomeMapFragment.class);
                //startActivity(intent);
                sendEmptyResult(true);
            }
        });
    }

    private void sendResult() {
        Intent i = new Intent();
        Bundle args = new Bundle();
        args.putString("origin", new Gson().toJson(origin));
        args.putString("destination", new Gson().toJson(destination));
        i.putExtra("route", args);
        setResult(RESULT_OK, i);
        UIHelper.hideSoftKeyboard(getDockActivity(), edtDestination);
        finish();
    }

    private void sendEmptyResult(Boolean setEmpty) {
        Intent i = new Intent();
        Bundle args = new Bundle();
        args.putBoolean("backPressed", setEmpty);
        i.putExtra("route", args);
        setResult(RESULT_OK, i);
        finish();
    }

    private void sendResult(Boolean setFromMap) {
        Intent i = new Intent();
        Bundle args = new Bundle();
        args.putString("origin", new Gson().toJson(origin));
        args.putString("destination", new Gson().toJson(destination));
        args.putBoolean("setonMap", setFromMap);
        i.putExtra("route", args);
        setResult(RESULT_OK, i);
        finish();
    }

    private void setListeners() {
        edtPickup.clearFocus();
        // edtDestination.clearFocus();
        edtDestination.setTag("des");
        edtPickup.setTag("org");
        edtPickup.setOnFocusChangeListener(this);
        edtDestination.setOnFocusChangeListener(this);
        if (edtDestination.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        maplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResult(true);
            }
        });
        edtPickup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentFocus = "origin";
                madapter.getFilter().filter(s);
            }
        });
        edtDestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                currentFocus = "destination";
                madapter.getFilter().filter(s);
            }
        });

        newPlaces.setOnItemClickListener(this);
    }

    public StringBuilder sbMethod(double latitude, double longitude) {

        //use your current location here

       /* double mLatitude = 37.77657;
        double mLongitude = -122.417506;*/

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location=" + latitude + "," + longitude);
        sb.append("&radius=5000");
        sb.append("&types=" + "restaurant");
        sb.append("&sensor=true");
        sb.append("&key=" + getDockActivity().getResources().getString(R.string.API_KEY));

        Log.d("Map", "api: " + sb.toString());

        return sb;
    }

    private void setAutocomplete() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(Places.GEO_DATA_API)
                //.addApi(AppIndex.API)
                .build();
        madapter = new AutoCompleteListAdapter(this, mGoogleApiClient, null, null, new AutocompleteBinder(), this);
        newPlaces.setAdapter(madapter);

        //enables filtering for the contents of the given ListView
        newPlaces.setTextFilterEnabled(true);

    }

    @Override
    protected void onPause() {
        if (getWindow().getDecorView() != null) {
            UIHelper.hideSoftKeyboard(this, getWindow().getDecorView());
        }
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getWindow().getDecorView() != null) {
            UIHelper.hideSoftKeyboard(this, getWindow().getDecorView());
        }
        mGoogleApiClient.connect();


    }

    @Override
    public void onLoadingStarted() {


    }

    @Override
    public void onLoadingFinished() {

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

        sendEmptyResult(true);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getTag().equals("des")) {
                if (edtPickup != null && edtPickup.getText().toString().equals("")) {
                    if (origin != null && origin.getAddress() != null && !origin.getAddress().equals("")) {
                        edtPickup.setText(origin.getAddress());
                    }
                }
                maplayout.setVisibility(View.VISIBLE);
                inputLayoutDestination.setBackgroundColor(getResources().getColor(R.color.edit_text_color));
            } else {
                maplayout.setVisibility(View.GONE);
                inputLayoutPickup.setBackgroundColor(getResources().getColor(R.color.edit_text_color));
            }

        } else {
            if (v.getTag().equals("des"))
                inputLayoutDestination.setBackgroundColor(getResources().getColor(R.color.transparent));
            else
                inputLayoutPickup.setBackgroundColor(getResources().getColor(R.color.transparent));

        }

    }

    @Override
    public void OnPlaceReceive() {
        newPlaces.setOnTouchListener(null);
        newPlaces.setScrollContainer(false);
        newPlaces.setExpanded(true);
        if (madapter.getCount() > 0) {
            dividerView.setVisibility(View.VISIBLE);
        } else {
            dividerView.setVisibility(View.GONE);
        }

        // setListViewHeightBasedOnChildren(newPlaces);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.new_places:
                getPlace(position);
                break;
            case R.id.recent_places:
                LocationEnt ent = (LocationEnt) recentPlacesAdapter.getItem(position);
                setPlace(ent.getAddress(), ent.getLatlng());
                break;

            case R.id.nearest_places:
                Result entity = (Result) nearestPlacesAdapter.getItem(position);
                setPlaceWithPlaceId(entity.getPlaceId());
                //      LatLng latLng=new LatLng(entity.getGeometry().getLocation().getLatitude(),entity.getGeometry().getLocation().getLatitude());
                //  setPlace(entity.getName(), latLng);
                break;
        }

    }

    private void setPlaceWithPlaceId(String placeId) {

        PendingResult<PlaceBuffer> placeResult =
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    places.release();
                    return;
                }
                if (places.getCount() > 0) {
                    // Got place details
                    final Place place = places.get(0);
                    setPlace(place.getAddress().toString(), place.getLatLng());
//                        madapter.clear();
//                        madapter.notifyDataSetChanged();
                    // Do your stuff
                } else {
                    // No place details
                    Toast.makeText(getApplicationContext(), "Place details not found.", Toast.LENGTH_LONG).show();
                }

                places.release();
            }

        });
    }

    private void getPlace(int position) {
        UIHelper.hideSoftKeyboard(getApplicationContext(), getWindow().getDecorView());
        final AutocompletePrediction item = madapter.getItem(position);
        if (item != null) {
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult =
                    Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                @Override
                public void onResult(@NonNull PlaceBuffer places) {
                    if (!places.getStatus().isSuccess()) {
                        places.release();
                        return;
                    }
                    if (places.getCount() > 0) {
                        // Got place details
                        final Place place = places.get(0);
                        setPlace(place.getAddress().toString(), place.getLatLng());
//                        madapter.clear();
//                        madapter.notifyDataSetChanged();
                        // Do your stuff
                    } else {
                        // No place details
                        Toast.makeText(getApplicationContext(), "Place details not found.", Toast.LENGTH_LONG).show();
                    }

                    places.release();
                }

            });


        }

    }

    private void setPlace(String address, LatLng latLng) {
//        if (currentFocus.equals("origin")) {
        if (edtPickup.isFocused()) {
            origin = new LocationEnt(address, latLng);
            if (destination == null) {
                if (edtDestination.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                edtPickup.setText(origin.getAddress());
                // edtDestination.requestFocus();
            } else {
                edtPickup.setText(origin.getAddress());
                sendResult();
            }
        } else {
            destination = new LocationEnt(address, latLng);
            if (origin == null) {
                if (edtPickup.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                edtDestination.setText(destination.getAddress());

            } else
                edtDestination.setText(destination.getAddress());
            sendResult();
        }
    }

    @Override
    public void places(String result) {
        Gson gson = new Gson();

        nearByPlaces = gson.fromJson(result, PlacesEnt.class);

        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
            if (nearByPlaces != null && nearByPlaces.getResults().size() > 0) {
                setNearestPlaces(nearByPlaces);
            }
        }
    }

    private void setNearestPlaces(PlacesEnt nearByPlaces) {

        if (nearByPlaces != null && nearByPlaces.getResults() != null && nearByPlaces.getResults().size() <= 0) {
            txtNearBy.setVisibility(View.GONE);
            viewNearBy.setVisibility(View.GONE);
        } else {
            txtNearBy.setVisibility(View.VISIBLE);
            viewNearBy.setVisibility(View.VISIBLE);
        }

        nearestPlacesAdapter.clearList();
        nearestPlaces.setAdapter(nearestPlacesAdapter);
        nearestPlacesAdapter.addAll(nearByPlaces.getResults());
        nearestPlacesAdapter.notifyDataSetChanged();

        rlMainFrame.setVisibility(View.VISIBLE);
        //  setListViewHeightBasedOnChildren(recentPlaces);

        nearestPlaces.setOnTouchListener(null);
        nearestPlaces.setScrollContainer(false);
        nearestPlaces.setExpanded(true);
        nearestPlaces.setOnItemClickListener(this);

    }

}
