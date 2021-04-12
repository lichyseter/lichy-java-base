package com.lichy.java.concurrent.threadpool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 先写一个临时的ThreadPoolExecutor的api，之后测试所有的api
 */
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) {
        ThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("lichyThreadFactory-").build();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 10, 10L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(10), basicThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        executor.submit(new Task());
        executor.shutdown();
    }

    private static class Task implements Runnable {

        @Override
        public void run() {
            System.out.println("gg");
        }
    }
}
