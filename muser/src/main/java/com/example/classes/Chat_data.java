package com.example.classes;

import java.io.Serializable;

public class Chat_data implements Serializable
{

	private String friend_id, message, added_date,message_id;

	public Chat_data(String friend_id, String message, String added_date)
	{
		super();
		this.friend_id = friend_id;
		this.added_date = added_date;
		this.message = message;

	}

	public Chat_data(String friend_id, String message,String message_id, String added_date)
	{
		super();
		this.friend_id = friend_id;
		this.added_date = added_date;
		this.message = message;
		this.message_id=message_id;

	}
	public void set_message_id(String message_id)
	{
		this.message_id=message_id;
	}

	public String getFriend_id()
	{
		return friend_id;
	}

	public String getMessage()
	{
		return message;
	}
	public String getMessageID()
	{
		return message_id;
	}

	public String getAdded_date()
	{
		return added_date;
	}

}
