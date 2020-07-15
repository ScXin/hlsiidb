package com.example.hlsiidb.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.hlsiidb.util.Config;
import org.epics.ca.Channel;
import org.epics.ca.Context;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class RadiationWebSocketController extends TextWebSocketHandler {
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception,IOException, JDOMException {
        Map<String, String> payload = new HashMap<>();
        try(Context context = new Context()){
            File file = new File(Config.DEFAULT_PATH + "/config/Radiation.xml");
            List<String> channelNames = new ArrayList<>();
            List<String> channelPvs = new ArrayList<>();
            List<String> channelStatusPvs = new ArrayList<>();
            List<String> channelStatusNames = new ArrayList<>();
            if(file != null) {
                SAXBuilder builder = new SAXBuilder();
                Document doc = builder.build(file);
                Element groups = doc.getRootElement();
                List<Element> topList1 = groups.getChildren("group1");
                List<Element> topList2 = groups.getChildren("group2");
                List<Element> subList1 = topList1.get(0).getChildren("channel");
                List<Element> subList2 = topList2.get(0).getChildren("channel");

                for (int i = 0; i < subList1.size(); i++) {
//                    System.out.println(topList.get(i).getAttributeValue("name"));
                    channelPvs.add(subList1.get(i).getAttributeValue("name"));
                    channelNames.add(subList1.get(i).getValue());
                    channelPvs.add(subList2.get(i).getAttributeValue("name"));
                    channelNames.add(subList2.get(i).getValue());
                    channelPvs.add(subList1.get(i).getAttributeValue("name")+".STAT");
                    channelNames.add(subList1.get(i).getValue()+"-S");
                    channelPvs.add(subList2.get(i).getAttributeValue("name")+".STAT");
                    channelNames.add(subList2.get(i).getValue()+"-S");
                }
                for (int i = 0; i < subList1.size(); i++) {
//                    System.out.println(topList.get(i).getAttributeValue("name"));
                    channelStatusPvs.add(subList1.get(i).getAttributeValue("name")+".STAT");
                    channelStatusNames.add(subList1.get(i).getValue()+"-S");
                    channelStatusPvs.add(subList2.get(i).getAttributeValue("name")+".STAT");
                    channelStatusNames.add(subList2.get(i).getValue()+"-S");
                }
                channelPvs.add("RNG:BEAM:CURR");
                channelNames.add("Current");
                channelPvs.add("RNG:BEAM:LIFE");
                channelNames.add("Lifetime");
                channelPvs.add("RNG:ENG");
                channelNames.add("Energy");
                channelStatusPvs.add("RNG:OPER:MODE");
                channelStatusNames.add("OperationMode");
                channelStatusPvs.add("RNG:OPER:STAT");
                channelStatusNames.add("OperationStatus");
            }
            List<Channel<Double>> channelList = new ArrayList<Channel<Double>>();
            for (int i = 0; i < channelNames.size(); i++) {
                channelList.add(context.createChannel(channelPvs.get(i),Double.class));
                channelList.get(i).connect();
            }
            List<Channel<String>> channelStatusList = new ArrayList<Channel<String>>();
            for (int i = 0; i < channelStatusNames.size(); i++) {
                channelStatusList.add(context.createChannel(channelStatusPvs.get(i),String.class));
                channelStatusList.get(i).connect();
            }
            int time = 1000;
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

