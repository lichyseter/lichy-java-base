package com.lichy.concurrent.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 用lock实现的一个简单的生产消费
 *
 * @author lichy
 * @since 2021/02/12 16:05
 */
public class LockDemo {
    public static void main(String[] args) {
        LockResource synchronizeResource = new LockResource();
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


class LockResource {
    int totalProduceCount;
    int totalConsumeCount;
    int num = 0;
    final int max = 50000;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    void produceOne() throws InterruptedException {
        lock.lock();
        try {
            while (num >= max) {
                condition.await();
            }
            num++;
            totalProduceCount++;
            System.out.println("共生产" + totalProduceCount);
            condition.signalAll();
        } finally {
            lock.unlock();
        }

    }

    void consumeOne() throws InterruptedException {
        lock.lock();
        try {
            while (num <= 0) {
                condition.await();
            }
            num--;
            totalConsumeCount++;
            System.out.println("共消费" + totalConsumeCount);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}