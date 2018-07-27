package com.ingic.driveuser.ui.viewbinder;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.NotificationListEnt;
import com.ingic.driveuser.helpers.DateHelper;
import com.ingic.driveuser.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.driveuser.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationitemBinder extends ViewBinder<NotificationListEnt> {


    public NotificationitemBinder() {
        super(R.layout.notification_item);
    }


    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final NotificationListEnt entity, int position, int grpPosition, View view, Activity activity) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        viewHolder.ivNotificationlogo.setImageResource(R.drawable.drive_logo);
        viewHolder.txtNotification.setText(entity.getMessage());

        String createDate= DateHelper.getLocalTime(entity.getCreated_at());
        String[] createAt=createDate.split("\\s+");
        String date=createAt[0].trim();
        String time=createAt[1].trim();


         viewHolder.txtDate.setText(DateHelper.getFormatedDate("yyyy-MM-dd","dd MMMM yyyy",date));
         viewHolder.txtTime.setText(DateHelper.getFormatedDate("HH:mm:ss","HH:mm",time));


    }



    class ViewHolder extends BaseViewHolder  {
        @BindView(R.id.iv_Notificationlogo)
        ImageView ivNotificationlogo;
        @BindView(R.id.txt_Notification)
        AnyTextView txtNotification;
        @BindView(R.id.txt_date)
        AnyTextView txtDate;
        @BindView(R.id.txt_time)
        AnyTextView txtTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
