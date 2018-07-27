package com.ingic.driveuser.ui.viewbinder;

import android.app.Activity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ingic.driveuser.R;
import com.ingic.driveuser.entities.MessagesEnt;
import com.ingic.driveuser.ui.viewbinders.abstracts.ViewBinder;
import com.ingic.driveuser.ui.views.AnyTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by saeedhyder on 6/22/2017.
 */

public class MessagesBinder extends ViewBinder<MessagesEnt> {

    public MessagesBinder() {
        super(R.layout.messages_item);
    }

    @Override
    public BaseViewHolder createViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public void bindView(final MessagesEnt entity, int position, int grpPosition, View view, Activity activity) {

        MessagesBinder.ViewHolder viewHolder = (MessagesBinder.ViewHolder) view.getTag();

        viewHolder.txtMessages.setText(entity.getMessagesTxt());

        viewHolder.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(entity.getMobileNo(),entity.getMessagesTxt());
            }
        });

    }

    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.txt_messages)
        AnyTextView txtMessages;
        @BindView(R.id.btn_send)
        Button btnSend;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
