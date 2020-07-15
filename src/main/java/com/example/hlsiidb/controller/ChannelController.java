package com.example.hlsiidb.controller;

import com.example.hlsiidb.dto.PvInfo;
import com.example.hlsiidb.dto.TimeValue;
import com.example.hlsiidb.entity.ChannelOut;
import com.example.hlsiidb.entity.IntegralCurr;
import com.example.hlsiidb.service.impl.BeamStatusServiceImpl;
import com.example.hlsiidb.service.impl.ChannelServiceImpl;
import com.example.hlsiidb.service.impl.IntegralCurrServiceImpl;
import com.example.hlsiidb.util.Config;
import com.example.hlsiidb.util.ReadConfig;
import io.swagger.annotations.ApiOperation;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin
public class ChannelController {
    @Autowired
    private ChannelServiceImpl channelService;
    @Autowired
    private IntegralCurrServiceImpl integralCurrService;
    @Autowired
    private BeamStatusServiceImpl beamStatusService;

    @ApiOperation(value = "获取运行状态的统计信息")
    @GetMapping("history/lastestStatusStatistics/{startTime}/{endTime}")
    public ChannelOut getLastestStatusStatistics(@PathVariable("startTime") String startTime ,
                                                 @PathVariable("endTime") String endTime) throws ParseException {
        return channelService.getLatestStatusStatistics(startTime,endTime);
    }

    @ApiOperation(value = "根据id查询指定时间段的历史数据")
    @GetMapping("history/id/{id}/{startTime}/{endTime}")
    public List<ChannelOut> getHistoryById(@PathVariable("id") int channel_id,
                                           @PathVariable("startTime") String startTime ,
                                           @PathVariable("endTime") String endTime){
        return channelService.getPartChannelOutById(channel_id,startTime,endTime);
    }

    @ApiOperation(value = "获取多组PV的历史数据")
    @GetMapping("history/nameMap/{pvList}/{startTime}/{endTime}")
    public Map<String,List<ChannelOut>> getIntevalHistoryMap(@PathVariable("pvList") String pvList,
                                                             @PathVariable("startTime") String startTime,
                                                             @PathVariable("endTime") String endTime){
        if(pvList == null || pvList == ""){
            return null;
        }
        String[] nameList = pvList.split("@");
        return channelService.getIntevalHistoryMap(nameList,startTime,endTime);
    }

    @ApiOperation(value = "根据PV名获取指定时间段的历史数据")
    @GetMapping("history/name/{name}/{startTime}/{endTime}")
    public List<ChannelOut> getHistoryByName(@PathVariable("name") String name,
                                           @PathVariable("startTime") String startTime ,
                                           @PathVariable("endTime") String endTime) {
        if(name == null){return null;}
        int id = channelService.getSingleChannelId(name);
        if(id == -1){
            return null;
        }
        List<ChannelOut> channelOuts = channelService.getPartChannelOutById(id,startTime,endTime);
        if(channelOuts == null ||channelOuts.size() == 0){
            return null;
        }
        return channelOuts;
    }

    @ApiOperation(value = "statistics")
    @GetMapping("history/status/{startTime}/{endTime}")
    public List<ChannelOut> getStatusHistory(@PathVariable("startTime") String startTime ,
                                                         @PathVariable("endTime") String endTime) {
        return channelService.getStatusHistory(startTime,endTime);
    }

    @ApiOperation(value = "根据pvName查询channel_id")
    @GetMapping("channel/{name}")
    public int getChannelId(@PathVariable("name") String name){
        return channelService.getSingleChannelId(name);
    }

    @ApiOperation(value = "查询积分流强")
    @GetMapping("integral/{startDate}/{endDate}")
    public List<IntegralCurr> getIntegralCurr(@PathVariable(value = "startDate") String startDate,
                                              @PathVariable(value = "endDate") String endDate){
        return integralCurrService.getIntegralCurr(startDate,endDate);
    }

    @ApiOperation(value = "获取PV分组信息")
    @GetMapping("pvtree")
    public Map<String, Map<String, List<String>>> getPvTree(){
        Map<String, Map<String, List<String>>> map = null;
        try {
            map = ReadConfig.readChannelRelation(Config.DEFAULT_PATH + "/config/historyDataTree.xml");
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    @ApiOperation(value = "获取设备pv")
    @GetMapping("pvlist/{pvname}")
    public List<PvInfo> getPvList(@PathVariable(value = "pvname") String name) throws IOException, JDOMException {
        return ReadConfig.getEquipList(name);
    }

    @ApiOperation(value = "获得需要监视辐射的pv列表")
    @GetMapping(value = "dmlist")
    public List<String> getDmList() throws IOException, JDOMException {
        return ReadConfig.getDmList();
    }

    @ApiOperation(value = "获取束流流强数据")
    @GetMapping("beamcurrent/{startTime}/{endTime}")
    public List<ChannelOut> getHistoryById(@PathVariable("startTime") String startTime ,
                                           @PathVariable("endTime") String endTime){
        return channelService.getPartChannelOutById(149,startTime,endTime);
    }

    @ApiOperation(value = "检验pv和单位是否存在")
    @GetMapping("/checkPvExistence/{pvlist}")
    public Map<String,List<String>> getPvWithUnit(@PathVariable("pvlist") String pvListStr){
        String[] pvList = pvListStr.split("@");
        if (pvList == null || pvList.length == 0){return null;}
        for (int i = 0; i < pvList.length; i++) {
            if(!ReadConfig.checkPvExistence(pvList[i])){
                return null;
            }
        }
        return ReadConfig.getMorePvInfo(pvList);
    }

    @ApiOperation(value = "根据pv名查询单位unit")
    @RequestMapping(value="/pvunit/{pv}", method=RequestMethod.GET)
    public String getPvUnit(@PathVariable(value = "pv") String pv){
        return ReadConfig.getPvUnit(pv);
    }

    @ApiOperation(value = "提供BeamCurrent和LifeTime数据")
    @GetMapping("/beamcurrentandlife")
    public Map<String, List<TimeValue>> getBeamCurrent(){
        Map<String,List<TimeValue>> map = new HashMap<>();
        map.put("BeamCurrent",beamStatusService.getBeamCurrent());
        map.put("LifeTime",beamStatusService.getBeamLife());
        return map;
    }

    @ApiOperation(value = "查询历史数据的统计信息(均值，方差，平方均值，最大值，最小值)")
    @GetMapping("/history/statistics/{name}/{startTime}/{endTime}")
    public Map<String, Double> getStatistics(@PathVariable("name") String name,
                                            @PathVariable("startTime") String startTime,
                                            @PathVariable("endTime") String endTime){
        if(name == null){return null;}
        int id = channelService.getSingleChannelId(name);
        if(id == -1){
            return null;
        }
        return channelService.getStatistics(id,startTime,endTime);
    }

    @ApiOperation(value = "获得一组PV的历史数据的统计信息")
    @GetMapping("/history/statisticsMap/{pvList}/{startTime}/{endTime}")
    public Map<String, Map<String, Double>> getStatisticsMap(@PathVariable("pvList") String pvList,
                                                             @PathVariable("startTime") String startTime,
                                                             @PathVariable("endTime") String endTime) {
        if(pvList == null || pvList == ""){
            return null;
        }
        String[] nameList = pvList.split("@");
        return channelService.getStatisticsMap(nameList, startTime, endTime);
    }

    @ApiOperation(value = "分页获取历史数据")
    @GetMapping("/history/page/{pageNum}/{pageSize}/{name}/{startTime}/{endTime}")
    public List<ChannelOut> getPageContent(@PathVariable("name") String name,
                                           @PathVariable("pageNum") int pageNum,
                                           @PathVariable("pageSize") int pageSize,
                                            @PathVariable("startTime") String startTime,
                                            @PathVariable("endTime") String endTime){
        if(name == null){return null;}
        int id = channelService.getSingleChannelId(name);
        if(id == -1){
            return null;
        }
        return channelService.getPageContent(id,pageNum,pageSize,startTime,endTime);
    }

    @ApiOperation(value = "获取多个pv中的最大数据量")
    @GetMapping("/history/page/count/{nameList}/{startTime}/{endTime}")
    public long getMaxCount(@PathVariable("nameList")String nameList,
                            @PathVariable("startTime") String startTime,
                            @PathVariable("endTime") String endTime){
        return channelService.getMaxCount(nameList, startTime, endTime);
    }
}
