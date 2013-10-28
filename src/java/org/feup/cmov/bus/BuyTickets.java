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
public class BuyTickets {
    
    public int ID;
    public int T1No;
    public int T2No;
    public int T3No;
    
    public BuyTickets() {
    }
    
    public BuyTickets(int ID, int T1No, int T2No, int T3No) {
        this.ID=ID;
        this.T1No=T1No;
        this.T2No=T2No;
        this.T3No=T3No;
    }
}
