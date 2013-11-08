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


@Path("AvailableTickets")
public class AvailableTicketsResource {
    public AvailableTicketsResource() {
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Tickets getAvailableTickets(@PathParam("id") String id) {
        Tickets at = new Tickets();
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            System.out.println(id);
            String query = "SELECT * FROM APP.USERTICKETS WHERE USERID = '" + id+"' AND STATUS=0 AND TICKETID IN (SELECT ID FROM APP.TICKETS WHERE TYPE='T1')";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                Ticket newticket = new Ticket();
                newticket.Id=rs.getString("TICKETID");
                newticket.Type="T1";
                newticket.State=0;
                newticket.UserId=rs.getString("USERID");
                at.Tickets.add(newticket);
            }
            System.out.println("end");
            query = "SELECT * FROM APP.USERTICKETS WHERE USERID = '" + id+"' AND STATUS=0 AND TICKETID IN (SELECT ID FROM APP.TICKETS WHERE TYPE='T2')";
            rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                Ticket newticket = new Ticket();
                newticket.Id=rs.getString("TICKETID");
                newticket.Type="T2";
                newticket.State=0;
                newticket.UserId=rs.getString("USERID");
                at.Tickets.add(newticket);
            }
            
            query = "SELECT * FROM APP.USERTICKETS WHERE USERID = '" + id+"' AND STATUS=0 AND TICKETID IN (SELECT ID FROM APP.TICKETS WHERE TYPE='T3')";
            rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                Ticket newticket = new Ticket();
                newticket.Id=rs.getString("TICKETID");
                newticket.Type="T3";
                newticket.State=0;
                newticket.UserId=rs.getString("USERID");
                at.Tickets.add(newticket);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());  // to the app server log
        }
        Calendar currentDate = Calendar.getInstance(); //Get the current date
        currentDate.add(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
        at.SyncDate = format.format(currentDate.getTime());
        System.out.println(at.SyncDate);
        return at;
    }
}
