package com.example.hlsiidb.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
/**
 * 查询到的积分流强数据
 * @author xie
 *
 */
@Entity
public class IntegralCurr {
    @Id
	private long curr_date;
    @Column
	private Float val;

	public long getCurr_date() {
		return curr_date;
	}

	public void setCurr_date(Timestamp curr_date) {
		this.curr_date =  curr_date.getTime();
	}

	public Float getVal() {
		return val;
	}

	public void setVal(Float val) {
		this.val = val;
	}
}
