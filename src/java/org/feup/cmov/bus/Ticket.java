/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;


@XmlRootElement
public class Ticket {
    public final int NOTVALIDATED=0;
    public final int VALIDATED=1;
    public final int USED=2;
    public final int INVALID=3;
    public int Id, State, busId;
    public Date validationDate;
    //public static int Idcounter = 1;
    public Ticket() {
        State = NOTVALIDATED;
    }
    
    /*public Ticket(int nr, String name) {
        ClinicNr = nr;
        Name = name;
    }*/
}