package com.common.log4j;

import com.common.utils.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:06 上午
 * description：用于拦截warn和error日志，输出到指定位置
 */
public class WarnAndErrorFilter extends Filter {

    /**
     * 错误日志
     */
    private static final Logger errorLog = LogFactory.ERROR_LOG;

    /**
     * 警告日志
     */
    private static final Logger warnLog = LogFactory.WARN_LOG;



    @Override
    public int decide(LoggingEvent loggingEvent) {
        Level level;
        if((level = loggingEvent.getLevel()) == Level.ERROR) {
            String msg = (String) loggingEvent.getMessage();
            ThrowableInformation throwableInformation = loggingEvent.getThrowableInformation();
            if (Objects.nonNull(throwableInformation)) {
                Throwable throwable = throwableInformation.getThrowable();
                if (Objects.nonNull(throwable)) {
                    errorLog.error(msg, throwable);
                } else {
                    errorLog.error(msg);
                }
            } else {
                errorLog.error(msg);
            }
            return ACCEPT;
        }else if(level == Level.WARN) {
            warnLog.warn((String) loggingEvent.getMessage());
            return ACCEPT;
        }else {
            return ACCEPT;
        }
    }
}
