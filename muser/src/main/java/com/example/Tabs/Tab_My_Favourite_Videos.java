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
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.Common_Grid_Video_Adapter;
import com.example.Adapter.My_Favourite_User_Adapter;
import com.example.ProgressTask.Get_Home_Pictures_Videos_ProgressTask;
import com.example.ProgressTask.Get_My_Favoutite_Pictures_Videos_Users_ProgressTask;
import com.example.ProgressTask.Get_Trending_Pictures_Videos_ProgressTask;
import com.example.classes.Global;
import com.ameba.muser.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

public class Tab_My_Favourite_Videos extends Fragment
{
	Context								con;
	Tab_My_Favourite_Videos				con2;
	public static GridView				gridView;
	public static TextView				error_message;
	PullToRefreshGridView				mPullRefreshGridView;
	SharedPreferences					rem_pref;
	Common_Grid_Video_Adapter			adapter;
	ArrayList<HashMap<String, String>>	list	= new ArrayList<HashMap<String, String>>();

	ImageView temp_logo;
	View rootView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		con = getActivity();
		con2 = this;

		if(savedInstanceState==null)
		{
			rootView = inflater.inflate(R.layout.custom_common_gridview, container, false);
			mPullRefreshGridView = (PullToRefreshGridView) rootView.findViewById(R.id.gridView);
			error_message = (TextView) rootView.findViewById(R.id.error_message);
			temp_logo = (ImageView) rootView.findViewById(R.id.temp_logo);
			gridView = mPullRefreshGridView.getRefreshableView();
			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

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

		Log.e("onResume:Tab_MyFavorite_Videos", rem_pref.getString("current_frag", ""));
		if (rem_pref.getString("current_frag","").equals("My Favorites"))
		{
			//list.clear();
			refresh();
		}

	}

	public void refresh()
	{
		
		if(list.size()==0)
		{
			show_temp_logo();
			error_message.setText(con.getResources().getString(R.string.please_wait));
		}
		
		Log.e("Where", "Tab_My_Favourite_Videos");
		Global.set_user_id(rem_pref.getString("user_id", ""));

	//	new Get_My_Favoutite_Pictures_Videos_Users_ProgressTask(con, con2, "V").execute();

		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new Get_My_Favoutite_Pictures_Videos_Users_ProgressTask(con, con2, "V").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			new Get_My_Favoutite_Pictures_Videos_Users_ProgressTask(con, con2, "V").execute();
		}

	}

	public void set_data(ArrayList<HashMap<String, String>> list)
	{

		hide_temp_logo();
		if(this.list.size() == 0)
		{
			this.list = list;
			adapter = new Common_Grid_Video_Adapter(con,con2, list);
			gridView.setAdapter(adapter);
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
