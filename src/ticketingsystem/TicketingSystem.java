package ticketingsystem;

/**
 * Created by Alchemist on 2016/9/17.
 */
public interface TicketingSystem {
    Ticket buyTicket(String passenger,int route,int departure,int arrival);
    int inquiry(int route,int departure,int arrival);
    boolean refundTicket(Ticket ticket);
}
class Ticket{
    long tid;
    String passenger;
    int route;
    int coach;
    int seat;
    int departure;
    int arrival;
}
