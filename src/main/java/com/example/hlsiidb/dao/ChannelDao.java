package com.example.hlsiidb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 与Channel表进行交互的类
 * @author xie
 */
@Component
public class ChannelDao {
    @Resource(name = "jdbcTemplateOne")
    JdbcTemplate jdbcTemplate;

    /**
     * 根据pv名查询channel_id
     * @param name
     * @return
     */
    public int getIdByName(String name){
        String sql = "SELECT CHANNEL_ID FROM CSS.CHANNEL WHERE NAME = '" + name + "'";
        int id = -1;
        try {
            id = jdbcTemplate.queryForObject(sql, Integer.class);
        }catch (Exception e){
        }
        return id;
    }

}
