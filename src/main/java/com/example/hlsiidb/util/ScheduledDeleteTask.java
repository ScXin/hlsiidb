package com.example.hlsiidb.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import static com.example.hlsiidb.util.Config.DEFAULT_PATH;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 设计定时任务，定时删除生成的历史纪录文件
 * @author xie
 */
@Component
public class ScheduledDeleteTask {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Scheduled注解，定时删除fixrate时间之前生成的下载文件
     */
    @Scheduled(fixedRate = 41200000)
    public void deleteRecordFile() {
        Date dateNow = new Date();
        long limitTime = dateNow.getTime() - 3600000;
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        File file = new File(DEFAULT_PATH + "/records");
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if(fileList[i].lastModified() < limitTime){
                if(fileList[i].delete()){
                }else{
                    logger.error("删除文件失败");
                }
            }
        }
    }

    //由于WebSocket 与 @Scheduled 注解不兼容，需要自己定义ThreadPoolTaskScheduler类的Bean
    @Bean
    public ThreadPoolTaskScheduler taskScheduler(){
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        return taskScheduler;
    }
}
