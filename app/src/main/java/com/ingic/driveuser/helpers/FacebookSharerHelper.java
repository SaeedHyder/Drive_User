package com.ingic.driveuser.helpers;


import android.content.Context;

import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;

public class FacebookSharerHelper implements FacebookCallback<Sharer.Result> {



    private Context mContext;

    public FacebookSharerHelper(Context context){
        mContext = context;


    }

    @Override
    public void onSuccess(Sharer.Result result) {
        UIHelper.showLongToastInCenter(mContext,"Successfully shared!");
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException e) {
        UIHelper.showLongToastInCenter(mContext,  e.getMessage());
    }


    public void showShareDialog(ShareDialog shareDialog) {


      /*  if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                   // .setImageUrl(ShareMessageHelper.getAppImage(mContext))
                    .setContentTitle(ShareMessageHelper.getTitle())
                    .setContentDescription(
                            ShareMessageHelper.getDescription())
                             //.setContentUrl(ShareMessageHelper.getAppUri(mContext)) use when app is uploaded
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))

                    .build();

            shareDialog.show(linkContent);
        }*/
    }

    public void showMessengerDialog(MessageDialog messengerDialog) {
/*
        if (MessageDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    //.setImageUrl(ShareMessageHelper.getAppImage(mContext))
                    .setContentTitle(ShareMessageHelper.getTitle())
                    .setContentDescription(
                            ShareMessageHelper.getDescription())
                            //.setContentUrl(ShareMessageHelper.getAppUri(mContext)) use when app is uploaded
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            messengerDialog.show(linkContent);


        } else {
            UIHelper.showLongToastInCenter(mContext, "You don't have facebook messenger");
        }*/
    }



}
