package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 4/17/2018.
 */

public class WalletInfoEnt {

    @SerializedName("wallet_amount")
    @Expose
    private String walletAmount;
    @SerializedName("wallet_status")
    @Expose
    private String walletStatus;

    public String getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(String walletAmount) {
        this.walletAmount = walletAmount;
    }

    public String getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        this.walletStatus = walletStatus;
    }
}
