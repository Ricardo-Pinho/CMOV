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


@Path("Tickets")
public class TicketResource {
    public TicketResource() {
    }

    /*@GET
    @Path("{id}")
    @Produces("application/json")
    public User getUser(@PathParam("id") String id) {
        User usr = new User("Error");
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            String query = "SELECT * FROM APP.Users WHERE Id = " + id;
            ResultSet rs = stmt.executeQuery(query);

            if ( rs.next() ) {
                String name = rs.getString("Name");
                String username = rs.getString("Username");
                String password = rs.getString("Password");
                int cardNo = rs.getInt("CcardNumber");
                String cardType = rs.getString("CcardType");
                String cardValid = rs.getString("CcardValidation");
                usr = new User(name, username, password, cardType, cardNo, cardValid);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());  // to the app server log
        }
        System.out.println(usr.Name);
        return usr;
    }*/
    
    /*@POST
    @Produces("text/plain")
    @Consumes("application/json")
    public String addUser(User usr) {
        int index = 0;
        System.out.println("ola");
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            String query = "SELECT MAX(Id) FROM APP.Users";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                index = rs.getInt(1) + 1;
            }
            query = "INSERT INTO APP.Users VALUES(" + index + ", '" + usr.Name + "', '" + usr.Username + "', '" + usr.CcardType+ "', " + usr.CcardNumber + ", '" + usr.CcardValidation+ "', '" + usr.encryptPassword(usr.Password) +"')";
            stmt.executeUpdate(query);
            conn.close();
        } catch (Exception e){
            return "error";
        }
        System.out.println("ok");
        return "ok";
    }*/
}
