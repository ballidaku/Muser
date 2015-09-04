package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.Adapter.Messages_Adapter;
import com.example.ProgressTask.Get_Messages_Contacts_Thread;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class Messages  extends Fragment 
{
	Messages messages;
	ListView messages_list;
	Context con;
	Messages con2;
	ArrayList<HashMap<String, String>> list;
	Messages_Adapter adapter;
	SharedPreferences rem_pref;
	View v;
  @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		if(savedInstanceState==null)
		{

			v = inflater.inflate(R.layout.messages, container, false);

			con = getActivity();
			con2 = this;
			messages = this;
			rem_pref	 		= con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
			messages_list = (ListView) v.findViewById(R.id.messages_list);

		}

		return v;
	}
  
  
	@Override
	public void onResume()
	{
		super.onResume();
		
		new Get_Messages_Contacts_Thread(con,con2);
	}
	
	
	public void add_list(ArrayList<HashMap<String, String>> list)
	{
		rem_pref.edit().putInt("message_count", 0).apply();
		((Drawer)con).refresh_menu_logo();

		this.list = list;
		adapter = new Messages_Adapter(con, list);
		messages_list.setAdapter(adapter);
	}
}
