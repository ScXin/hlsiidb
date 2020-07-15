package com.example.hlsiidb.entity;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 查询到的有用记录数据
 * @author wenpq
 */
public class ChannelOut implements Serializable {
	private Long smpl_time;
    private Float float_val;
    private Integer num_val;
    private String str_val;

//	public ChannelOut(Timestamp smpl_time, Float float_val) {
//		super();d
//		this.smpl_time = smpl_time;
//		this.float_val = float_val;
//	}
    public ChannelOut(Timestamp smpl_time, Float float_val,Integer num_val, String str_val) {
        super();
        this.smpl_time = smpl_time.getTime();
        this.float_val = float_val;
        this.num_val = num_val;
        this.str_val = str_val;
    }
	public ChannelOut() {}

	public Long getSmpl_time() {
		return smpl_time;
	}
	public void setSmpl_time(Timestamp smpl_time) {
		this.smpl_time = smpl_time.getTime();
	}
	public Float getFloat_val() {
		return float_val;
	}
	public void setFloat_val(Float float_val) {
		this.float_val = float_val;
	}	
	public Integer getNum_val() {
		return num_val;
	}
	public void setNum_val(Integer num_val) {
		this.num_val = num_val;
	}
	public String getStr_val() {
		return str_val;
	}
	public void setStr_val(String str_val) {
		this.str_val = str_val;
	}
	
	/** return String of smpl_time ---> 2014-08-16 16:56:55 */
	public String StringSmpl_time() {
		String str = new Timestamp(smpl_time).toString();
		str = str.substring(0,19);
		return str;
	}
}
