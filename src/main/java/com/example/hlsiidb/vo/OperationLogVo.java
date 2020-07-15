package com.example.hlsiidb.vo;

import com.example.hlsiidb.entity.OperationLog;

import java.util.List;

/**
 * @author ScXin
 * @date 7/13/2020 11:18 AM
 */
public class OperationLogVo {


    private int count;

    private List<OperationLog> operationLogList;

    public OperationLogVo() {
    }

    public OperationLogVo(int count, List<OperationLog> operationLogList) {
        this.count = count;
        this.operationLogList = operationLogList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<OperationLog> getOperationLogList() {
        return operationLogList;
    }

    public void setOperationLogList(List<OperationLog> operationLogList) {
        this.operationLogList = operationLogList;
    }
}
