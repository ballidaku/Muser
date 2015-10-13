package com.example.classes;

import java.util.ArrayList;

public class MyArray
{
    String            name/*, number*/;
    ArrayList<String> emails;
    String            is_checked;

    public MyArray(String name/*,String number*/, ArrayList<String> emails, String is_checked)
    {
        this.name = name;
        /*this.number = number;*/
        this.emails = emails;
        this.is_checked = is_checked;
    }

/*	public String getNumber()
	{
		return number;
	}*/

    public String getIs_checked()
    {
        return is_checked;
    }


    public void setIs_checked(String s)
    {
         is_checked=s;
    }



    public ArrayList<String> getEmails()
    {
        return emails;
    }

    public String getName()
    {
        return name;
    }

}
