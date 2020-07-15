package com.example.hlsiidb.service;

import java.io.IOException;
import java.util.List;

public interface DownloadService {
    public String writeToCsv(List<String> pvList, String startTime, String endTime, String name) throws IOException ;
}
