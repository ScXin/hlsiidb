package com.example.hlsiidb.service;

import com.example.hlsiidb.dto.TimeValue;

import java.util.List;
import java.util.Map;

public interface BeamStatusService {
    public List<TimeValue> getBeamCurrent();
    public List<TimeValue> getBeamLife();
    public Map<String,String> getProperties();
}
