package ticketingsystem;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Alchemist on 2016/9/17.
 */
public class Test {
    static AtomicLong totalTime = new AtomicLong(0);
    static AtomicLong inquiryTime = new AtomicLong(0);
    static AtomicLong purchaseTime = new AtomicLong(0);
    static AtomicLong refundTime = new AtomicLong(0);
    public static void main(String args[]) throws InterruptedException {
        TicketingDS ticketingDS = new TicketingDS();
        int threadNum;
        System.out.println("Please input the thread number:");
        Scanner scanner = new Scanner(System.in);
        threadNum  = scanner.nextInt();
        ConcurrentLinkedQueue<Ticket> ticketsBought = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Ticket> ticketsRefund = new ConcurrentLinkedQueue<>();
        for(int i = 0;i<threadNum;i++){
            TicketBuyer ticketBuyer = new TicketBuyer(threadNum,ticketingDS,ticketsBought,ticketsRefund);
            ticketBuyer.start();
        }
        Thread.sleep(5000);
        List<Long> verify = new LinkedList<>();
        System.out.println("tickets bought:");
        for(Ticket t:ticketsBought){
            System.out.println(t.tid);
            verify.add(t.tid);
        }
        System.out.println("tickets refund:");
        for(Ticket t:ticketsRefund){
            System.out.println(t.tid);
            verify.add(t.tid);
        }
        Collections.sort(verify);
        for(int i = 1;i<=verify.size();i++){
            if(i!=verify.get(i-1)){
                System.out.println("error!"+i);
            }
        }
        System.out.println("correct!");
        System.out.println(Test.totalTime.get()/threadNum/1000+" us");
        System.out.println(Test.inquiryTime.get()/threadNum/1000+" us");
        System.out.println(Test.purchaseTime.get()/threadNum/1000+" us");
        System.out.println(Test.refundTime.get()/threadNum/1000+" us");
    }
}


