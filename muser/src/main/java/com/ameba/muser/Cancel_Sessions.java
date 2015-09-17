package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ProgressTask.Get_Cancel_Subscription_Thread;
import com.example.ProgressTask.Get_Funds_Thread;
import com.example.ProgressTask.Get_Wallet_Trainers_Thread;
import com.example.ProgressTask.Subscribe_Unsubscribe_Trainer_ProgressTask;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Cancel_Sessions extends Activity implements OnClickListener
{
	ListView common_listview;
	Context con;
	LinearLayout logo_lay;
	TextView message;
	LinearLayout lay_search;
	EditText search_editText;
	Cancel_Session_Adapter  adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_block_followe_following);

		con = this;

		((TextView) findViewById(R.id.back)).setOnClickListener(this);
		logo_lay=(LinearLayout)findViewById(R.id.logo_lay);
		message=(TextView) findViewById(R.id.error_message);

		common_listview = (ListView) findViewById(R.id.common_listview);


		lay_search=(LinearLayout)findViewById(R.id.lay_search);
		lay_search.setVisibility(View.VISIBLE);

		search_editText=(EditText)findViewById(R.id.search_editText);
		
		refresh();


		search_editText.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

				Log.e("count", "" + count);

				ArrayList<HashMap<String, String>> tempList = new ArrayList<>();

				for (HashMap<String, String> data : list)
				{

					try
					{
						if ((data.get("user_name").toLowerCase()).contains(s.toString().toLowerCase()))
						{
							tempList.add(data);
						}
					}
					catch (Exception e)
					{
						Log.e("user_name", "" + data.get("user_name"));
						e.printStackTrace();
					}

				}

				if (count > 0)
				{
					adapter.add_data(tempList);
					adapter.notifyDataSetChanged();
				}
				else
				{
					adapter.add_data(list);
					adapter.notifyDataSetChanged();
				}

			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});


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
		
		show_temp_logo();
		message.setText(con.getResources().getString(R.string.no_data_found));
	}
	
	
	ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
	public void set_data(ArrayList<HashMap<String, String>> list)
	{
		hide_temp_logo();
		
		if(this.list.size()==0)
		{
			this.list=list;
			adapter=new Cancel_Session_Adapter(this.list);
			common_listview.setAdapter(adapter);
		}
		else
		{
			this.list=list;
			adapter.notifyDataSetChanged();
		}
		
	}
	
	public void refresh()
	{
		if(list.size()==0)
		{
			show_temp_logo();
			
		}
		
		message.setText("Please Wait...Loading...");
		new Get_Cancel_Subscription_Thread(con);
	}
	
	

	
	
	class Cancel_Session_Adapter extends BaseAdapter
	{
		ArrayList<HashMap<String, String>> local_list;

		Cancel_Session_Adapter(ArrayList<HashMap<String, String>> list)
		{
			local_list=list;
		}
		
		@Override
		public View getView(final int position, View row, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			row = inflater.inflate(R.layout.custom_trainer, parent, false);
			
			RoundedCornersGaganImg trainer_image	=(RoundedCornersGaganImg)row.findViewById(R.id.trainer_image);
			TextView trainer_name		=(TextView)row.findViewById(R.id.trainer_name);
			ImageView unsubscribe	=(ImageView)row.findViewById(R.id.unsubscribe);
			
			unsubscribe.setVisibility(View.VISIBLE);
			
			trainer_image.setImageUrl(con, local_list.get(position).get("profile_image"));
			trainer_name.setText(local_list.get(position).get("user_name"));
			
			
			
			
			unsubscribe.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{
					new Subscribe_Unsubscribe_Trainer_ProgressTask(con, local_list.get(position).get("user_id"), "U").execute();


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
			return local_list.size();
		}

		public void add_data(ArrayList<HashMap<String, String>> list)
		{
			local_list=list;
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
