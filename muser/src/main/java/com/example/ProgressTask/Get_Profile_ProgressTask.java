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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;

import com.example.classes.Global;
import com.example.classes.Util_Class;
import com.ameba.muser.My_Profile;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

public class Get_Profile_ProgressTask extends AsyncTask<String, Void, Void> 
{
	Context								con;
	//	private ProgressDialog dialog;
	String								device_id, message = null,/*user_id,*/msg_json;
	SharedPreferences					rem_pref;
	String								user_name, followers_count, following_count, user_description, web_address, is_subscribed;

	public static String				profile_image_url;
	public static ArrayList<String>		item	= new ArrayList<String>();
	public static ArrayAdapter<String>	spinner_adapter;
	
	HashMap<String,String> map;
	Fragment con2;

	public Get_Profile_ProgressTask(Context con)
	{
		this.con = con;

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		Log.e("qqqqqqqqqqqqq", con.getClass().getSimpleName());

	}

	public Get_Profile_ProgressTask(Context con, Fragment con2)
	{
		this.con = con;
		this.con2=con2;

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
		
		item.clear();
		item.add("Select");
		item.add("Follow");
		item.add("Block");
		item.add("Add To Favorites");
		item.add("Direct Message");
		
//		dialog = ProgressDialog.show(con, "", "");
//		dialog.setContentView(R.layout.main);
//		dialog.show();
	}
    @Override
	protected Void doInBackground(String... params)
	{
		String response = get_profile();
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
						JSONObject jo = new JSONObject(response).getJSONObject("user_info");
						
						if(con instanceof Other_Profile)
						{
							map=new HashMap<String,String>();
							
							profile_image_url=jo.getString("profile_image");	// for chatting purpose
							is_subscribed=jo.getString("is_subscribed");
							
							
							
							map.put("profile_image_url", profile_image_url);
							map.put("user_name", jo.getString("user_name"));
							map.put("user_description", jo.getString("user_description"));
							map.put("web_address", jo.getString("web_address"));
							map.put("is_subscribed", is_subscribed);
							
//							Global.setIs_public_or_private(jo.getString("privacy_status"));
//							Global.setIs_follow( jo.getString("follow"));
							
							
						//	Log.e("setIs_public_or_private", jo.getString("privacy_status"));
						//	Log.e("setIs_follow", jo.getString("follow"));
							
							//Other_Profile.is_follow=jo.getString("follow").equals("N")?false:true;
							((Other_Profile) con).is_add_to_favorite=jo.getString("favourite").equals("N")?false:true;
							
							if(jo.getString("follow").equals("N"))
							{
								item.set(1,"Follow");
							}
							else if(jo.getString("follow").equals("Y"))
							{
								item.set(1, "Unfollow");
							}
							else if(jo.getString("follow").equals("R"))
							{
								item.set(1, "Cancel Request");
							}
							
							
							item.set(3, jo.getString("favourite").equals("N")? "Add To Favorites":"Remove From Favorites");
							
							if(jo.getString("member_type").equals("T"))
							{
								String value=is_subscribed.equals("Y")?"Remove Training With":"Train With";
								item.add(value);
								
								/*if(is_subscribed.equals("Y"))
								{
									String value1=jo.getString("is_recommended").equals("Y")?"Remove Recommendation":"Recommend Trainer";
									item.add(value1);
								}*/
							}
							
							
							JSONObject j = jo.getJSONObject("profile_counts");
							
							map.put("followers_count", j.getString("followers_count"));
							map.put("following_count", j.getString("following_count"));
							
							
						}
						else
						{
						rem_pref.edit().putString("user_id", jo.getString("user_id")).apply();
						rem_pref.edit().putString("key", jo.getString("key")).apply();
//						rem_pref.edit().putString("password", jo.getString("password")).commit();
						rem_pref.edit().putString("user_name", jo.getString("user_name")).apply();
						rem_pref.edit().putString("full_name", jo.getString("full_name")).apply();
						
						rem_pref.edit().putString("user_description", jo.getString("user_description")).apply();
						rem_pref.edit().putString("profile_image", jo.getString("profile_image")).apply();
						rem_pref.edit().putString("privacy_status", jo.getString("privacy_status")).apply();
						rem_pref.edit().putString("web_address", jo.getString("web_address")).apply();
						rem_pref.edit().putString("phone_number", jo.getString("phone_number")).apply();
						rem_pref.edit().putString("registration_type", jo.getString("registration_type")).apply();
						rem_pref.edit().putString("member_type", jo.getString("member_type")).apply();
							rem_pref.edit().putString("paypal_id", jo.getString("paypal_id")).apply();
						
						JSONObject jo2 = jo.getJSONObject("optional_info");
						rem_pref.edit().putString("facebook_name", jo2.getString("facebook_name")).apply();
							rem_pref.edit().putString("facebook_phone", jo2.getString("facebook_phone")).apply();
						rem_pref.edit().putString("instagram_name", jo2.getString("instagram_name")).apply();
						rem_pref.edit().putString("instagram_phone", jo2.getString("instagram_phone")).apply();
						rem_pref.edit().putString("twitter_name", jo2.getString("twitter_name")).apply();
						rem_pref.edit().putString("twitter_phone", jo2.getString("twitter_phone")).apply();
						JSONObject jo3 = jo.getJSONObject("profile_counts");
						rem_pref.edit().putString("followers_count", jo3.getString("followers_count")).apply();
						rem_pref.edit().putString("following_count", jo3.getString("following_count")).apply();
						rem_pref.edit().putString("post_count", jo3.getString("post_count")).apply();
						Log.e("Preference", "" + rem_pref.getAll());
						
						//These are static values for MY Profile section 
//						Global.setIs_public_or_private("PB");
//						Global.setIs_follow("Y");
						
						}
					}
					else if(msg_json.equals("Failure"))
					{
						 message="Failure";
						
					}
					/*else if(msg_json.equals("Failure1"))
					{
						 message="Failure1";
						
					}*/
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
				new Get_Profile_ProgressTask(con).execute();
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
			if(con instanceof Other_Profile)
			{
				Log.e("dusyfguyref", "fiudgiuewrfgiew");
				((Other_Profile) con).set_data(map);
				
				
				
//				((Other_Profile) con).mPullRefreshScrollView.onRefreshComplete();

				spinner_adapter = new ArrayAdapter<String>(con, R.layout.textview, item);
				((Other_Profile) con).spinner.setAdapter(spinner_adapter);
				/*

				((Other_Profile) con).profile_image.setImageUrl(con, profile_image_url);

				//				Drawer.imageLoader.displayImage(profile_image_url, Other_Profile.profile_image, Drawer.options);
				((Other_Profile) con).user_name.setText(user_name);

				((Other_Profile) con).followers_value.setText(followers_count);
				((Other_Profile) con).following_value.setText(following_count);

				try
				{
					((Other_Profile) con).description.setText(Util_Class.emoji_decode(user_description));
				}
				catch(UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				((Other_Profile) con).web_address.setText(web_address);*/
			}
			else
			{
				((My_Profile) con2).mPullRefreshScrollView.onRefreshComplete();
				
				((My_Profile) con2).set_data();

				/*((My_Profile) con2).profile_image.setImageUrl(con, rem_pref.getString("profile_image", ""));
				//				Drawer.imageLoader.displayImage(rem_pref.getString("profile_image", ""), My_Profile.profile_image, Drawer.options);
				((My_Profile) con2).user_name.setText(rem_pref.getString("user_name", ""));

				((My_Profile) con2).followers_value.setText(rem_pref.getString("followers_count", ""));
				((My_Profile) con2).following_value.setText(rem_pref.getString("following_count", ""));

				try
				{
					((My_Profile) con2).description.setText(Util_Class.emoji_decode(rem_pref.getString("user_description", "")));
				}
				catch(UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				((My_Profile) con2).web_address.setText(rem_pref.getString("web_address", ""));*/

			}
			
		}
		else if(message.equals("Failure"))
		{
			
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		
		}
		/*else if(message.equals("Failure1"))
		{
			
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.email_password_error));
		}*/
	}

	public String get_profile()
	{
		HttpPost httppost = new HttpPost(Util_Class.get_profile);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", Global.get_user_id()));
			
			if(con instanceof Other_Profile)
			{
				param.add(new BasicNameValuePair("friend_id", Global.get_friend_id()));
			}
			
			Log.e("get_profile param", param.toString());
			
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
