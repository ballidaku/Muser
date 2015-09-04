package com.example.Adapter;

import com.ameba.muser.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Home_Pictures_Adapter extends BaseAdapter
{
	Context con;

	public Home_Pictures_Adapter(Context con) 
	{
		this.con = con;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View row, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		row = inflater.inflate(R.layout.custom_home_pictures, parent, false);
		
		return row;
	}

}
