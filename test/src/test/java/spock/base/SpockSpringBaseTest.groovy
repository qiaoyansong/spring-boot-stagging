package spock.base

import com.api.HelloRpcService
import com.mapper.UserMapper
import org.mockito.Mockito
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import spock.lang.Specification

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/3/28 23:15
 * description：基于IOC容器的base类
 */
@SpringBootTest(classes = SpockSpringBaseTest.class)
@ComponentScan(basePackages = ["com"])
class SpockSpringBaseTest extends Specification {

    @Autowired
    HelloRpcService helloRpcService;

    /**
     * 当IOC注入的时候，会将mock的类注入进去
     */
    @SpringBean
    UserMapper userMapper = Mockito.mock(UserMapper.class)

}
