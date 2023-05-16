package com.test.common;

import com.common.utils.CompletableFutureUtils;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author ：Qiao Yansong
 * @date ：Created in 5/16/23 2:33 下午
 * description：
 */
public class TestCompletableFutureUtils {

    @Test
    public void test() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        CompletableFuture<Integer> within = CompletableFutureUtils.completeOnTimeout(1, future, 1, TimeUnit.SECONDS, "within超时");
        System.out.println(CompletableFutureUtils.get(null, within));

        CompletableFuture<String> futureStr = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "正常执行";
        });

        CompletableFuture<String> withinStr = CompletableFutureUtils.completeOnTimeout("异常执行", futureStr, 1, TimeUnit.SECONDS, "withinStr超时");
        System.out.println(CompletableFutureUtils.get(null, withinStr));

    }
}
