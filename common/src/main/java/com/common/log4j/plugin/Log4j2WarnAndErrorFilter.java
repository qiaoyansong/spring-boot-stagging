package com.common.log4j.plugin;

import com.common.utils.LogFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2022/1/27 11:06 上午
 * description：用于拦截warn和error日志，输出到指定位置
 * 参考地址 https://blog.csdn.net/zhousenshan/article/details/119295023
 */
@Plugin(name = "Log4j2WarnAndErrorFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE, printObject = true)
public class Log4j2WarnAndErrorFilter extends AbstractFilter {

    /**
     * 错误日志
     */
    private static final Logger errorLog = LogFactory.ERROR_LOG;

    /**
     * 警告日志
     */
    private static final Logger warnLog = LogFactory.WARN_LOG;

    @Override
    public Result filter(final LogEvent loggingEvent) {
        Level level;
        Message msg = loggingEvent.getMessage();
        if ((level = loggingEvent.getLevel()) == Level.ERROR) {
            Throwable throwable = loggingEvent.getThrown();
            if (Objects.nonNull(throwable)) {
                errorLog.error(msg.getFormattedMessage(), throwable);
            } else {
                errorLog.error(msg.getFormattedMessage());
            }
        } else if (level == Level.WARN) {
            warnLog.warn(msg.getFormattedMessage());
        }
        return Result.ACCEPT;

    }


    @PluginFactory
    public static Log4j2WarnAndErrorFilter createFilter() {
        return new Log4j2WarnAndErrorFilter();
    }


}
