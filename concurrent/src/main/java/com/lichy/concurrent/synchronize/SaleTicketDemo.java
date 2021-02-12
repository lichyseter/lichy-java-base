package com.lichy.concurrent.synchronize;

/**
 * 不推荐使用这种，因为继承一个thread 耦合比较严重。
 *
 * @author lichy
 * @since 2021/02/11 15:58
 */
public class SaleTicketDemo {

    public static void main(String[] args) {
        Ticket t = new Ticket();
        new SellThread(t, "A").start();
        new SellThread(t, "B").start();
        new SellThread(t, "C").start();
    }

}

class SellThread extends Thread {
    final Ticket ticket;
    final String name;

    public SellThread(Ticket ticket, String name) {
        this.ticket = ticket;
        this.name = name;
    }

    @Override
    public void run() {

        while (ticket.hasTicket()) {
            synchronized (ticket) {
                if(ticket.hasTicket()) {
                    System.out.println(name + " sell one ticket. Still remain " + ticket.sellOneTicket());
                }
            }

        }
    }
}

class Ticket {
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
