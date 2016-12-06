package ticketingsystem;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by WangJian on 2016/11/28.
 */
public class Route {
    int stationNum;
    int coachNum;
    int seatNum;
    int ticketNum;
    int [] residualTickets;
    ConcurrentLinkedQueue<Seat> seatLinkedList = new ConcurrentLinkedQueue<>();
    ConcurrentHashMap<Ticket,Integer> ticketsBought = new ConcurrentHashMap<>();


    public Route(int stationNum, int coachNum, int seatNum) {

        this.stationNum = stationNum;
        this.coachNum = coachNum;
        this.seatNum = seatNum;
        this.ticketNum = coachNum*seatNum;
        this.residualTickets = new int[stationNum+1];
        for(int i = 2;i<=stationNum;i++){
            residualTickets[i] = ticketNum;
        }
        for(int i = 0;i<coachNum;i++){
            for(int j = 0;j<seatNum;j++){
                seatLinkedList.add(new Seat(i+1,j+1));
            }
        }
    }

    public int quiry(int start,int end){
        if(start<=end)
            return 0;
        int min = Integer.MAX_VALUE;
        for(int i = start+1;i<=end;i++){
            if(residualTickets[i]<min)
                min = residualTickets[i];
        }
        return min;
    }

    public Ticket buyTicket(int start, int end){
        if(quiry(start,end)>=1) {
            synchronized (residualTickets) {
                if(quiry(start,end)<1)
                    return null;
                for (int i = start + 1; i <= end; i++) {
                    residualTickets[i]--;
                }
            }
            Ticket ticket = new Ticket();
            Seat seat = seatLinkedList.poll();
            ticket.coach = seat.getCoach();
            ticket.seat = seat.getSeat();
            ticket.departure = start;
            ticket.arrival = end;
            return ticket;
        }else{
            return null;
        }
    }

    public boolean refundTicket(Ticket ticket){
        if(ticket==null)
            return false;
        else if(!this.ticketsBought.containsKey(ticket)){
            return false;
        }else{
            int start = ticket.departure;
            int end = ticket.arrival;
            ticketsBought.remove(ticket);
            synchronized (residualTickets) {
                for (int i = start + 1; i <= end; i++) {
                    residualTickets[i]++;
                }
            }
            seatLinkedList.add(new Seat(ticket.coach,ticket.seat));

            return true;
        }
    }

    public void addTicket(Ticket ticket){
        this.ticketsBought.put(ticket,1);
    }
}

class Seat{
    int coach;
    int seat;

    public Seat(int coach, int seat) {
        this.coach = coach;
        this.seat = seat;
    }

    public int getCoach() {
        return coach;
    }

    public void setCoach(int coach) {
        this.coach = coach;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
}
