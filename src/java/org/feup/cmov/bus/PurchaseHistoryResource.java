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


@Path("PurchaseHistory")
public class PurchaseHistoryResource {
    public PurchaseHistoryResource() {
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public PurchaseHistory getPurchaseHistory(@PathParam("id") String id) {
        PurchaseHistory ph = new PurchaseHistory();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            String query = "SELECT Value, Date FROM APP.Purchases WHERE USERID = '" + id+"'";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                String value = rs.getString("Value");
                String date = rs.getString("Date");
                String concat = value+";"+date;
                System.out.println(concat);
                ph.Purchase.add(concat);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());  // to the app server log
        }
        return ph;
    }
}
