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

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Adapter.Blocks_Adapter;
import com.example.Adapter.Find_Friends_Adapter;
import com.example.Adapter.Followers_Adapter;
import com.example.Adapter.Following_Adapter;
import com.example.Tabs.Tab_My_Profile_Videos;
import com.example.classes.Global;
import com.example.classes.Util_Class;
import com.ameba.muser.Blocks;
import com.ameba.muser.Find_Friends;
import com.ameba.muser.Followers;
import com.ameba.muser.Following;
import com.ameba.muser.R;

public class Get_Find_Friends_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
//	private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;

	public Get_Find_Friends_ProgressTask(Context con)
	{
		this.con = con;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
//		dialog = ProgressDialog.show(con, "", "");
//		dialog.setContentView(R.layout.main);
//		dialog.show();
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = get_find_friends();
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
						JSONArray jo = new JSONObject(response).getJSONArray("user_info");
						list = new ArrayList<HashMap<String, String>>();
						for(int i = 0; i < jo.length(); i++)
						{
							JSONObject obj = jo.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("user_id", obj.getString("user_id"));
							map.put("key", obj.getString("key"));
							map.put("web_address", obj.getString("web_address"));
							map.put("phone_number", obj.getString("phone_number"));
							map.put("user_name", obj.getString("user_name"));
							map.put("full_name", obj.getString("full_name"));
							map.put("user_description", obj.getString("user_description"));
							map.put("profile_image", obj.getString("profile_image"));
							map.put("privacy_status", obj.getString("privacy_status"));
//							map.put("device_id", obj.getString("device_id"));
							map.put("registration_type", obj.getString("registration_type"));
							map.put("member_type", obj.getString("member_type"));
							
							JSONObject jo2 = obj.getJSONObject("optional_info");
							
							map.put("facebook_name", jo2.getString("facebook_name"));
							map.put("facebook_phone", jo2.getString("facebook_phone"));
							map.put("instagram_name", jo2.getString("instagram_name"));
							map.put("instagram_phone", jo2.getString("instagram_phone"));
							map.put("twitter_name", jo2.getString("twitter_name"));
							map.put("twitter_phone", jo2.getString("twitter_phone"));
							
							JSONObject jo3 = obj.getJSONObject("profile_counts");
							
							map.put("followers_count", jo3.getString("followers_count"));
							map.put("following_count", jo3.getString("following_count"));
							map.put("post_count", jo3.getString("post_count"));
						
							list.add(map);
						}
					}
					else if(msg_json.equals("Failure"))
					{
						list = new ArrayList<HashMap<String, String>>();
						message = "Failure";
					}
					else if(msg_json.equals("Failure1"))
					{
						message = "Failure1";
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
//		if(dialog.isShowing())
//		{
//			dialog.dismiss();
//		}
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				new Get_Find_Friends_ProgressTask(con).execute();
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
		
			Find_Friends_Adapter adapter=new Find_Friends_Adapter(con,list);
			Find_Friends.find_friends_listView.setAdapter(adapter);
			
		}
		else if(message.equals("Failure"))
		{
			Find_Friends.find_friends_listView.setVisibility(View.GONE);
			Find_Friends.error_message.setVisibility(View.VISIBLE);
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
		/*else if(message.equals("Failure"))
		{
			
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}*/
		
	}

	public String get_find_friends()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_find_friends);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id",rem_pref.getString("user_id", "")));
			
			Log.e("login_progressTask param", param.toString());
			
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
