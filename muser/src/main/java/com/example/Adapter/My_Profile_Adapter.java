package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ameba.muser.R;

public class My_Profile_Adapter extends BaseAdapter
{
	Context con;

	public My_Profile_Adapter(Context con) 
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
		
		row = inflater.inflate(R.layout.custom_my_profile, parent, false);
		
		return row;
	}

}
