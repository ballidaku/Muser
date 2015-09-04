package com.example.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameba.muser.R;

public class Trending_Friends_GridView_Adapter extends BaseAdapter
{
	Context con;
	
	private int moreImg[] = {   R.drawable.ic_launcher, 
								R.drawable.ic_launcher,
								R.drawable.ic_launcher,
								R.drawable.ic_launcher, 
								R.drawable.ic_launcher,
								R.drawable.ic_launcher, 
								R.drawable.ic_launcher, 
								R.drawable.ic_launcher };
	

	public Trending_Friends_GridView_Adapter(Context con) 
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
	public View getView(int position, View row, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		row = inflater.inflate(R.layout.custom_trending_friends_gridview, parent, false);
		
	
		
		
		return row;
	}
	

}
