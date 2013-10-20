/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.apm.restclinic;

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

/**
 * REST Web Service
 *
 * @author Miguel Monteiro
 */
@Path("Docs")
public class DocResource {
    public DocResource() {
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Doctor getDoctor(@PathParam("id") String id) {
        Doctor doc = new Doctor(0, "Error");
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/ClinicData";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            String query = "SELECT ClinicNr, Name FROM APP.Doctors WHERE Id = " + id;
            ResultSet rs = stmt.executeQuery(query);

            if ( rs.next() ) {
                int nr = rs.getInt("ClinicNr");
                String name = rs.getString("Name");
                doc = new Doctor(nr, name);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());  // to the app server log
        }
        return doc;
    }
    
    @POST
    @Produces("text/plain")
    @Consumes("application/json")
    public String addDoctor(Doctor doc) {
        int index = 0;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/ClinicData";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            String query = "SELECT MAX(Id) FROM APP.Doctors";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                index = rs.getInt(1) + 1;
            }
            query = "INSERT INTO APP.Doctors VALUES(" + index + ", " + doc.ClinicNr + ", '" + doc.Name +"')";
            stmt.executeUpdate(query);
            conn.close();
        } catch (Exception e){
            System.out.println(e.getMessage());  // to the app server log
        }
        return new Integer(index).toString();
    }
}
