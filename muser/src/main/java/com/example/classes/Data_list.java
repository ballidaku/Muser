package com.example.classes;

import java.io.Serializable;
import java.util.List;

public class Data_list implements Serializable
{

	List<Chat_data> list;

	public Data_list(List<Chat_data> listData)
	{

		super();

		this.list = listData;

	}

	public List<Chat_data> getList()
	{
		return list;
	}

}
