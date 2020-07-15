package com.example.hlsiidb.service.impl;

import com.example.hlsiidb.dao.ChannelDao;
import com.example.hlsiidb.dao.HistoryDao;
import com.example.hlsiidb.dto.Statistics;
import com.example.hlsiidb.entity.ChannelOut;
import com.example.hlsiidb.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.util.*;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    HistoryDao historyDao;

    @Autowired
    ChannelDao channelDao;

    /**
     * 获得所有的channel信息
     * @return Channel表中的所有Channel对象的List集合
     */

    @Override
    public List<ChannelOut> getPartChannelOutById(int channelId, String startTime, String endTime) {
        return historyDao.getIntervalHistory(channelId, startTime, endTime);
    }

    @Override
    public List<ChannelOut> getStatusHistory(String startTime, String endTime) {
        return historyDao.getAllHistoryNumNotNull(1522,startTime,endTime);
    }


    @Override
    public List<ChannelOut> getIntervalChannelOutById(int channelId, String startTime, String endTime) {
        return historyDao.getIntervalHistory(channelId, startTime, endTime);
    }

    @Override
    public int getSingleChannelId(String name) {
        return channelDao.getIdByName(name);
    }

    @Override
    public Map<String,Map<String,Double>> getStatisticsMap(String[] nameList, String startTime, String endTime){
        if(nameList == null || nameList.length== 0){
            return null;
        }
        Map<String,Map<String,Double>> map = new HashMap<>();
        for (int i = 0; i < nameList.length; i++){
            int channelId =channelDao.getIdByName(nameList[i]);
            if(channelId == -1){
                map.put(nameList[i],null);
                continue;
            }
            map.put(nameList[i],getStatistics(channelId, startTime, endTime));
        }
        return map;
    }

    @Override
    public Map<String,Double> getStatistics(int channelId, String startTime, String endTime){
        if(channelId == 150){
            Statistics statistics = historyDao.getLifetimeStatistics(startTime, endTime);
            Map<String, Double> map = new HashMap<>();
            map.put("Mean", new Double(statistics.getMean()));
            map.put("Deviation", new Double(statistics.getDeviation()));
            map.put("Max", new Double(statistics.getMax()));
            map.put("Min", new Double(statistics.getMin()));
            map.put("RMS", new Double(statistics.getRms()));
            return map;
        }
        Statistics statistics = historyDao.getStatistics(channelId, startTime, endTime);
        if (statistics == null || statistics.getMean() == null){
            statistics = historyDao.getNumStatistics(channelId, startTime, endTime);
        }
        if(statistics == null || statistics.getMean() == null){
            return null;
        }
        Map<String, Double> map = new HashMap<>();
        map.put("Mean", new Double(statistics.getMean()));
        map.put("Deviation", new Double(statistics.getDeviation()));
        map.put("Max", new Double(statistics.getMax()));
        map.put("Min", new Double(statistics.getMin()));
        map.put("RMS", new Double(statistics.getRms()));
        return map;
    }

    @Override
    public List<ChannelOut> getPageContent(int channelId, int pageNum, int pageSize, String startTime, String endTime){
        return historyDao.getPagableHistory(channelId,pageNum,pageSize,startTime,endTime);
    }

    @Override
    public Map<String,List<ChannelOut>> getIntevalHistoryMap(String[] nameList, String startTime, String endTime){
        if(nameList == null || nameList.length == 0){
            return null;
        }
        int n = nameList.length;
        Map<String,List<ChannelOut>> map = new HashMap<>();
        for (int i = 0; i < n; i++ ){
            int channelId = channelDao.getIdByName(nameList[i]);
            if (channelId == -1){
                map.put(nameList[i],null);
            }
            map.put(nameList[i], historyDao.getIntervalHistory(channelId,startTime,endTime));
        }
        return map;
    }

    @Override
    public ChannelOut getLatestStatusStatistics(String startTime, String endTime) throws ParseException {
        return historyDao.getLatestRowStatus(startTime);
    }

    @Override
    public ChannelOut getStatusPercent(String start_time, String end_time) throws ParseException {
//        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//        ChannelOut latestChanelOut = channelOutRepository.getLastestRowStatusHistory(start_time, end_time);
//        List<ChannelOut> channelOutList = channelOutRepository.getChannelHistory(1522, start_time, end_time);
//        Map<String, Double> map = new HashMap<>();
//        "init","ShutDown", "Injection", "BeamAvailable","Tuning";
//        List<Double> list = new ArrayList<>();
//        map.put("init",0.0);
//        map.put("ShutDown",0.0);
//        map.put("Injection",0.0);
//        map.put("BeamAvailable",0.0);
//        map.put("Tuning",0.0);
//        sdf.parse(start_time).getTime()
//        Timestamp timestamp1 = Timestamp.valueOf(start_time);
        return null;
    }

    @Override
    public long getCountOfSingleChannel(int channelId, String startTime, String endTime){
        return historyDao.getSingleCount(channelId, startTime, endTime);
    };

    @Override
    public long getSumOfCount(String nameList, String startTime, String endTime){
        if(nameList == null && nameList == ""){
            return 0;
        }
        long sum = 0;
        String[] pvArray = nameList.split("@");
        for (int i = 0; i < pvArray.length; i++) {
            int id = channelDao.getIdByName(pvArray[i]);
            if(id == -1){
                break;
            }
            sum +=  historyDao.getSingleCount(id,startTime,endTime);
        }
        return sum;
    }

    @Override
    public long getMaxCount(String nameList, String startTime, String endTime){
        if(nameList == null && nameList == ""){
            return 0;
        }
        long count = 0;
        String[] pvArray = nameList.split("@");
        for (int i = 0; i < pvArray.length; i++) {
            int id = channelDao.getIdByName(pvArray[i]);
            if(id == -1){
                count = Math.max(count,0);
            }
            count = Math.max(count,historyDao.getSingleCount(id,startTime,endTime));
        }
        return count;
    }
}
