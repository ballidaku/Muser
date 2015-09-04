package com.ameba.muser;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.ProgressTask.Get_Recommended_ProgressTask;

public class Recomended extends Fragment
{
	Context con;
	public static ExpandableListView listView;

	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		con=getActivity();

		if(savedInstanceState==null)
		{

			rootView = inflater.inflate(R.layout.recommended_new, container, false);

			listView = (ExpandableListView) rootView.findViewById(R.id.listView);

			new Get_Recommended_ProgressTask(con).execute();
		}
		
		return rootView;
	}
	
	
}
