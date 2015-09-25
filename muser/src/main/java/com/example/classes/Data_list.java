package com.example.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Data_list implements Serializable
{

	ArrayList<Chat_data> list;

	public Data_list(ArrayList<Chat_data> listData)
	{

		super();

		this.list = listData;

	}

	public ArrayList<Chat_data> getList()
	{
		return list;
	}

}
