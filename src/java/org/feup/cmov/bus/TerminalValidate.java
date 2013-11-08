/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.feup.cmov.bus;

/**
 *
 * @author Ricardo
 */
public class TerminalValidate {
    public String UserId;
    public String Type;
    public int BusId;
    
    public TerminalValidate()
    {
        UserId="-1";
        BusId=-1;
        Type="";
    }
    
        public TerminalValidate(String UserId, int BusId)
    {
        this.UserId=UserId;
        this.BusId=BusId;
    }
    
    public TerminalValidate(String UserId, String Type, int BusId)
    {
        this.UserId=UserId;
        this.Type=Type;
        this.BusId=BusId;
    }
}
