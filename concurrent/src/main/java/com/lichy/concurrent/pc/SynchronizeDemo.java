package com.lichy.concurrent.pc;

/**
 * 写个最基础的produce custemer.
 * 这里的生产者和消费者有同等机会被notify 所以要是while而不能是if。
 *
 * @author lichy
 * @since 2021/02/12 14:23
 */
public class SynchronizeDemo {
    public static void main(String[] args) {
        SynchronizeResource synchronizeResource = new SynchronizeResource();
        new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                try {
                    synchronizeResource.produceOne();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producerA").start();
        new Thread(() -> {
            for (int i = 0; i < 50000; i++) {
                try {
                    synchronizeResource.produceOne();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "producerB").start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronizeResource.consumeOne();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumerA").start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronizeResource.consumeOne();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumerB").start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronizeResource.consumeOne();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumerC").start();
    }

}

class SynchronizeResource {
    int totalProduceCount;
    int totalConsumeCount;
    int num = 0;
    final int max = 50000;

    synchronized void produceOne() throws InterruptedException {
        // 需要while判断。
        while (num >= max) {
            this.wait();
        }
        num++;
        totalProduceCount++;
        System.out.println("共生产" + totalProduceCount);
        this.notifyAll();
    }

    synchronized void consumeOne() throws InterruptedException {
        while (num <= 0) {
            this.wait();
        }
        num--;
        totalConsumeCount++;
        System.out.println("共消费" + totalConsumeCount);
        this.notifyAll();
    }
}