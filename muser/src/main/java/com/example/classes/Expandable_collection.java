package com.example.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Expandable_collection
{

	String id,name;
	
	ArrayList<HashMap<String, String>> child_list;

	
	
	
	public Expandable_collection(String id,String name,ArrayList<HashMap<String, String>> child_list)
	{
		this.name = name;
		this.id = id;
		this.child_list=child_list;
		
	}
	
	public String getId()
	{
		return id;
	}

	public ArrayList<HashMap<String, String>> get_child_list()
	{
		return child_list;
	}

	public String getName()
	{
		return name;
	}
	


}
