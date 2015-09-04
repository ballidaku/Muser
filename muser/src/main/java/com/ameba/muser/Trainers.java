package com.ameba.muser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ProgressTask.Get_Funds_Thread;
import com.example.ProgressTask.Get_Wallet_Trainers_Thread;
import com.example.ProgressTask.Subscribe_Unsubscribe_Trainer_ProgressTask;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;

import java.util.ArrayList;
import java.util.HashMap;

public class Trainers extends Activity implements OnClickListener
{
	ListView common_listview;
	Context con;
	String user_id="";
	LinearLayout logo_lay;
	TextView message;
	SharedPreferences rem_pref;
	EditText search_editText;
	Trainer_Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trainers);
		
		con=this;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		
		(findViewById(R.id.back)).setOnClickListener(this);
		logo_lay=(LinearLayout)findViewById(R.id.logo_lay);
		message=(TextView) findViewById(R.id.error_message);

		search_editText = (EditText)findViewById(R.id.search_editText);
		common_listview=(ListView)findViewById(R.id.common_listview);


		/*search_editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					get_data_and_hit();
					return true;
				}
				return false;
			}
		});

		search_editText.setOnTouchListener(new View.OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					if(event.getRawX() >= (search_editText.getRight() - search_editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
					{
						get_data_and_hit();

						return true;
					}
				}
				return false;
			}
		});*/

		search_editText.addTextChangedListener(new TextWatcher()
		{
			@Override public
			void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override public
			void onTextChanged(CharSequence s, int start, int before, int count)
			{

				Log.e("count",""+count);

				ArrayList<HashMap<String, String>> tempList = new ArrayList<>();


				for(HashMap<String, String> data : list)
				{

					try
					{
						if((data.get("user_name").toLowerCase()).contains(s.toString().toLowerCase()))
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

				if(count>0)
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

			@Override public
			void afterTextChanged(Editable s)
			{

			}
		});



	//refresh();
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
			adapter=new Trainer_Adapter(this.list);
			common_listview.setAdapter(adapter);
		}
		else
		{
			adapter.add_data(this.list);
			adapter.notifyDataSetChanged();
		}
		
	}

	@Override protected
	void onResume()
	{
		super.onResume();
		refresh();
	}

	public void refresh()
	{
		if(list.size()==0)
		{
			show_temp_logo();
			
		}
		
		message.setText("Please Wait...Loading...");
		new Get_Wallet_Trainers_Thread(con);
	}
	
	

	
	

	
	
	private Handler	handler	= new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			Bundle bundle = msg.getData();
			String balance= bundle.getString("balance");
			
			if(Integer.parseInt(balance)>=5)
			{
				new Subscribe_Unsubscribe_Trainer_ProgressTask(con, user_id, "S").execute();
			}
			else
			{
				Util_Class.show_Toast("Insufficient Balance", con);
			}
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



	class Trainer_Adapter extends BaseAdapter
	{
		ArrayList<HashMap<String, String>> local_list;
		Trainer_Adapter(ArrayList<HashMap<String, String>> local_list)
		{
			this.local_list=local_list;
		}

		@Override
		public View getView(final int position, View row, ViewGroup parent)
		{
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			row = inflater.inflate(R.layout.custom_trainer, parent, false);

			RoundedCornersGaganImg trainer_image	=(RoundedCornersGaganImg)row.findViewById(R.id.trainer_image);
			TextView trainer_name		=(TextView)row.findViewById(R.id.trainer_name);
			ImageView train_with	=(ImageView)row.findViewById(R.id.train_with);

			train_with.setVisibility(View.VISIBLE);

			trainer_image.setImageUrl(con, local_list.get(position).get("profile_image"));
			trainer_name.setText(local_list.get(position).get("user_name"));




			train_with.setOnClickListener(new OnClickListener()
			{

				@Override
				public
				void onClick(View arg0)
				{
					user_id = "";
					OnClickListener proceed = new OnClickListener()
					{
						@Override
						public
						void onClick(View v)
						{
							Util_Class.super_dialog.dismiss();
							user_id = local_list.get(position).get("user_id");
							new Get_Funds_Thread(con, handler);

//							new Subscribe_Unsubscribe_Trainer_ProgressTask(con, user_id, "S").execute();
//							Paypal_idea p = new Paypal_idea(con, "5");
//							p.onBuyPressed();

						}
					};
					Util_Class.show_super_dialog(con, proceed, local_list.get(position).get("user_name"));

				}
			});


			row.setOnClickListener(new OnClickListener()
			{
				@Override public
				void onClick(View view)
				{
					Global.set_user_id(local_list.get(position).get("user_id"));
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
			return local_list.size();
		}

		public void add_data(ArrayList<HashMap<String, String>> list)
		{
			local_list=list;
		}
	}
}
