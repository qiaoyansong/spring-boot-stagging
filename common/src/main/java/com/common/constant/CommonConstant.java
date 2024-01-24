package com.common.constant;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 5:47 下午
 * description：通用常量配置
 */
public class CommonConstant {

    public static final String KEY_TRACE = "trace_id";

    public static final String TRACE_SYS_BAGGAGE_PREFIX = "trace.sys.";

    public static final String TRACE_BIZ_BAGGAGE_PREFIX = "trace.biz.";

    public static final String KEY_REVERSE_PARAM_PREFIX = "reverse-param-";


    /**
     * 参照时间：2020-03-06 00:00:00
     */
    public static final long REFERENCE_TIME = 1583424000000L;

    /**
     * 一分钟的毫秒数
     */
    public static final long ONE_MINUTE_MILLISECOND = 60000;

}
