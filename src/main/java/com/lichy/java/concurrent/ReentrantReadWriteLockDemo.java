package com.lichy.java.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 最简单的一个ReentrantReadWriteLock
 *
 */
public class ReentrantReadWriteLockDemo {

    public static void main(String[] args) throws InterruptedException {
        ReentrantReadWriteLock rrwLock = new ReentrantReadWriteLock();
        ReadThread rt1 = new ReadThread("rt1", rrwLock);
        ReadThread rt2 = new ReadThread("rt2", rrwLock);
        WriteThread wt1 = new WriteThread("wt1", rrwLock);
        WriteThread wt2 = new WriteThread("wt2", rrwLock);
        TimeUnit.SECONDS.sleep(1);
        rt1.start();
        TimeUnit.SECONDS.sleep(1);
        rt2.start();
        TimeUnit.SECONDS.sleep(1);
        wt1.start();
        TimeUnit.SECONDS.sleep(1);
        wt2.start();
    }

    static class ReadThread extends Thread {
        private ReentrantReadWriteLock rrwLock;

        public ReadThread(String name, ReentrantReadWriteLock rrwLock) {
            super(name);
            this.rrwLock = rrwLock;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " trying to lock");
            try {
                rrwLock.readLock().lock();
                System.out.println(Thread.currentThread().getName() + " lock successfully");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rrwLock.readLock().unlock();
                System.out.println(Thread.currentThread().getName() + " unlock successfully");
            }
        }
    }

    static class WriteThread extends Thread {
        private ReentrantReadWriteLock rrwLock;

        public WriteThread(String name, ReentrantReadWriteLock rrwLock) {
            super(name);
            this.rrwLock = rrwLock;
        }

        public void run() {
            System.out.println(Thread.currentThread().getName() + " trying to lock");
            try {
                rrwLock.writeLock().lock();
                System.out.println(Thread.currentThread().getName() + " lock successfully");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                rrwLock.writeLock().unlock();
                System.out.println(Thread.currentThread().getName() + " unlock successfully");
            }
        }
    }
}




