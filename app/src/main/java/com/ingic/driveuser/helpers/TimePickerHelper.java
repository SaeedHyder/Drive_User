package com.ingic.driveuser.helpers;


import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentManager;



import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created on 4/29/2017.
 */

public class TimePickerHelper {
    private TimePickerDialog dialog;

    public TimePickerHelper() {

    }
    public TimePickerDialog initTimeDialog(Context context, int hour, int minute, TimePickerDialog.OnTimeSetListener onTimeSetListener, boolean is24Hour){
        this.dialog = new TimePickerDialog(context, onTimeSetListener,hour,minute,is24Hour );
        // this.dialog = new TimePickerDialog();
        dialog.setTitle("Select Pickup Time");
        return dialog;
    }

    public void showTime(FragmentManager fm, String tag ) {
        if (this.dialog == null){
            throw  new NullPointerException("Initialize Dialog First");
        }else {
            //this.dialog.show(fm,tag);
            this.dialog.show();
        }

    }

    public String getTime(int hourOfDay, int minute) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        c.set(year,month,day,hourOfDay, minute);
        return  new SimpleDateFormat("h:mm a").format(c.getTime());
    }

}
