/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


@XmlRootElement
public class Tickets {
    public ArrayList<Ticket> Tickets = new ArrayList();
    public String SyncDate = "";
    
    public Tickets() {
    }
    
    
    public Tickets(ArrayList<Ticket> tickets) {
        this.Tickets=tickets;       
    }
}
