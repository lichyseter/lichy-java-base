package com.lichy.java.concurrent;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 控制总许可数量，有许可才能执行，类似控制并发数量
 */

public class SemaphoreDemo {
    public final static int SEM_SIZE = 10;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(SEM_SIZE);
        MyThread t1 = new MyThread("t1", semaphore);
        MyThread t2 = new MyThread("t2", semaphore);
        t1.start();
        t2.start();
        int permits = 5;
        System.out.println(Thread.currentThread().getName() + " trying to acquire");
        try {
            semaphore.acquire(permits);
            System.out.println(Thread.currentThread().getName() + " acquire successfully");
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " release successfully");
        }
    }

    static class MyThread extends Thread {
        private final Semaphore semaphore;

        public MyThread(String name, Semaphore semaphore) {
            super(name);
            this.semaphore = semaphore;
        }

        public void run() {
            int count = 3;
            System.out.println(Thread.currentThread().getName() + " trying to acquire");
            try {
                semaphore.acquire(count);
                System.out.println(Thread.currentThread().getName() + " acquire successfully");
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(count);
                System.out.println(Thread.currentThread().getName() + " release successfully");
            }
        }
    }
}

