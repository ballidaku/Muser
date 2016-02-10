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

import com.example.Adapter.Search_Hashtags_Adapter;
import com.example.Adapter.Search_Hashtags_Child_Adapter;
import com.example.Adapter.Search_User_Adapter;
import com.example.Tabs.Tab_Search_Hashtags;
import com.example.Tabs.Tab_Search_Users;
import com.example.classes.Util_Class;
import com.ameba.muser.R;
import com.ameba.muser.Search_Hashtags_Child;

public class Search_Child_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	SharedPreferences rem_pref;
	 ArrayList<HashMap<String, String>> user_list; 

	 ArrayList<HashMap<String, String>> hashtags_list_child;
	String message = null, msg_json, value, type;

	ProgressDialog dialog;
	public Search_Child_ProgressTask(Context con, String value, String type)
	{
		this.con = con;
		this.value = value;
		this.type = type;

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
		 dialog = ProgressDialog.show(con, "", ""); 
		 dialog.setContentView(R.layout.main); 
		 dialog.show(); 
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = get_search();
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


						JSONArray jo = new JSONObject(response).getJSONArray("post_info");

						String s=""+jo.length();
						if(type.equals("T"))
						{
							hashtags_list_child=new ArrayList<>();
							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<>();
								map.put("user_id", obj.getString("user_id"));
								map.put("user_name", obj.getString("user_name"));
								map.put("profile_image", obj.getString("profile_image"));
								map.put("post_id", obj.getString("post_id"));
								map.put("trainer_id", obj.getString("trainer_id"));
								map.put("trainer_name", obj.getString("trainer_name"));
								
								map.put("category_id", obj.getString("category_id"));
								map.put("category_name", obj.getString("category_name"));
								map.put("data_type", obj.getString("data_type"));
								map.put("data", obj.getString("data"));
								map.put("thumbnail", obj.getString("thumbnail"));
								map.put("duration", obj.getString("duration"));
								map.put("caption", obj.getString("caption"));
								map.put("tag", obj.getString("tag"));
								map.put("latitude", obj.getString("latitude"));
								map.put("longitude", obj.getString("longitude"));
								map.put("location", obj.getString("location"));
								
								map.put("fitness_goal", obj.getString("fitness_goal"));
								map.put("like_count", obj.getString("like_count"));
								map.put("comment_count", obj.getString("comment_count"));
								map.put("date", obj.getString("date"));
								
								map.put("focus_id", obj.getString("focus_id"));
								map.put("focus_name", obj.getString("focus_name"));
								map.put("is_self_liked", obj.getString("is_self_liked"));
								map.put("is_self_favourite", obj.getString("is_self_favourite"));
								
								hashtags_list_child.add(map);
							}
							
							Log.e("hashtags_list_child", ""+hashtags_list_child);
							
							
						}
						else
						{
							user_list = new ArrayList<HashMap<String, String>>();
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
								
								user_list.add(map);
							}
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
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				 new Search_Hashtags_User_ProgressTask(con, value,type,"Search").execute();
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
			if(type.equals("T"))
			{
//				My_Profile_Pictures_Adapter adapter = new My_Profile_Pictures_Adapter(con, list);
//				Tab_My_Profile_Pictures.pictures_gridview.setAdapter(adapter);
				
				
				Search_Hashtags_Child_Adapter adapter = new Search_Hashtags_Child_Adapter(con,hashtags_list_child);
				Search_Hashtags_Child.gridView.setAdapter(adapter);
			}
			/*else
			{
//				My_Profile_Videos_Adapter adapter = new My_Profile_Videos_Adapter(con, list);
//				Tab_My_Profile_Videos.videos_gridview.setAdapter(adapter);
				Search_User_Adapter adapter=new Search_User_Adapter(con,user_list);
				Tab_Search_Users.users_listView.setAdapter(adapter);
			}*/
		}
		else if(message.equals("Failure"))
		{
			/*if(type.equals("T"))
			{
				Tab_Search_Hashtags.hashtags_listView.setVisibility(View.GONE);
				Tab_Search_Hashtags.error_message.setVisibility(View.VISIBLE);

			}
			else
			{
				Tab_Search_Users.users_listView.setVisibility(View.GONE);
				Tab_Search_Users.error_message.setVisibility(View.VISIBLE);
			}*/

			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
	}

	public String get_search()
	{
		HttpPost httppost = new HttpPost(Util_Class.search_child);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{

			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("search_term", value));
			param.add(new BasicNameValuePair("search_type", type));
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			
			Log.e("search child param", param.toString());
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
