package com.example.hlsiidb.controller;


import com.example.hlsiidb.dto.TimeValue;
import com.example.hlsiidb.service.impl.BeamStatusServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/beam")
@CrossOrigin
public class BeamStatusController {
    @Autowired
    private BeamStatusServiceImpl beamStatusService;

    @GetMapping("/status")
    @ApiOperation(value = "获取最近24小时的束流流强和寿命数据")
    public Map<String,Map<String,List<TimeValue>>> getBeamCurrent(){
        Map<String,Map<String,List<TimeValue>>> map = new HashMap<>();
        Map<String,List<TimeValue>> beamMap = new HashMap<>();
        beamMap.put("BeamCurrent",beamStatusService.getBeamCurrent());
        beamMap.put("LifeTime",beamStatusService.getBeamLife());
        map.put("Beam",beamMap);
        Map<String,String> statusMap = beamStatusService.getProperties();
        for (Map.Entry<String, String> entry : statusMap.entrySet()){
            Map<String,List<TimeValue>> map1 = new HashMap<>();
            map1.put(entry.getValue(),null);
            map.put(entry.getKey(),map1);
        }
        return map;
    }
}
