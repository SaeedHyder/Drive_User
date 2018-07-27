package com.ingic.driveuser.entities;

/**
 * Created by saeedhyder on 6/22/2017.
 */

public class MessagesEnt {

    String messagesTxt;
    String mobileNo;

    public MessagesEnt(String messagesTxt,String mobileNo)
    {
        setMessagesTxt(messagesTxt);
        setMobileNo(mobileNo);
    }

    public String getMessagesTxt() {
        return messagesTxt;
    }

    public void setMessagesTxt(String messagesTxt) {
        this.messagesTxt = messagesTxt;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
}
