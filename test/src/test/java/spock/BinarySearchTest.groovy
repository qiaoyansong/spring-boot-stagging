package spock

import org.junit.Assert
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/3/28 22:54
 * description：
 */
class BinarySearchTest extends Specification {

    @Unroll
    def "testBinarySearch"() {
        setup: "mock数据"

        when: "调用方法"
        int inFactIndex = Arrays.binarySearch(arrays as int[], target)

        then: "比较结果"
        Assert.assertEquals(inFactIndex, excepectIndex)

        where:
        arrays                 | target | excepectIndex
        Arrays.asList(1, 2, 3) | 2      | 1
        Arrays.asList(1, 2, 3) | 3      | 2
    }

}
