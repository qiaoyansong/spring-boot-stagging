package com.service;

import com.BaseTestApplication;
import com.utils.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 6:45 下午
 * description：
 */
public class TestIoc extends BaseTestApplication {

    @Autowired
    private LogFactory logFactory;

    @Test
    public void test(){
        System.out.println(logFactory);
    }
}
