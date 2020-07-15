package com.example.hlsiidb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 与channel表映射的类
 * @author xzyuan
 */
@Entity
@ApiModel(value = "channel")
@Table(name = "CHANNEL",schema="css")
public class Channel implements Serializable {

	@Id
	@GeneratedValue
    private int channel_id;
	@Column(nullable = false)
    private String name;
	@Column
    private String descr;
	@Column
    private Integer grp_id;
	@Column
    private Integer smpl_mode_id;
	@Column
    private Float smpl_val;
	@Column
    private Float smpl_per;
	@Column
    private Integer retent_id;
	@Column
    private Float retent_val;

	protected Channel(){}

	public int getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(int channel_id) {
		this.channel_id = channel_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	public Integer getGrp_id() {
		return grp_id;
	}
	public void setGrp_id(Integer grp_id) {
		this.grp_id = grp_id;
	}
	public Integer getSmpl_mode_id() {
		return smpl_mode_id;
	}
	public void setSmpl_mode_id(Integer smpl_mode_id) {
		this.smpl_mode_id = smpl_mode_id;
	}
	public Float getSmpl_val() {
		return smpl_val;
	}
	public void setSmpl_val(Float smpl_val) {
		this.smpl_val = smpl_val;
	}
	public Float getSmpl_per() {
		return smpl_per;
	}
	public void setSmpl_per(Float smpl_per) {
		this.smpl_per = smpl_per;
	}
	public Integer getRetent_id() {
		return retent_id;
	}
	public void setRetent_id(Integer retent_id) {
		this.retent_id = retent_id;
	}
	public Float getRetent_val() {
		return retent_val;
	}
	public void setRetent_val(Float retent_val) {
		this.retent_val = retent_val;
	}
}
