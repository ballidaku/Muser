package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ProgressTask.Get_Toatal_Clients_Thread;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;

public class Total_Clients extends Activity implements OnClickListener
{
	
	ListView common_listview;
	Context con;
	SharedPreferences rem_pref;
	LinearLayout logo_lay;
	TextView message;
	TextView tv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_block_followe_following);

		con = this;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		(findViewById(R.id.back)).setOnClickListener(this);
		logo_lay=(LinearLayout)findViewById(R.id.logo_lay);
		message=(TextView) findViewById(R.id.error_message);

		LinearLayout lay=(LinearLayout)findViewById(R.id.dynamic_lay);


		tv1 = new TextView(this);
		tv1.setTextSize(10);

		tv1.setTextColor(Color.BLACK);
		tv1.setBackgroundColor(Color.LTGRAY);
		tv1.setPadding(20,20,20,20);

		lay.addView(tv1);
		

		common_listview = (ListView) findViewById(R.id.common_listview);

		refresh();
	}
	
	
	ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
	public void set_data(ArrayList<HashMap<String, String>> list)
	{
		hide_temp_logo();
		
		if(this.list.size()==0)
		{
			this.list=list;
			common_listview.setAdapter(adapter);
		}
		else
		{
			this.list=list;
			adapter.notifyDataSetChanged();
		}

		tv1.setText("You have a total of "+list.size()+" Clients.");
		
	}
	
	public void refresh()
	{
		if(list.size()==0)
		{
			show_temp_logo();
			
		}
		
		message.setText("Please Wait...Loading...");
		new Get_Toatal_Clients_Thread(con);
	}
	
	private void show_temp_logo()
	{
		logo_lay.setVisibility(View.VISIBLE);
		message.setVisibility(View.VISIBLE);
		common_listview.setVisibility(View.GONE);
		
	}
	
	private void hide_temp_logo()
	{
		logo_lay.setVisibility(View.GONE);
		message.setVisibility(View.GONE);
		common_listview.setVisibility(View.VISIBLE);
	}
	
	public void on_Failure()
	{
		tv1.setText("You have a total of 0 Clients.");
		show_temp_logo();
		message.setText(con.getResources().getString(R.string.no_data_found));
	}

	
	

	
	
	BaseAdapter adapter =new BaseAdapter()
	{
		
		@Override
		public View getView(final int position, View row, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			row = inflater.inflate(R.layout.custom_trainer, parent, false);
			
			RoundedCornersGaganImg trainer_image	=(RoundedCornersGaganImg)row.findViewById(R.id.trainer_image);
			TextView trainer_name		=(TextView)row.findViewById(R.id.trainer_name);
			//ImageView train_with	=(ImageView)row.findViewById(R.id.train_with);
			
			//train_with.setVisibility(View.VISIBLE);
			
			trainer_image.setImageUrl(con, list.get(position).get("profile_image"));
			trainer_name.setText(list.get(position).get("user_name"));
			
			
			
			
			row.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					Global.set_user_id(list.get(position).get("user_id"));
					Global.set_friend_id(rem_pref.getString("user_id", ""));
					
					Intent i=new Intent(con,Other_Profile.class);
					con.startActivity(i);
				}
			});
			
			return row;
		}
		
		@Override
		public long getItemId(int arg0)
		{
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return list.size();
		}
	};
	
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.back:
				this.finish();
				break;
				
			default:
				break;
		}
		
	}
}
