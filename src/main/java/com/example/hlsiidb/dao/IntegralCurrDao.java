package com.example.hlsiidb.dao;


import com.example.hlsiidb.entity.IntegralCurr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 与IntegralCurr表交互的类
 * @author xie
 */
@Component
public class IntegralCurrDao {
    @Resource(name = "jdbcTemplateOne")
    JdbcTemplate jdbcTemplate;


    /**
     * 查询积分流强数据
     * @param startTime
     * @param endTime
     * @return
     */
    public List<IntegralCurr> getIntegralHistory(String startTime, String endTime){
        String sql = "SELECT * FROM CSS.INTEGRAL_CURR where CURR_DATE >= TO_TIMESTAMP('" + startTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CURR_DATE <= TO_TIMESTAMP('" + endTime + "','yyyy-mm-dd hh24:mi:ss') ORDER BY CURR_DATE";
        List<IntegralCurr> integralCurrList = jdbcTemplate.query(sql,new IntegralCurrRowMapper());
        return  integralCurrList;
    }

    class IntegralCurrRowMapper implements RowMapper<IntegralCurr>{
        @Override
        public IntegralCurr mapRow(ResultSet rs, int rowNum) throws SQLException {
            IntegralCurr integralCurr = new IntegralCurr();
            integralCurr.setCurr_date(rs.getTimestamp("CURR_DATE"));
            integralCurr.setVal(rs.getFloat("VAL"));
            return integralCurr;
        }
    }
}
