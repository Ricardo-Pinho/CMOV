package com.feup.validator;

import java.util.Date;

public class Tickets {
	public int Id;
	public int BusId, UserId;
	public Date ValidatedTime;
	public String Type;
	
	public Tickets()
	{
		Id=-1;
		BusId=-1;
		ValidatedTime= new Date();
		Type="Error";
	}
	
	public Tickets(int Id, int BusId, Date ValidatedTime, String Type, int UserId)
	{
		this.Id=Id;
		this.BusId=BusId;
		this.ValidatedTime= ValidatedTime;
		this.Type=Type;
		this.UserId=UserId;
	}
	
	public Tickets(int Id, String Type, int UserId)
	{
		this.Id=Id;
		this.BusId=-1;
		this.ValidatedTime= new Date();
		this.Type=Type;
		this.UserId=UserId;
	}
	
	
}
