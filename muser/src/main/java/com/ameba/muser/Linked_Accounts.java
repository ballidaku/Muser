package com.ameba.muser;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class Linked_Accounts extends Activity
{
	EditText	optional_fb_name, optional_tw_name, optional_ig_name;
	EditText	optional_fb_phone, optional_tw_phone, optional_ig_phone;
	TextView back;

	SharedPreferences		rem_pref;
	Context					con;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linked_accounts);
		
		
		con = this;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		optional_fb_name = (EditText) findViewById(R.id.optional_fb_name);
		optional_tw_name = (EditText) findViewById(R.id.optional_tw_name);
		optional_ig_name = (EditText) findViewById(R.id.optional_ig_name);

		optional_fb_phone = (EditText) findViewById(R.id.optional_fb_phone);
		optional_tw_phone = (EditText) findViewById(R.id.optional_tw_phone);
		optional_ig_phone = (EditText) findViewById(R.id.optional_ig_phone);
		
		back=(TextView)findViewById(R.id.back);
		
		
		optional_fb_name.setText(rem_pref.getString("facebook_name", ""));
		optional_tw_name.setText(rem_pref.getString("twitter_name", ""));
		optional_ig_name.setText(rem_pref.getString("instagram_name", ""));
		
		optional_fb_phone.setText(rem_pref.getString("facebook_phone", ""));
		optional_tw_phone.setText(rem_pref.getString("twitter_phone", ""));
		optional_ig_phone.setText(rem_pref.getString("instagram_phone", ""));
		
		
		back.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
				
			}
		});
		
		
	}
}
