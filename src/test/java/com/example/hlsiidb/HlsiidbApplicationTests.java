package com.example.hlsiidb;

import com.example.hlsiidb.service.impl.ChannelServiceImpl;

import org.epics.ca.Context;
import org.jdom2.JDOMException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static com.example.hlsiidb.util.Config.DEFAULT_PATH;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HlsiidbApplicationTests {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ChannelServiceImpl channelService;



    @Test
    public void contextLoads() throws IOException, ParseException {

    }

    @Test
    public void testTimeUtil() {
//        System.out.println(userMapper.getStatistics(150,"2018-10-20 05:10:51","2018-10-21 05:10:51"));
/*        File file = new File(DEFAULT_PATH + "/records");
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            System.out.println(fileList[i].getName());
        }*/

    }


    @Test
    public void testPvList(){
        try(Context context = new Context()){
//            Channel<String> channel = context.createChannel("RNG:OPER:STAT",String.class);
//            Channel<Double> channel1= context.createChannel("RNG:OPER:STAT",Double.class);
//            channel.connect();
//            channel1.connect();
//            System.out.println(channel.get());
//            System.out.println(channel1.get());
        }
    }

    @Test
    public void example() throws IOException, JDOMException {

    }


}
