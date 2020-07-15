package com.example.hlsiidb.service.impl;

import com.commonuser.entity.User;
import com.example.hlsiidb.commdef.OperationType;
import com.example.hlsiidb.dao.OperationLogDao;
import com.example.hlsiidb.entity.OperationLog;
import com.example.hlsiidb.service.RecordUserLogService;
import com.example.hlsiidb.util.WebUtil;
import com.example.hlsiidb.vo.OperationLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author ScXin
 * @date 7/7/2020 4:33 PM
 */
@Service
@Transactional
public class RecordUserLogServiceImpl implements RecordUserLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    @Override
    @Transactional(readOnly = false)
    public void logOperation(OperationType opType) {
        User currentUser = WebUtil.getCurrentLoginUser();
        String ip = WebUtil.getLoginUserIP();
        OperationLog opLog = new OperationLog();
        opLog.setUser(currentUser);
        opLog.setOperType(opType);
        opLog.setRemoteIP(ip);

        operationLogDao.save(opLog);
    }

    @Override
    public List<OperationLog> getLogByCondition(String queryCondition, String userParam) {
        return operationLogDao.getLogByCondition(queryCondition, userParam);
    }

    @Override
    public List<OperationLog> getLogByPage(String queryCondition, String userParam, int pageNum, int pageSize) {
        return operationLogDao.getLogByPage(queryCondition, userParam, pageNum, pageSize);
    }

    @Override
    public int getCount() {
        return operationLogDao.getCount();
    }

    @Override
    public int calculateCount(String queryCondition, String userParm) {
        return operationLogDao.calculateCount(queryCondition, userParm);
    }
}
