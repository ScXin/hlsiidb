package com.example.hlsiidb.dto;

/**
 *  time+value对象 如：束流寿命 束流强度
 * @author wenpq
 */
public class TimeValue {
	private String timeStamp; // YYYY-MM-DD HH:MM:SS
	private Float value;
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	
	/**可以直接根据timeStamp, value产生对象 */
	public TimeValue(String timeStamp, Float value) {
		super();
		this.timeStamp = timeStamp;
		this.value = value;
	}
	
}
