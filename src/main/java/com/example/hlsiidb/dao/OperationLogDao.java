package com.example.hlsiidb.dao;

//import com.commonuser.entity.Role;

import com.commonuser.entity.User;
//import com.example.hlsiidb.commdef.OperationType;
import com.example.hlsiidb.commdef.OperationType;
import com.example.hlsiidb.entity.OperationLog;
//import jdk.nashorn.internal.scripts.JD;
//import org.springframework.beans.factory.annotation.Autowired;
import com.example.hlsiidb.vo.OperationLogVo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//import java.util.Optional;

/**
 * @author ScXin
 * @date 7/7/2020 2:50 PM
 */
@Component
public class OperationLogDao {

    @Resource(name = "jdbcTemplateTwo")
    JdbcTemplate jdbcTemplate;

    public void save(OperationLog operationLog) {
        int userId = operationLog.getUser().getId();
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String operTime = sp.format(new Date());
        String remoteIP = operationLog.getRemoteIP();
        String operType = operationLog.getOperType().getOperation();


        String sql = "insert into operation_log(user_id,remote_ip,op_type,op_time)" +
                "values('" + userId + "','" + remoteIP + "','" + operType + "','" + operTime + "')";
        try {
            jdbcTemplate.update(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //The SQL has been verified
    public List<OperationLog> getLogByCondition(String queryCondition, String userParam) {

        if (!userParam.equals("")) {
            String userIdSQL = "select id from user where " + userParam;
            int userId = jdbcTemplate.queryForObject(userIdSQL, Integer.class);
            if (!queryCondition.equals("")) {
                queryCondition += "and user_id='" + userId + "'";
            } else {
                queryCondition += "user_id='" + userId + "'";
            }
        }

        List<OperationLog> operationLogs = null;
        String sql = "";
        if (queryCondition != "") {
            String sq = "select * from operation_log where ";
            String sq2 = "as op left join user as u on op.user_id=u.id";

            queryCondition = " " + queryCondition;
            sql = "select op.id,op.op_time,op.op_type,op.remote_ip,u.user_name" +
                    " from (" + sq + queryCondition + ")" + " " + sq2;
        } else {
            sql = "select op.id,op.op_time,op.op_type,op.remote_ip,u.user_name" +
                    " from operation_log as op left join user as u on op.user_id=u.id";

        }
        try {
            operationLogs = jdbcTemplate.query(sql, new OperationLogMapper());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return operationLogs;
    }


    public List<OperationLog> getLogByPage(String queryCondition, String userParam, int pageNum, int pageSize) {

        if (!userParam.equals("")) {
            String userIdSQL = "select id from user where " + userParam;
            int userId = jdbcTemplate.queryForObject(userIdSQL, Integer.class);
            if (!queryCondition.equals("")) {
                queryCondition += "and user_id='" + userId + "'";
            } else {
                queryCondition += "user_id='" + userId + "'";
            }
        }
        List<OperationLog> operationLogs = null;
        List<OperationLog> operationLogs1 = null;
        String sql = "";
//        String sql3 = "";
        if (queryCondition != "") {
            String sq = "select * from operation_log where ";
            String sq2 = "as op left join user as u on op.user_id=u.id";
            queryCondition = " " + queryCondition;
            sql = "select op.id,op.op_time,op.op_type,op.remote_ip,u.user_name" +
                    " from (" + sq + queryCondition + ")" + " " + sq2 + " limit ?,?";
//            sql3 = "select op.id,op.op_time,op.op_type,op.remote_ip,u.user_name" +
//                    " from (" + sq + queryCondition + ")" + " " + sq2;
        } else {
            sql = "select op.id,op.op_time,op.op_type,op.remote_ip,u.user_name " +
                    "from operation_log as op left join user as u on op.user_id=u.id limit ?,?";
//
//            sql3 = "select op.id,op.op_time,op.op_type,op.remote_ip,u.user_name " +
//                    "from operation_log as op left join user as u on op.user_id=u.id limit ?,?";
        }
        try {
            operationLogs = jdbcTemplate.query(sql, new OperationLogMapper(), (pageNum - 1) * pageSize, pageSize);
//            operationLogs1 = jdbcTemplate.query(sql3, new OperationLogMapper());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return operationLogs;
    }


    public int getCount() {
        String sql = "select count(*) from operation_log";
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject(sql, Integer.class);
            return count;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return count;
    }


    public int calculateCount(String queryCondition, String userParam) {
        if (!userParam.equals("")) {
            String userIdSQL = "select id from user where " + userParam;
            int userId = jdbcTemplate.queryForObject(userIdSQL, Integer.class);
            if (!queryCondition.equals("")) {
                queryCondition += "and user_id='" + userId + "'";
            } else {
                queryCondition += "user_id='" + userId + "'";
            }
        }
        if(!queryCondition.equals("")){
            String sql = "select count(*) from operation_log where "+queryCondition;
            int count = 0;
            try {
                count = jdbcTemplate.queryForObject(sql, Integer.class);
                return count;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return count;
        }else{
            String sql = "select count(*) from operation_log";
            int count = 0;
            try {
                count = jdbcTemplate.queryForObject(sql, Integer.class);
                return count;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return count;
        }
    }

    class OperationLogMapper implements RowMapper<OperationLog> {
        @Override
        public OperationLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            OperationLog operationLog = new OperationLog();
            operationLog.setId(rs.getInt("id"));
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                operationLog.setOperTime(sp.parse(rs.getString("op_time")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            operationLog.setOperType(OperationType.stateOf(rs.getString("op_type")));
            operationLog.setRemoteIP(rs.getString("remote_ip"));
            User user = new User();
            user.setUsername(rs.getString("user_name"));
            operationLog.setUser(user);
            return operationLog;
        }
    }
}
