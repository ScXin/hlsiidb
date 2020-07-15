package com.example.hlsiidb.entity;

import java.sql.Timestamp;

import javax.persistence.*;

/**
 * 与sample表映射的类
 * @author wenpq
 */
@Entity
@IdClass(value=SamplePK.class)
public class Sample {
	int channel_id;
	Timestamp smpl_time;
	Integer nanosecs;
	Integer severity_id;
	Integer status_id;
	Integer num_val;
	Float float_val;
	String str_val;
	
	@Id
	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	
	@Id
//    @Temporal(TemporalType.TIMESTAMP)
	public Timestamp getSmpl_time() {
		return smpl_time;
	}
	public void setSmpl_time(Timestamp smpl_time) {
		this.smpl_time = smpl_time;
	}
	
	public Integer getNanosecs() {
		return nanosecs;
	}
	public void setNanosecs(Integer nanosecs) {
		this.nanosecs = nanosecs;
	}
	public Integer getSeverity_id() {
		return severity_id;
	}
	public void setSeverity_id(Integer severity_id) {
		this.severity_id = severity_id;
	}
	public Integer getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Integer status_id) {
		this.status_id = status_id;
	}
	public Integer getNum_val() {
		return num_val;
	}
	public void setNum_val(Integer num_val) {
		this.num_val = num_val;
	}
	public Float getFloat_val() {
		return float_val;
	}
	public void setFloat_val(Float float_val) {
		this.float_val = float_val;
	}
	public String getStr_val() {
		return str_val;
	}
	public void setStr_val(String str_val) {
		this.str_val = str_val;
	}

}
