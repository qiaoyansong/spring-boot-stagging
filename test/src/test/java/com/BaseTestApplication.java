package com;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/25 3:11 下午
 * description：测试基类
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { Starter.class })
public class BaseTestApplication {
}
