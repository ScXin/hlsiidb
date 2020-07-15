package com.example.hlsiidb.vo;

import java.io.Serializable;

/**
 * @author ScXin
 * @date 7/7/2020 8:58 AM
 */
public class ReturnCode implements Serializable {

    private boolean success;

    private String message;

    public ReturnCode(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}