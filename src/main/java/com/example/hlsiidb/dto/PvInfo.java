package com.example.hlsiidb.dto;

public class PvInfo {
    private String name;
    private String pv;

    public PvInfo(String name, String pv) {
        this.name = name;
        this.pv = pv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }
}
