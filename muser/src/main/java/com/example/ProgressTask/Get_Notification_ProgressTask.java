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

import com.example.Adapter.My_Profile_Pictures_Adapter;
import com.example.Adapter.My_Profile_Videos_Adapter;
import com.example.Adapter.Notification_Adapter;
import com.example.Tabs.Tab_My_Profile_Pictures;
import com.example.Tabs.Tab_My_Profile_Videos;
import com.example.Tabs.Tab_Notification_Connects;
import com.example.Tabs.Tab_Notification_Pictures;
import com.example.Tabs.Tab_Notification_Sessions;
import com.example.Tabs.Tab_Notification_Videos;
import com.example.classes.Global;
import com.example.classes.Util_Class;
import com.ameba.muser.R;

public class Get_Notification_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	//private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;
	String type;
	Fragment con2;

	public Get_Notification_ProgressTask(Context con,Fragment con2, String type)
	{
		this.con = con;
		this.con2=con2;
		this.type = type;
		
		Log.e("type", type);
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
						JSONArray jo = new JSONObject(response).getJSONArray("notification_info");
						list = new ArrayList<HashMap<String, String>>();
						
						if(type.equals("I") || type.equals("V"))
						{
							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("activity_id", obj.getString("activity_id"));
								map.put("activity", obj.getString("activity"));
								map.put("date", obj.getString("added_date"));
								map.put("post_id", obj.getString("post_id"));
								map.put("data", obj.getString("post_image"));
								map.put("user_id", obj.getString("user_id"));
								map.put("showing_name", obj.getString("user_name"));
								map.put("showing_image", obj.getString("profile_image"));
								map.put("extra", obj.getString("extra"));
								
							/*	JSONArray array = obj.getJSONArray("post_info");
//								for(int j = 0; j < array.length(); j++)
							//	{
									JSONObject obj1 = array.getJSONObject(0);
									
									map.put("user_id", obj1.getString("user_id"));
									map.put("user_name", obj1.getString("user_name"));
									map.put("profile_image", obj1.getString("profile_image"));
									map.put("post_id", obj1.getString("post_id"));
									map.put("category_id", obj1.getString("category_id"));
									map.put("data_type", obj1.getString("data_type"));
									map.put("data", obj1.getString("data"));
									map.put("thumbnail", obj1.getString("thumbnail"));
									map.put("caption", obj1.getString("caption"));
									map.put("tag", obj1.getString("tag"));
									map.put("latitude", obj1.getString("latitude"));
									map.put("longitude", obj1.getString("longitude"));
									map.put("duration", obj1.getString("duration"));
									map.put("fitness_goal", obj1.getString("fitness_goal"));
									map.put("like_count", obj1.getString("like_count"));
									map.put("comment_count", obj1.getString("comment_count"));
									map.put("date", obj1.getString("date"));
									map.put("focus_id", obj1.getString("focus_id"));
									map.put("focus_name", obj1.getString("focus_name"));
									map.put("is_self_liked", obj1.getString("is_self_liked"));
									map.put("is_self_favourite", obj1.getString("is_self_favourite"));
								//}
*/								list.add(map);
							}
						}
						else if (type.equals("S"))
						{
							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("activity_id", obj.getString("activity_id"));
								map.put("activity", obj.getString("activity"));
								map.put("date", obj.getString("added_date"));
								map.put("post_id", obj.getString("post_id"));
								//map.put("data", obj.getString("post_image"));
								map.put("user_id", obj.getString("user_id"));
								map.put("user_name", obj.getString("user_name"));
								map.put("profile_image", obj.getString("profile_image"));
								//map.put("extra", obj.getString("extra"));
								
								list.add(map);
							}
						}
						else if (type.equals("C"))
						{
							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("activity_id", obj.getString("activity_id"));
								map.put("activity", obj.getString("activity"));
								map.put("date", obj.getString("added_date"));
								map.put("approve_status", obj.getString("approve_status"));
								
								map.put("to_user_id", obj.getJSONObject("to").getString("user_id"));
								map.put("to_user_name", obj.getJSONObject("to").getString("user_name"));
								map.put("to_profile_image", obj.getJSONObject("to").getString("profile_image"));
								
								map.put("from_user_id", obj.getJSONObject("from").getString("user_id"));
								map.put("from_user_name", obj.getJSONObject("from").getString("user_name"));
								map.put("from_profile_image", obj.getJSONObject("from").getString("profile_image"));
								
								
							

								list.add(map);
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
		/*if(dialog.isShowing())
		{
			dialog.dismiss();
		}*/
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				new Get_Notification_ProgressTask(con,con2, type).execute();
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
			
			
			if(type.equals("I"))
			{
				
				((Tab_Notification_Pictures) con2).set_data(list);
			}
			else if(type.equals("V"))
			{
				((Tab_Notification_Videos) con2).set_data(list);
//				Notification_Adapter adapter = new Notification_Adapter(con, list,"V");
//				Tab_Notification_Videos.notification_listview.setAdapter(adapter);
			}
			else if(type.equals("S"))
			{
				((Tab_Notification_Sessions) con2).set_data(list);
//				Notification_Adapter adapter = new Notification_Adapter(con, list,"S");
//				Tab_Notification_Sessions.notification_listview.setAdapter(adapter);
			}
			else if(type.equals("C"))
			{
				((Tab_Notification_Connects) con2).set_data(list);
//				Notification_Adapter adapter = new Notification_Adapter(con, list,"C");
//				Tab_Notification_Connects.notification_listview.setAdapter(adapter);
			}
		}
		else if(message.equals("Failure"))
		{ 
			if(type.equals("I"))
			{
				((Tab_Notification_Pictures) con2).on_Failure();
//				Tab_Notification_Pictures.notification_listview.setVisibility(View.GONE);
//				Tab_Notification_Pictures.error_message.setVisibility(View.VISIBLE);
//				Tab_Notification_Pictures.error_message.setText(con.getResources().getString(R.string.no_data_found));

			}
			else if(type.equals("V"))
			{
				((Tab_Notification_Videos) con2).on_Failure();
//				Tab_Notification_Videos.notification_listview.setVisibility(View.GONE);
//				Tab_Notification_Videos.error_message.setVisibility(View.VISIBLE);
//				Tab_Notification_Videos.error_message.setText(con.getResources().getString(R.string.no_data_found));
			}
			else if(type.equals("S"))
			{
				((Tab_Notification_Sessions) con2).on_Failure();
//				Tab_Notification_Sessions.notification_listview.setVisibility(View.GONE);
//				Tab_Notification_Sessions.error_message.setVisibility(View.VISIBLE);
//				Tab_Notification_Sessions.error_message.setText(con.getResources().getString(R.string.no_data_found));
			}
			else
			{
				((Tab_Notification_Connects) con2).on_Failure();
//				Tab_Notification_Connects.notification_listview.setVisibility(View.GONE);
//				Tab_Notification_Connects.error_message.setVisibility(View.VISIBLE);
//				Tab_Notification_Connects.error_message.setText(con.getResources().getString(R.string.no_data_found));
			}
			
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
	}

	public String get_pictures()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_notifications);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			param.add(new BasicNameValuePair("type", type));
			param.add(new BasicNameValuePair("member_type", rem_pref.getString("member_type", "")));
			
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
