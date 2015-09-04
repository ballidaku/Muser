package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classes.RoundedCornersGaganImg;
import com.ameba.muser.Drawer;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.R;
import com.ameba.muser.Search_Hashtags_Child;

public class Search_Hashtags_Child_Adapter extends BaseAdapter 
{

	private LayoutInflater inflator;
	private Context con;
	ArrayList<HashMap<String, String>> hashtags_child_list;


	public Search_Hashtags_Child_Adapter(Context con, ArrayList<HashMap<String, String>> hashtags_child_list) 
	{

		this.con = con;
		inflator = LayoutInflater.from(con);
		this.hashtags_child_list=hashtags_child_list;
	}

	@Override
	public int getCount() {
		return hashtags_child_list.size();

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

		row = inflator.inflate(R.layout.custom_search_hashtags_child,null);
		
		RoundedCornersGaganImg hashtag_image=(RoundedCornersGaganImg)row.findViewById(R.id.hashtag_image);
		
		if(hashtags_child_list.get(position).get("data_type").equals("I"))
		{
			
			hashtag_image.setImageUrl(con, hashtags_child_list.get(position).get("data"));
//			Drawer.imageLoader.displayImage(hashtags_child_list.get(position).get("data"), hashtag_image, Drawer.options);
		}
		else
		{
			hashtag_image.setImageUrl(con, hashtags_child_list.get(position).get("thumbnail"));
//			Drawer.imageLoader.displayImage(hashtags_child_list.get(position).get("thumbnail"), hashtag_image, Drawer.options);
		}
		
		
		
		row.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				Intent i=new Intent(con,Image_Video_Details.class);
				i.putExtra("post_id",hashtags_child_list.get(position).get("post_id"));
				i.putExtra("user_id",hashtags_child_list.get(position).get("user_id"));
				i.putExtra("from_where", "serch_tags");
				con.startActivity(i);
			}
		});
		
		
		
		
		

		return row;
	}

}