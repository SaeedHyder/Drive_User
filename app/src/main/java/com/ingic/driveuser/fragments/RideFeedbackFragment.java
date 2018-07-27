package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.RideFeedbackEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.adapters.ArrayListAdapter;
import com.ingic.driveuser.ui.viewbinder.RideFeedbackBinder;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.CustomRatingBar;
import com.ingic.driveuser.ui.views.TitleBar;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 6/29/2017.
 */

public class RideFeedbackFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.edtComments)
    AnyEditTextView edtComments;
    @BindView(R.id.rbAddRating)
    CustomRatingBar rbAddRating;
    @BindView(R.id.SubmitButton)
    Button SubmitButton;

    private RideFeedbackBinder binder;
    private ArrayListAdapter<RideFeedbackEnt> adapter;
    private static String RideIdKey = "rideId";
    private static String DriverIdKey = "DriverId";

    private ArrayList<RideFeedbackEnt> userCollection = new ArrayList<>();

    public static RideFeedbackFragment newInstance() {
        return new RideFeedbackFragment();
    }

    public static RideFeedbackFragment newInstance(String rideId, String DriverId) {
        Bundle args = new Bundle();
        args.putString(RideIdKey, rideId);
        args.putString(DriverIdKey, DriverId);
        RideFeedbackFragment fragment = new RideFeedbackFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            RideIdKey = getArguments().getString(RideIdKey);
            DriverIdKey = getArguments().getString(DriverIdKey);
        }
        binder = new RideFeedbackBinder(getDockActivity());
        adapter = new ArrayListAdapter<RideFeedbackEnt>(getDockActivity(), binder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ride_feedback, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
            getAllImproveType();
        setListners();

    }

    private void getAllImproveType() {
        loadingStarted();
        Call<ResponseWrapper<ArrayList<RideFeedbackEnt>>> call = headerWebService.getImproveType();
        call.enqueue(new Callback<ResponseWrapper<ArrayList<RideFeedbackEnt>>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<ArrayList<RideFeedbackEnt>>> call, Response<ResponseWrapper<ArrayList<RideFeedbackEnt>>> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
               else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    getFeedbackData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<ArrayList<RideFeedbackEnt>>> call, Throwable t) {
                loadingFinished();
                Log.e(RideFeedbackFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    private void getFeedbackData(ArrayList<RideFeedbackEnt> result) {
        userCollection = new ArrayList<>();

        userCollection.addAll(result);


        bindData(userCollection);
    }

    private void bindData(ArrayList<RideFeedbackEnt> userCollection) {

        adapter.clearList();
        gridView.setAdapter(adapter);
        adapter.addAll(userCollection);
        adapter.notifyDataSetChanged();
    }


    private void setListners() {

        SubmitButton.setOnClickListener(this);


        rbAddRating.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if(score<1.0f)
                    rbAddRating.setScore(1.0f);
            }
        });

        edtComments.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.SubmitButton:
                if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                    submitRating();

                break;
        }
    }

    private void submitRating() {
        loadingStarted();
        Call<ResponseWrapper> call = headerWebService.submitAppFeedback(RideIdKey,
                DriverIdKey, edtComments.getText().toString(),
                (int) rbAddRating.getScore(), StringUtils.join(binder.getSelectedReasons(), ","));
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
               else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtComments);
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(), HomeMapFragment.class.getSimpleName());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e(RideFeedbackFragment.class.getSimpleName(), t.toString());
            }
        });
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.Ride_Feedback));
    }


}
