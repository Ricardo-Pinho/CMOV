/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.feup.cmov.bus;

import javax.xml.bind.annotation.XmlRootElement;
import java.security.MessageDigest;
import java.util.Formatter;
import java.security.NoSuchAlgorithmException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;


@XmlRootElement
public class User {
    public int CcardNumber;
    public String Name, Username, Password, CcardType, CcardValidation;
    public HashMap<Integer,ArrayList<Ticket>> tickets = new HashMap();
    
    public User() {
    }
     
     public User(String err) {
        Name=err;
    }
    
    
    public User(String name, String username, String password, String ccardType, int ccardNumber, String ccardValidation) {
        Name = name;
        Username=username;
        Password=password;
        CcardType=ccardType;
        CcardNumber=ccardNumber;
        CcardValidation=ccardValidation;
        ArrayList <Ticket> EmptyTickets = new ArrayList ();
        tickets.put(1, EmptyTickets);
        tickets.put(2, EmptyTickets);
        tickets.put(3, EmptyTickets);
    }
    
    public String encryptPassword(String password)
    {
        String sha1 = "";
        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        }
        catch(NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch(UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return sha1;
    }

    public String byteToHex(final byte[] hash)
    {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
