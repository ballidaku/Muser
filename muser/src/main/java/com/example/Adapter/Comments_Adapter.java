package com.example.Adapter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ameba.muser.R;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.rockerhieu.emojicon.EmojiconTextView;

public class Comments_Adapter extends BaseAdapter/*ArrayAdapter<HashMap<String,String>> */
{
	
	private ArrayList<HashMap<String,String>> list;
	private Context con;
	String from_where;
	
	 
	public  static ArrayList<Boolean> is_selected;
	 View result;
	
	public Comments_Adapter(Context ctx, ArrayList<HashMap<String,String>> comment_list,String from_where) 
	{
		list = comment_list;
		con = ctx;
		this.from_where=from_where;
		
		Log.e("comment_list", ""+comment_list);
		
	}




	@Override
	public int getCount() 
	{		
		return list.size() ;
	}

	@Override
	public Object getItem(int position)
	{		
		return list;
	}

	@Override
	public long getItemId(int position) 
	{		
		return list.get(position).hashCode();
	}

	@Override
	public View getView(final int position, View result, ViewGroup parent) 
	{
		
		
		if (result == null) 
		{
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = inflater.inflate(R.layout.custom_listview_comment, parent, false);
		}
		
		
		
		RoundedCornersGaganImg image=(RoundedCornersGaganImg)result.findViewById(R.id.image);
		final EmojiconTextView data = (EmojiconTextView) result.findViewById(R.id.data);
		TextView time = (TextView) result.findViewById(R.id.time);
		
		
		
		if(from_where.equals("View_All_Comments"))
		{

			image.setImageUrl(con, list.get(position).get("profile_image"));
			//Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), image, Drawer.options);
			Util_Class util = new Util_Class();
			String styledText = "<font color='#4F606A'><b>" + list.get(position).get("user_name") + " </b> </font>";

		//	data.setText(Html.fromHtml(styledText));
			
			try
			{
				data.setText( Html.fromHtml(styledText)+Util_Class.emoji_decode( list.get(position).get("data") ));
			}
			catch(UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			time.setText(util.get_time2(list.get(position).get("date")));
		}
		else if(from_where.equals("Image_Details") || from_where.equals("Image_Video_Details_bypostid"))
		{
			image.setVisibility(View.GONE);
			time.setVisibility(View.GONE);
			String styledText = "<font color='#4F606A'><b>" + list.get(position).get("user_name") + " </b> </font>";

			
			data.setSingleLine(false);
			data.setEllipsize(TruncateAt.END);
			data.setLines(1);
			
			//Html.fromHtml(styledText))
			
			
			try
			{
				
				data.setText(Html.fromHtml(styledText)+Util_Class.emoji_decode( list.get(position).get("data") ));
			}
			catch(UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			time.setVisibility(View.GONE);
			image.setImageUrl(con, list.get(position).get("profile_image"));
			//Drawer.imageLoader.displayImage(list.get(position).get("profile_image"), image, Drawer.options);
			data.setText(list.get(position).get("user_name"));
		}
		
		//Log.e(""+position,""+result.getHeight());
		
		return result;

	}
	/*public ArrayList<String> get(int position)
	{
		ArrayList<String> data=new ArrayList<String>();
		SharedPreferences.Editor editor=global_data.edit();
		editor.putString("tour_id", itemList.get(position).get("tour_id")).commit();
		editor.putString("tour_name",itemList.get(position).get("tour_name")).commit();
		
		data.add(itemList.get(position).get("tour_id"));
		data.add(itemList.get(position).get("tour_name"));
		return data;
	}
	*/
	
	/*public void addAll(ArrayList<HashMap<String, String>> data) 
	{
		
		comment_list.addAll(data);
	}*/
	

}
