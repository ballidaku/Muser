package com.example.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameba.muser.R;

public class Trending_Friends_Adapter extends BaseAdapter
{
	Context con;
	
	private int moreImg[] = {   R.drawable.ic_launcher, 
			R.drawable.ic_launcher,
			R.drawable.ic_launcher,
			R.drawable.ic_launcher, 
			R.drawable.ic_launcher,
			R.drawable.ic_launcher, 
			R.drawable.ic_launcher, 
			R.drawable.ic_launcher };
	
	private int imageArray[] = { R.drawable.ic_launcher, 
								R.drawable.ic_launcher,
								R.drawable.ic_launcher, 
								R.drawable.ic_launcher };
	
	private int singleImg[] = { R.drawable.ic_launcher, 
								R.drawable.ic_launcher,
								R.drawable.ic_launcher, 
								R.drawable.ic_launcher };
	
	private String name[] = { "Ram", "Vishnu", "Sita", "Vaani" };

	public Trending_Friends_Adapter(Context con) 
	{
		this.con = con;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return 4;
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
	public View getView(int position, View row, ViewGroup parent) 
	{
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		row = inflater.inflate(R.layout.custom_trending_friends, parent, false);
		
		ImageView friendIMG = (ImageView) row.findViewById(R.id.friendIMG);
		ImageView singleIMG = (ImageView) row.findViewById(R.id.singleImg);
		friendIMG.setImageResource(imageArray[position]);
		singleIMG.setImageResource(singleImg[position]);

		TextView TVfriendName = (TextView) row.findViewById(R.id.TVfriendName);

		TextView TVtime = (TextView) row.findViewById(R.id.TVtime);
		String friendNameStr = name[position];
		int totalPosts = moreImg.length;
		
		String styledText = "<font color='black'><b><big>" + friendNameStr
				+ "</big></b></font> likes " + totalPosts
				+ " posts 23secs. ";
		TVfriendName.setText(Html.fromHtml(styledText),	TextView.BufferType.SPANNABLE);
		// ==============================================

		// =================================================================

		int totalLikeImg = moreImg.length;
		int totalsingleImage = singleImg.length;
		if (totalLikeImg != 1) 
		{

			GridView gridViewObj = (GridView) row.findViewById(R.id.myId);
			
			Trending_Friends_GridView_Adapter adapterObj = new Trending_Friends_GridView_Adapter(con);
			gridViewObj.setAdapter(adapterObj);
			adapterObj.notifyDataSetChanged();
		}
		
		
		return row;
	}
	

}
