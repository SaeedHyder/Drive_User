package com.ingic.driveuser.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.MainActivity;
import com.ingic.driveuser.entities.FacebookLoginEnt;
import com.ingic.driveuser.entities.RegistrationEnt;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.CameraHelper;
import com.ingic.driveuser.helpers.DatePickerHelper;
import com.ingic.driveuser.helpers.FacebookLoginHelper;
import com.ingic.driveuser.helpers.InternetHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.interfaces.FacebookLoginListener;
import com.ingic.driveuser.interfaces.ImageSetter;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * Created by saeedhyder on 6/20/2017.
 */

public class SignUpFragment extends BaseFragment implements View.OnClickListener, ImageSetter, FacebookLoginListener {

    @BindView(R.id.CircularImageSharePop)
    CircleImageView CircularImageSharePop;
    @BindView(R.id.ll_ProfileImage)
    RelativeLayout llProfileImage;
    @BindView(R.id.iv_username)
    ImageView ivUsername;
    @BindView(R.id.edtUserName)
    AnyEditTextView edtUserName;
    @BindView(R.id.ll_userName)
    LinearLayout llUserName;
    @BindView(R.id.iv_email)
    ImageView ivEmail;
    @BindView(R.id.edtemail)
    AnyEditTextView edtemail;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.iv_ll_currentAddress)
    ImageView ivLlCurrentAddress;
    @BindView(R.id.edtll_currentAddress)
    AnyEditTextView edtllCurrentAddress;
    @BindView(R.id.ll_currentAddress)
    LinearLayout llCurrentAddress;
    @BindView(R.id.iv_MobileNumber)
    ImageView ivMobileNumber;
    @BindView(R.id.edtMobileNumber)
    AnyEditTextView edtMobileNumber;
    @BindView(R.id.ll_MobileNumber)
    LinearLayout llMobileNumber;
    @BindView(R.id.iv_zipCode)
    ImageView ivZipCode;
    @BindView(R.id.edtzipCode)
    AnyEditTextView edtzipCode;
    @BindView(R.id.ll_zipCode)
    LinearLayout llZipCode;
    @BindView(R.id.iv_DateOfBirth)
    ImageView ivDateOfBirth;
    @BindView(R.id.edtDateOfBirth)
    AnyTextView edtDateOfBirth;
    @BindView(R.id.ll_DateOfBirth)
    LinearLayout llDateOfBirth;
    @BindView(R.id.iv_Gender)
    ImageView ivGender;
    @BindView(R.id.sp_Gender)
    Spinner spGender;
    @BindView(R.id.ll_Gender)
    LinearLayout llGender;
    @BindView(R.id.iv_Password)
    ImageView ivPassword;
    @BindView(R.id.edtPassword)
    AnyEditTextView edtPassword;
    @BindView(R.id.ll_Password)
    LinearLayout llPassword;
    @BindView(R.id.iv_ConfirmPassword)
    ImageView ivConfirmPassword;
    @BindView(R.id.edt_ConfirmPassword)
    AnyEditTextView edtConfirmPassword;
    @BindView(R.id.ll_ConfirmPassword)
    LinearLayout llConfirmPassword;
    @BindView(R.id.btn_submuit)
    Button btnSubmuit;
    @BindView(R.id.ll_SignUpFields)
    LinearLayout llSignUpFields;
    @BindView(R.id.sv_signup)
    ScrollView svSignup;
    @BindView(R.id.ll_loginfacebook)
    RelativeLayout llLoginfacebook;
    @BindView(R.id.loginButton_fb)
    LoginButton btnfbLogin;
    Calendar calendar;
    int Year, Month, Day;
    @BindView(R.id.iv_camera)
    ImageView ivCamera;
    @BindView(R.id.txt_clickHere)
    AnyTextView txtClickHere;
    @BindView(R.id.txt_toAccept)
    AnyTextView txtToAccept;
    @BindView(R.id.txt_TermCond)
    AnyTextView txtTermCond;
    @BindView(R.id.ll_bottomText)
    LinearLayout llBottomText;
    @BindView(R.id.chk_read)
    CheckBox chk_read;
    File profilePic;
    String profilePath;
    MainActivity mainActivity;
    @BindView(R.id.iv_invitationCode)
    ImageView ivInvitationCode;
    @BindView(R.id.txt_invitationCode)
    AnyEditTextView txtInvitationCode;
    @BindView(R.id.ll_invitationCode)
    LinearLayout llInvitationCode;
    private CallbackManager callbackManager;
    private Date DateSelected;
    private List<String> genderList;
    private FacebookLoginEnt facebookLoginEnt;

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDatePickerVariables();
        edtMobileNumber.setText("+" + getDockActivity().getCountryCode());
        spGender();
        setListners();
        getMainActivity().setImageSetter(this);
        setupFacebookLogin();
        if (profilePath != null) {
            Glide.with(getDockActivity())
                    .load("file:///" + profilePath)
                    .into(CircularImageSharePop);
        }

    }

    FacebookLoginHelper facebookLoginHelper;

    private void setupFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        //btnfbLogin.setFragment(this);
        facebookLoginHelper = new FacebookLoginHelper(getDockActivity(), this, this);
        LoginManager.getInstance().registerCallback(callbackManager, facebookLoginHelper);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setListners() {
        llLoginfacebook.setOnClickListener(this);
        edtDateOfBirth.setOnClickListener(this);
        btnSubmuit.setOnClickListener(this);
        txtClickHere.setTypeface(null, Typeface.BOLD);
        txtTermCond.setTypeface(null, Typeface.BOLD);
        txtClickHere.setOnClickListener(this);
        txtTermCond.setOnClickListener(this);
        ivCamera.setOnClickListener(this);
    }

    private boolean isvalidate() {

        if (edtUserName.getText() == null || (edtUserName.getText().toString().isEmpty())) {
            if (edtUserName.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtUserName.setError(getString(R.string.enter_username));
            return false;
        } else if (edtemail.getText() == null || (edtemail.getText().toString().isEmpty()) ||
                (!Patterns.EMAIL_ADDRESS.matcher(edtemail.getText().toString()).matches())) {
            if (edtemail.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtemail.setError(getString(R.string.enter_email));
            return false;
        } else if (edtllCurrentAddress.getText() == null || (edtllCurrentAddress.getText().toString().isEmpty())) {
            if (edtllCurrentAddress.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtllCurrentAddress.setError(getString(R.string.enter_currentAddress));
            return false;
        } else if (edtMobileNumber.getText().toString().isEmpty()) {
            if (edtMobileNumber.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtMobileNumber.setError(getString(R.string.enter_phone));
            return false;
        } else if (edtMobileNumber.getText().toString().length() <= 12) {
            if (edtMobileNumber.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtMobileNumber.setError(getString(R.string.numberLength));
            return false;
        } else if (edtzipCode.getText() == null || (edtzipCode.getText().toString().isEmpty())) {
            if (edtzipCode.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtzipCode.setError(getString(R.string.enter_zipCode));
            return false;
        } else if (edtDateOfBirth.getText() == null || (edtDateOfBirth.getText().toString().isEmpty())) {
            edtDateOfBirth.setError(getString(R.string.enter_dob));
            return false;
        } else if (edtPassword.getText() == null || (edtPassword.getText().toString().isEmpty())) {
            if (edtPassword.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (edtPassword.getText().toString().length() < 8) {
            if (edtPassword.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtPassword.setError(getString(R.string.passwordLength));
            return false;
        } else if (edtConfirmPassword.getText() == null || (edtConfirmPassword.getText().toString().isEmpty()) || edtPassword.getText().toString().length() < 8) {
            if (edtConfirmPassword.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtConfirmPassword.setError(getString(R.string.enter_password));
            return false;
        } else if (!edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())) {
            if (edtConfirmPassword.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            edtConfirmPassword.setError("Password does not match");
            return false;
        } else if (!chk_read.isChecked()) {
            UIHelper.showShortToastInCenter(getDockActivity(), "Accept Term And Condition First");
            return false;
        } else if (profilePic == null) {
            UIHelper.showShortToastInCenter(getDockActivity(), "Select a Profile Picture");
            return false;
        } else if (spGender.getSelectedItemPosition() == 0) {
            UIHelper.showShortToastInCenter(getDockActivity(), "Select a Gender");
            return false;
        } else {
            return true;
        }


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

    private void setDatePickerVariables() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
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
                        if (dateSpecified.after(date)) {
                            UIHelper.showShortToastInCenter(getDockActivity(), getString(R.string.date_after_error));
                        } else {
                            DateSelected = dateSpecified;
                            String predate = new SimpleDateFormat("dd MMM yyyy").format(c.getTime());

                            textView.setText(predate);
                            textView.setPaintFlags(Typeface.BOLD);
                        }

                    }
                }, "PreferredDate", 1);

        datePickerHelper.showDate();
    }

    /*void ShowDateDialog(final AnyTextView txtView) {

        DatePickerDialog dpd = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date todayDate = null;
                        try {
                            todayDate = sdf.parse(sdf.format(new Date()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        txtView.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                    }
                }, Year, Month, Day
        );
        Calendar c = Calendar.getInstance();
        c.set(new Date().getYear(), new Date().getMonth(), new Date().getDate() - 1);

        dpd.setMaxDate(c);
        dpd.show(getFragmentManager(), "Datepickerdialog");


    }*/

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setSubHeading(getResources().getString(R.string.sign_up));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edtDateOfBirth:
                //ShowDateDialog(edtDateOfBirth);
                initDatePicker(edtDateOfBirth);
                break;

            case R.id.btn_submuit:
                if (isvalidate()) {
                    if (InternetHelper.CheckInternetConectivityandShowToast(getDockActivity())) {
                        makeUserSignup();
                    }
                }
                break;
            case R.id.txt_clickHere:
                getDockActivity().replaceDockableFragment(TermAndConditionFragment.newInstance(), "TermAndConditionFragment");
                break;

            case R.id.txt_TermCond:
                getDockActivity().replaceDockableFragment(TermAndConditionFragment.newInstance(), "TermAndConditionFragment");
                break;
            case R.id.iv_camera:
                CameraHelper.uploadPhotoDialog(getMainActivity());
                break;
            case R.id.ll_loginfacebook:
                LoginManager.getInstance().logInWithReadPermissions(SignUpFragment.this, facebookLoginHelper.getPermissionNeeds());
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (DateSelected != null) {
            String predate = new SimpleDateFormat("dd MMM yyyy").format(DateSelected.getTime());
            edtDateOfBirth.setText(predate);
            edtDateOfBirth.setPaintFlags(Typeface.BOLD);
        }
    }

    private void makeUserSignup() {
        loadingStarted();

        MultipartBody.Part filePart;
        if (profilePic != null) {
            filePart = MultipartBody.Part.createFormData("profile_picture",
                    profilePic.getName(), RequestBody.create(MediaType.parse("image/*"), profilePic));
        } else {
            filePart = MultipartBody.Part.createFormData("profile_picture", "",
                    RequestBody.create(MediaType.parse("*/*"), ""));
        }
        Call<ResponseWrapper<RegistrationEnt>> call = webService.registerUser(
                RequestBody.create(MediaType.parse("text/plain"), edtUserName.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtemail.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), WordUtils.uncapitalize(genderList.get(spGender.getSelectedItemPosition()))),
                RequestBody.create(MediaType.parse("text/plain"), edtMobileNumber.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtllCurrentAddress.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtzipCode.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtDateOfBirth.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), txtInvitationCode.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtPassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), edtConfirmPassword.getText().toString()),
                RequestBody.create(MediaType.parse("text/plain"), facebookLoginEnt != null ? facebookLoginEnt.getFacebookUID() : ""),
                RequestBody.create(MediaType.parse("text/plain"), facebookLoginEnt != null ? AppConstants.SOCIAL_MEDIA_TYPE : ""),
                filePart
        );


        call.enqueue(new Callback<ResponseWrapper<RegistrationEnt>>() {
            @Override
            public void onResponse(Call<ResponseWrapper<RegistrationEnt>> call, Response<ResponseWrapper<RegistrationEnt>> response) {
                loadingFinished();

                if (response.body() != null)
                   /* if (response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)) {
                        UpdateToken token = new UpdateToken(prefHelper.getUserId(), prefHelper.getUser().getEmail(), prefHelper.getUser().getRoleId(), getDockActivity(), prefHelper);
                        token.updateTokenService();
                    } else*/
                    if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                        LoginManager.getInstance().logOut();
                        prefHelper.putRegistrationData(response.body().getResult());
                        prefHelper.setUsrId(response.body().getResult().getId() + "");
                   /* TokenUpdater.getInstance().UpdateToken(getDockActivity(),
                            prefHelper.getUserId(),
                            AppConstants.Device_Type,
                            prefHelper.getFirebase_TOKEN());*/
                        UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                        getDockActivity().replaceDockableFragment(VerifyNumFragment.newInstance(response.body().getResult().getPhoneNo()), "VerifyNumFragment");
                    } else {
                        LoginManager.getInstance().logOut();
                        UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                    }
            }

            @Override
            public void onFailure(Call<ResponseWrapper<RegistrationEnt>> call, Throwable t) {
                loadingFinished();
                Log.e(LoginFragment.class.getSimpleName(), t.toString());
            }
        });
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
            // profilePic = new File(imagePath);
            profilePath = imagePath;
            Glide.with(getDockActivity())
                    .load("file:///" + imagePath)
                    .into(CircularImageSharePop);
            //  ImageLoader.getInstance().displayImage(
            //     "file:///" +imagePath, CircularImageSharePop);
        }
    }

    private void SetFaceBookImage(String imagePath) {
        if (imagePath != null) {
            //profilePic = new File(imagePath);

            profilePath = imagePath;
            Glide.with(getDockActivity())
                    .load(imagePath)
                    .into(CircularImageSharePop);

            Glide.with(getDockActivity())
                    .load(imagePath)
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {
                            profilePic = new File(saveImage(resource));

                        }
                    });
                 //   .into(CircularImageSharePop);
            //  ImageLoader.getInstance().displayImage(
            //     "file:///" +imagePath, CircularImageSharePop);
        }
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + "FILE_NAME" + ".jpg";
        File storageDir = new File(            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/YOUR_FOLDER_NAME");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery


            //Toast.makeText(getDockActivity(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }



    @Override
    public void setFilePath(String filePath) {

    }

    @Override
    public void setVideo(String videoPath, String VideoThumbail) {

    }

    @Override
    public void onSuccessfulFacebookLogin(FacebookLoginEnt loginEnt) {
        facebookLoginEnt = loginEnt;
        edtUserName.setText(checkForNullOREmpty(loginEnt.getFacebookFullName()));
        edtDateOfBirth.setText(checkForNullOREmpty(loginEnt.getFacebookBirthday()));
        edtemail.setText(checkForNullOREmpty(loginEnt.getFacebookEmail()));
        SetFaceBookImage(loginEnt.getFacebookUProfilePicture());
        if (genderList.contains(loginEnt.getFacebookGender()))
            spGender.setSelection(genderList.indexOf(loginEnt.getFacebookGender()));

    }

}
