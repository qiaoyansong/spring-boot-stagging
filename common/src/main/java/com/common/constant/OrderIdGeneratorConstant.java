package com.common.constant;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 5/15/23 5:46 下午
 * description：
 */
public class OrderIdGeneratorConstant {

    /**
     * 乘客bits
     */
    public static final int PASSENGER_BITS = 10;

    /**
     * 自增bits
     */
    public static final int INCR_BITS = 15;

    /**
     * 机器位bits，本地生成算法可以使用
     */
    public static final int MACHINE_BITS = 5;

    /**
     * 时间分钟bits
     */
    public static final int MINUTE_BITS = 26;

    /**
     * 版本bits
     */
    public static final int VERSION_BITS = 5;

    /**
     * 压测bits
     */
    public static final int STRESS_BITS = 1;

    /**
     * 压测移动bits
     */
    public static final int STRESS_MOVE_BITS = STRESS_BITS + VERSION_BITS + MACHINE_BITS + INCR_BITS + MINUTE_BITS
            + PASSENGER_BITS;
    /**
     * 版本移动bits
     */
    public static final int VERSION_MOVE_BITS = VERSION_BITS + MACHINE_BITS + INCR_BITS + MINUTE_BITS
            + PASSENGER_BITS;
    /**
     * 时间移动bits
     */
    public static final int MINUTE_MOVE_BITS = MINUTE_BITS + MACHINE_BITS + INCR_BITS + PASSENGER_BITS;

    /**
     * 机器移动bits
     */
    public static final int MACHINE_MOVE_BITS = MACHINE_BITS + INCR_BITS + PASSENGER_BITS;
    /**
     * 自增移动bits
     */
    public static final int INCR_MOVE_BITS = INCR_BITS + PASSENGER_BITS;

    /**
     * 压测bits掩码
     */
    public static final long STRESS_BITS_MASK = (1L << STRESS_MOVE_BITS) - 1;
    /**
     * 版本bits掩码
     */
    public static final long VERSION_BITS_MASK = (1L << VERSION_MOVE_BITS) - 1;
    /**
     * 时间bits掩码
     */
    public static final long MINUTE_BITS_MASK = (1L << MINUTE_MOVE_BITS) - 1;
    /**
     * 机器bits掩码
     */
    public static final long MACHINE_BITS_MASK = (1L << MACHINE_MOVE_BITS) - 1;
    /**
     * 自增bits掩码
     */
    public static final long INCR_BITS_MASK = (1L << INCR_MOVE_BITS) - 1;
    /**
     * 乘客bits掩码
     */
    public static final long PASSENGER_BITS_MASK = (1L << PASSENGER_BITS) - 1;

    public static final int MAX_INCR_NUM = (1 << (INCR_BITS + MACHINE_BITS)) - 1;

}
