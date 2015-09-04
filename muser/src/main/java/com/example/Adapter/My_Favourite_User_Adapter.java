package com.example.Adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

public class My_Favourite_User_Adapter extends BaseAdapter
{
	Context con;
	ArrayList<HashMap<String, String>> list;
	SharedPreferences rem_pref;
	
	public My_Favourite_User_Adapter(Context con,ArrayList<HashMap<String, String>> list) 
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
		
		row = inflater.inflate(R.layout.custom_favourite_user, parent, false);
		
	
		RoundedCornersGaganImg user_pic	=(RoundedCornersGaganImg)row.findViewById(R.id.user_pic);
		TextView user_name		=(TextView)row.findViewById(R.id.user_name);
		//TextView time		=(TextView)row.findViewById(R.id.time);
		//TextView likes			=(TextView)row.findViewById(R.id.likes);
		
		//LinearLayout click_me=(LinearLayout)row.findViewById(R.id.click_me);
		
		//image.setImageUrl(list.get(position).get("data"));
		//user_pic.setImageUrl(list.get(position).get("profile_image"));
		user_name.setSelected(true);
		
		user_pic.setImageUrl(con, list.get(position).get("profile_image"));
		
//		Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), user_pic, Drawer.options);
		
		
		
		user_name.setText(list.get(position).get("user_name"));
	//	likes.setText(list.get(position).get("like_count"));
		
		//String last =list.get(position).get("date");
		
		
		/*Calendar c = Calendar.getInstance();
		SimpleDateFormat dfJOBJ = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String formattedDateJOBJ = dfJOBJ.format(c.getTime());

		java.util.Date currentdate;
		try
		{
			currentdate = dfJOBJ.parse(formattedDateJOBJ);
			java.util.Date other_date = dfJOBJ.parse(last);
			
			long diff = currentdate.getTime() - other_date.getTime();
			
			time.setText(Util_Class.millisToLongDHMS(diff));
		}
		catch(ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0)
			{
				
				/*Global.set_user_id(list.get(position).get("user_id"));
				Global.set_friend_id(rem_pref.getString("user_id", ""));
				
				Intent i=new Intent(con,Other_Profile.class);
				//i.putExtra("user_id", list.get(position).get("user_id"));
				con.startActivity(i);*/
				
				if(list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")))
				{
					((Drawer) con).click();
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
