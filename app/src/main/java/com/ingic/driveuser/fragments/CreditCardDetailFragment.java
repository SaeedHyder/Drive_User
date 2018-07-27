package com.ingic.driveuser.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.helpers.DatePickerHelper;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.TitleBar;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by saeedhyder on 6/22/2017.
 */

public class CreditCardDetailFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.edtCreditCardNum)
    AnyEditTextView edtCreditCardNum;
    @BindView(R.id.edtExpirationDate)
    AnyTextView edtExpirationDate;
    @BindView(R.id.edt_CVV)
    AnyEditTextView edtCVV;
    @BindView(R.id.edtNameOnCard)
    AnyEditTextView edtNameOnCard;
    @BindView(R.id.UpdateButton)
    Button UpdateButton;

    public static CreditCardDetailFragment newInstance() {
        return new CreditCardDetailFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creditcard, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListners();
    }

    private void setListners() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showBackButton();
        titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        titleBar.setSubHeading(getResources().getString(R.string.card_detail));
    }
    private void initDatePicker(final TextView textView){
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
                            textView.setText(datePickerHelper.getStringDate(year, month, dayOfMonth));
                        }
                    }
                },"PreferredDate");

        datePickerHelper.showDate();
    }


    private boolean validate() {
        if (edtCreditCardNum.getText().toString().isEmpty()){
            edtCreditCardNum.setError(getString(R.string.enter_cc_number));
            return false;
        }else if (edtCreditCardNum.getText().toString().length()<16){
            edtCreditCardNum.setError(getString(R.string.cc_valid_error));
            return false;
        }
        else if (edtExpirationDate.getText().toString().isEmpty()){
            edtExpirationDate.setError(getString(R.string.expiredate_error));
            return false;
        }
        else if (edtCVV.getText().toString().isEmpty()){
            edtCVV.setError(getString(R.string.cvv_error));
            return false;
        }
        else if (edtNameOnCard.getText().toString().isEmpty()){
            edtNameOnCard.setError(getString(R.string.cc_name_error));
            return false;
        }
        else {
            return true;
        }
    }
    @OnClick({R.id.edtExpirationDate, R.id.UpdateButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.edtExpirationDate:
                initDatePicker(edtExpirationDate);
                break;
            case R.id.UpdateButton:
                if (validate()){
                    UIHelper.hideSoftKeyboard(getDockActivity(),getView());
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtCreditCardNum);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtCVV);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtExpirationDate);
                    UIHelper.hideSoftKeyboard(getDockActivity(),edtNameOnCard);
                   getDockActivity().popBackStackTillEntry(0);
                    getDockActivity().replaceDockableFragment(HomeMapFragment.newInstance(),HomeMapFragment.class.getSimpleName());
                }
                break;
        }
    }
}
