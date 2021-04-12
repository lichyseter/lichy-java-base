package com.lichy.concurrent.synchronize;

/**
 * 使用runnable
 *
 * @author lichy
 * @since 2021/02/11 15:58
 */
public class SaleTicketRunnable {

    public static void main(String[] args) {
        Ticket3 t = new Ticket3();
        new Thread(() -> {
            while (t.hasTicket()) {
                synchronized (t) {
                    if (t.hasTicket()) {
                        System.out.println(Thread.currentThread().getName() + " sell one ticket. Still remain " + t.sellOneTicket());
                    }
                }
            }
        }, "A").start();
        new Thread(() -> {
            while (t.hasTicket()) {
                synchronized (t) {
                    if (t.hasTicket()) {
                        System.out.println(Thread.currentThread().getName() + " sell one ticket. Still remain " + t.sellOneTicket());
                    }
                }
            }
        }, "B").start();
        new Thread(() -> {
            while (t.hasTicket()) {
                synchronized (t) {
                    if (t.hasTicket()) {
                        System.out.println(Thread.currentThread().getName() + " sell one ticket. Still remain " + t.sellOneTicket());
                    }
                }
            }
        }, "C").start();
    }

}

class Ticket3 {
    int num = 5000;

    public int sellOneTicket() {
        if (hasTicket()) {
            num--;
        }
        return num;
    }

    public boolean hasTicket() {
        return num > 0;
    }
}

