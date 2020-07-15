package com.example.hlsiidb.dao;

import com.example.hlsiidb.dto.Statistics;
import com.example.hlsiidb.entity.ChannelOut;
import com.example.hlsiidb.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 与Sample表交互的类
 * @author xie
 */

@Component
public class HistoryDao {
    @Resource(name = "jdbcTemplateOne")
    JdbcTemplate jdbcTemplate;


    /**
     * 查询规定时间段内PV的的数据量
     * @return
     */
    public long getSingleCount(int channelId, String startTime, String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select count(*) from " +tableName +
                " where SMPL_TIME  between to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=" + channelId;
        return jdbcTemplate.queryForObject(sql,Long.class);
    }

    /**
     * 间隔取历史数据，超过4000采用间隔取点
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ChannelOut> getIntervalHistory(int channelId, String startTime, String endTime){
        int y = Integer.parseInt(startTime.substring(0, 4));
        Calendar now = Calendar.getInstance();
        int ny = now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        long n = getSingleCount(channelId,startTime,endTime);
        if(channelId == 150){      //束流寿命数据作特殊处理
            if(n >= 4000){
                int modNum = (int) (n/Config.DATA_NUM);
                String sql = "select * from (select SMPL_TIME,NUM_VAL,FLOAT_VAL, STR_VAL " +
                        "from (select rownum rn,SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from " + tableName +
                        " where SMPL_TIME  > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                        "and SMPL_TIME < to_timestamp('" + endTime +"','yyyy-mm-dd hh24:mi:ss') " +
                        "and CHANNEL_ID=150) where mod(rn," + modNum + ")=0) where FLOAT_VAL IS NOT NULL order by SMPL_TIME";
                return jdbcTemplate.query(sql,new ChannelOutRowMapper());
            }
            String sql = "select SMPL_TIME,NUM_VAL,FLOAT_VAL, STR_VAL " +
                    "from (select SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from " + tableName +
                    " where SMPL_TIME > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                    "and SMPL_TIME < to_timestamp('" + endTime +"','yyyy-mm-dd hh24:mi:ss') " +
                    "and CHANNEL_ID=150) where FLOAT_VAL IS NOT NULL order by SMPL_TIME";
            return jdbcTemplate.query(sql,new ChannelOutRowMapper());
        }
        if(n >= 4000){
            int modNum = (int) (n/Config.DATA_NUM);
            String sql = "select * from (select SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL " +
                    "from (select rownum rn,SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from " + tableName +
                    " where SMPL_TIME > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                    "and SMPL_TIME < to_timestamp('" + endTime +"','yyyy-mm-dd hh24:mi:ss') " +
                    "and CHANNEL_ID="+ channelId +") where mod(rn," + modNum + ")=0) order by SMPL_TIME";
            return jdbcTemplate.query(sql,new ChannelOutRowMapper());
        }
        String sql = "select SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from " + tableName +
                " where SMPL_TIME > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                "and SMPL_TIME < to_timestamp('" + endTime +"','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID="+ channelId +" order by SMPL_TIME";
        return jdbcTemplate.query(sql,new ChannelOutRowMapper());
    }

    /**
     * 获取单页数据
     * @param channelId
     * @param pageNum
     * @param pageSize
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ChannelOut> getPagableHistory(int channelId, int pageNum, int pageSize, String startTime, String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from " +
                "(select ROWNUM rn, SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from (select * from " + tableName +
                " where SMPL_TIME > to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=" + channelId+ " order by SMPL_TIME) " +
                "where ROWNUM <=" + pageNum * pageSize + ") where rn <= " + pageNum * pageSize + " and rn > " + (pageNum - 1 ) * pageSize + " order by SMPL_TIME";
        if (channelId == 150){
            sql = "select SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from " +
                    "(select ROWNUM rn, SMPL_TIME,NUM_VAL,FLOAT_VAL,STR_VAL from (select * from " + tableName +
                    " where SMPL_TIME > to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') " +
                    "and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                    "and CHANNEL_ID=" + channelId+ " order by SMPL_TIME) " +
                    "where ROWNUM <=" + pageNum * pageSize + ") where rn <= " + pageNum * pageSize + " and rn > " +
                    (pageNum - 1 ) * pageSize + " and FLOAT_VAL is not null order by SMPL_TIME";

        }
        return jdbcTemplate.query(sql,new ChannelOutRowMapper());
    }

    /**
     * 获取某个时间段内某个PV的所有数据
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public List<ChannelOut> getAllHistory(int channelId, String startTime, String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "SELECT SMPL_TIME,FLOAT_VAL,NUM_VAL,STR_VAL FROM " + tableName +
                " where SMPL_TIME BETWEEN TO_TIMESTAMP('" +  startTime + "','yyyy-mm-dd hh24:mi:ss') AND TO_TIMESTAMP('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "AND CHANNEL_ID = " + channelId + " ORDER BY SMPL_TIME";
        return jdbcTemplate.query(sql,new ChannelOutRowMapper());
    }

    public List<ChannelOut> getAllHistoryNumNotNull(int channelId, String startTime,String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select * from (SELECT SMPL_TIME,FLOAT_VAL,NUM_VAL,STR_VAL FROM " + tableName +
                " where SMPL_TIME BETWEEN TO_TIMESTAMP('" +  startTime + "','yyyy-mm-dd hh24:mi:ss') AND TO_TIMESTAMP('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "AND CHANNEL_ID = " + channelId + " ) where NUM_VAL IS NOT NULL ORDER BY SMPL_TIME";
        return jdbcTemplate.query(sql,new ChannelOutRowMapper());
    }

    /**
     * 获取某个PV某段时间内的统计数据
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public Statistics getStatistics(int channelId, String startTime, String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select * from (select TO_CHAR(AVG(FLOAT_VAL)) avg_col ,TO_CHAR(STDDEV(FLOAT_VAL)) stddev_col,TO_CHAR(MAX(FLOAT_VAL)) max_col,TO_CHAR(MIN(FLOAT_VAL)) min_col " +
                "from " + tableName +
                " where SMPL_TIME  > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=" + channelId + " ) cross join (select TO_CHAR(SQRT(sum/co)) rms_col from (select SUM(FLOAT_VAL*FLOAT_VAL) sum, count(*) co " +
                "from " + tableName +
                " where SMPL_TIME  > to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=" + channelId + "))";
        return jdbcTemplate.query(sql,new StatisticsRowMapper()).get(0);
    }

    /**
     * 获取束流寿命某段时间内的统计数据
     * @param startTime
     * @param endTime
     * @return
     */
    public Statistics getLifetimeStatistics(String startTime, String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select * from (select TO_CHAR(AVG(FLOAT_VAL)) avg_col ,TO_CHAR(STDDEV(FLOAT_VAL)) stddev_col,TO_CHAR(MAX(FLOAT_VAL)) max_col,TO_CHAR(MIN(FLOAT_VAL)) min_col " +
                "from (select SMPL_TIME,CASE WHEN FLOAT_VAL > 40 THEN NULL ELSE FLOAT_VAL END FLOAT_VAL from " + tableName +
                " where SMPL_TIME  > to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=150 )) cross join (select TO_CHAR(SQRT(sum/co)) rms_col from (select SUM(FLOAT_VAL*FLOAT_VAL) sum, count(*) co " +
                "from (select SMPL_TIME,CASE WHEN FLOAT_VAL > 40 THEN NULL ELSE FLOAT_VAL END FLOAT_VAL from CSS.SAMPLE " +
                "where SMPL_TIME  > to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=150)))";
        return jdbcTemplate.query(sql,new StatisticsRowMapper()).get(0);
    }


    /**
     * 获取整形数据的统计信息
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public Statistics getNumStatistics(int channelId, String startTime, String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select * from (select TO_CHAR(AVG(NUM_VAL)) avg_col,TO_CHAR(STDDEV(NUM_VAL)) stddev_col,TO_CHAR(MAX(NUM_VAL)) max_col,TO_CHAR(MIN(NUM_VAL)) min_col " +
                "from " + tableName +
                " where SMPL_TIME  > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=" + channelId + " ) cross join (select TO_CHAR(SQRT(sum/co)) rms_col from (select SUM(NUM_VAL*NUM_VAL) sum, count(*) co " +
                "from " + tableName +
                " where SMPL_TIME  > to_timestamp('" + startTime + "','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                "and CHANNEL_ID=" + channelId + "))";
        return jdbcTemplate.query(sql,new StatisticsRowMapper()).get(0);
    }

    /**
     * 垃圾方法
     * @param channelId
     * @param startTime
     * @param endTime
     * @return
     */
    public double getAvg(int channelId, String startTime,String endTime){
        int y=Integer.parseInt(startTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        String tableName = "CSS.SAMPLE";
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql = "select TO_CHAR(AVG(FLOAT_VAL)) avg_col from " + tableName +
                        " where SMPL_TIME  > to_timestamp('" + startTime +"','yyyy-mm-dd hh24:mi:ss') and SMPL_TIME < to_timestamp('" + endTime + "','yyyy-mm-dd hh24:mi:ss') " +
                        "and CHANNEL_ID=" + channelId;
        double avg = new Double(jdbcTemplate.queryForObject(sql,String.class));
        return avg;
    }

    /**
     * 获取查询时间段前的最后一个数据
     * @param startTime
     * @return
     * @throws ParseException
     */
    public ChannelOut getLatestRowStatus(String startTime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date date1;
        Calendar right1 = Calendar.getInstance();
        date1 = sdf.parse(startTime);
        right1.setTime(date1);
        right1.add(Calendar.HOUR_OF_DAY, -3);
        String beginTime = sdf.format(right1.getTime());
        String tableName = "CSS.SAMPLE";
        int y=Integer.parseInt(beginTime.substring(0, 4));
        Calendar now=Calendar.getInstance();
        int ny=now.get(Calendar.YEAR);
        if(y<ny-1){
            tableName = "CSS.SAMPLE_ALL";
        }
        String sql =  "select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                "(select rownum rn, SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                "(select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " + tableName +
                " where SMPL_TIME < to_timestamp('"+ startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                "and SMPL_TIME > to_timestamp('"+ beginTime +"','yyyy-mm-dd hh24:mi:ss') and CHANNEL_ID = 1522 order by SMPL_TIME DESC) " +
                "where rownum < 2) where rn = 1 ";
        List<ChannelOut> channelOutList;
        channelOutList = jdbcTemplate.query(sql, new ChannelOutRowMapper());
        if(channelOutList == null || channelOutList.size() == 0){
            right1.add(Calendar.HOUR_OF_DAY, -24);
            beginTime = sdf.format(right1.getTime());
            y=Integer.parseInt(beginTime.substring(0, 4));
            if(y<ny-1){
                tableName = "CSS.SAMPLE_ALL";
            }else{
                tableName = "CSS.SAMPLE";
            }
            sql =  "select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                    "(select rownum rn, SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                    "(select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from  " + tableName +
                    " where SMPL_TIME < to_timestamp('"+ startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                    "and SMPL_TIME > to_timestamp('"+ beginTime +"','yyyy-mm-dd hh24:mi:ss') and CHANNEL_ID = 1522 order by SMPL_TIME DESC) " +
                    "where rownum < 2) where rn = 1 ";
            channelOutList = jdbcTemplate.query(sql, new ChannelOutRowMapper());
            if(channelOutList == null || channelOutList.size() == 0){
                right1.add(Calendar.HOUR_OF_DAY, -100);
                beginTime = sdf.format(right1.getTime());
                y=Integer.parseInt(beginTime.substring(0, 4));
                if(y<ny-1){
                    tableName = "CSS.SAMPLE_ALL";
                }else{
                    tableName = "CSS.SAMPLE";
                }
                sql =  "select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                        "(select rownum rn, SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                        "(select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " + tableName +
                        " where SMPL_TIME < to_timestamp('"+ startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                        "and SMPL_TIME > to_timestamp('"+ beginTime +"','yyyy-mm-dd hh24:mi:ss') and CHANNEL_ID = 1522 order by SMPL_TIME DESC) " +
                        "where rownum < 2) where rn = 1 ";
                channelOutList = jdbcTemplate.query(sql, new ChannelOutRowMapper());
                if(channelOutList == null || channelOutList.size() == 0){
                    right1.add(Calendar.HOUR_OF_DAY, -720);
                    beginTime = sdf.format(right1.getTime());
                    y=Integer.parseInt(beginTime.substring(0, 4));
                    if(y<ny-1){
                        tableName = "CSS.SAMPLE_ALL";
                    }else{
                        tableName = "CSS.SAMPLE";
                    }
                    sql =  "select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                            "(select rownum rn, SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " +
                            "(select SMPL_TIME, NUM_VAL, FLOAT_VAL, STR_VAL from " + tableName +
                            " where SMPL_TIME < to_timestamp('"+ startTime +"','yyyy-mm-dd hh24:mi:ss') " +
                            "and SMPL_TIME > to_timestamp('"+ beginTime +"','yyyy-mm-dd hh24:mi:ss') and CHANNEL_ID = 1522 order by SMPL_TIME DESC) " +
                            "where rownum < 2) where rn = 1 ";
                    channelOutList = jdbcTemplate.query(sql, new ChannelOutRowMapper());
                }
            }
        }
        if(channelOutList == null || channelOutList.size() == 0){
            return null;
        }
        return channelOutList.get(0);
    }

    class ChannelOutRowMapper implements RowMapper<ChannelOut> {
        @Override
        public ChannelOut mapRow(ResultSet rs, int rowNum) throws SQLException {
            ChannelOut channelOut = new ChannelOut();
            channelOut.setSmpl_time(rs.getTimestamp("SMPL_TIME"));
            if (rs.getObject("FLOAT_VAL") != null) {
                channelOut.setFloat_val(rs.getFloat("FLOAT_VAL"));
            }
            if (rs.getObject("NUM_VAL") != null) {
                channelOut.setNum_val(rs.getInt("NUM_VAL"));
            }
            channelOut.setStr_val(rs.getString("STR_VAL"));
            return channelOut;
        }
    }

    class StatisticsRowMapper implements RowMapper<Statistics> {
        @Override
        public Statistics mapRow(ResultSet rs, int rowNum) throws SQLException {
            Statistics statistics = new Statistics();
            statistics.setMean(rs.getString("AVG_COL"));
            statistics.setDeviation(rs.getString("STDDEV_COL"));
            statistics.setMax(rs.getString("MAX_COL"));
            statistics.setMin(rs.getString("MIN_COL"));
            statistics.setRms(rs.getString("RMS_COL"));
            return statistics;
        }
    }
}