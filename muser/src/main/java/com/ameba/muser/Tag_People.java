package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.ProgressTask.Search_Hashtags_User_ProgressTask;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;

public class Tag_People extends Activity
{
	
	EditText search_editText;
	ListView users_listView;
	TextView error_message,back;
	ImageView done;
	Context con;
	Tag_People con2;
	ArrayList<Boolean> check_list; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_people);
		
		con=this;
		con2=this;
		back = (TextView) findViewById(R.id.back);
		search_editText = (EditText) findViewById(R.id.search_editText);
		error_message = (TextView) findViewById(R.id.error_message);
		users_listView = (ListView) findViewById(R.id.users_listView);
		done=(ImageView)findViewById(R.id.done);
		
		search_editText.setOnEditorActionListener(new OnEditorActionListener()
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
		
		
		search_editText.setOnTouchListener(new OnTouchListener()
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
		});
		
		back.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				con2.finish();
				
			}
		});
		
		
		done.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				get_add_data();
				
				con2.finish();
				
			}
		});
	}
	
	public void get_data_and_hit()
	{
		get_add_data();
		
		
		
		String value = search_editText.getText().toString().trim();

		if(value.length() > 0)
		{
			users_listView.setVisibility(View.VISIBLE);
			error_message.setVisibility(View.GONE);

			new Search_Hashtags_User_ProgressTask(con,con2, search_editText.getText().toString(), "U","Tag_People").execute();
			
			
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(search_editText.getWindowToken(), 0);
		}
		else
		{
			Util_Class.show_Toast("Please enter search value.", con);
		}
	}
	
	
	
	 private void get_add_data()
	{
		if(user_list.size()>0)
		{
			for(int i = 0; i < user_list.size(); i++)
			{
				if(check_list.get(i))
				{
					Captured_Image.tag_people_list.add(user_list.get(i));
				}
			}
		}
	}

	ArrayList<HashMap<String, String>> user_list=new ArrayList<HashMap<String, String>>(); 
	 boolean is_check=false;

	public void set_data(ArrayList<HashMap<String, String>> user_list)
	{
		this.user_list.clear();
		this.user_list=user_list;
		
		check_list=new ArrayList<Boolean>();
		for(int i = 0; i < user_list.size(); i++)
		{
			check_list.add(is_check);
		}
		
		
		users_listView.setAdapter(adapter);
	}
	
	
	
	BaseAdapter adapter=new BaseAdapter()
	{

		
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
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.custom_tag_people, null);

			RoundedCornersGaganImg image = (RoundedCornersGaganImg) row.findViewById(R.id.image);
			TextView user_name = (TextView) row.findViewById(R.id.user_name);
			final ImageView check=(ImageView)row.findViewById(R.id.check);
			
			
			image.setImageUrl(con, user_list.get(position).get("profile_image"));
			
			user_name.setText(user_list.get(position).get("user_name"));

			check.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					
					check_list.set(position, !check_list.get(position));
					
					//change_image(position);
					
					//Log.e("check_list", ""+check_list+".."+check_list.size());

					if(check_list.get(position))
					{
						check.setImageResource(R.drawable.check_box);
					}
					else
					{
						check.setImageResource(R.drawable.uncheck_box);
					}
				}

			});
			

			if(check_list.get(position))
			{
				check.setImageResource(R.drawable.check_box);
			}
			else
			{
				check.setImageResource(R.drawable.uncheck_box);
			}

			return row;
		}
	};
}