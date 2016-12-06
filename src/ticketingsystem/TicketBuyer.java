package ticketingsystem;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by WangJian on 2016/12/5.
 */
public class TicketBuyer extends Thread{
    int threadNum;
    TicketingDS ticketingDS;
    ConcurrentLinkedQueue<Ticket> ticketsBought;
    ConcurrentLinkedQueue<Ticket> ticketsRefund;

    public TicketBuyer(int threadNum, TicketingDS ticketingDS, ConcurrentLinkedQueue<Ticket> ticketsBought,ConcurrentLinkedQueue<Ticket> ticketsRefund) {
        this.threadNum = threadNum;
        this.ticketingDS = ticketingDS;
        this.ticketsBought = ticketsBought;
        this.ticketsRefund = ticketsRefund;
    }
    Set<Ticket> ticketsBuyed = new HashSet<>();
    @Override
    public void run() {
        super.run();
        for(int i = 0;i<10000/threadNum;i++){
            double rdm = Math.random();
            int routeNum = ticketingDS.getRouteNum();
            int coachNum = ticketingDS.getCoachNum();
            int seatNum = ticketingDS.getSeatNum();
            int stationNum = ticketingDS.getStationNum();
            long startTime = 0;
            long endTime = 0;
            if(rdm<=0.6){
                int route = (int) (Math.random() * routeNum)+1;
                int departure = (int)(Math.random()*(stationNum-1))+1;
                int arrival = (int)(Math.random()*(stationNum-departure))+1;
                startTime = System.nanoTime();
                int residualNum = ticketingDS.inquiry(route,departure,arrival);
                endTime = System.nanoTime();
                Test.inquiryTime.addAndGet(endTime-startTime);
            }else if(rdm<=0.9){
                int route = (int) (Math.random() * routeNum)+1;
                int departure = (int)(Math.random()*(stationNum-1))+1;
                int arrival = (int)(Math.random()*(stationNum-departure))+1;
                startTime = System.nanoTime();
                Ticket ticket = ticketingDS.buyTicket("wj",route,departure,arrival);
                endTime = System.nanoTime();
                Test.purchaseTime.addAndGet(endTime-startTime);
                if(ticket!=null)
                    ticketsBought.add(ticket);
            }else{
                Ticket ticket = ticketsBought.poll();
                if(ticket!=null) {
                    startTime = System.nanoTime();
                    boolean b = ticketingDS.refundTicket(ticket);
                    endTime = System.nanoTime();
                    Test.refundTime.addAndGet(endTime-startTime);
                    if (b) {
                        ticketsRefund.add(ticket);
                    } else {
                        ticketsBought.add(ticket);
                    }
                }
            }
            Test.totalTime.addAndGet(endTime-startTime);
        }
    }
}