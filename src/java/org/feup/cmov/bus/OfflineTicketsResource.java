/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.feup.cmov.bus.Ticket;

@Path("OfflineTickets")
public class OfflineTicketsResource {
    public OfflineTicketsResource() {
    }
   
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Tickets OfflineTickets(BuyTickets ticket) {
            String index = "0";
            Tickets bt = new Tickets();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            String query;
            ResultSet rs;
            for(int i=0; i<ticket.T1No;i++){      
                query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.USERID='"+ticket.ID+"' AND APP.TICKETS.TYPE='T1' AND APP.USERTICKETS.STATUS=0";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    index = rs.getString("TICKETID");
                }
                
                query = "UPDATE APP.USERTICKETS SET APP.USERTICKETS.STATUS=2 WHERE APP.USERTICKETS.TICKETID='"+index+"'";
                stmt.executeUpdate(query);
                Ticket newticket= new Ticket();
                newticket.Id = index;
                bt.Tickets.add(newticket);
                System.out.println("one");
            }
            
            for(int i=0; i<ticket.T2No;i++){
                query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.USERID='"+ticket.ID+"' AND APP.TICKETS.TYPE='T2' AND APP.USERTICKETS.STATUS=0";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    index = rs.getString("TICKETID");
                }
                
                query = "UPDATE APP.USERTICKETS SET APP.USERTICKETS.STATUS=2 WHERE APP.USERTICKETS.TICKETID='"+index+"'";
                stmt.executeUpdate(query);
                Ticket newticket= new Ticket();
                newticket.Id = index;
                bt.Tickets.add(newticket);
                System.out.println("two");
            }
            
            for(int i=0; i<ticket.T3No;i++){
                query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.USERID='"+ticket.ID+"' AND APP.TICKETS.TYPE='T3' AND APP.USERTICKETS.STATUS=0";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    index = rs.getString("TICKETID");
                    System.out.println(index);
                }
                
                query = "UPDATE APP.USERTICKETS SET APP.USERTICKETS.STATUS=2 WHERE APP.USERTICKETS.TICKETID='"+index+"'";
                stmt.executeUpdate(query);
                Ticket newticket= new Ticket();
                newticket.Id = index;
                bt.Tickets.add(newticket);
                System.out.println("three");
            }
            
            conn.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        System.out.println("ok champ");
        return bt;
    }
}
