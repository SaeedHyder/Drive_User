package com.ingic.driveuser.ui.viewbinder;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.LocationEnt;
import com.ingic.driveuser.ui.viewbinders.abstracts.ViewBinder;

import java.util.StringTokenizer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 7/11/2017.
 */

public class RecentPlaceViewBinder extends ViewBinder<LocationEnt> {
    private String address;
    public RecentPlaceViewBinder() {
        super(R.layout.row_item_history);
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(LocationEnt entity, int position, int grpPosition, View view, Activity activity) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        StringTokenizer tokens = new StringTokenizer(entity.getAddress(), ",");
        String first = tokens.nextToken();
        if(first.contains("null")){
            first = first.replace("null,", "");
        }
        viewHolder.text1.setText(first);
       // viewHolder.text1.setAlpha(0.7f);
       // viewHolder.text1.setPaintFlags(Typeface.BOLD);
        address=entity.getAddress();
        if(address.contains("null")){
            address = address.replace("null,", "");
        }
        viewHolder.text2.setText(address);
        viewHolder.text2.setPaintFlags((Typeface.BOLD));
        viewHolder.imgIcon.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.drive_hilton_sharja));
    }

    public static class ViewHolder extends ViewBinder.BaseViewHolder {
        @BindView(R.id.img_icon)
        ImageView imgIcon;
        @BindView(R.id.text1)
        TextView text1;
        @BindView(R.id.text2)
        TextView text2;


        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}