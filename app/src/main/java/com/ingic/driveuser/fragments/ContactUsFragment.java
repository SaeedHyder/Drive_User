package com.ingic.driveuser.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.ResponseWrapper;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.global.WebServiceConstants;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.helpers.UpdateToken;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ingic.driveuser.R.id.edtDateOfBirth;
import static com.ingic.driveuser.R.id.edtMobileNumber;
import static com.ingic.driveuser.R.id.edtPassword;
import static com.ingic.driveuser.R.id.edtUserName;
import static com.ingic.driveuser.R.id.edtemail;
import static com.ingic.driveuser.R.id.edtzipCode;

/**
 * Created by saeedhyder on 6/23/2017.
 */

public class ContactUsFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.edtContactUs)
    AnyEditTextView edtContactUs;
    @BindView(R.id.SubmitButton)
    Button SubmitButton;
    @BindView(R.id.ll_contactUs)
    LinearLayout llContactUs;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListners();
    }

    private void setListners() {
        SubmitButton.setOnClickListener(this);
        edtContactUs.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

    }

    private boolean isvalidate() {

        if (edtContactUs.getText() == null || (edtContactUs.getText().toString().isEmpty())) {
            if (edtContactUs.requestFocus()) {
                getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
            //edtContactUs.setError(getString(R.string.enter_contactUs));
            UIHelper.showShortToastInCenter(getDockActivity(),getString(R.string.enter_contactUs));
            return false;
        }  else {
            return true;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.SubmitButton:
                if(isvalidate()){
                    SubmitContactUS();
                }
                break;
        }
    }

    public void SubmitContactUS() {
        loadingStarted();
        Call<ResponseWrapper> call = headerWebService.ContactUs(edtContactUs.getText().toString());
        call.enqueue(new Callback<ResponseWrapper>() {
            @Override
            public void onResponse(Call<ResponseWrapper> call, Response<ResponseWrapper> response) {
                loadingFinished();
                if(response.body().getResponse().equals(WebServiceConstants.TOKEN_EXPIRE_CODE)||  prefHelper.get_TOKEN().equals("")){
                    UpdateToken token=new UpdateToken(prefHelper.getUserId(),prefHelper.getUser().getEmail(),prefHelper.getUser().getRoleId(),getDockActivity(),prefHelper);
                    token.updateTokenService();
                }
               else  if (response.body().getResponse().equals(WebServiceConstants.SUCCESS_RESPONSE_CODE)) {
                    getDockActivity().popBackStackTillEntry(0);
                    UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtContactUs);
                    UIHelper.showShortToastInCenter(getDockActivity(),"Your message has been sent successfully");
                    getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(),HomeMapFragment.class.getSimpleName());
                } else {
                    UIHelper.showShortToastInCenter(getDockActivity(), response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseWrapper> call, Throwable t) {
                loadingFinished();
                Log.e(ContactUsFragment.class.getSimpleName(), t.toString());
            }
        });
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setSubHeading(getString(R.string.Contact_Us));
    }


}
