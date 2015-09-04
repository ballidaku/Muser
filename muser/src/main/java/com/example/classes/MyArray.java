package com.example.classes;

import java.util.ArrayList;

public class MyArray
{
	String name/*, number*/;
	ArrayList<String> emails;



	public MyArray(String name/*,String number*/,ArrayList<String> emails)
	{
		this.name = name;
		/*this.number = number;*/
		this.emails = emails;
	}

/*	public String getNumber()
	{
		return number;
	}*/

	public ArrayList<String> getEmails()
	{
		return emails;
	}
	
	public String getName()
	{
		return name;
	}

	

}
