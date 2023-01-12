package com.test.common;

import com.test.BaseTestApplication;
import com.common.utils.LogFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 3:10 下午
 * description：
 */
public class TestLog extends BaseTestApplication {

    /**
     * 同步日志
     */
    private static Logger serviceLog = LogFactory.SERVICE_LOG;

    private static Logger bizServiceLog = LogFactory.BIZ_SERVICE_LOG;

    private static Logger commonLog = LogFactory.COMMON_LOG;

    private static Logger integrationLog = LogFactory.INTEGRATION_LOG;

    /**
     * 异步日志
     */
    private static Logger redisLogger = LogFactory.REDIS_LOGGER;

    private static Logger hotSpotLogger = LoggerFactory.getLogger("hotSpotLogger");


    @Test
    public void testGetLog() {
        serviceLog.info("这是一个测试2");
    }

    @Test
    public void testWarnAndError() {
        serviceLog.warn("这是一个测试");
        serviceLog.error("这是一个测试");
    }

    @Test
    public void testSync() {
        serviceLog.info("serviceLog");
        bizServiceLog.info("bizServiceLog");
        commonLog.info("commonLog");
        integrationLog.info("integrationLog");

        serviceLog.warn("serviceLog");
        bizServiceLog.warn("bizServiceLog");
        commonLog.warn("commonLog");
        integrationLog.warn("integrationLog");

        serviceLog.error("serviceLog");
        bizServiceLog.error("bizServiceLog");
        commonLog.error("commonLog");
        integrationLog.error("integrationLog");
    }

    @Test
    public void testAsync() {
        while (true) {
            redisLogger.info("redisLogger");
            hotSpotLogger.info("hotSpotLogger");
        }
    }
}
