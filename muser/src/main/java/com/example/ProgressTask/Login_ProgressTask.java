package com.example.ProgressTask;


import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.Login;
import com.ameba.muser.R;
import com.ameba.muser.Registration;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;



public class Login_ProgressTask extends AsyncTask<String, Void, Void> 
{
	Context con;
	ProgressDialog dialog;
	String type="",key="", message=null,user_id,msg_json,password,image_path,user_name,full_name;
    SharedPreferences rem_pref;
//    public OnTaskCompleted listener=null;
	 
   public Login_ProgressTask(Context con,
		   					String key, 
		   					String password, 
		   					String type,
		   					String image_path,
		   					String user_name,
		   					String full_name) 
   {
	   this.con=con;
	   this.key=key;
	   this.type=type;
	   this.image_path=image_path;
	   this.user_name=user_name;
	   this.full_name=full_name;
	   
	   if(type.equals("M"))
	   {
		   this.password=password;
	   }
	   else
	   {
		   this.password="";
	   }
	   
	   
	   Log.e("Login", "key:" + key + ",type:" + type + ",image_path:" + image_path + "" +
			   ",user_name:" + user_name + ",full_name:" + full_name + ",password:" + password);
	   rem_pref=con.getSharedPreferences("Remember",con.MODE_WORLD_READABLE);
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
		String response = login();
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
						rem_pref.edit().putString("user_id", jo.getString("user_id")).apply();
						rem_pref.edit().putString("key", jo.getString("key")).apply();
//						rem_pref.edit().putString("password", jo.getString("password")).commit();
						rem_pref.edit().putString("user_name", jo.getString("user_name")).apply();
						rem_pref.edit().putString("privacy_status", jo.getString("privacy_status")).apply();
						rem_pref.edit().putString("user_description", jo.getString("user_description")).apply();
						rem_pref.edit().putString("profile_image", jo.getString("profile_image")).apply();
						rem_pref.edit().putString("web_address", jo.getString("web_address")).apply();
						rem_pref.edit().putString("phone_number", jo.getString("phone_number")).apply();
						rem_pref.edit().putString("registration_type", jo.getString("registration_type")).apply();
						rem_pref.edit().putString("member_type", jo.getString("member_type")).apply();
						rem_pref.edit().putString("full_name", jo.getString("full_name")).apply();
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
					}
					else 
					{
						 message=msg_json;
						
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
				new Login_ProgressTask(con, key, password, type, image_path, user_name, full_name).execute();
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
			
			Util_Class.show_Toast("Login Successfull", con);
			rem_pref.edit().putString("current_frag", "Home").apply();
			con.startActivity(new Intent(con, Drawer.class));
			((Activity) con).finish();
		}
		else if(message.equals("Failure"))
		{
			if(type.equals("M"))
			{
				
				Util_Class.show_global_dialog(con, con.getResources().getString(R.string.email_not_exists));
			}
			else
			{
				Intent intent = new Intent(con, Registration.class);
				intent.putExtra("image_path", image_path);
				intent.putExtra("user_id", key);
				intent.putExtra("user_name", user_name);
				intent.putExtra("full_name", full_name);
				intent.putExtra("identifier", type);
				con.startActivity(intent);
				((Activity) con).finish();
			}
		}
		else if(message.equals("Failure1"))
		{
			
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.email_password_error));
		
		}
		else if(message.equals("Failure2"))
		{
			
			Util_Class.show_global_dialog(con, "User has already registered in 2 devices.");


			if(type.equals("F"))
			{
				((Login)con).stop_fb();
			}
		}
	}

	public String login()
	{
		HttpPost httppost = new HttpPost(Util_Class.login);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("key", key));
			param.add(new BasicNameValuePair("registration_type", type));
			param.add(new BasicNameValuePair("password", password));
			param.add(new BasicNameValuePair("device_id", rem_pref.getString("GCM_Reg_id", "")));
			param.add(new BasicNameValuePair("device_type", "A"));

			if(type.equals("F"))
			{
				param.add(new BasicNameValuePair("user_name", user_name));
			}


			Log.e("login_progressTask param",param.toString());
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
