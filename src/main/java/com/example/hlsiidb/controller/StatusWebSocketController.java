package com.example.hlsiidb.controller;

import com.alibaba.fastjson.JSONObject;
import org.epics.ca.Channel;
import org.epics.ca.Context;
import org.jdom2.JDOMException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

public class StatusWebSocketController extends TextWebSocketHandler {



    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception,IOException, JDOMException {
        Map<String, String> payload = new HashMap<>();
        List<String> channelNames = new ArrayList<>();
        List<String> channelPvs = new ArrayList<>();
        List<String> channelStatusPvs = new ArrayList<>();
        List<String> channelStatusNames = new ArrayList<>();
        try(Context context = new Context()){
            channelPvs.add("RNG:BEAM:CURR");    //自定义添加PV
            channelNames.add("Current");
            channelPvs.add("RNG:BEAM:LIFE");
            channelNames.add("Lifetime");
            channelPvs.add("RNG:ENG");
            channelNames.add("Energy");
            channelStatusPvs.add("RNG:OPER:MODE");
            channelStatusNames.add("OperationMode");
            channelStatusPvs.add("RNG:OPER:STAT");
            channelStatusNames.add("OperationStatus");
            List<Channel<Double>> channelList = new ArrayList<Channel<Double>>(); //根据配置文件添加PV
            for (int i = 0; i < channelNames.size(); i++) {
                channelList.add(context.createChannel(channelPvs.get(i),Double.class));
                channelList.get(i).connect();
            }
            List<Channel<String>> channelStatusList = new ArrayList<Channel<String>>();
            for (int i = 0; i < channelStatusNames.size(); i++) {
                channelStatusList.add(context.createChannel(channelStatusPvs.get(i),String.class));
                channelStatusList.get(i).connect();
            }
            int time = 1000;    //设置发送数据的时间间隔
            while(true){
                try {
                    Thread.sleep(time);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                for (int i = 0; i < channelList.size(); i++) {
                    payload.put(channelNames.get(i),channelList.get(i).get().toString());
                }
                for (int i = 0; i < channelStatusList.size(); i++) {
                    payload.put(channelStatusNames.get(i),channelStatusList.get(i).get());
                }
                session.sendMessage(new TextMessage(JSONObject.toJSONString(payload)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

