package com.ameba.muser;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.Adapter.Find_Friends_Adapter;
import com.example.ProgressTask.Get_Find_Friends_ProgressTask;
import com.example.ProgressTask.Get_Home_Pictures_Videos_ProgressTask;
import com.example.classes.Global;

public class Find_Friends extends Fragment 
{

	Context con;
	public static  ListView find_friends_listView;
	public static TextView error_message;
	View v;
  @Override
   public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
   {

	   if(savedInstanceState==null)
	   {

		    v = inflater.inflate(R.layout.find_friends, container, false);
			con = getActivity();
			find_friends_listView = (ListView) v.findViewById(R.id.find_friends_listView);
		    error_message = (TextView) v.findViewById(R.id.error_message);



		   //  new Get_Find_Friends_ProgressTask(con).execute();


		   if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		   {
			   new Get_Find_Friends_ProgressTask(con).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		   }
		   else
		   {
			   new Get_Find_Friends_ProgressTask(con).execute();
		   }
	   }
    
      return v;
   }
}
