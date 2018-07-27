package com.ingic.driveuser.interfaces;

import com.ingic.driveuser.entities.FacebookLoginEnt;

/**
 * Created on 7/15/2017.
 */

public interface FacebookLoginListener {
    public void onSuccessfulFacebookLogin(FacebookLoginEnt LoginEnt);
}
