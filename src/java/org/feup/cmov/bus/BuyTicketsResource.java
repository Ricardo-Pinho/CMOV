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
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.feup.cmov.bus.Ticket;

@Path("BuyTickets")
public class BuyTicketsResource {
    public BuyTicketsResource() {
    }
   
    @POST
    @Produces("application/json")
    @Consumes("application/json")
    public Tickets Buy(BuyTickets ticket) {
            String index = "";
            String UserTicketIndex="0";
            Tickets bt = new Tickets();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            String query;
            ResultSet rs;
            System.out.println("Begin - "+Integer.toString(ticket.T1No));
            for(int i=0; i<ticket.T1No;i++){      
                UUID uuid = UUID.randomUUID();
                index = uuid.toString();
                
                query = "INSERT INTO APP.Tickets VALUES('" + index + "', 'T1' )";
                stmt.executeUpdate(query);
                
                
                uuid = UUID.randomUUID();
                UserTicketIndex = uuid.toString();
                
                query = "INSERT INTO APP.USERTICKETS VALUES('" + UserTicketIndex + "', '"+ticket.ID+"', '"+index+"', 0 )";
                stmt.executeUpdate(query);
                Ticket newticket= new Ticket();
                newticket.Id = index;
                newticket.State=0;
                newticket.Type="T1";
                newticket.UserId=ticket.ID;
                bt.Tickets.add(newticket);
            }
            
            for(int i=0; i<ticket.T2No;i++){
                UUID uuid = UUID.randomUUID();
                index = uuid.toString();
                
                query = "INSERT INTO APP.Tickets VALUES('" + index + "', 'T2' )";
                stmt.executeUpdate(query);
                
                uuid = UUID.randomUUID();
                UserTicketIndex = uuid.toString();
                
                query = "INSERT INTO APP.USERTICKETS VALUES('" + UserTicketIndex + "', '"+ticket.ID+"', '"+index+"', 0 )";
                stmt.executeUpdate(query);
                Ticket newticket= new Ticket();
                newticket.Id = index;
                newticket.State=0;
                newticket.Type="T2";
                newticket.UserId=ticket.ID;
                bt.Tickets.add(newticket);
            }
            
            for(int i=0; i<ticket.T3No;i++){
                UUID uuid = UUID.randomUUID();
                index = uuid.toString();
                
                query = "INSERT INTO APP.Tickets VALUES('" + index + "', 'T3' )";
                stmt.executeUpdate(query);
                             
                uuid = UUID.randomUUID();
                UserTicketIndex = uuid.toString();
                
                query = "INSERT INTO APP.USERTICKETS VALUES('" + UserTicketIndex + "', '"+ticket.ID+"', '"+index+"', 0 )";
                stmt.executeUpdate(query);
                Ticket newticket= new Ticket();
                newticket.Id = index;
                newticket.State=0;
                newticket.Type="T3";
                newticket.UserId=ticket.ID;
                bt.Tickets.add(newticket);
            }
            int totalNumTickets = ticket.T1No+ticket.T2No+ticket.T3No;
            if(totalNumTickets>9)
            {
                if(ticket.T1No>0)
                {
                    UUID uuid = UUID.randomUUID();
                    index = uuid.toString();
                
                    query = "INSERT INTO APP.Tickets VALUES('" + index + "', 'T1' )";
                    stmt.executeUpdate(query);

                    uuid = UUID.randomUUID();
                    UserTicketIndex = uuid.toString();
                
                    query = "INSERT INTO APP.USERTICKETS VALUES('" + UserTicketIndex + "', '"+ticket.ID+"', '"+index+"', 0 )";
                    stmt.executeUpdate(query);
                    Ticket newticket= new Ticket();
                    newticket.Id = index;
                    newticket.State=0;
                    newticket.Type="T1";
                    newticket.UserId=ticket.ID;
                    bt.Tickets.add(newticket);
                }
                else if(ticket.T2No>0)
                {
                    UUID uuid = UUID.randomUUID();
                    index = uuid.toString();
                
                    query = "INSERT INTO APP.Tickets VALUES('" + index + "', 'T2' )";
                    stmt.executeUpdate(query);

                    uuid = UUID.randomUUID();
                    UserTicketIndex = uuid.toString();
                
                    query = "INSERT INTO APP.USERTICKETS VALUES('" + UserTicketIndex + "', '"+ticket.ID+"', '"+index+"', 0 )";
                    stmt.executeUpdate(query);
                    Ticket newticket= new Ticket();
                    newticket.Id = index;
                    newticket.State=0;
                    newticket.Type="T1";
                    newticket.UserId=ticket.ID;
                    bt.Tickets.add(newticket);
                }
                else
                {
                    UUID uuid = UUID.randomUUID();
                    index = uuid.toString();
                    
                    query = "INSERT INTO APP.Tickets VALUES(" + index + ", 'T3' )";
                    stmt.executeUpdate(query);

                    uuid = UUID.randomUUID();
                    UserTicketIndex = uuid.toString();
                
                    query = "INSERT INTO APP.USERTICKETS VALUES('" + UserTicketIndex + "', '"+ticket.ID+"', '"+index+"', 0 )";
                    stmt.executeUpdate(query);
                    Ticket newticket= new Ticket();
                    newticket.Id = index;
                    newticket.State=0;
                    newticket.Type="T1";
                    newticket.UserId=ticket.ID;
                    bt.Tickets.add(newticket);
                }
            }
            
            
            System.out.println("End Tickets");
            double totalValue=ticket.T1No*Ticket.T1price+ticket.T2No*Ticket.T2price+ticket.T3No*Ticket.T3price;
            
            UUID uuid = UUID.randomUUID();
            index = uuid.toString();
                
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd"); //format it as per your requirement
            String dateNow = formatter.format(currentDate.getTime());
            query = "INSERT INTO APP.PURCHASES VALUES('" + index + "', '"+ticket.ID+"', "+totalValue+", '"+dateNow+"' )";
            stmt.executeUpdate(query);
            
            conn.close();
        } catch (Exception e){
            System.out.println("error");
        }
        System.out.println("ok");
        return bt;
    }
}
