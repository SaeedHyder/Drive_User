package com.ingic.driveuser.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;

import static com.ingic.driveuser.R.id.imageView;

/**
 * Created by saeedhyder on 7/18/2017.
 */

public class RideFeedBack_GridAdapter extends BaseAdapter {

    private DockActivity mContext;

    public RideFeedBack_GridAdapter(DockActivity mContext, String[] array) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.row_item_ride_feedback, null);
          //  TextView textView = (TextView) grid.findViewById(R.id.grid_text);
           // ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
           // textView.setText(web[position]);
           // imageView.setImageResource(Imageid[position]);
        } else {
            grid = (View) convertView;
        }

        return grid;
    }
}
