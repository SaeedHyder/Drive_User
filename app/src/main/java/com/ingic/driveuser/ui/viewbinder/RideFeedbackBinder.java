package com.ingic.driveuser.ui.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.RideFeedbackEnt;
import com.ingic.driveuser.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saeedhyder on 7/18/2017.
 */

public class RideFeedbackBinder extends ViewBinder<RideFeedbackEnt> {

    private ImageLoader imageLoader;
    private Boolean reasonsBoolean = false;
    private DockActivity context;
    private ArrayList<Integer> selectedReasons;

    public RideFeedbackBinder(DockActivity context) {
        super(R.layout.row_item_ride_feedback);
        imageLoader = ImageLoader.getInstance();
        this.context = context;
        selectedReasons = new ArrayList<>();
    }

    public ArrayList<Integer> getSelectedReasons() {
        return selectedReasons;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final RideFeedbackEnt entity, int position, int grpPosition, View view, Activity activity) {

        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.txtReason.setText(entity.getType());

        viewHolder.txtReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!reasonsBoolean) {
                    viewHolder.txtReason.setBackground(context.getResources().getDrawable(R.drawable.blue_rect));
                    viewHolder.drivingTick.setVisibility(View.VISIBLE);
                    viewHolder.txtReason.setTextColor(context.getResources().getColor(R.color.white));
                    viewHolder.txtReason.setAlpha(1);
                    reasonsBoolean = true;
                    if (!selectedReasons.contains(entity.getId())) {
                        selectedReasons.add(entity.getId());
                    }

                } else {
                    viewHolder.txtReason.setBackground(context.getResources().getDrawable(R.drawable.black_border));
                    viewHolder.drivingTick.setVisibility(View.GONE);
                    viewHolder.txtReason.setTextColor(context.getResources().getColor(R.color.black));
                    viewHolder.txtReason.setAlpha((float) 0.6);
                    reasonsBoolean = false;
                    if (selectedReasons.contains(entity.getId())) {
                        selectedReasons.remove(entity.getId());
                    }
                }
            }
        });
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_reason)
        AnyTextView txtReason;
        @BindView(R.id.drivingTick)
        ImageView drivingTick;
        @BindView(R.id.ll_reason)
        RelativeLayout llReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
