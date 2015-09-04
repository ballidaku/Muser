package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Tabs.Tab_Invite_Others_Twitter;
import com.example.classes.RoundedCornersGaganImg;
import com.ameba.muser.Drawer;
import com.ameba.muser.R;

public class Invite_Others_Twitter_Adapter extends BaseAdapter
{
	Context con;
	public ArrayList<HashMap<String, String>> list;
	
	ArrayList<Boolean> check_list; 
	boolean check_uncheck;
	Fragment con2;
	

	public Invite_Others_Twitter_Adapter(Context con ,Fragment con2,ArrayList<HashMap<String, String>> list,boolean check_uncheck)
	{
		this.con = con;
		this.con2 = con2;
		this.list=list;
		this.check_uncheck=check_uncheck;
		
		check_list=new ArrayList<Boolean>();
		for(int i = 0; i < list.size(); i++)
		{
			check_list.add(check_uncheck);
		}
		
		Log.e("check_list", ""+check_list);
	}

	public void do_refresh()
	{

		for(int i = 0; i < list.size(); i++)
		{
			check_list.set(i, false);
		}

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
		
		row = inflater.inflate(R.layout.custom_invite_others_twitter, parent, false);
		
		RoundedCornersGaganImg image=(RoundedCornersGaganImg)row.findViewById(R.id.image);
		TextView name=(TextView)row.findViewById(R.id.name);
		final ImageView check=(ImageView)row.findViewById(R.id.check);
		
		
		image.setImageUrl(con, list.get(position).get("profile_image_url"));
		
//		Drawer.imageLoader.displayImage(list.get(position).get("profile_image_url"), image, Drawer.options);
		name.setText(list.get(position).get("screen_name"));


		row.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				
				check_list.set(position, !check_list.get(position));


				if(((Tab_Invite_Others_Twitter) con2).is_cjheck_all)
				{
					((Tab_Invite_Others_Twitter) con2).is_cjheck_all =! ((Tab_Invite_Others_Twitter) con2).is_cjheck_all;
					((Tab_Invite_Others_Twitter) con2).check_all.setImageResource(R.drawable.uncheck_box);
				}

				if(check_list.get(position))
				{
					check.setImageResource(R.drawable.check_box);
				}
				else
				{
					check.setImageResource(R.drawable.uncheck_box);
				}
			}

		});
		

		if(check_list.get(position))
		{
			check.setImageResource(R.drawable.check_box);
		}
		else
		{
			check.setImageResource(R.drawable.uncheck_box);
		}
		
		
		return row;
	}
	
	
	public ArrayList<String>  get_selected_screen_name()
	{
		ArrayList<String> selected_screen_name_list=new ArrayList<String> ();
		
		for(int i = 0; i < check_list.size(); i++)
		{
			
			if(check_list.get(i))
			selected_screen_name_list.add(list.get(i).get("screen_name"));
		}
		
		
		return selected_screen_name_list;
		
	}


	public void addAll(ArrayList<HashMap<String, String>> data)
	{

		list.addAll(data);

		for (int i = 0; i < data.size(); i++)
		{
			check_list.add(false);
		}
	}
	
}
