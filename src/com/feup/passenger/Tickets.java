package com.feup.passenger;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Tickets implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int BusId, Status;
	public Calendar ValidatedTime;
	public String Type, Id, UserId;
	//public boolean visited=false;
	public Tickets()
	{
		Id="-1";
		BusId=-1;
		UserId="-1";
		Type="Error";
		Status=0;
	}
	
	public Tickets(String Id, int BusId, Calendar ValidatedTime, String Type, String UserId, int Status)
	{
		this.Id=Id;
		this.BusId=BusId;
		this.ValidatedTime= ValidatedTime;
		this.Type=Type;
		this.UserId=UserId;
		this.Status=Status;
	}
}
