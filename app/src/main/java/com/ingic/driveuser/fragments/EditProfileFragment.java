package com.ingic.driveuser.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.entities.UserEnt;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.CameraHelper;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.TokenUpdater;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.interfaces.ImageSetter;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.TitleBar;
import com.bumptech.glide.Glide;


import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by saeedhyder on 6/21/2017.
 */

public class EditProfileFragment extends BaseFragment implements View.OnClickListener, ImageSetter {

    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.ll_ProfileImage)
    RelativeLayout llProfileImage;
    @BindView(R.id.iv_username)
    ImageView ivUsername;
    @BindView(R.id.edtUserName)
    AnyEditTextView edtUserName;
    @BindView(R.id.ll_userName)
    LinearLayout llUserName;
    @BindView(R.id.iv_MobileNumber)
    ImageView ivMobileNumber;
    @BindView(R.id.edtMobileNumber)
    AnyEditTextView edtMobileNumber;
    @BindView(R.id.ll_MobileNumber)
    LinearLayout llMobileNumber;
    @BindView(R.id.iv_Gender)
    ImageView ivGender;
    @BindView(R.id.sp_Gender)
    Spinner spGender;
    @BindView(R.id.ll_Gender)
    LinearLayout llGender;
    @BindView(R.id.iv_ll_currentAddress)
    ImageView ivLlCurrentAddress;
    @BindView(R.id.edtll_currentAddress)
    AnyEditTextView edtllCurrentAddress;
    @BindView(R.id.ll_currentAddress)
    LinearLayout llCurrentAddress;
    @BindView(R.id.iv_City)
    ImageView ivCity;
    @BindView(R.id.edtCity)
    AnyEditTextView edtCity;
    @BindView(R.id.ll_City)
    LinearLayout llCity;
    @BindView(R.id.iv_State)
    ImageView ivState;
    @BindView(R.id.edt_State)
    AnyEditTextView edtState;
    @BindView(R.id.ll_State)
    LinearLayout llState;
    @BindView(R.id.ll_city_state)
    LinearLayout llCityState;
    @BindView(R.id.iv_zipCode)
    ImageView ivZipCode;
    @BindView(R.id.edtzipCode)
    AnyEditTextView edtzipCode;
    @BindView(R.id.ll_zipCode)
    LinearLayout llZipCode;
    @BindView(R.id.ll_SignUpFields)
    LinearLayout llSignUpFields;
    @BindView(R.id.sv_signup)
    ScrollView svSignup;
    File profilePic;
    String profilePath;
    private List<String> genderList;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

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
        spGender();
        getMainActivity().setImageSetter(this);
    }

    public void getUserProfile() {
        loadingStarted();
        Call<ResponseWrapper<UserEnt>> call = headerWebService.getProfile(prefHelper.getUserId());
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
                loadingFinished();
                if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)||  prefHelper.get_TOKEN().equals("")){
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
                    BindData(prefHelper.getUser());
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
        edtllCurrentAddress.setText(user.getAddress() + "");
        if (genderList.contains(WordUtils.capitalize(user.getGender()) + ""))
            spGender.setSelection(genderList.indexOf(WordUtils.capitalize(user.getGender()) + ""));
        edtCity.setText(user.getCity() + "");
        edtMobileNumber.setText(user.getPhoneNo() + "");
        edtUserName.setText(user.getFullName() + "");
        edtState.setText(user.getState() + "");
        edtzipCode.setText(user.getZipCode() + "");
    }

    private void spGender() {
        genderList = new ArrayList<>();
        genderList.add("Gender");
        genderList.add("Male");
        genderList.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getDockActivity(), R.layout.spinner_item, genderList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter.setDropDownViewResource(R.layout.spinner_dropdown);

        spGender.setAdapter(dataAdapter);

        spGender.setSelection(0);
    }


    private void setListners() {

        ivCamera.setOnClickListener(this);

    }

    private void makeUserProfileUpdate() {
        getMainActivity().onLoadingStarted();

        MultipartBody.Part filePart;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_picture",
                    profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
        } else {
            filePart = MultipartBody.Part.createFormData("profile_picture", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }
        Call<ResponseWrapper<UserEnt>> call = headerWebService.UpdateUser(
                RequestBody.create(MediaType.parse("text/plain"), edtUserName.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtMobileNumber.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtzipCode.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), WordUtils.uncapitalize(genderList.get(spGender.getSelectedItemPosition()))),
                RequestBody.create(MediaType.parse("text/plain"), edtllCurrentAddress.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtCity.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtState.getText().toString()),
                filePart
        );
        call.enqueue(new Callback<ResponseWrapper<UserEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<UserEnt>> call, Response<ResponseWrapper<UserEnt>> response) {
                getMainActivity().onLoadingFinished();
                if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)||  prefHelper.get_TOKEN().equals("")){
                    UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
                else if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    prefHelper.putUser(response.body().getResult());
                    prefHelper.setUsrId(response.body().getResult().getId() + "");
                    TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                            AppConstants.Device_Type,
                            prefHelper.getFirebase_TOKEN());
                    UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtUserName);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtMobileNumber);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtCity);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtllCurrentAddress);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtzipCode);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtState);
                    UIHelper.showShortToastInCenter(getDockActivity(),"Profile has been edited successfully");
                    getDockActivity().replaceDockableFragment(ProfileFragment.newInstance(), ProfileFragment.class.getSimpleName());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<UserEnt>> call, Throwable t) {
                getMainActivity().onLoadingFinished();
                Log.e(LoginFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    private boolean isvalidate() {

        if (edtUserName.getText() == null || (edtUserName.getText().toString().isEmpty())) {
            if (edtUserName.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtUserName.setError(getString(R.string.enter_username));
            return false;
        } else if (edtMobileNumber.getText().toString().isEmpty()) {
            if (edtMobileNumber.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtMobileNumber.setError(getString(R.string.enter_phone));
            return false;
        } else if (edtMobileNumber.getText().toString().length() < 11) {
            if (edtMobileNumber.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtMobileNumber.setError(getString(R.string.numberLength));
            return false;
        } else if (edtllCurrentAddress.getText() == null || (edtllCurrentAddress.getText().toString().isEmpty())) {
            if (edtllCurrentAddress.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtllCurrentAddress.setError(getString(R.string.enter_currentAddress));
            return false;
        } else if (edtCity.getText() == null || (edtCity.getText().toString().isEmpty())) {
            if (edtCity.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtCity.setError(getString(R.string.enter_city));
            return false;
        } else if (edtState.getText() == null || (edtState.getText().toString().isEmpty())) {
            if (edtState.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtState.setError(getString(R.string.enter_state));
            return false;
        } else if (edtzipCode.getText() == null || (edtzipCode.getText().toString().isEmpty())) {
            if (edtzipCode.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtzipCode.setError(getString(R.string.enter_zipCode));
            return false;
        } else {
            return true;
        }

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleBar.showTickButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isvalidate()) {
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtMobileNumber);
                    UIHelper.hideSoftKeyboard(getDockActivity(), getMainActivity()
                            .getWindow().getDecorView());
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity()))
                        makeUserProfileUpdate();
                }
            }
        });
        titleBar.setSubHeading(getString(R.string.Edit_Profile));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_camera:
                CameraHelper.uploadPhotoDialog(getMainActivity());
                break;
        }
    }


    @Override
    public void setImage(String imagePath) {
        if (imagePath != null) {
            //profilePic = new File(imagePath);
            try {
                profilePic = new Compressor(getDockActivity()).compressToFile(new File(imagePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            profilePath = imagePath;
            Glide.with(getDockActivity())
                    .load("file:///" + imagePath)
                    .into(CircularImageSharePop);
            //  ImageLoader.getInstance().displayImage(
            //     "file:///" +imagePath, CircularImageSharePop);
        }
    }

    @Override
    public void setFilePath(String filePath) {

    }

    @Override
    public void setVideo(String videoPath, String VideoThumbail) {

    }
}
