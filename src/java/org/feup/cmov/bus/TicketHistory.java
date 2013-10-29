/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


@XmlRootElement
public class TicketHistory {
    public ArrayList<String> Tickets = new ArrayList();
    
    public TicketHistory() {
    }
    
    
    public TicketHistory(ArrayList<String> tickets) {
        this.Tickets=tickets;       
    }
}
