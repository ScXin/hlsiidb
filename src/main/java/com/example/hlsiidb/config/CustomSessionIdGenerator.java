package com.example.hlsiidb.config;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

import java.io.Serializable;
import java.util.UUID;

public class CustomSessionIdGenerator implements SessionIdGenerator {

    /**
     * 自定义sessionId生成
     * @param session
     * @return
     */
    @Override
    public Serializable generateId(Session session) {
        //根据需求换成不同的加密算法生成token，可以进行自由设定，本项目未使用
        return UUID.randomUUID().toString().replace("-", "");
    }
}
