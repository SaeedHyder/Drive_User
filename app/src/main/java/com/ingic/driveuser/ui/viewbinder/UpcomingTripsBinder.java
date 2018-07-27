package com.ingic.driveuser.ui.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.ProgressEnt;
import com.ingic.driveuser.fragments.MyRideDetailFragment;
import com.ingic.driveuser.helpers.DateHelper;
import com.ingic.driveuser.interfaces.OnItemClickListner;
import com.ingic.driveuser.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saeedhyder on 7/3/2017.
 */

public class UpcomingTripsBinder extends ViewBinder<ProgressEnt> {
    private Picasso picasso;
    private DockActivity dockActivity;
    private OnItemClickListner onItemClickListner;

    public UpcomingTripsBinder(DockActivity dockActivity, OnItemClickListner onItemClickListner) {
        super(R.layout.upcoming_trips_item);
        this.dockActivity = dockActivity;
        picasso = Picasso.with(dockActivity);
        this.onItemClickListner = onItemClickListner;

    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final ProgressEnt entity, int position, int grpPosition, View view, Activity activity) {
        view.setVisibility(View.VISIBLE);
       /* String origin = "24.839611,67.082231";
        String destination = "24.829428,67.073822";*/
       /* String origin = entity.getPickupLatitude() + "," + entity.getPickupLongitude();
        String destination = entity.getDestinationLatitude() + "," + entity.getDestinationLongitude();
        // imageLoader.displayImage(entity.getUpcomingImg(),viewHolder.ivUpcomingTrips);
        try {
            new DirectionFinder(this, origin, destination, view, entity).execute();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
*/
     /*
        final String image_map = entity.getImageUrl();
        viewHolder.txtRideNo.setText(entity.getId() + "");
        viewHolder.txtFare.setText("AED " + entity.getEstimateFare());
        viewHolder.txtUpcomingTimeDate.setText(DateHelper.getDesireFormatDate(entity.getDate(), "yyyy-MM-dd", "EEE,MMM d") + " at "
                + DateHelper.getDesireFormatDate(entity.getTime(), "hh:mm:ss", "HH:mm a"));
        viewHolder.txtUpcomingType.setText(entity.getVechicleDetail().getType() + "");
        picasso.load(image_map==null||image_map.trim().equals("")
                ?"asd":image_map)
                .fit().into(viewHolder.ivUpcomingTrips);*/

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        if (entity.getStatus().equals("1") || entity.getStatus().equals("4") || entity.getStatus().equals("5")) {
            viewHolder.llTrackRide.setVisibility(View.VISIBLE);
        }
        viewHolder.txtPickText.setText(entity.getPickupAddress() + "");
        viewHolder.txtDestinationText.setText(entity.getDestinationAddress() + "");
        if (entity.getType().equals("later")) {
            viewHolder.txtTimeDate.setText(entity.getDate() + " " + entity.getTime());
        } else {
            viewHolder.txtTimeDate.setText(entity.getDate() + " " + DateHelper.getLocalTimeDate(entity.getTime()));
        }

        viewHolder.txtType.setText(entity.getVechicleDetail().getVehicleTypeDetail().getType() + "");
        viewHolder.txtCar.setText(entity.getVechicleDetail().getVehicleName() + "");
        viewHolder.txtFare.setText("AED " + entity.getEstimateFare() + "");


        if (!(entity.getType().equals("later"))) {
            viewHolder.llMainframe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dockActivity.replaceDockableFragment(MyRideDetailFragment.newInstance(entity));
                }
            });
        }

        viewHolder.llTrackRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListner.onClick(entity);
            }
        });
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.iv_pastTrips)
        ImageView ivPastTrips;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txt_pickup)
        AnyTextView txtPickup;
        @BindView(R.id.txt_pick_text)
        AnyTextView txtPickText;
        @BindView(R.id.imageView_destination)
        ImageView imageViewDestination;
        @BindView(R.id.txt_destination)
        AnyTextView txtDestination;
        @BindView(R.id.txt_destination_text)
        AnyTextView txtDestinationText;
        @BindView(R.id.txt_time_date)
        AnyTextView txtTimeDate;
        @BindView(R.id.txt_car)
        AnyTextView txtCar;
        @BindView(R.id.txt_fare)
        AnyTextView txtFare;
        @BindView(R.id.txt_type)
        AnyTextView txtType;
        @BindView(R.id.ll_pastTripDetail)
        LinearLayout llPastTripDetail;
        @BindView(R.id.ll_mainframe)
        LinearLayout llMainframe;
        @BindView(R.id.ll_track_ride)
        LinearLayout llTrackRide;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


 /*   @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route) {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> route, View view, Object object) {

        if (view != null && object != null) {
            ProgressEnt entity = (ProgressEnt) object;
          *//*  String origin = "24.839611,67.082231";
            String destination = "24.829428,67.073822";*//*
            String origin = entity.getPickupLatitude() + "," + entity.getPickupLongitude();
            String destination = entity.getDestinationLatitude() + "," + entity.getDestinationLongitude();
            String CustomMarkerOrigin = "http://35.160.175.165/portfolio/fast_cab/public/images/profile_images/pickup.png";
            String CustomMarkerDestination = "http://35.160.175.165/portfolio/fast_cab/public/images/profile_images/destination.png";
            StringBuilder stringBuilder = new StringBuilder();

            for (Route routesingle : route) {
                for (int i = 0; i < routesingle.points.size(); i++) {
                    stringBuilder.append(routesingle.points.get(i) + "|");
                }
            }
            String routesList = stringBuilder.toString();
            routesList = routesList.replaceAll("[^\\d.|,]", "");
            try {
                routesList = routesList.substring(0, routesList.length() - 1);

            } catch (Exception e) {
                e.printStackTrace();
                routesList = "";
            }
            final UpcomingTripsBinder.ViewHolder viewHolder = (UpcomingTripsBinder.ViewHolder) view.getTag();
            viewHolder.txtRideNo.setText(entity.getId() + "");
            viewHolder.txtFare.setText("AED " + entity.getEstimateFare());
            viewHolder.txtUpcomingTimeDate.setText(DateHelper.getDesireFormatDate(entity.getDate(), "yyyy-MM-dd", "EEE,MMM d") + " at "
                    + DateHelper.getDesireFormatDate(entity.getTime(), "hh:mm:ss", "HH:mm a"));
            viewHolder.txtUpcomingType.setText(entity.getVechicleDetail().getType() + "");
            Picasso.with(view.getContext()).load(getStaticMapURL(origin, destination, routesList, CustomMarkerOrigin,
                    CustomMarkerDestination, view.getResources().getString(R.string.API_KEY)))
                    .fit().into(viewHolder.ivUpcomingTrips);
            view.setVisibility(View.VISIBLE);
        }

        }

    private String getStaticMapURL(String origin, String destination, String routelist, String customMarkerOrigin, String customMarkerDestination, String APIKEY) {
        return "https://maps.googleapis.com/maps/api/staticmap?visible=" + routelist + "&scale=2&size=300x150&maptype=roadmap" +
                "&markers=icon:" + customMarkerOrigin + "|" + origin + "&markers=icon:" + customMarkerDestination + "|" + destination +
                "&path=color:0x070707FF|weight:5|" + routelist + "&key=" + APIKEY;
    }*/


}
