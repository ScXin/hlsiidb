package com.example.hlsiidb.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Statistics {
    @Id
    private String mean;
    @Column
    private String deviation;
    @Column
    private String rms;
    @Column
    private String max;
    @Column
    private String min;

//    public Statistics(float mean, float deviation, float rms, float max, float min) {
//        this.mean = mean;
//        this.deviation = deviation;
//        this.rms = rms;
//        this.max = max;
//        this.min = min;
//    }



    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getDeviation() {
        return deviation;
    }

    public void setDeviation(String deviation) {
        this.deviation = deviation;
    }

    public String getRms() {
        return rms;
    }

    public void setRms(String rms) {
        this.rms = rms;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }
}

