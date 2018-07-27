package com.ingic.driveuser.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ingic.driveuser.R;
import com.ingic.driveuser.fragments.abstracts.BaseFragment;
import com.ingic.driveuser.helpers.UIHelper;
import com.ingic.driveuser.ui.views.AnyTextView;
import com.ingic.driveuser.ui.views.PinEntryEditText;
import com.ingic.driveuser.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by saeedhyder on 6/23/2017.
 */

public class InviteAndEarnFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.carLogo)
    ImageView carLogo;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText txtPinEntry;
    @BindView(R.id.txt_share)
    AnyTextView txtShare;
    @BindView(R.id.iv_messenger)
    ImageView ivMessenger;
    @BindView(R.id.iv_whatsup)
    ImageView ivWhatsup;
    @BindView(R.id.ll_socialIcons)
    LinearLayout llSocialIcons;
    @BindView(R.id.ll_invite)
    LinearLayout llInvite;

    public static InviteAndEarnFragment newInstance() {
        return new InviteAndEarnFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtPinEntry.setText(prefHelper.getUser().getInviteCode()+"");
        setListners();
    }

    private void setListners() {
        ivMessenger.setOnClickListener(this);
        ivWhatsup.setOnClickListener(this);
        txtPinEntry.setFocusable(false);
        txtPinEntry.setFocusableInTouchMode(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_messenger:
                ShareMyPromoCode("com.facebook.orca",getString(R.string.messenger_share_error),getString(R.string.Share_message)+prefHelper.getUser().getInviteCode()+"");
                break;
            case R.id.iv_whatsup:
                ShareMyPromoCode("com.whatsapp",getString(R.string.whatsapp_share_error),getString(R.string.Share_message)+prefHelper.getUser().getInviteCode()+"");
                //UIHelper.showShortToastInCenter(getDockActivity(),"Will be implemented in Beta Version");
                break;
        }
    }
    private void ShareMyPromoCode(String packageName,String ErrorMessage,String Message){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage(packageName);
        intent.putExtra(Intent.EXTRA_TEXT, Message);
        try {
            this.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            UIHelper.showShortToastInCenter(getDockActivity(),ErrorMessage);
        }
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.hideButtons();
        titleBar.showMenuButton();
        titleBar.setSubHeading(getString(R.string.Invite_and_earn));
    }


}
