package com.lichy.java.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个简单的ReentrantLock 和 Condition 的demo
 */
public class ReentrantLockDemo {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock(true);
        Condition condition = lock.newCondition();
        MyThread t1 = new MyThread("t1", lock, condition, 1);
        MyThread t2 = new MyThread("t2", lock, condition, 5);
        MyThread t3 = new MyThread("t3", lock, condition, 10);
        t1.start();
        t2.start();
        t3.start();
    }

    static class MyThread extends Thread {
        private final Lock lock;
        private final Condition condition;
        private final int second;

        public MyThread(String name, Lock lock, Condition condition, int second) {
            super(name);
            this.lock = lock;
            this.condition = condition;
            this.second = second;
        }

        public void run() {
            lock.lock();
            try {
                condition.await(second, TimeUnit.SECONDS);
                System.out.println(Thread.currentThread() + " running");
                condition.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

    }


}
