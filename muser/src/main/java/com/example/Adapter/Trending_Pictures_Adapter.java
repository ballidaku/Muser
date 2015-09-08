package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ProgressTask.Add_Like_Comment_Repost_ProgressTask;
import com.example.Tabs.Tab_Home_Pictures;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

public class Trending_Pictures_Adapter extends BaseAdapter/* implements OnTouchListener*/
{
	Context con;
	Fragment con2;
	ArrayList<HashMap<String, String>> list;
	SharedPreferences rem_pref;
	
	public Trending_Pictures_Adapter(Context con,Fragment con2, ArrayList<HashMap<String, String>> list) 
	{
		
		
		this.con = con;
		this.con2 = con2;
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
		
		row = inflater.inflate(R.layout.custom_common_pictures, parent, false);
		
		//Log.e("1", "1");
		
		RoundedCornersGaganImg image	=(RoundedCornersGaganImg)row.findViewById(R.id.image_pic);
		RoundedCornersGaganImg user_pic	=(RoundedCornersGaganImg)row.findViewById(R.id.user_pic);
		TextView user_name		=(TextView)row.findViewById(R.id.user_name);
		final TextView time		=(TextView)row.findViewById(R.id.time);
		TextView likes			=(TextView)row.findViewById(R.id.likes);
		TextView comments_count=(TextView)row.findViewById(R.id.comments_count);
		LinearLayout click_me=(LinearLayout)row.findViewById(R.id.click_me);
		
		//image.setImageUrl(list.get(position).get("data"));
		//user_pic.setImageUrl(list.get(position).get("profile_image"));
		user_name.setSelected(true);
		
		image.setImageUrl(con, list.get(position).get("data"));
		user_pic.setImageUrl(con, list.get(position).get("profile_image"));
		

		
		
		
		user_name.setText(list.get(position).get("user_name"));
		likes.setText(list.get(position).get("like_count"));
		comments_count.setText(list.get(position).get("comment_count"));
		//String last =list.get(position).get("date");
		
		Util_Class util=new Util_Class();
		time.setText(util.get_time2(list.get(position).get("date")));
		

		
		
		
		click_me.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{

				if(list.get(position).get("user_id").equals(rem_pref.getString("user_id", "")))
				{
					((Drawer) con).click();
				}
				else
				{
					Global.set_user_id(list.get(position).get("user_id"));
					Global.set_friend_id(rem_pref.getString("user_id", ""));

					Intent i = new Intent(con, Other_Profile.class);
					con.startActivity(i);
				}

			}
		});
		
		image.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if(count == 0)
				{
					count++;
					new MyCountDownTimer(250, 250, position).start();

				}
				else
				{

					double_tap = true;
				}

			}

		});
		
		return row;
	}
	
	int count=0;
	boolean double_tap=false;
	
	public class MyCountDownTimer extends CountDownTimer
	{
		int position;
		public MyCountDownTimer(long startTime, long interval, int position)
			{
				super(startTime, interval);
				this.position=position;
			}

		@Override
		public void onFinish()
			{
			  if(!double_tap)
			  {
				count = 0;
				double_tap = false;

				Intent i = new Intent(con, Image_Video_Details.class);
				i.putExtra("map", list.get(position));
				i.putExtra("from_where", "Data");
				con.startActivity(i);
			  }
			  else
			  {
				 
				 // Log.e("** DOUBLE TAP**", ""+position);
				  count=0;
				  double_tap=false;
				  
				 String action=list.get(position).get("is_self_liked").equals("N")?"L":"U";
				  
//				 Util_Class.show_Toast("Double  "+position +""+action, con);
				 
				  HashMap<String, String> map = new HashMap<String, String>();
				  map.put("post_id", list.get(position).get("post_id"));
				  map.put("action", action);
				  map.put("data", "");
					
				  new Add_Like_Comment_Repost_ProgressTask(con,con2, map).execute();
			  }
			}

		@Override
		public void onTick(long millisUntilFinished){}
	}
	
	public void add_data(ArrayList<HashMap<String, String>> list)
	{
		this.list=list;
	}

	


}