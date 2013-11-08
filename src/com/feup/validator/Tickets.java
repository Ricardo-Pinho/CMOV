package com.feup.validator;

import java.util.Calendar;

public class Tickets {
	public String Id, UserId;
	public int BusId;
	public Calendar ValidatedTime;
	public String Type, Nickname;
	
	public Tickets()
	{
		Id="-1";
		BusId=-1;
		ValidatedTime= Calendar.getInstance();
		Type="Error";
		Nickname="Error";
		UserId="-1";
	}
	
	public Tickets(String Id, int BusId, Calendar ValidatedTime, String Type, String UserId, String Nickname)
	{
		this.Id=Id;
		this.BusId=BusId;
		this.ValidatedTime= ValidatedTime;
		this.Type=Type;
		this.UserId=UserId;
		this.Nickname = Nickname;
	}
	
	public Tickets(String Id, String Type, String UserId, String Nickname)
	{
		this.Id=Id;
		this.BusId=-1;
		this.ValidatedTime= Calendar.getInstance();
		this.Type=Type;
		this.UserId=UserId;
		this.Nickname = Nickname;
	}
	
	
}
