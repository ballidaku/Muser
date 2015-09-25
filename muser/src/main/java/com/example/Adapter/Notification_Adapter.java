package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ProgressTask.Accept_Reject_Request_Thread;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

public class Notification_Adapter extends BaseAdapter
{
	Context con;
	ArrayList<HashMap<String, String>> list;
	String type;
	SharedPreferences rem_pref;
	Fragment con2;

	int picture_count=0,video_count=0,session_count=0,connect_count=0;

	public Notification_Adapter(Context con,ArrayList<HashMap<String, String>> list, String type) 
	{
		this.con = con;
		this.list=list;
		this.type=type;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		picture_count=rem_pref.getInt("Picture_count", 0);
		video_count=rem_pref.getInt("Video_count",0);
		session_count=rem_pref.getInt("Session_count",0);
		connect_count=rem_pref.getInt("Connect_count", 0);

	}
	
	public Notification_Adapter(Context con, Fragment con2, ArrayList<HashMap<String, String>> list, String type)
	{
		this.con = con;
		this.con2 = con2;
		this.list=list;
		this.type=type;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);


		picture_count=rem_pref.getInt("Picture_count", 0);
		video_count=rem_pref.getInt("Video_count",0);
		session_count=rem_pref.getInt("Session_count",0);
		connect_count=rem_pref.getInt("Connect_count",0);


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
		
		row = inflater.inflate(R.layout.custom_notification, parent, false);
		
		RoundedCornersGaganImg image	=(RoundedCornersGaganImg)row.findViewById(R.id.image);
		TextView description = (TextView) row.findViewById(R.id.description);
		final TextView time = (TextView) row.findViewById(R.id.time);
		
		ImageView accept	=(ImageView)row.findViewById(R.id.accept);
		ImageView reject	=(ImageView)row.findViewById(R.id.reject);
		
		
		/*String styledText = "<font color='black'><b>Dannie Dee </b></font>(friends with <font color='black'><b>"
				+ "Jorge Luis"
				+ "</b></font> )and also commented on <font color='black'><b>"
				+" Sharanpal" + "</b></font> ";*/





		
		if(type.equals("I") || type.equals("V"))
		{
			
			image.setImageUrl(con, list.get(position).get("showing_image"));
//			Drawer.imageLoader.displayImage(list.get(position).get("showing_image"), image, Drawer.options);
			String styledText = "<font color='#4F606A'><b>" + list.get(position).get("showing_name") + " </b> </font><font color='Black' style='font-size:9px;'> "
					+ list.get(position).get("activity") + "</font>";
			description.setText(Html.fromHtml(styledText));
			
			
			row.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					
					/*Intent i=new Intent(con,Image_Video_Details.class);
					i.putExtra("map",list.get(position));
					i.putExtra("time", time.getText().toString());
					i.putExtra("type", type);
					con.startActivity(i);*/

					Intent i = new Intent(con, Image_Video_Details.class);
					i.putExtra("post_id", list.get(position).get("post_id"));
					i.putExtra("user_id", rem_pref.getString("user_id", ""));
					i.putExtra("from_where", "Notification");
					i.putExtra("which", "IV");
					con.startActivity(i);

				}
			});
		}
		else if(type.equals("S"))
		{
			image.setImageUrl(con, list.get(position).get("profile_image"));
//			Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), image, Drawer.options);
			String styledText = "<font color='#4F606A'><b>"+list.get(position).get("user_name")+"</b> </font><font color='Black' style='font-size:9px;'> "
					+list.get(position).get("activity") +  "</font>";
			description.setText(Html.fromHtml(styledText));
			
			
			row.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
				
					if(list.get(position).get("activity").contains("liked") || list.get(position).get("activity").contains("commented"))
					{
						Intent i=new Intent(con,Image_Video_Details.class);
						i.putExtra("post_id",list.get(position).get("post_id"));
						i.putExtra("user_id",rem_pref.getString("user_id", ""));
						i.putExtra("from_where", "Notification");
						i.putExtra("which", "S");
						con.startActivity(i);
					}
					else if(list.get(position).get("activity").contains("subscribed"))
					{
						Global.set_user_id(list.get(position).get("user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						con.startActivity(i);
					}
					
				}
			});
		}
		else if(type.equals("C"))
		{
			//first N then changed by arsh to R on sunday 31 may 
			if(list.get(position).get("approve_status").equals("R") && !list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")))
			{ 
				image.setImageUrl(con,list.get(position).get("from_profile_image"));
				//Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);
				
				String styledText = "<font color='#4F606A'><b>"+list.get(position).get("from_user_name")+"</b></font><font color='Black' style='font-size:9px;'> is requesting to follow you.</font>";
				description.setText(Html.fromHtml(styledText));
				
				
				accept.setVisibility(View.VISIBLE);
				reject.setVisibility(View.VISIBLE);
				
				accept.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View arg0)
					{
						new Accept_Reject_Request_Thread(con,con2, list.get(position).get("from_user_id"), "Y");
						
					}
				});
				
				
				reject.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View arg0)
					{
						new Accept_Reject_Request_Thread(con,con2, list.get(position).get("from_user_id"), "N");
						
					}
				});
				
				
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0)
					{
						
						/*Global.set_user_id(list.get(position).get("from_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);*/
						
						if(list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")))
						{
							((Drawer) con).click();
						}
						else
						{
							Global.set_user_id(list.get(position).get("from_user_id"));
							Global.set_friend_id(rem_pref.getString("user_id", ""));
							
							Intent i=new Intent(con,Other_Profile.class);
							//i.putExtra("user_id", list.get(position).get("user_id"));
							con.startActivity(i);
						}

					}
				});
				
				
			}
			else if(list.get(position).get("approve_status").equals("N") && list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")))
			{
				
				image.setImageUrl(con,list.get(position).get("to_profile_image"));
				//Drawer.imageLoader.displayImage(list.get(position).get("to_profile_image"), image, Drawer.options);

				String styledText = "<font color='#4F606A'><b> You're </b></font><font color='Black' style='font-size:9px;'> requesting to follow "+list.get(position).get("to_user_name")+".</font>";
				description.setText(Html.fromHtml(styledText));
				
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0)
					{
						
						Global.set_user_id(list.get(position).get("to_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);

					}
				});
			}
			else if(list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")) && list.get(position).get("approve_status").equals("R"))
			{   
				image.setImageUrl(con,list.get(position).get("to_profile_image"));
				//Drawer.imageLoader.displayImage(list.get(position).get("to_profile_image"), image, Drawer.options);
				String styledText = "<font color='#4F606A'><b> You </b></font><font color='Black' style='font-size:9px;'> "
						+ list.get(position).get("activity") + list.get(position).get("to_user_name")+"."+ "</font>";
				description.setText(Html.fromHtml(styledText));
				
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0)
					{
						
						Global.set_user_id(list.get(position).get("to_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);

					}
				});
			}
			else if(list.get(position).get("from_user_id").equals(rem_pref.getString("user_id", "")) && list.get(position).get("approve_status").equals("Y"))
			{   
				image.setImageUrl(con,list.get(position).get("to_profile_image"));
				//Drawer.imageLoader.displayImage(list.get(position).get("to_profile_image"), image, Drawer.options);
				String styledText = "<font color='#4F606A'><b> You're </b></font><font color='Black' style='font-size:9px;'> "
						+ list.get(position).get("activity") + list.get(position).get("to_user_name")+"."+ "</font>";
				description.setText(Html.fromHtml(styledText));
				
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0)
					{
						
						Global.set_user_id(list.get(position).get("to_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);

					}
				});
			}
			else if(list.get(position).get("to_user_id").equals(rem_pref.getString("user_id", "")))
			{
				
				image.setImageUrl(con,list.get(position).get("from_profile_image"));
				//Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);
				String styledText = "<font color='#4F606A'><b>"+list.get(position).get("from_user_name")+"</b> </font><font color='Black' style='font-size:9px;'> "
						+"is now "+list.get(position).get("activity") + " you"+"."+ "</font>";
				description.setText(Html.fromHtml(styledText));
				
				
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0)
					{
						
						Global.set_user_id(list.get(position).get("from_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);

					}
				});
			}
			else
			{
				image.setImageUrl(con,list.get(position).get("from_profile_image"));
				//Drawer.imageLoader.displayImage(list.get(position).get("from_profile_image"), image, Drawer.options);
				String styledText = "<font color='#4F606A'><b>"+list.get(position).get("from_user_name")+"</b> </font><font color='Black' style='font-size:9px;'> "
						+list.get(position).get("activity") + list.get(position).get("to_user_name")+"."+ "</font>";
				description.setText(Html.fromHtml(styledText));
				
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0)
					{
						
						Global.set_user_id(list.get(position).get("from_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);

					}
				});
			}
			
			
		}
		else if(type.equals("F"))
		{
			
			image.setImageUrl(con,list.get(position).get("profile_image"));
			//String who=list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")?"you":;
			//Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), image, Drawer.options);
			String styledText = "<font color='#4F606A'><b>" + list.get(position).get("user_name") + " </b> </font><font color='Black' style='font-size:9px;'> "
					+ list.get(position).get("activity") + "</font>";
			description.setText(Html.fromHtml(styledText));
			
			
			row.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{

					if (list.get(position).get("activity").contains("likes"))
					{
						Intent i = new Intent(con, Image_Video_Details.class);
						i.putExtra("post_id", list.get(position).get("post_id"));
						i.putExtra("user_id", list.get(position).get("user_id"));
						i.putExtra("from_where", "Notification");
						i.putExtra("which", "");
						con.startActivity(i);
					}
					else if (list.get(position).get("activity").contains("followed") || list.get(position).get("activity").contains("training with"))
					{
						Global.set_user_id(list.get(position).get("friend_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));

						Intent i = new Intent(con, Other_Profile.class);
						con.startActivity(i);
					}
					else if (list.get(position).get("activity").contains("commented"))// actually this is for under trending under friends
					{
						Intent i=new Intent(con,Image_Video_Details.class);
						i.putExtra("post_id",list.get(position).get("post_id"));
						i.putExtra("user_id",rem_pref.getString("user_id", ""));
						i.putExtra("from_where", "Notification");
						i.putExtra("which", "IV");
						con.startActivity(i);
					}

				}
			});
			
			
			
			
		}

		if(picture_count>0 && type.equals("I") && picture_count > position)
		{
			description.setTypeface(null, Typeface.BOLD);
		}
		else if(video_count>0 && type.equals("V") && video_count > position)
		{
			description.setTypeface(null, Typeface.BOLD);
		}
		else if(session_count>0 && type.equals("S") && session_count > position)
		{
			description.setTypeface(null, Typeface.BOLD);
		}
		else if(connect_count>0 && type.equals("C") && connect_count > position)
		{
			description.setTypeface(null, Typeface.BOLD);
		}
		
		
		
		
		
		
		Util_Class util=new Util_Class();
		
		time.setText(util.get_time2(list.get(position).get("date")));

		return row;
	}
	
	
	public void add_data(ArrayList<HashMap<String, String>> list)
	{
		this.list=list;
		picture_count=0;
		video_count=0;
		session_count=0;
		connect_count=0;

	}
	
	
	

}
