package com.ameba.muser;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.Followers_Adapter;
import com.example.Adapter.Following_Adapter;
import com.example.ProgressTask.Get_Followes_Following_Blocks_ProgressTask;

public class Following extends Activity implements OnClickListener
{
	public static ListView common_listview;
	Context con;
	
	TextView back,title;
	public static TextView message;
	LinearLayout logo_lay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_listview_block_followe_following);
		
		con=this;
		
		back	=(TextView)findViewById(R.id.back);
		title	=(TextView)findViewById(R.id.title);
		message=(TextView)findViewById(R.id.error_message);
		logo_lay=(LinearLayout)findViewById(R.id.logo_lay);
		title.setText("Following");
		
		common_listview=(ListView)findViewById(R.id.common_listview);
		
		
		refresh();
		
		back.setOnClickListener(this);
		
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
	
	
	Following_Adapter adapter;//=new Following_Adapter(con, list);

	public void set_data(ArrayList<HashMap<String, String>> list)
	{
		
		hide_temp_logo();
		
		if(this.list.size()==0)
		{
			this.list=list;
			adapter=new Following_Adapter(con, list);
			common_listview.setAdapter(adapter);
		}
		else
		{
			this.list=list;
			adapter.add_data(this.list);
			adapter.notifyDataSetChanged();
		}
		
	}
	
	public void refresh()
	{
		if(list.size()==0)
		{
			show_temp_logo();
			
		}
		
		message.setText("Please Wait...Loading...");
		new Get_Followes_Following_Blocks_ProgressTask(con, "FG").execute();
	}
	
	@Override
	public void onClick(View v)
	{
//		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
//		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		switch (v.getId())
		{

		/*case R.id.public_lay:
			color_changer2("public");
			break;

		case R.id.private_lay:
			color_changer2("private");
			break;*/

		case R.id.back: 
			((Activity) con).finish();
			break;
			
		default:
			break;
		}
	}
	

}