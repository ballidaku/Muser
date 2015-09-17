package com.example.Tabs;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.Adapter.My_Profile_Pictures_Adapter;
import com.example.Adapter.My_Profile_Videos_Adapter;
import com.example.ProgressTask.Get_My_Favoutite_Pictures_Videos_Users_ProgressTask;
import com.example.ProgressTask.Get_Pictures_Videos_ProgressTask;
import com.example.classes.Global;
import com.ameba.muser.My_Profile;
import com.ameba.muser.R;
import com.example.classes.Util_Class;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class Tab_My_Profile_Videos extends Fragment
{
	Context					con;
	Tab_My_Profile_Videos con2;
	GridView	gridView;
	 TextView	error_message;
	//PullToRefreshGridView	mPullRefreshGridView;
	My_Profile_Videos_Adapter adapter ;
	ArrayList<HashMap<String, String>> list=new ArrayList<HashMap<String, String>>();
	
	PullToRefreshGridView mPullRefreshGridView;
	SharedPreferences rem_pref;

	MyBroadcastReceiverVideos mReceiver;
	private boolean mIsReceiverRegistered = false;
//	GridView mGridView;
	
	ImageView temp_logo;
	View rootView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		con = getActivity();
		con2=this;
		if(savedInstanceState==null)
		{

			rootView = inflater.inflate(R.layout.custom_common_gridview, container, false);
			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
			mPullRefreshGridView = (PullToRefreshGridView) rootView.findViewById(R.id.gridView);
			gridView = mPullRefreshGridView.getRefreshableView();
			error_message = (TextView) rootView.findViewById(R.id.error_message);
			//gridView = mPullRefreshGridView.getRefreshableView();
			temp_logo = (ImageView) rootView.findViewById(R.id.temp_logo);



			error_message.setText("You got 15 seconds! Promote your session, share your fitness story, show us who you are! ");
		}
	

		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView)
			{
				refresh();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView)
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

		if (!mIsReceiverRegistered)
		{
			if (mReceiver == null)
			{
				mReceiver = new MyBroadcastReceiverVideos();
			}
			getActivity().registerReceiver(mReceiver, new IntentFilter(Util_Class.BROADCAST_UPDATE_MYProfileVideos));
			mIsReceiverRegistered = true;
		}


		Log.e("onResume:Tab_MyProfile_Videos", rem_pref.getString("current_frag", ""));
		if (rem_pref.getString("current_frag","").equals("My Profile")  || getActivity().getLocalClassName().equals("Other_Profile"))
		{
			refresh();
		}

	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mIsReceiverRegistered)
		{
			getActivity().unregisterReceiver(mReceiver);
			mReceiver = null;
			mIsReceiverRegistered = false;
		}

	}


	private
	class MyBroadcastReceiverVideos extends BroadcastReceiver
	{

		@Override
		public
		void onReceive(Context context, Intent intent)
		{
			refresh();
		}
	}
	
	public void refresh()
	{
		
		if(list.size()==0)
		{
			show_temp_logo();
			error_message.setText("You got 15 seconds! Promote your session, share your fitness story, show us who you are! ");
		}
		
		Log.e("Where", "Tab_My_Profile_Videos");
		

	/*	if(Global.is_public_or_private.equals("PR") && Global.is_follow.equals("N"))
		{
			show_temp_logo();
			refresh_complete();
			error_message.setText(con.getResources().getString(R.string.private_user));
		}
		else
		{*/


		//}


		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new Get_Pictures_Videos_ProgressTask(con, con2, Global.get_user_id(), "V", "").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			new Get_Pictures_Videos_ProgressTask(con, con2, Global.get_user_id(), "V", "").execute();
		}
	}
	
	public void private_user()
	{

		show_temp_logo();
		refresh_complete();
		error_message.setText(con.getResources().getString(R.string.private_user));

	}

	public void set_data(ArrayList<HashMap<String, String>> list)
	{
		
		
		hide_temp_logo();

		//mPullRefreshGridView.onRefreshComplete();
		
		
		if(this.list.size() == 0)
		{
			this.list = list;
			adapter = new My_Profile_Videos_Adapter(con,con2, list);
			mPullRefreshGridView.setAdapter(adapter);
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
		error_message.setText("You got 15 seconds! Promote your session, share your fitness story, show us who you are! ");
	}

	public void refresh_complete()
	{
		if(mPullRefreshGridView.isRefreshing())
			mPullRefreshGridView.onRefreshComplete();
	}
	
	private void show_temp_logo()
	{
		temp_logo.setVisibility(View.VISIBLE);
		error_message.setVisibility(View.VISIBLE);
		mPullRefreshGridView.setVisibility(View.GONE);
		
	}
	
	private void hide_temp_logo()
	{
		temp_logo.setVisibility(View.GONE);
		error_message.setVisibility(View.GONE);
		mPullRefreshGridView.setVisibility(View.VISIBLE);
	}
}
