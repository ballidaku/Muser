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
import com.example.Adapter.Trending_Pictures_Adapter;
import com.example.Adapter.Trending_Videos_Adapter;
import com.example.Tabs.Tab_Home_Pictures;
import com.example.Tabs.Tab_Home_Videos;
import com.example.Tabs.Tab_My_Profile_Pictures;
import com.example.Tabs.Tab_My_Profile_Videos;
import com.example.Tabs.Tab_Trending_Pictures;
import com.example.Tabs.Tab_Trending_Videos;
import com.example.classes.Util_Class;
import com.ameba.muser.R;

public class Get_Trending_Pictures_Videos_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	Fragment con2;
	//private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;
	String type;

	public Get_Trending_Pictures_Videos_ProgressTask(Context con,Fragment con2, String type)
	{
		this.con = con;
		this.con2 = con2;
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
						JSONArray jo = new JSONObject(response).getJSONArray("post_info");
						list = new ArrayList<HashMap<String, String>>();
						for(int i = 0; i < jo.length(); i++)
						{
							JSONObject obj = jo.getJSONObject(i);
							HashMap<String, String> map = new HashMap<String, String>();
							map.put("user_id", obj.getString("user_id"));
							map.put("user_name", obj.getString("user_name"));
							map.put("profile_image", obj.getString("profile_image"));
							map.put("post_id", obj.getString("post_id"));
							map.put("category_id", obj.getString("category_id"));
							map.put("data_type", obj.getString("data_type"));
							map.put("data", obj.getString("data"));
							map.put("thumbnail", obj.getString("thumbnail"));
							map.put("caption", obj.getString("caption"));
							map.put("tag", obj.getString("tag"));
							map.put("latitude", obj.getString("latitude"));
							map.put("duration", obj.getString("duration"));
							map.put("fitness_goal", obj.getString("fitness_goal"));
							map.put("like_count", obj.getString("like_count"));
							map.put("comment_count", obj.getString("comment_count"));
							map.put("date", obj.getString("date"));
							map.put("is_self_liked", obj.getString("is_self_liked"));
							map.put("is_self_favourite", obj.getString("is_self_favourite"));
							map.put("location", obj.getString("location"));
							map.put("is_self_posted", obj.getString("is_self_posted"));
							
							if(obj.getJSONObject("get_post_owner").getString("is_repost").equals("Y"))
							{
								map.put("is_repost", "Y");
								JSONObject ob=obj.getJSONObject("get_post_owner").getJSONObject("user_info");
								
								map.put("repost_user_id", ob.getString("user_id"));
								map.put("repost_user_name", ob.getString("user_name"));
								map.put("repost_profile_image", ob.getString("profile_image"));
							}
							else
							{
								map.put("is_repost", "N");
							}
							
							if(obj.getJSONObject("tagged_peoples").getString("is_tagged_peoples").equals("Y"))
							{
								map.put("is_tagged", "Y");
								JSONArray array=obj.getJSONObject("tagged_peoples").getJSONArray("tagged_ppl");
								
								String tagged_user_id="",tagged_user_name="";
								for(int j = 0; j < array.length(); j++)
								{
									tagged_user_id += j==0?array.getJSONObject(j).getString("user_id"):","+array.getJSONObject(j).getString("user_id");
									tagged_user_name += j==0?"@"+array.getJSONObject(j).getString("user_name"):", @"+array.getJSONObject(j).getString("user_name");
								}
								Log.e(tagged_user_id, tagged_user_name);
								
								map.put("tagged_user_id", tagged_user_id);
								map.put("tagged_user_name", tagged_user_name);
								/*map.put("repost_user_id", ob.getString("user_id"));
								map.put("repost_user_name", ob.getString("user_name"));
								map.put("repost_profile_image", ob.getString("profile_image"));*/
							}
							else
							{
								map.put("is_tagged", "N");
							}
							
							map.put("is_self_subscribed", obj.getString("is_self_subscribed"));
							map.put("is_self_recommended", obj.getString("is_self_recommended"));
							
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
		
		if(type.equals("I"))
		{
			((Tab_Trending_Pictures) con2).refresh_complete();
		}
		else if(type.equals("V"))
		{
			((Tab_Trending_Videos) con2).refresh_complete();
		}
		
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				new Get_Trending_Pictures_Videos_ProgressTask(con,con2, type).execute();
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
				((Tab_Trending_Pictures) con2).set_data(list);
				/*Trending_Pictures_Adapter adapter=new Trending_Pictures_Adapter(con, list);
				Tab_Trending_Pictures.gridView.setAdapter(adapter);*/
				
//				My_Profile_Pictures_Adapter adapter = new My_Profile_Pictures_Adapter(con, list);
//				Tab_My_Profile_Pictures.pictures_gridview.setAdapter(adapter);
			}
			else
			{
				//My_Profile_Videos_Adapter adapter = new My_Profile_Videos_Adapter(con, list);
				//Tab_My_Profile_Videos.videos_gridview.setAdapter(adapter);
				
				((Tab_Trending_Videos) con2).set_data(list);
				
				/*Trending_Videos_Adapter adapter=new Trending_Videos_Adapter(con, list);
				Tab_Trending_Videos.gridView.setAdapter(adapter);*/
			}
		}
		else if(message.equals("Failure"))
		{
			if(type.equals("I"))
			{
				((Tab_Trending_Pictures) con2).on_Failure();
				/*Tab_Trending_Pictures.gridView.setVisibility(View.GONE);
				Tab_Trending_Pictures.error_message.setVisibility(View.VISIBLE);
				Tab_Trending_Pictures.error_message.setText(con.getResources().getString(R.string.no_data_found));*/

			}
			else
			{
				((Tab_Trending_Videos) con2).on_Failure();
			/*	Tab_Trending_Videos.gridView.setVisibility(View.GONE);
				Tab_Trending_Videos.error_message.setVisibility(View.VISIBLE);
				Tab_Trending_Videos.error_message.setText(con.getResources().getString(R.string.no_data_found));*/
			}
			
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
	}

	public String get_pictures()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_trending_pic_videos);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			param.add(new BasicNameValuePair("post_type", type));
			Log.e("trending param", param.toString());
			httppost.setEntity(new UrlEncodedFormEntity(param));
			HttpResponse response = httpclient.execute(httppost);
			String data = EntityUtils.toString(response.getEntity());
			Log.e(" Trending DATAAA", data);
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
