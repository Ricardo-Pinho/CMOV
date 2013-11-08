package com.feup.inspector;

import java.util.Calendar;

public class Tickets {
	public String Id;
	public String UserId;
	public int BusId;
	public Calendar ValidatedTime;
	public String Type;
	public String Nickname;
	public int Verified;
	public int counter;
	
	public Tickets()
	{
		Id="-1";
		BusId=-1;
		ValidatedTime= Calendar.getInstance();
		Type="Error";
		Nickname="Error";
		Verified=-1;
		counter=1;
	}
	
	public Tickets(String Id, int BusId, Calendar ValidatedTime, String Type, String UserId, String Nickname)
	{
		this.Id=Id;
		this.BusId=BusId;
		this.ValidatedTime= ValidatedTime;
		this.Type=Type;
		this.UserId=UserId;
		this.Nickname=Nickname;
		Verified=-1;
		counter=1;
	}
	
	
}
