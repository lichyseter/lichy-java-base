package com.lichy.concurrent.synchronize;

/**
 * 测试为什么方法加上synchronized的结果不对。应该是返回之后字符串拼接和sout导致输出循序不一致。sout放到同步块里面就行了。
 *
 * @author lichy
 * @since 2021/02/11 15:58
 */
public class SaleTicketDemo2 {

    public static void main(String[] args) {
        Ticket2 t = new Ticket2();
        new SellThread2(t, "A").start();
        new SellThread2(t, "B").start();
        new SellThread2(t, "C").start();
    }

}

class SellThread2 extends Thread {
    final Ticket2 ticket;
    final String name;

    public SellThread2(Ticket2 ticket, String name) {
        this.ticket = ticket;
        this.name = name;
    }

    @Override
    public void run() {

        while (ticket.hasTicket()) {

            ticket.sellOneTicket(name);
            // 像下面这样调用打印顺序是不对的，可能是因为sout本身不同步，或者是+的字符串连接不同步。。
            // System.out.println(name + " sell one ticket. Still remain " + ticket.sellOneTicket(name));
        }
    }
}

class Ticket2 {
    int num = 5000;

    public synchronized void sellOneTicket(String name) {
        if (hasTicket()) {
            num--;
            System.out.println(name + " sell one ticket. Still remain " + num);
        }
    }

    public boolean hasTicket() {
        return num > 0;
    }
}
