package com.example.hlsiidb.dao;

import com.example.hlsiidb.dto.TimeValue;
import com.example.hlsiidb.util.ReadTxtUtil;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 读取python程序产生的文件数据的dao
 * 
 * @author wenpq
 */
@Component("beamStatusPython")
public class BeamStatusPython {
	/**
	 * 读取python程序产生的文件数据
	 */
	public List<TimeValue> getBeamStatus(String dataDir) {
		List<TimeValue> list = new ArrayList<TimeValue>();
		String[] data = ReadTxtUtil.readArray(dataDir);

		try {
			for (int i = 0; i < data.length; i++) {
				// 得到采集时间
				String time = data[i].substring(2, data[i].length() - 1).split(
						" ")[1];
				long time1 = Long.parseLong(time.split("\\.")[0]);
				String timeStamp = String.format("%tF %<tT", time1 * 1000); // 把时间转化为格式为YYYY-MM-DD HH:MM:SS的形式
				// 得到采集数据
				String value;
				if(i == 0){
					value = data[i].substring(1, data[i].length() - 1)
							.split(" ")[0];
				}else {
					value = data[i].substring(2, data[i].length() - 1)
							.split(" ")[0];
				}
				TimeValue tv = new TimeValue(timeStamp, Float.parseFloat(value));
				list.add(tv);
			}
		} catch (Exception e) {
			System.out.println("Warning: online current data is None");
		}

		return list;
	}

	public Map<String,String> getOtherStatus(String path){
		return ReadTxtUtil.getOtherStatus(path);
	}

}
