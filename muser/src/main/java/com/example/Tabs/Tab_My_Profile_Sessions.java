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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.Adapter.My_Profile_Videos_Adapter;
import com.example.Adapter.Trending_Pictures_Adapter;
import com.example.ProgressTask.Get_Pictures_Videos_ProgressTask;
import com.example.ProgressTask.Get_Subscribed_Trainer_Session_ProgressTask;
import com.example.classes.Global;
import com.ameba.muser.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class Tab_My_Profile_Sessions extends Fragment
{
	Context								con;
	Tab_My_Profile_Sessions				con2;
	GridView				gridView;
	public static TextView				error_message;
	//PullToRefreshGridView	mPullRefreshGridView;
	SharedPreferences					rem_pref;
	My_Profile_Videos_Adapter			adapter;
	ArrayList<HashMap<String, String>>	list	= new ArrayList<HashMap<String, String>>();

	PullToRefreshGridView mPullRefreshGridView;
	//GridView mGridView;
	ImageView temp_logo;
	View rootView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		//String s = "Session's are only available with paid <\br> subscriptions, if  you would like access to a<\br> trainer(s) session's <Html> <b>click here...</b></Html";
//		 rootView = inflater.inflate(R.layout.custom_common_gridview, container, false);

		con = getActivity();
		con2 = this;
		//list.clear();

		//text.setText(Html.fromHtml(s));
		if(savedInstanceState==null)
		{
			rootView = inflater.inflate(R.layout.custom_common_gridview, container, false);
			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
			mPullRefreshGridView = (PullToRefreshGridView) rootView.findViewById(R.id.gridView);
			gridView = mPullRefreshGridView.getRefreshableView();

			error_message = (TextView) rootView.findViewById(R.id.error_message);
			temp_logo = (ImageView) rootView.findViewById(R.id.temp_logo);
			//		gridView = mPullRefreshGridView.getRefreshableView();

		}

		
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>()
		{

			@Override
			public
			void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView)
			{
				refresh();
			}

			@Override
			public
			void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView)
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

		Log.e("onResume:Tab_MyProfile_Session", rem_pref.getString("current_frag", ""));
		if (rem_pref.getString("current_frag","").equals("My Profile") ||rem_pref.getString("current_frag","").equals("Home") || getActivity().getLocalClassName().equals("Other_Profile"))
		{
			refresh();
		}

	}
	public void refresh()
	{
		if(list.size()==0)
		{
			show_temp_logo();
			//error_message.setText(con.getResources().getString(R.string.please_wait));
			error_message.setText("This screen shows you latest training videos feed from your trainers.");
		}
		
		Log.e("Where", "MyProfile Sessions");
	/*	if(Global.is_public_or_private.equals("Private") && Global.is_follow.equals("N"))
		{
			show_temp_logo();
			refresh_complete();
			error_message.setText(con.getResources().getString(R.string.private_user));
		}
		else
		{*/
		

		//new Get_Subscribed_Trainer_Session_ProgressTask(con, con2).execute();

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
		//error_message.setText(con.getResources().getString(R.string.no_data_found));
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