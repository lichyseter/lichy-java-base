package com.lichy.concurrent.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用lock
 *
 * @author lichy
 * @since 2021/02/11 15:58
 */
public class SaleTicketRunnable {

    public static void main(String[] args) {
        Ticket3 t = new Ticket3();
        new Thread(() -> {
            while (t.hasTicket()) {
                t.sellOneTicket(Thread.currentThread().getName());
            }
        }, "A").start();
        new Thread(() -> {
            while (t.hasTicket()) {
                t.sellOneTicket(Thread.currentThread().getName());
            }
        }, "B").start();
        new Thread(() -> {
            while (t.hasTicket()) {
                t.sellOneTicket(Thread.currentThread().getName());
            }
        }, "C").start();
    }

}

class Ticket3 {
    int num = 5000;
    Lock lock = new ReentrantLock();

    public void sellOneTicket(String name) {
        lock.lock();
        try {
            if (hasTicket()) {
                num--;
                System.out.println(name + " sell one ticket. Still remain " + num);
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean hasTicket() {
        return num > 0;
    }
}

