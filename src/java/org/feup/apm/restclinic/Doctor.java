/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.apm.restclinic;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Miguel Monteiro
 */
@XmlRootElement
public class Doctor {
    public int ClinicNr;
    public String Name;
    
    public Doctor() {
    }
    
    public Doctor(int nr, String name) {
        ClinicNr = nr;
        Name = name;
    }
}