package com.kayali_developer.sobhimohammad.data.model;

import com.google.gson.annotations.*;

public class PushNotification {

    @SerializedName("payload")
    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
