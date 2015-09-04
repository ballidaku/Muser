package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.ProgressTask.Get_Comment_Likes_ProgressTask;
import com.example.classes.Util_Class;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class View_All_Likes extends Activity implements OnClickListener
{
	public static PullToRefreshListView likes_listview;
	Context con;
	int limit = 1;
	TextView back;
	//HashMap<String, String> hashMap;
	public static ArrayList<HashMap<String, String>> list;
	
	String post_id="";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_likes);
		con = this;

		post_id = getIntent().getStringExtra("post_id");
		//hashMap = (HashMap<String, String>) intent.getSerializableExtra("map");

		list = new ArrayList<HashMap<String, String>>();
		likes_listview = (PullToRefreshListView) findViewById(R.id.likes_listview);
		(back = (TextView) findViewById(R.id.back)).setOnClickListener(this);

		likes_listview.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh(PullToRefreshBase refreshView)
			{
				if(Util_Class.checknetwork(con))
				{
					limit++;
					new Get_Comment_Likes_ProgressTask(con, "L", post_id, limit, "View_All_Likes").execute();
				}

			}
		});
	}

	@Override
	protected void onResume()
	{
		if(Util_Class.checknetwork(con))
		{
			list.clear();
			new Get_Comment_Likes_ProgressTask(con, "L",post_id, limit, "View_All_Likes").execute();
		}
		else
		{
			//  Util_Class.show_Toast("Internet is not http://cg95.com/mobile_apps/project_management/webservices.php", con);
		}
		super.onResume();
	}

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
