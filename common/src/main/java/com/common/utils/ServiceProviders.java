package com.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 2024/1/24 4:51 下午
 * description：
 */
public class ServiceProviders {

    public ServiceProviders() {
    }

    public static <T> List<T> loadFromSpi(Class<T> type) {
        List<T> result = new ArrayList<>();
        Iterator<T> iterator = ServiceLoader.load(type).iterator();
        while (iterator.hasNext()) {
            try {
                T next = iterator.next();
                result.add(next);
            } catch (Throwable ignore) {

            }
        }
        return result;
    }
}
