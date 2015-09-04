package com.example.Tabs;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.Notification_Adapter;
import com.example.ProgressTask.Get_Notification_ProgressTask;
import com.ameba.muser.Drawer;
import com.ameba.muser.R;

public class Tab_Notification_Videos extends Fragment
{
	Context		con;
	ListView	notification_listview;
	TextView	message;
	LinearLayout logo_lay;
	Tab_Notification_Videos con2;
	SharedPreferences rem_pref;
	View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		con = getActivity();
		con2=this;

		if(savedInstanceState==null)
		{
			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

			rootView = inflater.inflate(R.layout.tab_notification, container, false);

			notification_listview = (ListView) rootView.findViewById(R.id.notification_listview);
			message = (TextView) rootView.findViewById(R.id.error_message);
			logo_lay = (LinearLayout) rootView.findViewById(R.id.logo_lay);

			//		notification_listview.setVisibility(View.VISIBLE);
			//		error_message.setVisibility(View.GONE);

		}

		return rootView;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		System.out.println(rem_pref.getString("current_frag", ""));
		if (rem_pref.getString("current_frag","").equals("Notification"))
		{

			refresh();
		}
	}

	public void refresh()
	{
		if(list.size() == 0)
		{
			show_temp_logo();
			message.setText("Please Wait...Loading...");
		}


		//new Get_Notification_ProgressTask(con,con2, "V").execute();


		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new Get_Notification_ProgressTask(con,con2, "V").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			new Get_Notification_ProgressTask(con,con2, "V").execute();
		}
	}

	private void show_temp_logo()
	{
		logo_lay.setVisibility(View.VISIBLE);
		message.setVisibility(View.VISIBLE);
		notification_listview.setVisibility(View.GONE);

	}

	private void hide_temp_logo()
	{
		logo_lay.setVisibility(View.GONE);
		message.setVisibility(View.GONE);
		notification_listview.setVisibility(View.VISIBLE);
	}

	public void on_Failure()
	{

		show_temp_logo();
		message.setText(con.getResources().getString(R.string.no_data_found));
	}

	ArrayList<HashMap<String, String>>	list	= new ArrayList<HashMap<String, String>>();
	Notification_Adapter				adapter;

	public void set_data(ArrayList<HashMap<String, String>> list)
	{

		hide_temp_logo();

		if(this.list.size() == 0)
		{
			this.list = list;
			adapter = new Notification_Adapter(con, list,"V");
			notification_listview.setAdapter(adapter);
		}
		else
		{
			this.list = list;
			adapter.add_data(list);
			adapter.notifyDataSetChanged();
		}

	}
}
