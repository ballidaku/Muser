package com.ameba.muser;

import com.example.ProgressTask.Search_Child_ProgressTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class Search_Hashtags_Child extends Activity implements OnClickListener
{
	Context con;
	public static GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		
		con=this;
		
		setContentView(R.layout.activity_search_hashtags_child);
		
		String value=getIntent().getStringExtra("value").replaceAll("#", "");
		
		new Search_Child_ProgressTask(con, value, "T").execute();
		
		
		TextView title=(TextView)findViewById(R.id.title_header);
		TextView back=(TextView)findViewById(R.id.back);
		
		gridView=(GridView)findViewById(R.id.gridView);
		
		title.setText(value+" posts");
		
		
		
		
		back.setOnClickListener(this);
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
