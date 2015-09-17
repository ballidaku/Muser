package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.ameba.muser.Drawer;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;
import com.ameba.muser.Search_Hashtags_Child;

public class Search_User_Adapter extends BaseAdapter 
{



	private LayoutInflater inflator;
	private Context con;
	ArrayList<HashMap<String, String>> user_list; 
	SharedPreferences rem_pref;

	public Search_User_Adapter(Context context,ArrayList<HashMap<String, String>> user_list) 
	{

		this.con = context;
		inflator = LayoutInflater.from(context);
		this.user_list=user_list;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	@Override
	public int getCount()
	{
		return user_list.size();

	}

	@Override
	public Object getItem(int position) 
	{
		return position;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	
	@Override
	public View getView(final int position, View row, ViewGroup parent) 
	{

		row = inflator.inflate(R.layout.custom_search_user,null);
				
		RoundedCornersGaganImg image=(RoundedCornersGaganImg)row.findViewById(R.id.image);
		TextView user_name=(TextView)row.findViewById(R.id.user_name);
		
		image.setImageUrl(con, user_list.get(position).get("profile_image"));
//		Drawer.imageLoader.displayImage(user_list.get(position).get("profile_image"), image, Drawer.options);
		user_name.setText(user_list.get(position).get("user_name"));
		
		
		row.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
			
				/*Global.set_user_id(user_list.get(position).get("user_id"));
				Global.set_friend_id(rem_pref.getString("user_id", ""));
				
				Intent i=new Intent(con,Other_Profile.class);
				//i.putExtra("user_id", list.get(position).get("user_id"));
				con.startActivity(i);*/
				
				if(user_list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")))
				{
					((Drawer) con).click();
				}
				else
				{
					Global.set_user_id(user_list.get(position).get("user_id"));
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
		user_list=list;
	}

}
