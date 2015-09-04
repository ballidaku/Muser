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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.Notification_Adapter;
import com.example.ProgressTask.Get_Trending_FriendActivities_ProgressTask;
import com.ameba.muser.R;
import com.example.ProgressTask.Get_Trending_Pictures_Videos_ProgressTask;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class Tab_Trending_Friends extends Fragment
{
	Context								con;
	Tab_Trending_Friends				con2;
	public static ListView				listview;
	public static TextView				error_message;
	//PullToRefreshScrollView mPullRefreshScrollView;
	Notification_Adapter				adapter;
	ArrayList<HashMap<String, String>>	list	= new ArrayList<HashMap<String, String>>();
	ImageView							temp_logo;
	PullToRefreshListView				mPullRefreshListView;
	LinearLayout logo_lay;
	SharedPreferences rem_pref;
	View rootView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		

		
		con=getActivity();
		con2=this;

		if(savedInstanceState==null)
		{
			rootView = inflater.inflate(R.layout.tab_trending_friends, container, false);
			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

			logo_lay = (LinearLayout) rootView.findViewById(R.id.logo_lay);
			mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.friends_listView);
			listview = mPullRefreshListView.getRefreshableView();

			error_message = (TextView) rootView.findViewById(R.id.error_message);

			temp_logo = (ImageView) rootView.findViewById(R.id.temp_logo);

		}
		/*Trending_Friends_Adapter adapter=new Trending_Friends_Adapter(getActivity());
		listview.setAdapter(adapter);*/


		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView)
			{
				refresh();
			}
		});
		
		
		return rootView;
		}

	@Override public
	void onResume()
	{
		super.onResume();

		Log.e("InonResume:Tab_Trending_Friends", rem_pref.getString("current_frag", ""));
		if (rem_pref.getString("current_frag","").equals("Trending"))
		{
			//list.clear();
			refresh();
		}

	}

	
	private void refresh()
	{
		if(list.size()==0)
		{
			show_temp_logo();
			
		}
		
		
		Log.e("Where", "Tab_Trending_Friends");
		error_message.setText(con.getResources().getString(R.string.please_wait));
		//new Get_Trending_FriendActivities_ProgressTask(con,con2).execute();


		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new Get_Trending_FriendActivities_ProgressTask(con,con2).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			new Get_Trending_FriendActivities_ProgressTask(con,con2).execute();
		}
		
	}
	
	public void set_data(ArrayList<HashMap<String, String>> list)
	{
		hide_temp_logo();
		
		if(this.list.size() == 0)
		{
			this.list = list;
			adapter  = new Notification_Adapter(con, list,"F");
			listview.setAdapter(adapter);
		}
		else
		{
			this.list = list;
			adapter.add_data(this.list);
			adapter.notifyDataSetChanged(); 
		}
		
	}
	
	public void on_Failure()
	{
		
		show_temp_logo();
		error_message.setText(con.getResources().getString(R.string.no_data_found));
	}
	
	
	
	public void refresh_complete()
	{
		if(mPullRefreshListView.isRefreshing())
			mPullRefreshListView.onRefreshComplete();
	}
	
	private void show_temp_logo()
	{
		temp_logo.setVisibility(View.VISIBLE);
		error_message.setVisibility(View.VISIBLE);
		mPullRefreshListView.setVisibility(View.GONE);
		
	}
	
	private void hide_temp_logo()
	{
		logo_lay.setVisibility(View.GONE);
		temp_logo.setVisibility(View.GONE);
		error_message.setVisibility(View.GONE);
		mPullRefreshListView.setVisibility(View.VISIBLE);
	}

	



}