package com.example.hlsiidb.service.impl;

import com.example.hlsiidb.dao.BeamStatusPython;
import com.example.hlsiidb.dto.TimeValue;
import com.example.hlsiidb.util.Config;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 束流流强及状态的业务逻辑类
 * @author wenpq
 */
@Component("beamStatusService")
public class BeamStatusServiceImpl {
	
	/** 读取python文件的dao对象 */
	private BeamStatusPython beamStatusPython;
	public BeamStatusPython getBeamStatusPython() {
		return beamStatusPython;
	}
	@Resource
	public void setBeamStatusPython(BeamStatusPython beamStatusPython) {
		this.beamStatusPython = beamStatusPython;
	}

	/**
	 * 调用dao方法，从 python 程序产生的文本文件中读取一天的流强数据
	 * @return 封装TimeValue的list对象
	 */
	public List<TimeValue> getBeamCurrent(){
		String currentDir=Config.DEFAULT_PATH + "/python/sample1.txt";
		return beamStatusPython.getBeamStatus(currentDir);
	}
	
	
	/**
	 * 调用dao方法，从 python 程序产生的文本文件中读取一天的状态数据
	 * @return 封装TimeValue的list对象
	 */
	public List<TimeValue> getBeamLife(){
		String lifetimeDir= Config.DEFAULT_PATH + "/python/sample2.txt";
		List<TimeValue> list = beamStatusPython.getBeamStatus(lifetimeDir);
		return list;
	}

	public Map<String,String> getProperties(){
		String propertiesDir = Config.DEFAULT_PATH + "/python/parameters.properties";
		return beamStatusPython.getOtherStatus(propertiesDir);
	}
	
	
}
