package com.example.Adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.Tabs.Tab_My_Favourite_User;
import com.example.classes.Expandable_collection;
import com.ameba.muser.R;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class Expandable_list_adapter  extends BaseExpandableListAdapter 
{
	
	    private Context con;
	    ArrayList<Expandable_collection> expandable_list;
	   /* private ArrayList<String> _listDataHeader; // header titles
	    
	    private HashMap<String, ArrayList<String>> _listDataChild;
	    private HashMap<String, ArrayList<String>> _listDataChild_r;
	    
	    ArrayList<HashMap<String, String>> list_edit;*/
	    /*int z=0;
	  
	    String childText , childText_r;
	    
	   Button txtListChild_r;
	    AlertDialog levelDialog;
	    View row;
	  
	    String header_name;
	    
	   static ArrayList<String> child1,child2;
	   static ArrayList<String> child_main;
	   
	   HashMap<String, ArrayList<String>> listDataChild2;
	   
	   ArrayList<HashMap<String, String>> static_list ;
	 // HashMap<String, ArrayList<String>> list_donnes;
	   String id;
	   Search_first search1=new Search_first();
	   Search_second search2=new  Search_second();
	  static String head;
	  Dialog dialog;
	  
	 static boolean selected =false;
	 static String key=null;
	   */
	    public Expandable_list_adapter(Context con,ArrayList<Expandable_collection> expandable_list )
	    {
	        this.con = con;
	        this.expandable_list= expandable_list;
	       
	    }
	 
	  
	@Override
	public Object getChild(int groupPosition, int childPosititon)
	{
		//return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
		return expandable_list.get(groupPosition).get_child_list().get(childPosititon).get("user_name");
	}

/*	public Object getChild_r(int groupPosition, int childPosititon)
	{
		return this._listDataChild_r.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
	}*/
	 
	@Override
	public long getChildId(int groupPosition, int childPosition)
	{
		return childPosition;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
	{

		String childText = (String) getChild(groupPosition, childPosition);

		if(convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.custom_common_gridview, null);

		}
		
		PullToRefreshGridView	gridView = (PullToRefreshGridView) convertView.findViewById(R.id.gridView);
		
		/*ImageView temp_logo=*/((ImageView)convertView.findViewById(R.id.temp_logo)).setVisibility(View.GONE);
		/*TextView error_message=*/((TextView)convertView.findViewById(R.id.error_message)).setVisibility(View.GONE);
		
		My_Favourite_User_Adapter adapter = new My_Favourite_User_Adapter(con, expandable_list.get(groupPosition).get_child_list());
		gridView.setAdapter(adapter);

		/*TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
		txtListChild.setText(childText);*/

		//  Log.e("Header", ""+header_name);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition)
	{
		//  return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
		return this.expandable_list.get(groupPosition).get_child_list().size();
	}
	 
	@Override
	public Object getGroup(int groupPosition)
	{
		return expandable_list.get(groupPosition).getName();
	}
	 
	    @Override
	    public int getGroupCount() 
	    {
	        return this.expandable_list.size();
	    }
	 
	    @Override
	    public long getGroupId(int groupPosition) 
	    {
	        return groupPosition;
	    }
	 
	    @Override
	    public View getGroupView(int groupPosition, boolean isExpanded,
	            View convertView, ViewGroup parent) 
	    {
	        String headerTitle = (String) getGroup(groupPosition);
	     
		if(convertView == null)
		{
			LayoutInflater infalInflater = (LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_header, null);
		}
	 
	        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
	        lblListHeader.setTypeface(null, Typeface.BOLD);
	        lblListHeader.setText(headerTitle);
	 
	        return convertView;
	    }
	 
	    @Override
	    public boolean hasStableIds() 
	    {
	        return false;
	    }
	 
	    @Override
	    public boolean isChildSelectable(int groupPosition, int childPosition)
{
	        return true;
	    }

}
