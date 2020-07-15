package com.example.hlsiidb.service;

import com.example.hlsiidb.entity.ChannelOut;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ChannelService {

    List<ChannelOut> getPartChannelOutById(int channel_id, String start_time, String end_time);

    List<ChannelOut> getStatusHistory(String start_time, String end_time);

    List<ChannelOut> getIntervalChannelOutById(int channel_id, String start_time, String end_time);

    int getSingleChannelId(String name);

    Map<String,Map<String,Double>> getStatisticsMap(String[] nameList, String start_time, String end_time);

    Map<String,Double> getStatistics(int channel_id, String start_time, String end_time);

    List<ChannelOut> getPageContent(int channel_id, int pageNum, int pageSize, String start_time, String end_time);

    Map<String,List<ChannelOut>> getIntevalHistoryMap(String[] nameList, String startTime, String endTime);

    ChannelOut getLatestStatusStatistics(String start_time, String end_time) throws ParseException;

    ChannelOut getStatusPercent(String start_time, String end_time) throws ParseException;

    long getSumOfCount(String nameList, String startTime, String endTime);

    long getMaxCount(String nameList, String startTime, String endTime);

    long getCountOfSingleChannel(int channelId, String startTime, String endTime);
}
