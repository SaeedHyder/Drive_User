package com.ingic.driveuser.ui.viewbinders.abstracts;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.activities.DockActivity;
import com.ingic.driveuser.entities.NavigationEnt;
import com.ingic.driveuser.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 5/24/2017.
 */

public class NavigationItemBinder extends ViewBinder<NavigationEnt> {
    DockActivity activity;

    public NavigationItemBinder(DockActivity activity) {
        super(R.layout.row_item_nav);
        this.activity = activity;
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new NavViewHolder(view);
    }

    @Override
    public void bindView(NavigationEnt entity, int position, int grpPosition, View view, Activity activity) {
        NavViewHolder viewHolder = (NavViewHolder) view.getTag();
        viewHolder.imgSelected.setImageResource(entity.getResId());
        viewHolder.txtHome.setText(entity.getTitle());


    }


    public static class NavViewHolder extends BaseViewHolder {
        @BindView(R.id.img_selected)
        ImageView imgSelected;
        @BindView(R.id.txt_home)
        AnyTextView txtHome;
        @BindView(R.id.ll_item_container)
        LinearLayout llItemContainer;

        NavViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
