package com.example.hlsiidb.service.impl;

import com.example.hlsiidb.dao.ChannelDao;
import com.example.hlsiidb.dao.HistoryDao;
import com.example.hlsiidb.dao.ProcessBar;
import com.example.hlsiidb.entity.ChannelOut;
import com.example.hlsiidb.service.DownloadService;
import com.example.hlsiidb.util.Config;
import com.example.hlsiidb.util.ExportUtil;
import com.example.hlsiidb.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DownloadServiceImpl implements DownloadService {

    @Autowired
    HistoryDao historyDao;

    @Autowired
    ChannelDao channelDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    //将Oracle中的数据写入文件中
    public String writeToCsv(List<String> pvList, String startTime, String endTime, String name) throws IOException {
        String filename = Config.DEFAULT_PATH + "/records/"+ name + ".txt";
        File file = new File(filename);
        if(!file.exists()) {
            file.createNewFile();
        }
        List<String> timeList;
        try {
            timeList = TimeUtil.splitTime(startTime,endTime);
        } catch (ParseException e) {
            logger.error("历史数据下载：日期格式转化出现异常");
            return null;
        }
        if (timeList.size() < 2){
            return null;
        }
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(0);
        ProcessBar.addProcess(name,list1);
        List<ChannelOut> list;
//        System.out.println(timeList);
        for (int i = 0; i < pvList.size(); i++) {   //对每个PV分别写入
            String pvName = pvList.get(i);
            int channelId = channelDao.getIdByName(pvName);
            ExportUtil.exportHeader(file, pvName, startTime, endTime);
            if (historyDao.getSingleCount(channelId, startTime,endTime) < 20000 ){
                list = historyDao.getAllHistory(channelId,startTime,endTime);
                ExportUtil.exportPv(file, pvName, startTime, endTime, list);
                list1.set(0, i + 1);
            }else {
                for (int j = 0; j <= timeList.size() - 2; j++) {     //
                    list = historyDao.getAllHistory(channelId, timeList.get(j), timeList.get(j + 1));
                    ExportUtil.exportPv(file, pvName, timeList.get(j), timeList.get(j + 1), list);
                    list1.set(0, i + 1);
                    list1.set(1, (int) 100 * j / timeList.size());
                }
            }
        }
        ProcessBar.deleteProcess(name);
        return name+".txt";
    }

    /**
     * 查询文件写入的进度
     * @param key
     * @return
     */
    public List<Integer> queryDownloadProcessStatus(String key){
        return ProcessBar.getProcessStatus(key);
    }
}
