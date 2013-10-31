/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import static com.sun.xml.bind.util.CalendarConv.formatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("Tickets")
public class TicketResource {
    public TicketResource() {
    }
    @GET
    @Path("GetTickets/{key}")
    @Produces("application/json")
    public Tickets getUnvalidatedTickets(@PathParam("key") String key) {
        Tickets tickets = new Tickets();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            String query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.Tickets.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.STATUS=0";
            ResultSet rs = stmt.executeQuery(query);

            while ( rs.next() ) {
                Ticket newTicket = new Ticket(rs.getInt("TICKETID"), rs.getString("TYPE"), rs.getInt("USERID"));
                tickets.Tickets.add(newTicket);
                System.out.println(newTicket.Id);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Man:" + e.getMessage());  // to the app server log
        }
        System.out.println("ok");
        return tickets;
    }
    
    @GET
    @Path("GetValidTickets/{key}")
    @Produces("application/json")
    public Tickets getValidatedTickets(@PathParam("key") String key) {
        Tickets tickets = new Tickets();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.HOUR_OF_DAY, -1);
            currentDate.add(Calendar.MINUTE, -30);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
            System.out.println(format.format(currentDate.getTime()));
            
            String query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS, APP.BUSTICKET WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.TICKETID=APP.BUSTICKET.TICKETID AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.STATUS=1";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                String date= rs.getString("VALIDATIONDATE");
                date = date.substring(0,date.length()-5);
                Ticket newTicket = new Ticket(rs.getInt("TICKETID"), rs.getInt("BUSID"), date, rs.getString("TYPE"), rs.getInt("USERID"));
                tickets.Tickets.add(newTicket);
                System.out.println(newTicket.Id);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println("Error Man:" + e.getMessage());  // to the app server log
        }
        System.out.println("ok");
        return tickets;
    }
    
    
    @POST
    @Path("TerminalValidate/{key}")
    @Produces("text/plain")
    @Consumes("application/json")
    public String terminalValidate(@PathParam("key") String key, TerminalValidate tv) {
        int index = 0;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            Ticket ticket = new Ticket();
           
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.MINUTE, -15);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");
            System.out.println(format.format(currentDate.getTime()));
            
            System.out.println("Check if there is already a validated ticket");
            
            String query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T1' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID="+ tv.UserId;
            ResultSet rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                return "-3";
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.MINUTE, -30);
            System.out.println(format.format(currentDate.getTime()));
            
            query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T2' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID="+ tv.UserId;
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                return "-3";
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.HOUR_OF_DAY, -1);
            System.out.println(format.format(currentDate.getTime()));
            
            query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T3' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID="+ tv.UserId;
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                return "-3";
            }
            
            System.out.println("Checking Available Tickets");
            
            query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.STATUS=0 AND APP.USERTICKETS.USERID="+tv.UserId+" AND APP.TICKETS.TYPE='"+tv.Type+"'";
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                ticket.UserId = rs.getInt("USERID");
                ticket.Id= rs.getInt("TICKETID");
                ticket.Type = rs.getString("TYPE");
                ticket.State=1;
            }
            else
            {
                return "-2";
            }
            System.out.println("Updating Usertickets");
            
            query = "UPDATE APP.USERTICKETS SET STATUS=1 WHERE TICKETID="+ticket.Id;
            stmt.executeUpdate(query);
            
            
            query = "SELECT MAX(Id) FROM APP.BUSTICKET";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                index = rs.getInt(1) + 1;
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            System.out.println("Inserting in BusTicket");
            query = "INSERT INTO APP.BUSTICKET VALUES(" + index + ", " + ticket.Id + ", " + tv.BusId + ", '" + format.format(currentDate.getTime())+ "')";
            stmt.executeUpdate(query);
            
            query = "SELECT COUNT(TICKETID) AS COUNTRESULT FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.STATUS=0 AND APP.USERTICKETS.USERID="+tv.UserId+" AND APP.TICKETS.TYPE='"+tv.Type+"'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                index = rs.getInt("COUNTRESULT");
            }
            
            conn.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return "-1";
        }
        System.out.println("ok");
        return Integer.toString(index);
    }
    
    
}
