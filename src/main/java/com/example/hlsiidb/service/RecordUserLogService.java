package com.example.hlsiidb.service;

import com.example.hlsiidb.commdef.OperationType;
import com.example.hlsiidb.entity.OperationLog;

import java.util.List;

/**
 * @author ScXin
 * @date 7/7/2020 2:38 PM
 */
public interface RecordUserLogService {

    /**
     * Log user operation
     *
     * @param opType - operation type
     */
    public void logOperation(OperationType opType);

    public List<OperationLog> getLogByCondition(String queryCondition, String userParam);

    public List<OperationLog> getLogByPage(String queryCondition, String userParam, int pageNum, int pageSize);

    public int getCount();

    public int calculateCount(String queryCondition, String userParm);
}
