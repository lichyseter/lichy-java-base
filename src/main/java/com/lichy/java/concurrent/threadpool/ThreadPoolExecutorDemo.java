package com.lichy.java.concurrent.threadpool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 * 先写一个临时的ThreadPoolExecutor的api，之后测试所有的api
 * <p>
 * 1. ThreadPoolExecutor对于线程的执行顺序是，先塞满blocking queue 在拓展core size.
 * 2. 对于shutdownnow, 会忽略队列里的task， 尝试 interrupt掉正在执行的。 会返回未执行列表。
 * 3. shutdown 会等待pool里面的和队列里的都执行完，不做interrupt.
 * 4. awaitTermination 阻塞等待。awaitTermination 一般会与shutdown配合使用。 会在pool上的执行完才开始terminate 等待计时。
 */
public class ThreadPoolExecutorDemo {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
                .namingPattern("lichyThreadFactory-%d").build();

//        // 测试AbortPolicy ----------------------------------------
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//        for (int i = 0; i < 11; i++) {
//            executor.submit(new Task(i));
//        }

//        // 测试DiscardOldestPolicy ------------------------------------
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());
//        for (int i = 0; i < 11; i++) {
//            executor.submit(new Task(i));
//        }

//        // 测试DiscardPolicy ------------------------------------
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.DiscardPolicy());
//        for (int i = 0; i < 11; i++) {
//            executor.submit(new Task(i));
//        }

//        // 测试CallerRunsPolicy ------------------------------------
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//        for (int i = 0; i < 11; i++) {
//            executor.submit(new Task(i));
//        }

//        对于shutdownnow, 会忽略队列里的task， 尝试 interrupt掉正在执行的。
//        executor.shutdownNow();

//        // shutdown 会等待pool里面的和队列里的都执行完，不做interrupt.
//        executor.shutdown();

//        // awaitTermination 一般会与shutdown配合使用。 会在pool上的执行完才开始terminate 等待计时。
//        executor.shutdown();
//        if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
//            System.out.println("超时了");
//            System.out.printf("taskcount: %d", executor.getCompletedTaskCount());
//        }

        // 测试submit(Runnable task, T result) ------------------------------------
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 10L,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
        Future<String> myResult = executor.submit(new Task(0), "myResult");
        try {
            myResult.get(1, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            System.out.println("future get 超时");
        }
        System.out.println(myResult.get());
        executor.shutdown();
    }

    private static class Task implements Runnable {

        private final int num;

        public Task(int num) {
            this.num = num;
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(5 + num);
                System.out.printf("线程名称为：%s, 线程序号为 %d。%n", Thread.currentThread().getName(), num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
