/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("TicketHistory")
public class TicketHistoryResource {
    public TicketHistoryResource() {
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Tickets getTicketHistory(@PathParam("id") String id) {
        Tickets th = new Tickets();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            System.out.println("Try Scone"+id);
            String query = "SELECT * FROM APP.BUSTICKET,APP.TICKETS WHERE APP.BUSTICKET.TICKETID=APP.TICKETS.ID AND APP.TICKETS.ID IN (SELECT TICKETID FROM APP.USERTICKETS WHERE USERID = '"+ id +"' AND STATUS=1)";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                Ticket newticket = new Ticket();
                newticket.ValidatedTime = rs.getString("VALIDATIONDATE");
                //date=date.substring(0,date.length()-5);
                newticket.Type = rs.getString("TYPE");
                newticket.BusId = rs.getInt("BUSID");
                newticket.UserId = id;
                newticket.Id= rs.getString("TICKETID");
                newticket.State = 1;
                th.Tickets.add(newticket);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());  // to the app server log
        }
        return th;
    }
}
