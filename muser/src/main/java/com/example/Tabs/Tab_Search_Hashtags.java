package com.example.Tabs;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.Adapter.Search_Hashtags_Adapter;
import com.example.ProgressTask.Get_Home_Pictures_Videos_ProgressTask;
import com.example.ProgressTask.Search_Hashtags_User_ProgressTask;
import com.example.classes.Global;
import com.example.classes.Util_Class;
import com.ameba.muser.R;

public class Tab_Search_Hashtags extends Fragment
{

	EditText search_editText;
	public static ListView hashtags_listView;
	public static TextView error_message;

	Context con;
	View rootView;

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		con = getActivity();

		if(savedInstanceState==null)
		{
			rootView = inflater.inflate(R.layout.tab_search_hashtags, container, false);
			search_editText = (EditText) rootView.findViewById(R.id.search_editText);
			hashtags_listView = (ListView) rootView.findViewById(R.id.hashtags_listView);
			error_message = (TextView) rootView.findViewById(R.id.error_message);

		}

		search_editText.setOnEditorActionListener(new OnEditorActionListener()
		{
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
			{
				if(actionId == EditorInfo.IME_ACTION_SEARCH)
				{
					get_data_and_hit();
					
					return true;
				}
				return false;
			}
		});
		
		
		search_editText.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					if(event.getRawX() >= (search_editText.getRight() - search_editText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
					{
						get_data_and_hit();

						return true;
					}
				}
				return false;
			}
		});

			

		return rootView;
	}
	
	public void 	get_data_and_hit()
	{
		String value = search_editText.getText().toString().trim();

		if(value.length() > 0)
		{
			hashtags_listView.setVisibility(View.VISIBLE);
			error_message.setVisibility(View.GONE);

		//	new Search_Hashtags_User_ProgressTask(con, value, "T","Search").execute();


			if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
			{
				new Search_Hashtags_User_ProgressTask(con, value, "T","Search").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
			else
			{
				new Search_Hashtags_User_ProgressTask(con, value, "T","Search").execute();
			}

			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(search_editText.getWindowToken(), 0);
		}
		else
		{
			Util_Class.show_Toast("Please enter search value.", con);
		}
	}

}