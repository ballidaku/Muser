package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameba.muser.R;
import com.example.ProgressTask.Set_Block_Follow_Users_ProgressTask;
import com.example.classes.RoundedCornersGaganImg;

public class Blocks_Adapter extends BaseAdapter
{
	Context con;
	ArrayList<HashMap<String, String>> list;
	

	public Blocks_Adapter(Context con,ArrayList<HashMap<String, String>> list) 
	{
		this.con = con;
		this.list=list;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View row, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		row = inflater.inflate(R.layout.custom_blocks, parent, false);
		
		RoundedCornersGaganImg block_user_image	=(RoundedCornersGaganImg)row.findViewById(R.id.block_user_image);
		TextView block_user_name		=(TextView)row.findViewById(R.id.block_user_name);
		ImageView unblock	=(ImageView)row.findViewById(R.id.unblock);
		
		block_user_image.setImageUrl(con,list.get(position).get("profile_image"));
		
		
		//Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), block_user_image, Drawer.options);
				
		block_user_name.setText(list.get(position).get("user_name"));
		
		
		unblock.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				final HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("user_id", list.get(position).get("user_id"));
				new Set_Block_Follow_Users_ProgressTask(con, map1,"U","Blocks_Adapter").execute();
			}
		});
		return row;
	}
	
	public void add_data(ArrayList<HashMap<String, String>> list)
	{
		this.list=list;
	}

}
