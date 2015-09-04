package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ProgressTask.Set_Block_Follow_Users_ProgressTask;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.ameba.muser.Drawer;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

public class Followers_Adapter extends BaseAdapter
{
	Context con;
	ArrayList<HashMap<String, String>> list;
	SharedPreferences rem_pref;

	public Followers_Adapter(Context con,ArrayList<HashMap<String, String>> list) 
	{
		this.con = con;
		this.list=list;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
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
	public View getView(final int position, View row, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		row = inflater.inflate(R.layout.custom_followers, parent, false);
		
		RoundedCornersGaganImg follower_user_image	=(RoundedCornersGaganImg)row.findViewById(R.id.follower_user_image);
		TextView follower_user_name		=(TextView)row.findViewById(R.id.follower_user_name);
		ImageView follow	=(ImageView)row.findViewById(R.id.follow);
		ImageView is_following		=(ImageView)row.findViewById(R.id.is_following);
		ImageView requested		=(ImageView)row.findViewById(R.id.requested);
		LinearLayout click_me		=(LinearLayout)row.findViewById(R.id.click_me);
		
		follower_user_image.setImageUrl(con, list.get(position).get("profile_image"));
		
//		Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), follower_user_image, Drawer.options);
		
		//follower_user_image.setImageUrl(list.get(position).get("profile_image"));
				
		follower_user_name.setText(list.get(position).get("user_name"));
		
		
		if(list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")))
		{
			follow.setVisibility(View.GONE);
			is_following.setVisibility(View.GONE);
			requested.setVisibility(View.GONE);
		}
		else if(list.get(position).get("is_following").equals("Y"))
		{
			follow.setVisibility(View.GONE);
			is_following.setVisibility(View.VISIBLE);
			requested.setVisibility(View.GONE);
		}
		else if(list.get(position).get("is_following").equals("R"))
		{
			follow.setVisibility(View.GONE);
			is_following.setVisibility(View.GONE);
			requested.setVisibility(View.VISIBLE);
		}
		else
		{
			follow.setVisibility(View.VISIBLE);
			is_following.setVisibility(View.GONE);
			requested.setVisibility(View.GONE);
		}
		
		follow.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				HashMap<String, String> map2 = new HashMap<String, String>();
				map2.put("user_id",list.get(position).get("user_id"));
				new Set_Block_Follow_Users_ProgressTask(con, map2,"F","Followers_Adapter").execute();

			}
		});
		
		
		
		
		click_me.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				
			/*	Global.set_user_id(list.get(position).get("user_id"));
				Global.set_friend_id(rem_pref.getString("user_id", ""));
				
				Intent i=new Intent(con,Other_Profile.class);
				con.startActivity(i);*/
				
				if(list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")))
				{
					
				}
				else
				{
					Global.set_user_id(list.get(position).get("user_id"));
					Global.set_friend_id(rem_pref.getString("user_id", ""));
					
					Intent i=new Intent(con,Other_Profile.class);
					//i.putExtra("user_id", list.get(position).get("user_id"));
					con.startActivity(i);
				}

			}
		});
		 
		
		
		return row;
	}
	
	public void add_data(ArrayList<HashMap<String, String>> list)
	{
		this.list=list;
	}

}
