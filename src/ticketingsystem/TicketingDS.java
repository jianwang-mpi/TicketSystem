package ticketingsystem;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alchemist on 2016/9/17.
 */
public class TicketingDS implements TicketingSystem {
    private int routeNum = 5;
    private int coachNum = 8;
    private int seatNum = 100;
    private int stationNum = 10;
    private AtomicInteger ticketID;
    private Route[] routes;     //这里注意，route的标号是提供的route标号减去1//


    @Override
    public Ticket buyTicket(String passenger, int route, int departure, int arrival) {
        Ticket ticket = routes[route-1].buyTicket(departure,arrival);
        if(ticket==null)
            return null;
        else {
            ticket.passenger = passenger;
            ticket.tid = ticketID.getAndIncrement();
            ticket.route = route;
            routes[route-1].addTicket(ticket);
            return ticket;
        }
    }

    @Override
    public int inquiry(int route, int departure, int arrival) {
        return routes[route-1].quiry(departure,arrival);
    }

    @Override
    public boolean refundTicket(Ticket ticket) {
        return this.routes[ticket.route-1].refundTicket(ticket);
    }

    //routeNum是车次总数，coachNum是车厢总数，seatNum是每节车厢的座位数，stationNum是每个车次经停站的数量

    public TicketingDS(int routeNum, int coachNum, int seatNum, int stationNum) {
        this.routeNum = routeNum;
        this.coachNum = coachNum;
        this.seatNum = seatNum;
        this.stationNum = stationNum;
        this.ticketID = new AtomicInteger(1);
        this.routes = new Route[routeNum];
        for(int i = 0;i<routeNum;i++){
            routes[i] = new Route(stationNum,coachNum,seatNum);
        }
    }

    public TicketingDS(){
        this.ticketID = new AtomicInteger(1);
        this.routes = new Route[routeNum];
        for(int i = 0;i<routeNum;i++){
            routes[i] = new Route(stationNum,coachNum,seatNum);
        }
    }
    public int getRouteNum() {
        return routeNum;
    }

    public void setRouteNum(int routeNum) {
        this.routeNum = routeNum;
    }

    public int getCoachNum() {
        return coachNum;
    }

    public void setCoachNum(int coachNum) {
        this.coachNum = coachNum;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public int getStationNum() {
        return stationNum;
    }

    public void setStationNum(int stationNum) {
        this.stationNum = stationNum;
    }
}
