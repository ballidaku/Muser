package com.example.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ProgressTask.Search_Child_ProgressTask;
import com.ameba.muser.R;
import com.ameba.muser.Search_Hashtags_Child;

public class Search_Hashtags_Adapter extends BaseAdapter 
{

	private LayoutInflater inflator;
	private Context con;
	 ArrayList<String>hashtags_list;


	public Search_Hashtags_Adapter(Context con, ArrayList<String>hashtags_list) 
	{

		this.con = con;
		inflator = LayoutInflater.from(con);
		this.hashtags_list=hashtags_list;
	}

	@Override
	public int getCount() {
		return hashtags_list.size();

	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(final int position, View row, ViewGroup parent) 
	{

		row = inflator.inflate(R.layout.custom_search_hashtags,null);
		
		TextView hashtag_text=(TextView)row.findViewById(R.id.hashtag_text);
		
		hashtag_text.setText(hashtags_list.get(position));
		
		row.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
			
				Intent intent=new Intent(con,Search_Hashtags_Child.class);
				intent.putExtra("value", hashtags_list.get(position));
				con.startActivity(intent);
				
			}
		});

		return row;
	}

	public void add_data(ArrayList<String> list)
	{
		hashtags_list=list;
	}

}