package com.example.hlsiidb.entity;

import com.commonuser.entity.User;
import com.example.hlsiidb.commdef.OperationType;


import java.io.Serializable;
import java.util.Date;

/**
 * @author ScXin
 * @date 7/7/2020 2:44 PM
 */


public class OperationLog implements Serializable {
    private int id;
    private String remoteIP;
    private OperationType operType;
    private Date operTime;
    private User user;

    public OperationLog() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(String remoteIP) {
        this.remoteIP = remoteIP;
    }

    public OperationType getOperType() {
        return operType;
    }

    public void setOperType(OperationType operType) {
        this.operType = operType;
    }


    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
