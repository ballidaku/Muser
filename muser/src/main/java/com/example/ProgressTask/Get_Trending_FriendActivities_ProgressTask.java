package com.example.ProgressTask;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Adapter.Notification_Adapter;
import com.example.Adapter.Trending_Pictures_Adapter;
import com.example.Adapter.Trending_Videos_Adapter;
import com.example.Tabs.Tab_Home_Pictures;
import com.example.Tabs.Tab_My_Profile_Pictures;
import com.example.Tabs.Tab_Notification_Pictures;
import com.example.Tabs.Tab_Trending_Friends;
import com.example.Tabs.Tab_Trending_Pictures;
import com.example.Tabs.Tab_Trending_Videos;
import com.example.classes.Util_Class;
import com.ameba.muser.R;

public class Get_Trending_FriendActivities_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	Fragment con2;
	//private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;


	public Get_Trending_FriendActivities_ProgressTask(Context con, Fragment con2)
	{
		this.con = con;
		this.con2=con2;
		
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
		/*dialog = ProgressDialog.show(con, "", "");
		dialog.setContentView(R.layout.main);
		dialog.show();*/
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = get_pictures();
		try
		{
			if(response != null)
			{
				if(response.equals("Slow"))
				{
					message = "Slow";
				}
				else
				{
					msg_json = new JSONObject(response).getString("status");
					if(msg_json.equals("Success"))
					{
						message = "Success";
						JSONArray jo = new JSONObject(response).getJSONArray("activity_info");
						list = new ArrayList<HashMap<String, String>>();
						for(int i = 0; i < jo.length(); i++)
						{
							JSONObject obj = jo.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("activity_id", obj.getString("activity_id"));
							map.put("activity", obj.getString("activity"));
							map.put("date", obj.getString("added_date"));
							map.put("post_id", obj.getString("post_id"));
							map.put("post_image", obj.getString("post_image"));
							map.put("user_id", obj.getString("user_id"));
							map.put("friend_id", obj.getString("friend_id"));
							map.put("user_name", obj.getString("user_name"));
							map.put("profile_image", obj.getString("profile_image"));
							map.put("extra", obj.getString("extra"));
							
							list.add(map);
						}
					}
					else
					{
						message = "Failure";
						// message = null;
					}
				}
			}
			else
			{
				message = "null";
			}
		}
		catch(JSONException e)
		{
			e.printStackTrace();
			message = "null";
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result)
	{
		/*if(dialog.isShowing())
		{
			dialog.dismiss();
		}*/
		
		((Tab_Trending_Friends) con2).refresh_complete();
		
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				new Get_Trending_FriendActivities_ProgressTask(con,con2).execute();
			}
		};
		System.out.println("message-->" + message);
		if(message.equals("null"))
		{
			
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.server_error));
		}
		else if(message.equals("Slow"))
		{
			
			Util_Class.show_internet_dialog(con, retry);
		}
		else if(message.equals("Success"))
		{
			((Tab_Trending_Friends) con2).set_data(list);
			

		}
		else if(message.equals("Failure"))
		{
			
			((Tab_Trending_Friends) con2).on_Failure();
			   /* Tab_Trending_Friends.listview.setVisibility(View.GONE);
				Tab_Trending_Friends.error_message.setVisibility(View.VISIBLE);
				Tab_Trending_Friends.error_message.setText(con.getResources().getString(R.string.no_data_found));*/

		
		}
	}

	public String get_pictures()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_trending_friendactivities);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
//			param.add(new BasicNameValuePair("member_type", rem_pref.getString("member_type", "")));
			Log.e("trending param", param.toString());
			httppost.setEntity(new UrlEncodedFormEntity(param));
			HttpResponse response = httpclient.execute(httppost);
			String data = EntityUtils.toString(response.getEntity());
			Log.e("DATAAA", data);
			return data;
		}
		catch(SocketTimeoutException e)
		{
			e.printStackTrace();
			return "Slow";
		}
		catch(ConnectTimeoutException e)
		{
			e.printStackTrace();
			return "Slow";
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			return "null";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "Slow";
		}
	}
}
