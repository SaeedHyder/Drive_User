package com.ingic.driveuser.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.CarRide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created on 6/22/2017.
 */

public class RideSelectionAdapter extends RecyclerView.Adapter<RideSelectionAdapter.MyViewHolder>  {
    private DockActivity context;
    private ImageLoader imageLoader;
    private List<CarRide> rideEntList;

    public RideSelectionAdapter(DockActivity context, List<CarRide> rideEntList) {
        this.context = context;
        this.rideEntList = rideEntList;
    }

    @Override
    public RideSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
/*        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_ride_selection, parent, false);

        return new MyViewHolder(itemView);*/
        return null;

    }

    @Override
    public void onBindViewHolder(RideSelectionAdapter.MyViewHolder holder, int position) {
        holder.img_icon_big.setImageResource(rideEntList.get(position).getDrawable_big());
        holder.img_icon.setImageResource(rideEntList.get(position).getDrawable());
        holder.title.setText(rideEntList.get(position).getTitle());
        if (rideEntList.get(position).isSelected()){
            holder.img_icon_big.setVisibility(View.GONE);
            holder.title.setTextColor(holder.title.getResources().getColor(R.color.button_color));
        }
    }

    @Override
    public int getItemCount() {
        return rideEntList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_icon;
        private ImageView img_icon_big;
        public TextView title;

        private MyViewHolder(View view) {
            super(view);
           /* img_icon = (ImageView) view.findViewById(R.id.img_icon);
            img_icon_big = (ImageView) view.findViewById(R.id.img_icon_big);
            title = (TextView) view.findViewById(R.id.txt_title);*/

        }
    }
}
