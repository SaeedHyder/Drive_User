package com.ingic.driveuser.helpers;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.EstimateFareEnt;
import com.ingic.driveuser.entities.PromoCodeEnt;
import com.ingic.driveuser.entities.RideDriverEnt;
import com.ingic.driveuser.entities.SelectCarEnt;
import com.ingic.driveuser.global.AppConstants;
import com.ingic.driveuser.ui.adapters.SelectCarAdapter;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.CustomRatingBar;
import com.ingic.driveuser.ui.views.ExpandedBottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created on 6/30/2017.
 */

public class BottomSheetDialogHelper {
    ExpandedBottomSheetBehavior bottomSheetBehavior;
    // private BottomSheetDialog dialog;
    private NestedScrollView dialog;
    private DockActivity context;

    private CoordinatorLayout mainParent;
    private RecyclerView recyclerView;
    private SelectCarAdapter mAdapter;

    public BottomSheetDialogHelper(DockActivity context, CoordinatorLayout mainParent, int LayoutID) {
        this.context = context;
        this.mainParent = mainParent;
        LayoutInflater inflater = LayoutInflater.from(context);
        dialog = (NestedScrollView) inflater.inflate(LayoutID, null, false);
        mainParent.addView(dialog);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) dialog.getLayoutParams();
        params.setBehavior(new ExpandedBottomSheetBehavior());
        dialog.requestLayout();
        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bottomSheetBehavior = (ExpandedBottomSheetBehavior) ExpandedBottomSheetBehavior.from(dialog);
        //  bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        bottomSheetBehavior.setAllowUserDragging(false);
        bottomSheetBehavior.setPeekHeight(0);
    }

    public void initRatingDialog(View.OnClickListener onClickListener, RideDriverEnt result) {
        if (result.getDriverDetail() != null && result.getVehicleDetail() != null) {
            CircleImageView driver_image = (CircleImageView) dialog.findViewById(R.id.CircularImageSharePop);
            Glide.with(context).load(result.getDriverDetail().getProfileImage()).into(driver_image);
            AnyTextView drivername = (AnyTextView) dialog.findViewById(R.id.txtDriverName);
            drivername.setText(result.getDriverDetail().getFullName() + "");
            AnyTextView carplate = (AnyTextView) dialog.findViewById(R.id.txtCarNo);
            carplate.setText(result.getVehicleDetail().getVehicleNumber() + "");
        }
        if (result.getRideDetail() != null) {
            AnyTextView pickup = (AnyTextView) dialog.findViewById(R.id.txt_pick_text);
            pickup.setText(result.getRideDetail().getPickupAddress() + "");
            AnyTextView Destination = (AnyTextView) dialog.findViewById(R.id.txt_destination_text);
            Destination.setText(result.getRideDetail().getDestinationAddress() + "");
            AnyTextView fare = (AnyTextView) dialog.findViewById(R.id.txtFareAmount);

            Double amount=Double.parseDouble(result.getRideDetail().getTotalAmount());
            fare.setText("AED "+(int)Math.ceil(amount));
        }

        final CustomRatingBar ratingBar = (CustomRatingBar) dialog.findViewById(R.id.rbAddRating);

        ratingBar.setOnScoreChanged(new CustomRatingBar.IRatingBarCallbacks() {
            @Override
            public void scoreChanged(float score) {
                if (score < 1.0f)
                    ratingBar.setScore(1.0f);
            }
        });


        bottomSheetBehavior.setPeekHeight((int) context.getResources().getDimension(R.dimen.x150));
        bottomSheetBehavior.setAllowUserDragging(false);
        Button submit = (Button) dialog.findViewById(R.id.SubmitButton);
        submit.setOnClickListener(onClickListener);
    }

    public int getRatingScore() {
        CustomRatingBar submit = (CustomRatingBar) dialog.findViewById(R.id.rbAddRating);
        return (int) submit.getScore();
    }

    public void initRideDetailBottomSheet(View.OnClickListener oncancelclicklistener, RideDriverEnt result, boolean isEnabled) {
        AnyTextView pickup = (AnyTextView) dialog.findViewById(R.id.txt_pick_text);

        String org = result.getRideDetail().getPickupAddress();
        if (org.contains("null")) {
            org = org.replace("null,", "");
        }
        pickup.setText(org + "");

        AnyTextView drivername = (AnyTextView) dialog.findViewById(R.id.txt_drivername);
        drivername.setText(result.getDriverDetail().getFullName() + "");
        if (result.getVehicleDetail() != null) {
            ImageView imageView2 = (ImageView) dialog.findViewById(R.id.imageView2);
            Glide.with(context).load(result.getVehicleDetail().getVehicleImage() + "").into(imageView2);
            AnyTextView carname = (AnyTextView) dialog.findViewById(R.id.txt_car_model);
            carname.setText(result.getVehicleDetail().getVehicleModel() + " " + result.getVehicleDetail().getVehicleName() + "");
            ImageView iv_carIcon = (ImageView) dialog.findViewById(R.id.iv_carIcon);
            Glide.with(context).load(result.getVehicleDetail().getVehicleImage() + "").into(iv_carIcon);

            AnyTextView carcolor = (AnyTextView) dialog.findViewById(R.id.txt_car_color);
            //     carcolor.setText(result.getVehicleDetail().getVehicleColor() + "");
            AnyTextView carplate = (AnyTextView) dialog.findViewById(R.id.txt_car_number);
            carplate.setText(result.getVehicleDetail().getVehicleNumber() + "");
            AnyTextView txtDistance = (AnyTextView) dialog.findViewById(R.id.txt_distance);
            txtDistance.setText(result.getRideDetail().getDistance() + " km");


        }
        ImageView driverimage = (ImageView) dialog.findViewById(R.id.img_driver);
        Glide.with(context).load(result.getDriverDetail().getProfileImage() + "").into(driverimage);
        CustomRatingBar driverrating = (CustomRatingBar) dialog.findViewById(R.id.rb_rating);
        if (result.getDriverDetail() != null && result.getDriverDetail().getAverageRate() != null)
            driverrating.setScore(Float.parseFloat(result.getDriverDetail().getAverageRate()));
        else {
            driverrating.setScore(0);
        }
        bottomSheetBehavior.setAllowUserDragging(false);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_cancel_ride);
        cancelbutton.setOnClickListener(oncancelclicklistener);
       // cancelbutton.setEnabled(!isEnabled);
    }

    public Button getDesiredButton(int ID) {
        return (Button) dialog.findViewById(ID);
    }

    public void initSelectRideBottomSheet(View.OnClickListener promoclicklistener,
                                          View.OnClickListener oncancelclicklistener, ArrayList<SelectCarEnt> carTypes, BasePreferenceHelper preferHelper, BottomSheetDialogHelper setupRideDialoge) {
        // this.dialog.setContentView(layoutID);
        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);

        if (carTypes != null && carTypes.size() > 0) {
            setSelectAdapter(carTypes);
        } else {
            setupRideDialoge.hideDialog();
        }

        Button cancelbutton = (Button) dialog.findViewById(R.id.SubmitButton);
        AnyTextView promocode = (AnyTextView) dialog.findViewById(R.id.txt_promoCode);
        AnyTextView peakfactor = (AnyTextView) dialog.findViewById(R.id.txt_peakFactor);
        View peakFactorView = (View) dialog.findViewById(R.id.peakFactorView);

        if (!preferHelper.getPeakFactor().equals("")) {
            peakfactor.setVisibility(View.VISIBLE);
            peakFactorView.setVisibility(View.VISIBLE);
            peakfactor.setText("Peak Factor : " + preferHelper.getPeakFactor());
        } else {
            peakFactorView.setVisibility(View.GONE);
            peakfactor.setVisibility(View.GONE);
        }
        promocode.setOnClickListener(promoclicklistener);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        //setButtonChanger();
        // dialog.setCanceledOnTouchOutside(false);
        // return this.dialog;
    }

    public void initNewSelectRideBottomSheet(View.OnClickListener selectCarListner,
                                             View.OnClickListener promoclicklistener,
                                             View.OnClickListener paymentMethodListner,
                                             View.OnClickListener requestGoListner, BasePreferenceHelper preferHelper,
                                             String origion, String destination, SelectCarEnt selectCarEnt,
                                             PromoCodeEnt promoCodeEnt, EstimateFareEnt estimateFareEnt, String peakFactor) {

        LinearLayout promocode = (LinearLayout) dialog.findViewById(R.id.ll_promoCode);
        LinearLayout selectCar = (LinearLayout) dialog.findViewById(R.id.ll_car_type);
        LinearLayout paymentMethod = (LinearLayout) dialog.findViewById(R.id.ll_payment_method);
        LinearLayout ll_peakFactor = (LinearLayout) dialog.findViewById(R.id.ll_peakFactor);
        Button requestGo = (Button) dialog.findViewById(R.id.SubmitButton);
        AnyTextView txt_orign = (AnyTextView) dialog.findViewById(R.id.txt_pick_text);
        AnyTextView txt_dest = (AnyTextView) dialog.findViewById(R.id.txt_destination_text);
        AnyTextView txt_estimatedFare = (AnyTextView) dialog.findViewById(R.id.txt_estimated_fare);
        AnyTextView txt_car_type = (AnyTextView) dialog.findViewById(R.id.txt_car_type);
        AnyTextView txt_promo_code = (AnyTextView) dialog.findViewById(R.id.txt_promo_code);
        AnyTextView txt_payment_method = (AnyTextView) dialog.findViewById(R.id.txt_payment_method);
        AnyTextView txt_peakFactor = (AnyTextView) dialog.findViewById(R.id.txt_peakFactor);


        txt_orign.setText(origion);
        txt_dest.setText(destination);
        txt_payment_method.setVisibility(View.VISIBLE);
        txt_payment_method.setText(AppConstants.cash);

        if (!peakFactor.equals("")) {
            ll_peakFactor.setVisibility(View.VISIBLE);
            txt_peakFactor.setText(peakFactor);
        }

        if (selectCarEnt != null) {
            txt_car_type.setVisibility(View.VISIBLE);
            txt_car_type.setText(selectCarEnt.getType() + "");
        }

        if (promoCodeEnt != null) {
            txt_promo_code.setVisibility(View.VISIBLE);
            txt_promo_code.setText(promoCodeEnt.getPercentage()+"%");
        }

        if (estimateFareEnt != null) {
            txt_estimatedFare.setVisibility(View.VISIBLE);
            txt_estimatedFare.setText("AED "+estimateFareEnt.getEstimateFare());
        }
        if (preferHelper.getPaymentType().equals(AppConstants.creditCard)) {
            txt_payment_method.setText(AppConstants.creditCard);
        } else {
            txt_payment_method.setText(AppConstants.cash);
        }


        selectCar.setOnClickListener(selectCarListner);
        promocode.setOnClickListener(promoclicklistener);
        paymentMethod.setOnClickListener(paymentMethodListner);
        requestGo.setOnClickListener(requestGoListner);

    }

    public void initNewSelectRideLaterBottomSheet(View.OnClickListener selectCarListner,
                                                  View.OnClickListener promoclicklistener, View.OnClickListener DatePickclicklistener, View.OnClickListener TimePickclicklistener,
                                                  View.OnClickListener paymentMethodListner, View.OnClickListener requestGoListner,
                                                  BasePreferenceHelper preferHelper, String origion, String destination, SelectCarEnt selectCarEnt,
                                                  PromoCodeEnt promoCodeEnt, EstimateFareEnt estimateFareEnt, String peakFactor,Date date,Date time) {

        LinearLayout promocode = (LinearLayout) dialog.findViewById(R.id.ll_promoCode);
        LinearLayout selectCar = (LinearLayout) dialog.findViewById(R.id.ll_car_type);
        LinearLayout paymentMethod = (LinearLayout) dialog.findViewById(R.id.ll_payment_method);
        LinearLayout ll_pickup_date = (LinearLayout) dialog.findViewById(R.id.ll_pickup_date);
        LinearLayout ll_pickupTime = (LinearLayout) dialog.findViewById(R.id.ll_pickupTime);
        LinearLayout ll_peakFactor = (LinearLayout) dialog.findViewById(R.id.ll_peakFactor);
        LinearLayout ll_schedule_time = (LinearLayout) dialog.findViewById(R.id.ll_schedule_time);
        Button requestGo = (Button) dialog.findViewById(R.id.SubmitButton);
        AnyTextView txt_orign = (AnyTextView) dialog.findViewById(R.id.txt_pick_text);
        AnyTextView txt_dest = (AnyTextView) dialog.findViewById(R.id.txt_destination_text);
        AnyTextView txt_estimatedFare = (AnyTextView) dialog.findViewById(R.id.txt_estimated_fare);
        AnyTextView txt_car_type = (AnyTextView) dialog.findViewById(R.id.txt_car_type);
        AnyTextView txt_promo_code = (AnyTextView) dialog.findViewById(R.id.txt_promo_code);
        AnyTextView txt_payment_method = (AnyTextView) dialog.findViewById(R.id.txt_payment_method);
        AnyTextView txt_peakFactor = (AnyTextView) dialog.findViewById(R.id.txt_peakFactor);
        ImageView scheduleView = (ImageView) dialog.findViewById(R.id.view);
        AnyTextView txt_pickup_date = (AnyTextView) dialog.findViewById(R.id.txt_pickup_date);
        AnyTextView txt_pickupTime = (AnyTextView) dialog.findViewById(R.id.txt_pickupTime);


        ll_schedule_time.setVisibility(View.VISIBLE);
        scheduleView.setVisibility(View.VISIBLE);
        txt_orign.setText(origion);
        txt_dest.setText(destination);
        txt_payment_method.setVisibility(View.VISIBLE);
        txt_payment_method.setText(AppConstants.cash);

        if (!peakFactor.equals("")) {
            ll_peakFactor.setVisibility(View.VISIBLE);
            txt_peakFactor.setText(peakFactor);
        }

        if (selectCarEnt != null) {
            txt_car_type.setVisibility(View.VISIBLE);
            txt_car_type.setText(selectCarEnt.getType() + "");
        }

        if (promoCodeEnt != null) {
            txt_promo_code.setVisibility(View.VISIBLE);
            txt_promo_code.setText(promoCodeEnt.getPercentage()+"%");
        }

        if (estimateFareEnt != null) {
            txt_estimatedFare.setVisibility(View.VISIBLE);
            txt_estimatedFare.setText("AED "+estimateFareEnt.getEstimateFare());
        }
        if (preferHelper.getPaymentType().equals(AppConstants.creditCard)) {
            txt_payment_method.setText(AppConstants.creditCard);
        } else {
            txt_payment_method.setText(AppConstants.cash);
        }

        if (date != null) {
            String predate = new SimpleDateFormat("EEE,MMM d").format(date.getTime());
            txt_pickup_date.setVisibility(View.VISIBLE);
            txt_pickup_date.setText(predate);

        }
        if(time != null){
            String preTime = new SimpleDateFormat("HH:mm a").format(time.getTime()) + "-" +
                    new SimpleDateFormat("HH:mm a").format(time.getTime());
            txt_pickupTime.setVisibility(View.VISIBLE);
            txt_pickupTime.setText(preTime);
        }


        selectCar.setOnClickListener(selectCarListner);
        promocode.setOnClickListener(promoclicklistener);
        paymentMethod.setOnClickListener(paymentMethodListner);
        ll_pickup_date.setOnClickListener(DatePickclicklistener);
        ll_pickupTime.setOnClickListener(TimePickclicklistener);
        requestGo.setOnClickListener(requestGoListner);

    }

    public AnyTextView getDateEditText() {
        AnyTextView txt_pickup_date = (AnyTextView) dialog.findViewById(R.id.txt_pickup_date);
        return txt_pickup_date;
    }

    public AnyTextView getTimeEditText() {
        AnyTextView txt_pickupTime = (AnyTextView) dialog.findViewById(R.id.txt_pickupTime);
        return txt_pickupTime;
    }

    public void initSelectRideBottomSheet(View.OnClickListener promoclicklistener,
                                          View.OnClickListener oncancelclicklistener, int text, ArrayList<SelectCarEnt> carTypes, BasePreferenceHelper preferHelper, BottomSheetDialogHelper setupRideDialoge) {
        // this.dialog.setContentView(layoutID);

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);

        if (carTypes != null && carTypes.size() > 0) {
            setSelectAdapter(carTypes);
        } else {
            setupRideDialoge.hideDialog();
        }

        AnyTextView peakfactor = (AnyTextView) dialog.findViewById(R.id.txt_peakFactor);
        View peakFactorView = (View) dialog.findViewById(R.id.peakFactorView);

        if (!preferHelper.getPeakFactor().equals("")) {
            peakfactor.setVisibility(View.VISIBLE);
            peakFactorView.setVisibility(View.VISIBLE);
            peakfactor.setText("Peak Factor : " + preferHelper.getPeakFactor());
        } else {
            peakFactorView.setVisibility(View.GONE);
            peakfactor.setVisibility(View.GONE);
        }

        Button cancelbutton = (Button) dialog.findViewById(R.id.SubmitButton);
        AnyTextView promocode = (AnyTextView) dialog.findViewById(R.id.txt_promoCode);
        cancelbutton.setText(context.getResources().getString(text));
        promocode.setOnClickListener(promoclicklistener);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        //setButtonChanger();
        // dialog.setCanceledOnTouchOutside(false);
        // dialog.setCancelable(false);
        // return this.dialog;
    }

    private void setSelectAdapter(ArrayList<SelectCarEnt> carTypes) {
        mAdapter = new SelectCarAdapter(carTypes, context);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    public SelectCarEnt getSelectedType() {
        return mAdapter.getSelectedItemPosition();
    }

    public void initEstimateFareBottomSheet(View.OnClickListener requestclicklistener, String Type,
                                            String numberOfPeople, String ImageUrl, int fare) {
        //this.dialog.setContentView(layoutID);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.img_selected_ride);
        Glide.with(context).load(ImageUrl).into(imageView);
        AnyTextView textView = (AnyTextView) dialog.findViewById(R.id.txt_no_people);
        textView.setText(numberOfPeople + " People");
        AnyTextView name = (AnyTextView) dialog.findViewById(R.id.txt_type);
        name.setText(Type);
        AnyTextView faretextView = (AnyTextView) dialog.findViewById(R.id.txt_fare_ammount);
        double percentage = 0.3;
        if (fare == 0) {
            fare = 1;
        }
        int fare_increse = (int) (fare + fare * percentage);
        faretextView.setText("AED " + fare + " - " + fare_increse);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_done);
        cancelbutton.setOnClickListener(requestclicklistener);
        // dialog.setCanceledOnTouchOutside(false);
        //return this.dialog;
    }

    public void initScheduleDateTimeDialog(View.OnClickListener okclicklistener,
                                           View.OnClickListener Dateclicklistener,
                                           View.OnClickListener Timeclicklistener) {
        //this.dialog.setContentView(layoutID);
        Button cancelbutton = (Button) dialog.findViewById(R.id.SubmitButton);
        cancelbutton.setOnClickListener(okclicklistener);
        //  dialog.setCanceledOnTouchOutside(false);
        final AnyTextView date_pick = (AnyTextView) dialog.findViewById(R.id.txt_datepicker);
        date_pick.setOnClickListener(Dateclicklistener);
        final AnyTextView time_pick = (AnyTextView) dialog.findViewById(R.id.txt_timepicker);
        time_pick.setOnClickListener(Timeclicklistener);
        // return this.dialog;
    }

    public void initSchedulesTimeDialog(View.OnClickListener okclicklistener,

                                        Date startDate,
                                        Date StartTime) {
        // this.dialog.setContentView(layoutID);
        Button cancelbutton = (Button) dialog.findViewById(R.id.SubmitButton);
        cancelbutton.setOnClickListener(okclicklistener);
        // dialog.setCanceledOnTouchOutside(false);
        /*final WheelView date_loop = (WheelView) dialog.findViewById(R.id.date);
        date_loop.setInitialPosition(2);
        date_loop.setIsLoopEnabled(false);
        date_loop.addOnLoopScrollListener(dateloop);
        date_loop.setTextSize(15);
        date_loop.setItems(getdateitems(startDate));
        final WheelView time_loop = (WheelView) dialog.findViewById(R.id.time_loop);
        time_loop.setInitialPosition(2);
        time_loop.setIsLoopEnabled(false);
        time_loop.addOnLoopScrollListener(dateloop);
        time_loop.setTextSize(15);
        time_loop.setItems(gettimeList(StartTime));*/
        // return this.dialog;
    }

    private List gettimeList(Date startTime) {

        return null;
    }

    private List getdateitems(Date startDate) {
        return null;
    }

  /*  private void setButtonChanger() {
        AnyTextView economyInactivetxt = (AnyTextView) dialog.findViewById(R.id.txt_economy);
        final AnyTextView economyactivetxt = (AnyTextView) dialog.findViewById(R.id.txteconomyActive);
        AnyTextView businesstxt = (AnyTextView) dialog.findViewById(R.id.txt_business);
        final AnyTextView businessActivetxt = (AnyTextView) dialog.findViewById(R.id.txt_businessActive);
        AnyTextView viptxt = (AnyTextView) dialog.findViewById(R.id.txt_vip);
        final AnyTextView vipActivetxt = (AnyTextView) dialog.findViewById(R.id.txt_vipActive);
        ImageView economyinactive = (ImageView) dialog.findViewById(R.id.iv_economyCar);
        final ImageView economyactive = (ImageView) dialog.findViewById(R.id.iv_economyCarActive);
        ImageView businessinactive = (ImageView) dialog.findViewById(R.id.iv_businessCar);
        final ImageView businessactive = (ImageView) dialog.findViewById(R.id.iv_businessCarActive);
        ImageView vipinactive = (ImageView) dialog.findViewById(R.id.iv_vipCar);
        final ImageView vipactive = (ImageView) dialog.findViewById(R.id.iv_vipCarActive);
        economyinactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                economyactive.setVisibility(View.VISIBLE);
                economyactivetxt.setVisibility(View.VISIBLE);
                vipactive.setVisibility(View.GONE);
                vipActivetxt.setVisibility(View.GONE);
                businessactive.setVisibility(View.GONE);
                businessActivetxt.setVisibility(View.GONE);
            }
        });
        economyactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        businessactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        businessinactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                economyactive.setVisibility(View.GONE);
                economyactivetxt.setVisibility(View.GONE);
                vipactive.setVisibility(View.GONE);
                vipActivetxt.setVisibility(View.GONE);
                businessactive.setVisibility(View.VISIBLE);
                businessActivetxt.setVisibility(View.VISIBLE);
            }
        });
        vipactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        vipinactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                economyactive.setVisibility(View.GONE);
                economyactivetxt.setVisibility(View.GONE);
                vipactive.setVisibility(View.VISIBLE);
                vipActivetxt.setVisibility(View.VISIBLE);
                businessactive.setVisibility(View.GONE);
                businessActivetxt.setVisibility(View.GONE);
            }
        });


    }*/

    public void showDialog() {
        // setupRideNowDialog();
// init the bottom sheet behavior


// change the drive_state of the bottom sheet
        //  bottomSheetBehavior.setAllowUserDragging(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

// set the peek height
        bottomSheetBehavior.setPeekHeight((int) context.getResources().getDimension(R.dimen.x100));

// set hideable or not
        bottomSheetBehavior.setHideable(false);

        //dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        //dialog.setCancelable(isCancelable);
        //  dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        RemoveDialog();
        // dialog.dismiss();
    }

    private void RemoveDialog() {
        mainParent.removeView(dialog);
        // dialog.dismiss();
    }


}
