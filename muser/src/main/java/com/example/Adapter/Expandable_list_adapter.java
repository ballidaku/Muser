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

public class Expandable_list_adapter extends BaseExpandableListAdapter
{

    private Context con;
    ArrayList<Expandable_collection> expandable_list;


    public Expandable_list_adapter(Context con, ArrayList<Expandable_collection> expandable_list)
    {
        this.con = con;
        this.expandable_list = expandable_list;

    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {

        return expandable_list.get(groupPosition).get_child_list().get(childPosititon).get("user_name");
    }


    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

        //String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this.con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.custom_common_gridview, null);

            PullToRefreshGridView gridView = (PullToRefreshGridView) convertView.findViewById(R.id.gridView);
		

            ( convertView.findViewById(R.id.temp_logo)).setVisibility(View.GONE);

            ( convertView.findViewById(R.id.error_message)).setVisibility(View.GONE);

            My_Favourite_User_Adapter adapter = new My_Favourite_User_Adapter(con, expandable_list.get(groupPosition).get_child_list());
            gridView.setAdapter(adapter);
        }








        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        if(this.expandable_list.get(groupPosition).get_child_list().size()>0)
        {
            return 1;
        }
        else
        {
            return this.expandable_list.get(groupPosition).get_child_list().size();
        }

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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null)
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
