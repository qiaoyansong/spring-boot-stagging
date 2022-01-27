package com.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 2:33 下午
 * description：日志管理工具类
 */
public class LogFactory {

    /**
     * service日志
     */
    public static final Logger SERVICE_LOG = LoggerFactory.getLogger("serviceLogger");

    /**
     * 业务日志
     */
    public static final Logger BIZ_SERVICE_LOG = LoggerFactory.getLogger("bizServiceLogger");

    /**
     * 业务日志
     */
    public static final Logger WARN_LOG = LoggerFactory.getLogger("warnLogger");

    /**
     * 业务日志
     */
    public static final Logger ERROR_LOG = LoggerFactory.getLogger("errorLogger");


}
