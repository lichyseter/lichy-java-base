package com.lichy.concurrent.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 用lock 有顺序的消费。
 *
 * @author lichy
 * @since 2021/02/12 16:05
 */
public class OrderedConditionDemo {
    public static void main(String[] args) {
        ConditionLockResource synchronizeResource = new ConditionLockResource();
        new Thread(() -> {
            while (true) {
                try {
                    synchronizeResource.executeA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumerA").start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronizeResource.executeB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumerB").start();
        new Thread(() -> {
            while (true) {
                try {
                    synchronizeResource.executeC();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "consumerC").start();
    }
}


class ConditionLockResource {
    int num = 0;
    final int max = 50000;
    ReentrantLock lock = new ReentrantLock();
    Condition conditionA = lock.newCondition();
    Condition conditionB = lock.newCondition();
    Condition conditionC = lock.newCondition();


    void executeA() throws InterruptedException {
        lock.lock();
        try {
            if (num < max) {
                while (num % 3 != 0) {
                    conditionA.await();
                }
                if (num < max) {
                    num++;
                    System.out.println(Thread.currentThread().getName() + "执行" + num);
                    conditionB.signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    void executeB() throws InterruptedException {
        lock.lock();
        try {
            if (num < max) {
                while (num % 3 != 1) {
                    conditionB.await();
                }
                if (num < max) {
                    num++;
                    System.out.println(Thread.currentThread().getName() + "执行" + num);
                    conditionC.signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    void executeC() throws InterruptedException {
        lock.lock();
        try {
            if (num < max) {
                while (num % 3 != 2) {
                    conditionC.await();
                }
                if (num < max) {
                    num++;
                    System.out.println(Thread.currentThread().getName() + "执行" + num);
                    conditionA.signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}