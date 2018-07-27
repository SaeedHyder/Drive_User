package com.ingic.driveuser.entities;

/**
 * Created by saeedhyder on 6/21/2017.
 */

public class NotificationEnt {

    Integer Notificationlogo;
    String NotificationTxt;

    public NotificationEnt(Integer Notificationlogo, String NotificationTxt)
    {
        setNotificationlogo(Notificationlogo);
        setNotificationTxt(NotificationTxt);

    }

    public Integer getNotificationlogo() {
        return Notificationlogo;
    }

    public void setNotificationlogo(Integer notificationlogo) {
        Notificationlogo = notificationlogo;
    }

    public String getNotificationTxt() {
        return NotificationTxt;
    }

    public void setNotificationTxt(String notificationTxt) {
        NotificationTxt = notificationTxt;
    }
}
