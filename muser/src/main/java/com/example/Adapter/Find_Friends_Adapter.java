package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ProgressTask.Set_Block_Follow_Users_ProgressTask;
import com.example.classes.RoundedCornersGaganImg;
import com.ameba.muser.R;

public class Find_Friends_Adapter extends BaseAdapter
{
	
	Context con;
	Fragment fragment;
	ArrayList<HashMap<String, String>> list;

	public Find_Friends_Adapter(Context con,Fragment fragment ,ArrayList<HashMap<String, String>> list)
	{
		this.con = con;
		this.fragment=fragment;
		this.list=list;
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
		
		row = inflater.inflate(R.layout.custom_find_friends, parent, false);
		
		RoundedCornersGaganImg profile_image=(RoundedCornersGaganImg)row.findViewById(R.id.profile_image);
		TextView user_name=(TextView)row.findViewById(R.id.user_name);
		ImageView follow=(ImageView)row.findViewById(R.id.follow);
		
		profile_image.setImageUrl(con, list.get(position).get("profile_image"));
		
//		Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), profile_image, Drawer.options);
		
		user_name.setText(list.get(position).get("user_name"));
		
		
		follow.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				new Set_Block_Follow_Users_ProgressTask(con,fragment, list.get(position),"F","Find_Friends").execute();
			
			}
		});
		
		
		return row;
	}

}
