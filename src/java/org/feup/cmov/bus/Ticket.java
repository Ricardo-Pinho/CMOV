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
    public final int INVALID=2;
    public final static double T1price=0.50;
    public final static double T2price=1.00;
    public final static double T3price=1.50;
    public int Id, State, BusId,UserId;
    public String ValidatedTime;
    public String Type;
    public String Username;
    //public static int Idcounter = 1;
    public Ticket() {
        Id=-1;
        BusId=-1;
        ValidatedTime= "";
        Type="Error";
    }
    
    public Ticket(int Id, String Type, int UserId) {
        this.Id = Id;
        this.Type=Type;
        this.UserId=UserId;
    }
    
    public Ticket(int Id, int BusId, String ValidatedTime, String Type, int UserId) {
        this.Id = Id;
        this.BusId= BusId;
        this.ValidatedTime = ValidatedTime;
        this.Type=Type;
        this.UserId=UserId;
    }
    
        public Ticket(int Id, int BusId, String ValidatedTime, String Type, int UserId, String Username) {
        this.Id = Id;
        this.BusId= BusId;
        this.ValidatedTime = ValidatedTime;
        this.Type=Type;
        this.UserId=UserId;
        this.Username=Username;
    }
}