package com.lichy.java.concurrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * 两个线程不断交换数据。当有数据的时候才能交换，用于交替运行之类的。
 */
public class ExchangerDemo {

    public static void main(String[] args) throws InterruptedException {
        Exchanger<Integer> exchanger = new Exchanger<>();
        new Producer("", exchanger).start();
        new Consumer("", exchanger).start();
    }

    static class Producer extends Thread {
        private final Exchanger<Integer> exchanger;
        private static int data = 0;

        Producer(String name, Exchanger<Integer> exchanger) {
            super("Producer-" + name);
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            for (int i = 0; i < 2; i++) {
                try {
                    data = i;
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println(getName() + " 交换前:" + data);
                    data = exchanger.exchange(data);
                    System.out.println(getName() + " 交换后:" + data);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Consumer extends Thread {
        private final Exchanger<Integer> exchanger;
        private static int data = 100;

        Consumer(String name, Exchanger<Integer> exchanger) {
            super("Consumer-" + name);
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println(getName() + " 交换前:" + data);
                try {
                    data = exchanger.exchange(data);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(getName() + " 交换后:" + data);
            }
        }
    }


}
