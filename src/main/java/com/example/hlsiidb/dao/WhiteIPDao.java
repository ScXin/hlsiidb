package com.example.hlsiidb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ScXin
 * @date 7/14/2020 12:16 PM
 */

@Component
public class WhiteIPDao {


    @Resource(name = "jdbcTemplateTwo")
    private JdbcTemplate jdbcTemplate;


    public List<String> egtAllWhiteIP() {
        String sql = "select addr from white_ip";
        List<String> whiteList = null;
        try {
            whiteList = jdbcTemplate.queryForList(sql, String.class);
            return whiteList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return whiteList;
    }
}
