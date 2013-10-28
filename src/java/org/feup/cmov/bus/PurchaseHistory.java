/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;


@XmlRootElement
public class PurchaseHistory {
    public ArrayList<String> Purchase = new ArrayList();
    
    public PurchaseHistory() {
    }
    
    
    public PurchaseHistory(ArrayList<String> purchase) {
        this.Purchase=purchase;       
    }
}
