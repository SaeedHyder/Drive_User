package com.ingic.driveuser.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 7/17/2017.
 */

public class NotificationListEnt {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private Integer senderId;
    @SerializedName("reciever_id")
    @Expose
    private Integer recieverId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("action_type")
    @Expose
    private String actionType;
    @SerializedName("action_id")
    @Expose
    private Integer actionId;
    @SerializedName("created_at")
    @Expose
    private String created_at;


    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(Integer recieverId) {
        this.recieverId = recieverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getActionId() {
        return actionId;
    }

    public void setActionId(Integer actionId) {
        this.actionId = actionId;
    }
}
