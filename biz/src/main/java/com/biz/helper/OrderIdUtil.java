package com.biz.helper;

import com.common.constant.CommonConstant;
import com.common.constant.OrderIdGeneratorConstant;
import com.dal.redis.RedisService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 5/15/23 5:46 下午
 * description：生成订单ID工具类
 */
@Component
public class OrderIdUtil {

    @Resource
    private RedisService redisService;

    /**
     * 订单IDRedis 生成key，替换符为订单类型和分钟
     */
    public static final String ORDER_ID_KEY = "store.oid:%s:%s";

    private static Random random = new Random(11);

    /**
     * 生成订单ID
     * 订单ID规则为long
     *
     * 1         5     26   5      15               10
     * 压测标识位  版本  分钟  机器位  相同分钟内自增ID   购买者ID
     * @param orderType          订单类型
     * @param isFullLinkPressure
     * @param passengerId
     * @return
     */
    public long createOrderId(Integer orderType, Long passengerId, boolean isFullLinkPressure) {
        long minute = getDiffMinutess();

        String orderIdKey = String.format(ORDER_ID_KEY, orderType, minute);
        Long incrNum = null;
        try {
            incrNum = redisService.incr(orderIdKey);
            if (incrNum == 1) {
                redisService.expire(orderIdKey, 120);
            }
        } catch (Exception e) {
            incrNum = (long) random.nextInt(OrderIdGeneratorConstant.MAX_INCR_NUM);
        }

        return doCreateOrderId(orderType, passengerId, minute, incrNum, isFullLinkPressure);
    }

    /**
     * 生成订单id
     *
     * @param passengerId
     * @param minute
     * @param incrNum
     * @return
     */
    private long doCreateOrderId(Integer bizType, Long passengerId, Long minute, Long incrNum, boolean isFullLinkPressure) {
        long passengerBits = passengerId & OrderIdGeneratorConstant.PASSENGER_BITS_MASK;

        long incrNumBits = (incrNum << OrderIdGeneratorConstant.PASSENGER_BITS)
                & OrderIdGeneratorConstant.MACHINE_BITS_MASK;

        long minuteBits = (minute << OrderIdGeneratorConstant.MACHINE_MOVE_BITS)
                & OrderIdGeneratorConstant.MINUTE_BITS_MASK;

        long orderVersion = bizType;
        long versionBits = (orderVersion << OrderIdGeneratorConstant.MINUTE_MOVE_BITS)
                & OrderIdGeneratorConstant.VERSION_BITS_MASK;

        long stressBits = 0;
        if (isFullLinkPressure) {
            stressBits = (1L << OrderIdGeneratorConstant.VERSION_MOVE_BITS) & OrderIdGeneratorConstant.STRESS_BITS_MASK;
        }

        return stressBits | versionBits | incrNumBits | minuteBits | passengerBits;
    }

    private long getDiffMinutess() {
        return (System.currentTimeMillis() - CommonConstant.REFERENCE_TIME) / CommonConstant.ONE_MINUTE_MILLISECOND;
    }
}
