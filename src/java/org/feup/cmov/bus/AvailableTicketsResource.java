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


@Path("AvailableTickets")
public class AvailableTicketsResource {
    public AvailableTicketsResource() {
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public AvailableTickets getAvailableTickets(@PathParam("id") String id) {
        AvailableTickets at = new AvailableTickets();
        int T1No=0;
        int T2No=0;
        int T3No=0;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            System.out.println(id);
            String query = "SELECT COUNT(*) AS TICKETNO FROM APP.USERTICKETS WHERE USERID = " + id+" AND STATUS=0 AND TICKETID IN (SELECT ID FROM APP.TICKETS WHERE TYPE='T1')";
            ResultSet rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                T1No= rs.getInt("TICKETNO");
            }
            System.out.println("end");
            query = "SELECT COUNT(*) AS TICKETNO FROM APP.USERTICKETS WHERE USERID = " + id+" AND STATUS=0 AND TICKETID IN (SELECT ID FROM APP.TICKETS WHERE TYPE='T2')";
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                T2No= rs.getInt("TICKETNO");
            }
            
            query = "SELECT COUNT(*) AS TICKETNO FROM APP.USERTICKETS WHERE USERID = " + id+" AND STATUS=0 AND TICKETID IN (SELECT ID FROM APP.TICKETS WHERE TYPE='T3')";
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                T3No= rs.getInt("TICKETNO");
            }
            at.Tickets=Integer.toString(T1No)+";"+Integer.toString(T2No)+";"+Integer.toString(T3No);
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());  // to the app server log
            at.Tickets="Error";
        }
        System.out.println(at.Tickets);
        return at;
    }
}
