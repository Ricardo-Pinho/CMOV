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
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.joda.time.DateTime;
import org.joda.time.Minutes;


@Path("Tickets")
public class TicketResource {
    public TicketResource() {
    }
    
    @GET
    @Path("GetTickets/{busid}/{key}")
    @Produces("application/json")
    public Tickets getBusValidatedTickets(@PathParam("busid") String BusId, @PathParam("key") String key) {
        Tickets tickets = new Tickets();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.DAY_OF_MONTH, -1);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            System.out.println(format.format(currentDate.getTime()));
            
            String query = "SELECT UT.TICKETID,BUSID,TYPE,USERID,USERNAME, VALIDATIONDATE FROM APP.TICKETS, APP.USERTICKETS AS UT, APP.BUSTICKET, APP.USERS WHERE APP.USERS.ID=UT.USERID AND APP.TICKETS.ID=UT.TICKETID AND UT.TICKETID=APP.BUSTICKET.TICKETID AND APP.BUSTICKET.BUSID="+ BusId +" AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND UT.STATUS=1";
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                String date= rs.getString("VALIDATIONDATE");
                date = date.substring(0,date.length()-5);
                Ticket newTicket = new Ticket(rs.getString("TICKETID"), rs.getInt("BUSID"), date, rs.getString("TYPE"), rs.getString("USERID"), rs.getString("USERNAME"));
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
    @Path("GetValidTickets/{busid}/{key}")
    @Produces("application/json")
    public Tickets getValidatedTickets(@PathParam("busid") String busid, @PathParam("key") String key) {
        Tickets tickets = new Tickets();
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.HOUR_OF_DAY, -1);
            currentDate.add(Calendar.MINUTE, -30);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            System.out.println(format.format(currentDate.getTime()));
            
            String query = "SELECT UT.TICKETID,BUSID,TYPE,USERID,USERNAME, VALIDATIONDATE FROM APP.TICKETS, APP.USERTICKETS AS UT, APP.BUSTICKET, APP.USERS WHERE APP.USERS.ID=UT.USERID AND APP.TICKETS.ID=UT.TICKETID AND UT.TICKETID=APP.BUSTICKET.TICKETID AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND UT.STATUS=1 AND APP.BUSTICKET.BUSID="+busid;
            ResultSet rs = stmt.executeQuery(query);
            while ( rs.next() ) {
                String date= rs.getString("VALIDATIONDATE");
                date = date.substring(0,date.length()-5);
                Ticket newTicket = new Ticket(rs.getString("TICKETID"), rs.getInt("BUSID"), date, rs.getString("TYPE"), rs.getString("USERID"), rs.getString("USERNAME"));
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
        String index = "0";
        int counter=0;
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
            Ticket ticket = new Ticket();
           
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.MINUTE, -15);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            System.out.println(format.format(currentDate.getTime()));
            
            System.out.println("Check if there is already a validated ticket");
            
            String query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T1' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ tv.UserId+"' AND APP.BUSTICKET.BUSID="+tv.BusId;
            ResultSet rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                currentDate = Calendar.getInstance();
                Calendar validate = Calendar.getInstance();
                validate.setTime(format.parse(rs.getString("VALIDATIONDATE")));
                DateTime dtime1 = new DateTime(currentDate);
                DateTime dtime2 = new DateTime(validate);
                int diff = 15 - (Minutes.minutesBetween(dtime2,dtime1).getMinutes());
                return "-3;"+Integer.toString(diff);
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.MINUTE, -30);
            System.out.println(format.format(currentDate.getTime()));
            
            query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T2' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ tv.UserId+"' AND APP.BUSTICKET.BUSID="+tv.BusId;
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                currentDate = Calendar.getInstance();
                Calendar validate = Calendar.getInstance();
                validate.setTime(format.parse(rs.getString("VALIDATIONDATE")));
                DateTime dtime1 = new DateTime(currentDate);
                DateTime dtime2 = new DateTime(validate);
                int diff = 30 - (Minutes.minutesBetween(dtime2,dtime1).getMinutes());
                return "-3;"+Integer.toString(diff);
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.HOUR_OF_DAY, -1);
            System.out.println(format.format(currentDate.getTime()));
            
            query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T3' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ tv.UserId+"' AND APP.BUSTICKET.BUSID="+tv.BusId;
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                currentDate = Calendar.getInstance();
                Calendar validate = Calendar.getInstance();
                validate.setTime(format.parse(rs.getString("VALIDATIONDATE")));
                DateTime dtime1 = new DateTime(currentDate);
                DateTime dtime2 = new DateTime(validate);
                int diff = 60 - (Minutes.minutesBetween(dtime2,dtime1).getMinutes());
                return "-3;"+Integer.toString(diff);
            }
            
            System.out.println("Checking Available Tickets");
            
            query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.STATUS=0 AND APP.USERTICKETS.USERID='"+tv.UserId+"' AND APP.TICKETS.TYPE='"+tv.Type+"'";
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                ticket.UserId = rs.getString("USERID");
                ticket.Id= rs.getString("TICKETID");
                ticket.Type = rs.getString("TYPE");
                ticket.State=1;
            }
            else
            {
                return "-2;0";
            }
            System.out.println("Updating Usertickets");
            
            query = "UPDATE APP.USERTICKETS SET STATUS=1 WHERE TICKETID='"+ticket.Id+"'";
            stmt.executeUpdate(query);
            
            
            UUID uuid = UUID.randomUUID();
            index = uuid.toString();
           
            currentDate = Calendar.getInstance(); //Get the current date
            System.out.println("Inserting in BusTicket");
            query = "INSERT INTO APP.BUSTICKET VALUES('" + index + "', '" + ticket.Id + "', " + tv.BusId + ", '" + format.format(currentDate.getTime())+ "')";
            stmt.executeUpdate(query);
            
            query = "SELECT COUNT(TICKETID) AS COUNTRESULT FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.STATUS=0 AND APP.USERTICKETS.USERID='"+tv.UserId+"' AND APP.TICKETS.TYPE='"+tv.Type+"'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                counter = rs.getInt("COUNTRESULT");
            }
            
            conn.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return "-1;0";
        }
        System.out.println("ok");
        return Integer.toString(counter)+";0";
    }
    
    @POST
    @Path("TerminalOfflineValidate/{key}")
    @Produces("text/plain")
    @Consumes("application/json")
    public String terminalOfflineValidate(@PathParam("key") String key, Tickets ts) {
        String index = "0";
        for(int i=0; i<ts.Tickets.size();i++)
            {
            try {
                Class.forName("org.apache.derby.jdbc.ClientDriver");
                String url = "jdbc:derby://localhost:1527/BusDB";
                Connection conn = DriverManager.getConnection(url,"test","test");
                Statement stmt = conn.createStatement();
                Ticket ticket = new Ticket();
                    Calendar currentDate = Calendar.getInstance(); //Get the current date
                    currentDate.add(Calendar.MINUTE, -15);
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
                    System.out.println(format.format(currentDate.getTime()));

                    System.out.println("Check if there is already a validated ticket");

                    String query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T1' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ ts.Tickets.get(i).UserId+"' AND APP.BUSTICKET.BUSID="+ts.Tickets.get(i).BusId;
                    ResultSet rs = stmt.executeQuery(query);
                    if ( rs.next() ) {
                        return "-3";
                    }

                    currentDate = Calendar.getInstance(); //Get the current date
                    currentDate.add(Calendar.MINUTE, -30);
                    System.out.println(format.format(currentDate.getTime()));

                    query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T2' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ ts.Tickets.get(i).UserId+"' AND APP.BUSTICKET.BUSID="+ts.Tickets.get(i).BusId;
                    rs = stmt.executeQuery(query);
                    if ( rs.next() ) {
                        return "-3";
                    }

                    currentDate = Calendar.getInstance(); //Get the current date
                    currentDate.add(Calendar.HOUR_OF_DAY, -1);
                    System.out.println(format.format(currentDate.getTime()));

                    query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T3' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ ts.Tickets.get(i).UserId+"' AND APP.BUSTICKET.BUSID="+ts.Tickets.get(i).BusId;
                    rs = stmt.executeQuery(query);
                    if ( rs.next() ) {
                        return "-3";
                    }

                    System.out.println("Checking Available Tickets");

                    query = "SELECT * FROM APP.TICKETS, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.USERTICKETS.STATUS=0 AND APP.USERTICKETS.USERID='"+ts.Tickets.get(i).UserId+"' AND APP.TICKETS.TYPE='"+ts.Tickets.get(i).Type+"'";
                    rs = stmt.executeQuery(query);
                    if ( rs.next() ) {
                        ticket.UserId = rs.getString("USERID");
                        ticket.Id= rs.getString("TICKETID");
                        ticket.Type = rs.getString("TYPE");
                        ticket.State=1;
                    }
                    else
                    {
                        return "-2";
                    }
                    System.out.println("Updating Usertickets");

                    query = "UPDATE APP.USERTICKETS SET STATUS=1 WHERE TICKETID='"+ticket.Id+"'";
                    stmt.executeUpdate(query);


                    UUID uuid = UUID.randomUUID();
                    index = uuid.toString();

                    currentDate = Calendar.getInstance(); //Get the current date
                    System.out.println("Inserting in BusTicket");
                    query = "INSERT INTO APP.BUSTICKET VALUES('" + index + "', '" + ticket.Id + "', " + ts.Tickets.get(i).BusId + ", '" + format.format(currentDate.getTime())+ "')";
                    stmt.executeUpdate(query);

                    conn.close();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                    return "-1";
                }
        }
        System.out.println("ok");
        return "1";
    }
    
    
    @POST
    @Path("InspectorVerify/{key}")
    @Produces("text/plain")
    @Consumes("application/json")
    public String InspectorVerify(@PathParam("key") String key, TerminalValidate tv) {
        String index = "0";
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            String url = "jdbc:derby://localhost:1527/BusDB";
            Connection conn = DriverManager.getConnection(url,"test","test");
            Statement stmt = conn.createStatement();
           
            Calendar currentDate = Calendar.getInstance(); //Get the current date
            Calendar ValidateDate = Calendar.getInstance(); //Get the current date
            int diff=0;
            DateTime dtime1;
            DateTime dtime2;
            currentDate.add(Calendar.MINUTE, -15);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-M-dd HH:mm:ss");
            
            System.out.println("Check if there is already a validated ticket");
            
            String query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T1' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ tv.UserId+"' AND APP.BUSTICKET.BUSID="+ tv.BusId;
            ResultSet rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                ValidateDate.setTime(format.parse(rs.getString("VALIDATIONDATE")));
                ValidateDate.add(Calendar.MINUTE, 15);
                currentDate = Calendar.getInstance(); //Get the current date
                dtime2= new DateTime(ValidateDate);
                dtime1 = new DateTime(currentDate);
                diff = Minutes.minutesBetween(dtime1, dtime2).getMinutes();
                System.out.println(diff);
                return Integer.toString(diff);
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.MINUTE, -30);
            
            query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T2' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ tv.UserId+"' AND APP.BUSTICKET.BUSID="+ tv.BusId;
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                ValidateDate.setTime(format.parse(rs.getString("VALIDATIONDATE")));
                ValidateDate.add(Calendar.MINUTE, 30);
                currentDate = Calendar.getInstance(); //Get the current date
                dtime2= new DateTime(ValidateDate);
                dtime1 = new DateTime(currentDate);
                diff = Minutes.minutesBetween(dtime1, dtime2).getMinutes();
                System.out.println(diff);
                return Integer.toString(diff);
            }
            
            currentDate = Calendar.getInstance(); //Get the current date
            currentDate.add(Calendar.HOUR_OF_DAY, -1);
            
            query = "SELECT * FROM APP.TICKETS, APP.BUSTICKET, APP.USERTICKETS WHERE APP.TICKETS.ID=APP.BUSTICKET.TICKETID AND APP.TICKETS.ID=APP.USERTICKETS.TICKETID AND APP.TICKETS.TYPE='T3' AND APP.BUSTICKET.VALIDATIONDATE >= '"+format.format(currentDate.getTime())+"' AND APP.USERTICKETS.USERID='"+ tv.UserId+"' AND APP.BUSTICKET.BUSID="+ tv.BusId;
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                ValidateDate.setTime(format.parse(rs.getString("VALIDATIONDATE")));
                ValidateDate.add(Calendar.HOUR_OF_DAY,1);
                currentDate = Calendar.getInstance(); //Get the current date
                dtime2= new DateTime(ValidateDate);
                dtime1 = new DateTime(currentDate);
                diff = Minutes.minutesBetween(dtime1, dtime2).getMinutes();
                System.out.println(diff);
                return Integer.toString(diff);
            }
            
            query = "SELECT * FROM APP.USERTICKETS WHERE APP.USERTICKETS.USERID='"+ tv.UserId+"' AND STATUS=0";
            rs = stmt.executeQuery(query);
            if ( rs.next() ) {
                System.out.println("-2");
                return "-2";
            }
            
            
            
            conn.close();
        } catch (Exception e){
            System.out.println(e.getMessage());
            return "-1";
        }
        System.out.println("-3");
        return "-3";
    }
    
    
}
