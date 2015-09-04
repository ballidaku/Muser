package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Adapter.Trending_Pictures_Adapter.MyCountDownTimer;
import com.example.ProgressTask.Add_Like_Comment_Repost_ProgressTask;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.ameba.muser.Image_Video_Details;
import com.ameba.muser.R;

public class My_Profile_Videos_Adapter  extends BaseAdapter
{
	Context con;
	Fragment con2;
	ArrayList<HashMap<String, String>> list;

	
	public My_Profile_Videos_Adapter(Context con,Fragment con2,ArrayList<HashMap<String, String>> list) 
	{
		this.con = con;
		this.con2 = con2;
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

	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View row, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		row = inflater.inflate(R.layout.custom_myprofile_videos, parent, false);
		
		RoundedCornersGaganImg video_thumbnail=(RoundedCornersGaganImg)row.findViewById(R.id.video_thumbnail);
		RoundedCornersGaganImg user_pic=(RoundedCornersGaganImg)row.findViewById(R.id.user_pic);
		TextView user_name=(TextView)row.findViewById(R.id.user_name);
		TextView likes=(TextView)row.findViewById(R.id.likes);
		final TextView time			=(TextView)row.findViewById(R.id.time);
		TextView comments_count=(TextView)row.findViewById(R.id.comments_count);
		TextView duration = (TextView) row.findViewById(R.id.duration);
		
		user_name.setSelected(true);
		
		int duration_s=Integer.parseInt(list.get(position).get("duration"));
		
		duration.setText(secondsToString(duration_s));
	
		video_thumbnail.setImageUrl(con, list.get(position).get("thumbnail"));
		user_pic.setImageUrl(con, list.get(position).get("profile_image"));
		

		user_name.setText(list.get(position).get("user_name"));
		likes.setText(list.get(position).get("like_count"));
		comments_count.setText(list.get(position).get("comment_count"));

		
		Util_Class util=new Util_Class();
		time.setText(util.get_time2(list.get(position).get("date")));

		video_thumbnail.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0)
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
	
	private String secondsToString(int pTime)
	{
		return String.format("%02d:%02d", pTime / 60, pTime % 60);
	}
	
	public void add_data(ArrayList<HashMap<String, String>> list)
	{
		this.list=list;
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

				Intent i=new Intent(con,Image_Video_Details.class);
				i.putExtra("map",list.get(position));
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

}
