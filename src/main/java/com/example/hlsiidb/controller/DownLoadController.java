package com.example.hlsiidb.controller;


import com.example.hlsiidb.service.impl.ChannelServiceImpl;
import com.example.hlsiidb.service.impl.DownloadServiceImpl;
import com.example.hlsiidb.util.Config;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/download")
@RestController
@CrossOrigin
public class DownLoadController {
    @Autowired
    private ChannelServiceImpl channelService;

    @Autowired
    private DownloadServiceImpl downloadService;

    @ApiOperation(value = "按照pvlist进行历史数据下载")
    @RequestMapping(value = "/{startTime}/{endTime}/{name}/{pvList}", method = RequestMethod.GET)
    public String download(@PathVariable("startTime") String startTime,
                           @PathVariable("endTime") String endTime,
                           @PathVariable("name") String name,
                           @PathVariable("pvList") String pvListString) throws IOException {
        if(pvListString == null && pvListString == ""){
            return null;
        }
        List<String> pvList = new ArrayList<>();
        String[] pvArray = pvListString.split("@");
        for (int i = 0; i < pvArray.length; i++) {
            pvList.add(pvArray[i]);
        }
        String fileName = downloadService.writeToCsv(pvList,startTime,endTime,name);
        return fileName;
    }

    @ApiOperation(value = "查询要下载的行数")
    @GetMapping(value = "/{pvList}/{startTime}/{endTime}")
    public long getSumOfCount(@PathVariable("startTime") String startTime,
                              @PathVariable("endTime") String endTime,
                              @PathVariable("pvList") String pvList){
        return channelService.getSumOfCount(pvList, startTime, endTime);
    }

    @ApiOperation(value = "根据文件名进行下载")
    @GetMapping(value = "/media/{name}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable(value = "name",required = true) String name)
            throws IOException {
        FileSystemResource file = new FileSystemResource(Config.DEFAULT_PATH + "/records/"+name);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", name);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }

    @ApiOperation(value = "查询下载进度")
    @RequestMapping(value = "/status/{name}", method = RequestMethod.GET)
    public List<Integer> getProgress(@PathVariable("name") String name){
        return downloadService.queryDownloadProcessStatus(name);
    }
}
