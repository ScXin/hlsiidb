package com.example.hlsiidb.controller;

import com.example.hlsiidb.entity.OperationLog;
import com.example.hlsiidb.service.RecordUserLogService;
import com.example.hlsiidb.service.UserService;
import com.example.hlsiidb.vo.OperationLogVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ScXin
 * @date 7/7/2020 8:50 PM
 */
@RestController
@RequestMapping("/hlsii")
public class OperationLogController {


    @Autowired
    private RecordUserLogService recordUserLogService;


    @Autowired
    private UserService userService;


    @ApiOperation("根据参数查询日志")
    @GetMapping("/userLog/viewLog")
    public List<OperationLog> getLogByCondition(@RequestParam(value = "userName", required = false) String userName,
                                                @RequestParam(value = "loginIP", required = false) String loginIP,
                                                @RequestParam(value = "operationType", required = false) String operationType,
                                                @RequestParam(value = "startTime", required = false) String startTime,
                                                @RequestParam(value = "endTime", required = false) String endTime) {


        String queryCondition = "";
        String userParam = "";
        if (userName != null) {
            userParam += "user_name='" + userName + "'";
        }
        if (loginIP != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and remote_ip='" + loginIP + "'";
            } else {
                queryCondition += "remote_ip='" + loginIP + "'";
            }
        }
        if (operationType != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and op_type='" + operationType + "'";
            } else {
                queryCondition += "op_type='" + operationType + "'";
            }
        }
        if (startTime != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and op_time>='" + startTime + "'";
            } else {
                queryCondition += "op_time>='" + startTime + "'";
            }
        }

        if (endTime != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and op_time<='" + endTime + "'";
            } else {
                queryCondition += "op_time<='" + endTime + "'";
            }
        }
        return recordUserLogService.getLogByCondition(queryCondition, userParam);
    }

    @ApiOperation("按页获取日志")
    @GetMapping("/userLog/page")
    public OperationLogVo getOperlogByPage(@RequestParam(value = "pageNum") int pageNum,
                                           @RequestParam(value = "pageSize") int pageSize,
                                           @RequestParam(value = "userName", required = false) String userName,
                                           @RequestParam(value = "loginIP", required = false) String loginIP,
                                           @RequestParam(value = "operationType", required = false) String operationType,
                                           @RequestParam(value = "startTime", required = false) String startTime,
                                           @RequestParam(value = "endTime", required = false) String endTime) {

        String queryCondition = "";
        String userParam = "";
        if (userName != null) {
            if (!userParam.equals("")) {
                userParam += " and user_name='" + userName + "'";
            } else {
                userParam += "user_name='" + userName + "'";
            }
        }
        if (loginIP != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and remote_ip='" + loginIP + "'";
            } else {
                queryCondition += "remote_ip='" + loginIP + "'";
            }
        }
        if (operationType != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and op_type='" + operationType + "'";
            } else {
                queryCondition += "op_type='" + operationType + "'";
            }
        }
        if (startTime != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and op_time>='" + startTime + "'";
            } else {
                queryCondition += "op_time>='" + startTime + "'";
            }
        }

        if (endTime != null) {
            if (!queryCondition.equals("")) {
                queryCondition += " and op_time<='" + endTime + "'";
            } else {
                queryCondition += "op_time<='" + endTime + "'";
            }
        }
        List<OperationLog> operationLogs = recordUserLogService.getLogByPage(queryCondition, userParam, pageNum, pageSize);

        int count = recordUserLogService.calculateCount(queryCondition, userParam);

        OperationLogVo operationLogVo = new OperationLogVo();
        operationLogVo.setCount(count);
        operationLogVo.setOperationLogList(operationLogs);
        return operationLogVo;
    }

    @ApiOperation("获取用户日志数量")
    @GetMapping("/userLog/totalCount")
    public int logCount() {
        return recordUserLogService.getCount();
    }
}
