package com.lichy.java.concurrent.queue;

import java.util.concurrent.*;

/**
 * 所有阻塞队列测试.
 * 1. offer，add 区别：
 * <p>
 * 一些队列有大小限制，因此如果想在一个满的队列中加入一个新项，多出的项就会被拒绝。
 * <p>
 * 这时新的 offer 方法就可以起作用了。它不是对调用 add() 方法抛出一个 unchecked 异常，而只是得到由 offer() 返回的 false。
 * <p>
 * 2. poll，remove 区别：
 * <p>
 * remove() 和 poll() 方法都是从队列中删除第一个元素。remove() 的行为与 Collection 接口的版本相似， 但是新的 poll() 方法在用空集合调用时不是抛出异常，只是返回 null。因此新的方法更适合容易出现异常条件的情况。
 * <p>
 * 3. peek，element区别：
 * <p>
 * element() 和 peek() 用于在队列的头部查询元素。与 remove() 方法类似，在队列为空时， element() 抛出一个异常，而 peek() 返回 null。
 */
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        // ArrayBlockingQueue 需要指定大小，这样就能控制内存。同时应该可以同构offer和poll来实现等待。
        BlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(10);

        // LinkedBlockingQueue 可以是无界的，也可以是有界的，通过构造函数传入。 但是性能可能会消耗在节点添加删除之类的，性能也许会不稳定。
        BlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>(10);

        // priorityBlockingQueue 通过二叉堆来实现的。可能是大顶堆/小顶堆那种吧。默认从小到大，构造参数可以有一个compartor
        BlockingQueue<String> priorityBlockingQueue = new PriorityBlockingQueue<>(10);
        priorityBlockingQueue.add("3");
        priorityBlockingQueue.add("2");
        priorityBlockingQueue.add("1");
        System.out.printf("%s%s%s\n", priorityBlockingQueue.take(), priorityBlockingQueue.take(), priorityBlockingQueue.take());

        // DelayQueue 延时阻塞队列。
//        DelayQueue<MyDelayed> delayQueue = new DelayQueue<>();
//        ThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
//                .namingPattern("lichyThreadFactory-%d").build();
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//        delayQueue.add(new MyDelayed("第一个延迟2s", 2000));
//        delayQueue.add(new MyDelayed("第二个延迟3s", 3000));
//        delayQueue.add(new MyDelayed("第三个延迟1s", 1000));
//        executor.submit(new DelayQueueConsumer(delayQueue));
//        executor.awaitTermination(5, TimeUnit.SECONDS);
//        executor.shutdown();

//        // LinkedTransferQueue 需要有位置才能生产。有人消费，才能生产。但是可以有多个生产者和消费者在这里等着。
//        TransferQueue<String> linkedTransferQueue = new LinkedTransferQueue<>();
//        ThreadFactory basicThreadFactory = new BasicThreadFactory.Builder()
//                .namingPattern("lichyThreadFactory-%d").build();
//        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 10L,
//                TimeUnit.SECONDS, new ArrayBlockingQueue<>(5), basicThreadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.submit(new TransferConsumer(linkedTransferQueue));
//        TimeUnit.SECONDS.sleep(1);
//        executor.submit(new TransferConsumer(linkedTransferQueue));
//        TimeUnit.SECONDS.sleep(1);
//        executor.submit(new TransferProducer(linkedTransferQueue));
//        TimeUnit.SECONDS.sleep(1);
//        executor.submit(new TransferProducer(linkedTransferQueue));
//        TimeUnit.SECONDS.sleep(1);
//        executor.submit(new TransferProducer(linkedTransferQueue));
//        executor.awaitTermination(10, TimeUnit.SECONDS);
//        executor.shutdown();

        // SynchronousQueue 只有一个位置，可以用于线程间通信，有人娶了，才有人嫁，反过来也是如此。先来的等着。 和LinkedTransferQueue类似吧。
//        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();

//        // 再加一个无阻塞自旋的queue, 线程安全。用于一些强要求不准阻塞的地方吧。
//        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();

    }

    static class MyDelayed implements Delayed {

        private String message;   // 延迟任务中的任务数据
        private final long ttl;         // 延迟任务到期时间（过期时间）

        /**
         * 构造函数
         *
         * @param message 消息实体
         * @param ttl     延迟时间，单位毫秒
         */
        public MyDelayed(String message, long ttl) {
            setMessage(message);
            this.ttl = System.currentTimeMillis() + ttl;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            // 计算该任务距离过期还剩多少时间
            long remaining = ttl - System.currentTimeMillis();
            return unit.convert(remaining, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            // 比较、排序：对任务的延时大小进行排序，将延时时间最小的任务放到队列头部
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    static class DelayQueueConsumer implements Runnable {

        private final DelayQueue<MyDelayed> delayQueue;

        /**
         * 构造函数
         *
         * @param delayQueue 延迟队列
         */
        public DelayQueueConsumer(DelayQueue<MyDelayed> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 从延迟队列的头部获取已经过期的消息
                    // 如果暂时没有过期消息或者队列为空，则take()方法会被阻塞，直到有过期的消息为止
                    MyDelayed delayMessage = delayQueue.take();
                    System.out.printf("Consumer received message: %s \n", delayMessage.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
//            这里无法理解为什么下边这种方式无法正确工作。 debug 的时候能执行，run的时候无法执行。很像一个bug，或者jvm的机制。残念
            // 这里也有可能是对java某些机制不了解导致的，暂时不管了。
//            MyDelayed delayMessage;
//            while ((delayMessage = delayQueue.poll()) != null) {
//                // 从延迟队列的头部获取已经过期的消息
//                // 如果暂时没有过期消息或者队列为空，则take()方法会被阻塞，直到有过期的消息为止
//                System.out.printf("Consumer received message: %s \n", delayMessage.getMessage());
//
//            }
        }

    }

    static class TransferConsumer implements Runnable {

        private final TransferQueue<String> linkedTransferQueue;

        public TransferConsumer(TransferQueue<String> linkedTransferQueue) {
            this.linkedTransferQueue = linkedTransferQueue;
        }

        @Override
        public void run() {
            try {
                System.out.printf("消费：%s\n", linkedTransferQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class TransferProducer implements Runnable {

        private final TransferQueue<String> linkedTransferQueue;

        public TransferProducer(TransferQueue<String> linkedTransferQueue) {
            this.linkedTransferQueue = linkedTransferQueue;
        }

        @Override
        public void run() {
            try {
                linkedTransferQueue.transfer("一个");
                System.out.println("生产一个");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
