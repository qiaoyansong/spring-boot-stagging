package com.common.util;

import com.BaseTestApplication;
import com.util.LogFactory;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 3:10 下午
 * description：
 */
public class TestLog extends BaseTestApplication {

    private static Logger serviceLog = LogFactory.SERVICE_LOG;


    @Test
    public void testGetLog() {
        serviceLog.info("这是一个测试");
    }

}
