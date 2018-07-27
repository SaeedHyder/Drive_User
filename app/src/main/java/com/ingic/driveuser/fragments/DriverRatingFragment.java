package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.RideDriverEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.CustomRatingBar;
import com.ingic.driveuser.ui.views.TitleBar;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ingic.driveuser.global.WebServiceConstants.RIDE_RATING_current;

/**
 * Created by saeedhyder on 4/12/2018.
 */
public class DriverRatingFragment extends BaseFragment {
    @BindView(R.id.txtDriverName)
    AnyTextView txtDriverName;
    @BindView(R.id.txtCarNo)
    AnyTextView txtCarNo;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.txt_pickup)
    AnyTextView txtPickup;
    @BindView(R.id.txt_pick_text)
    AnyTextView txtPickText;
    @BindView(R.id.imageView_destination)
    ImageView imageViewDestination;
    @BindView(R.id.txt_destination)
    AnyTextView txtDestination;
    @BindView(R.id.txt_destination_text)
    AnyTextView txtDestinationText;
    @BindView(R.id.txtFare)
    AnyTextView txtFare;
    @BindView(R.id.txtFareAmount)
    AnyTextView txtFareAmount;
    @BindView(R.id.ll_fare)
    LinearLayout llFare;
    @BindView(R.id.txtRateDriver)
    AnyTextView txtRateDriver;
    @BindView(R.id.rbAddRating)
    CustomRatingBar rbAddRating;
    @BindView(R.id.SubmitButton)
    Button SubmitButton;
    @BindView(R.id.ll_rateDriver)
    LinearLayout llRateDriver;
    @BindView(R.id.ll_submitRating)
    LinearLayout llSubmitRating;
    @BindView(R.id.iv_tripMap)
    ImageView ivTripMap;
    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.bottom_sheet)
    ScrollView bottomSheet;
    Unbinder unbinder;

    private static String RideDetailKey = "RideDetailKey";
    private String RideDetailString;
    private RideDriverEnt result;
    private int Rating;

    public static DriverRatingFragment newInstance(RideDriverEnt entity) {
        Bundle args = new Bundle();
        args.putString(RideDetailKey, new Gson().toJson(entity));
        DriverRatingFragment fragment = new DriverRatingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            RideDetailString = getArguments().getString(RideDetailKey);
        }
        if (RideDetailString != null) {
            result = new Gson().fromJson(RideDetailString, RideDriverEnt.class);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_driver, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setData();

    }

    private void setData() {

        if (result.getDriverDetail() != null && result.getVehicleDetail() != null) {
            Glide.with(getDockActivity()).load(result.getDriverDetail().getProfileImage()).into(CircularImageSharePop);
            txtDriverName.setText(result.getDriverDetail().getFullName() + "");
            txtCarNo.setText(result.getVehicleDetail().getVehicleNumber() + "");
        }
        if (result.getRideDetail() != null) {
            Glide.with(getDockActivity()).load(result.getRideDetail().getImageUrl()).into(ivTripMap);
            txtPickText.setText(result.getRideDetail().getPickupAddress() + "");
            txtDestinationText.setText(result.getRideDetail().getDestinationAddress() + "");
            double amount=Double.parseDouble(result.getRideDetail().getTotalAmount());
            double x = Math.abs(Math.round(Double.parseDouble(result.getRideDetail().getNegativeWalletAmount())));
            txtFareAmount.setText("AED "+((int)Math.ceil(amount)+(int)x));
        }

        rbAddRating.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    rbAddRating.setScore(1.0f);
            }
        });

        Rating = (int) Math.ceil(rbAddRating.getScore());
        Rating = Rating + 0;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.setSubHeading(getString(R.string.rate_your_trip));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.SubmitButton)
    public void onViewClicked() {
        submitRating();
    }



    private void submitRating() {
        loadingStarted();
        Call<ResponseWrapper> call = headerWebService.submitRideFeedback(result.getRideDetail().getId() + "",
                result.getDriverDetail().getId() + "",(int) Math.ceil(rbAddRating.getScore()) + ""
               );
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")) {
                    UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
                else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.setRideInSession(false);
                    prefHelper.removeRideSessionPreferences();
                    getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(RideFeedbackFragment.newInstance(result.getRideDetail().getId() + "", result.getDriverDetail().getId() + ""), RideFeedbackFragment.class.getSimpleName());
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
}
