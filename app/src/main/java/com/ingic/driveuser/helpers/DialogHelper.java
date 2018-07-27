package com.ingic.driveuser.helpers;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.DriverEnt;
import com.ingic.driveuser.entities.RideDriverEnt;
import com.ingic.driveuser.entities.SelectCarEnt;
import com.ingic.driveuser.ui.adapters.SelectCarAdapter;
import com.ingic.driveuser.ui.views.AnyEditTextView;
import com.ingic.driveuser.ui.views.AnyTextView;

import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created on 5/24/2017.
 */

public class DialogHelper {
    private Dialog dialog;
    private DockActivity context;
    private RecyclerView recyclerView;
    private SelectCarAdapter mAdapter;

    public DialogHelper(DockActivity context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public Dialog initForgotPasswordDialog(int layoutID, View.OnClickListener onclicklistener, String Title, String message) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        TextView closeButton = (TextView) dialog.findViewById(R.id.btn_ok);
        closeButton.setOnClickListener(onclicklistener);
        AnyTextView title = (AnyTextView) dialog.findViewById(R.id.txt_title);
        title.setText(Title);
        AnyTextView Message = (AnyTextView) dialog.findViewById(R.id.txt_message);
        // Message.setText(message);
        return this.dialog;
    }

    public void setTextViewText(int ID, String Text) {
        AnyTextView textView = (AnyTextView) dialog.findViewById(ID);
        textView.setText(Text);
    }


    public Dialog initJobRefusalDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }

    public Dialog initDriverArrivedDialoge(int layoutID, View.OnClickListener okListner, RideDriverEnt driverEnt) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okButton = (Button) dialog.findViewById(R.id.btn_ok);
        AnyTextView txt_captain_name = (AnyTextView) dialog.findViewById(R.id.txt_captain_name);
        AnyTextView txt_car = (AnyTextView) dialog.findViewById(R.id.txt_car);
        AnyTextView txt_plate = (AnyTextView) dialog.findViewById(R.id.txt_plate);
        okButton.setOnClickListener(okListner);

        txt_captain_name.setText(driverEnt.getDriverDetail().getFullName()+"");
        txt_car.setText(driverEnt.getVehicleDetail().getVehicleName()+"");
        txt_plate.setText(driverEnt.getVehicleDetail().getVehicleNumber()+"");


        return this.dialog;
    }

    public Dialog initCancelQuotationDialog(int layoutID, View.OnClickListener onclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
        closeButton.setOnClickListener(onclicklistener);
        return this.dialog;
    }

    public Dialog LastRidePayment(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_No);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_yes);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog promoCode(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_cancel);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_submit);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog selectCar(int layoutID, View.OnClickListener oncancelclicklistener, View.OnClickListener onSubmitListner, ArrayList<SelectCarEnt> carTypes, SelectCarEnt selectCarEnt) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_cancel);
        okbutton.setOnClickListener(oncancelclicklistener);
        Button selectButton = (Button) dialog.findViewById(R.id.btn_select);
        selectButton.setOnClickListener(onSubmitListner);

        recyclerView = (RecyclerView) dialog.findViewById(R.id.recycler_view);

        mAdapter = new SelectCarAdapter(carTypes, context);

        if (selectCarEnt != null) {
            int position = 0;
            for (int i = 0; i < carTypes.size(); i++) {
                if (carTypes.get(i).getId() == selectCarEnt.getId()) {
                    position = i;
                }
            }
            mAdapter.setSelectedItemId(position);
        }

        if (carTypes != null && carTypes.size() > 0) {
            setSelectAdapter(carTypes);
        }
        return this.dialog;
    }

    private void setSelectAdapter(ArrayList<SelectCarEnt> carTypes) {

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


    public Dialog requestForCancellation(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_No);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_yes);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog logout(int layoutID, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);
        return this.dialog;
    }

    public Dialog scheduleDialoge(int layoutID, View.OnClickListener okbtn) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        Button okbutton = (Button) dialog.findViewById(R.id.btn_ok);
        okbutton.setOnClickListener(okbtn);
        return this.dialog;
    }

    public Dialog cancelRide(int layoutID, View.OnClickListener linkclicklistener, View.OnClickListener onokclicklistener, View.OnClickListener oncancelclicklistener,String cancelTime) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(layoutID);
        AnyTextView link = (AnyTextView) dialog.findViewById(R.id.txt_link);
        link.setPaintFlags(link.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        link.setOnClickListener(linkclicklistener);
        AnyTextView cancelReason = (AnyTextView) dialog.findViewById(R.id.txt_text);
        cancelReason.setText("If you cancel "+cancelTime+" minutes after requesting.a certain charges may apply");
        Button okbutton = (Button) dialog.findViewById(R.id.btn_yes);
        okbutton.setOnClickListener(onokclicklistener);
        Button cancelbutton = (Button) dialog.findViewById(R.id.btn_No);
        cancelbutton.setOnClickListener(oncancelclicklistener);

        return this.dialog;
    }

    /*
        public Dialog initRequestSendDialog(int layoutID, View.OnClickListener onclicklistener) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.dialog.setContentView(layoutID);
            Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
            closeButton.setOnClickListener(onclicklistener);
            return this.dialog;
        }


        public Dialog initJobDetailDialog(int layoutID, View.OnClickListener onclicklistener, String title, String person_name,
                                          View.OnClickListener arriveclickListener, View.OnClickListener completeclickListener) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.dialog.setContentView(layoutID);
            Button closeButton = (Button) dialog.findViewById(R.id.btn_submit);
            closeButton.setOnClickListener(onclicklistener);
            AnyTextView txttitle = (AnyTextView) dialog.findViewById(R.id.txt_problem_name);
            txttitle.setText(title);
            AnyTextView txtperson = (AnyTextView) dialog.findViewById(R.id.txt_personname);
            txtperson.setText(person_name);
            AnyTextView arrive = (AnyTextView) dialog.findViewById(R.id.txt_arrval_time);
            arrive.setOnClickListener(arriveclickListener);
            AnyTextView complete = (AnyTextView) dialog.findViewById(R.id.txt_complete_time);
            complete.setOnClickListener(completeclickListener);
            return this.dialog;
        }

        public AnyTextView getTimeTextview(int ID) {
            return (AnyTextView) dialog.findViewById(ID);

        }

        public Dialog initRatingDialog(int layoutID, View.OnClickListener onclicklistener, String title, String message) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            this.dialog.setContentView(layoutID);
            Button closeButton = (Button) dialog.findViewById(R.id.btn_close);
            closeButton.setOnClickListener(onclicklistener);
            AnyTextView txttitle = (AnyTextView) dialog.findViewById(R.id.txtHeader);
            txttitle.setText(title);
            AnyTextView txtmessage = (AnyTextView) dialog.findViewById(R.id.notwell_tv);
            txtmessage.setText(message);
            return this.dialog;
        }

        public float getRating(int ratingBarID) {
            CustomRatingBar ratingBar = (CustomRatingBar) dialog.findViewById(ratingBarID);
            return ratingBar.getScore();
        }
    */
    public String getEditText(int editTextID) {
        AnyEditTextView editTextView = (AnyEditTextView) dialog.findViewById(editTextID);
        UIHelper.hideSoftKeyboard(dialog.getContext(), editTextView);
        if (editTextView.getText().toString().equals("")) {
            editTextView.setError("Enter Correct Promo Code");
        }
        return editTextView.getText().toString();
    }

    public void showDialog() {

        dialog.show();
    }

    public void setCancelable(boolean isCancelable) {
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelable);
    }

    public void hideDialog() {
        dialog.dismiss();
    }
}
