package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.TokenUpdater;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.CustomRatingBar;
import com.ingic.driveuser.ui.views.TitleBar;
import com.bumptech.glide.Glide;

import org.apache.commons.lang3.text.WordUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 6/23/2017.
 */

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.txtProfileName)
    AnyTextView txtProfileName;
    @BindView(R.id.rbAddRating)
    CustomRatingBar rbAddRating;
    @BindView(R.id.txt_rides)
    AnyTextView txtRidesName;
    @BindView(R.id.txt_miles)
    AnyTextView txtMilesName;
    @BindView(R.id.txtGender)
    AnyTextView txtGender;
    @BindView(R.id.txtphone)
    AnyTextView txtphone;
    @BindView(R.id.txtAddress)
    AnyTextView txtAddress;
    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
            getUserProfile();
        else {
            BindData(prefHelper.getUser());
        }
        setListners();
    }

    public void getUserProfile() {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = headerWebService.getProfile(prefHelper.getUserId());
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
                loadingFinished();
                if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE) || prefHelper.get_TOKEN().equals("")){
                    UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
                else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUser(response.body().getResult());
                    prefHelper.setUsrId(response.body().getResult().getId() + "");
                    prefHelper.setLoginStatus(true);
                    TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                            AppConstants.Device_Type,
                            prefHelper.getFirebase_TOKEN());
                    BindData(response.body().getResult());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(LoginFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    private void BindData(UserEnt user) {
        Glide.with(getDockActivity()).load(user.getProfileImage()).into(CircularImageSharePop);
        txtAddress.setText(user.getAddress() + "");
        txtGender.setText(WordUtils.capitalize(user.getGender()) + "");
        txtphone.setText(user.getPhoneNo() + "");
        txtProfileName.setText(user.getFullName() + "");
        if (user.getTotalDistance() != 0){
            txtMilesName.setText(((int)user.getTotalDistance())+"");
        }
        txtRidesName.setText(user.getTotalRide() + "");
        if(user.getAverageRate() != null && user.getAverageRate()>0 ){
        rbAddRating.setScore((user.getAverageRate()));}
        else{
            rbAddRating.setScore(0);
        }
    }

    private void setListners() {

    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showEditButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDockActivity().replaceDockableFragment(EditProfileFragment.newInstance(), EditProfileFragment.class.getSimpleName());
            }
        });
        titleBar.showMenuButton();
        titleBar.setSubHeading(getResources().getString(R.string.profile));
    }


}
