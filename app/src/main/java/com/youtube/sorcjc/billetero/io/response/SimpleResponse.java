package com.youtube.sorcjc.billetero.io.response;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse {

    @SerializedName("error_message")
    private String errorMessage;
    private boolean success;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}