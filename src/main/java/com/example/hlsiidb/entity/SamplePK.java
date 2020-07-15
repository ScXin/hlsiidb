package com.example.hlsiidb.entity;

import java.sql.Timestamp;

/**
 * 与channel表主键类
 * @author wenpq
 */
public class SamplePK  implements java.io.Serializable{
	int channel_id;
	Timestamp smpl_time;
	
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public Timestamp getSmpl_time() { 
		return smpl_time;
	}
	public void setSmpl_time(Timestamp smpl_time) {
		this.smpl_time = smpl_time;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof SamplePK){// 确认是为SamplePK对象
			SamplePK pk = (SamplePK) o;
			if(channel_id == pk.getChannel_id() && smpl_time == pk.getSmpl_time()){
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.smpl_time.hashCode();
	}
		
}
