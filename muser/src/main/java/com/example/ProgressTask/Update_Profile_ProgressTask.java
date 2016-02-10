package com.example.ProgressTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.classes.Util_Class;
import com.ameba.muser.My_Profile;
import com.ameba.muser.R;
import com.ameba.muser.Update_Profile;

public class Update_Profile_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	private ProgressDialog dialog;
	String message = null, msg_json;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;
	String update_full_name, quote, web_address, email, phone, old_password,new_password, paypal_id;
//	  ImageLoader imageLoader = ImageLoader.getInstance();
	  
	public Update_Profile_ProgressTask(Context con,
										String update_full_name,
										String quote,
										String web_address,
										String email,
										String phone,
										String old_password,
										String new_password,
										String paypal_id)
	{
		this.con = con;
		
		 this.update_full_name=update_full_name;
		 this.quote=quote;
		 this.web_address=web_address;
		 this.email=email;
		 this.phone=phone;
		 this.old_password=old_password;
		 this.new_password=new_password;
		 this.paypal_id=paypal_id;
		
		Log.e("paypal_id", paypal_id);
		
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
		String response = update_profile();
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
						//rem_pref.edit().putString("password", jo.getString("password")).apply();
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
					}
					else
					{
						message = msg_json;
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
				new Update_Profile_ProgressTask( con,
												 update_full_name,
												 quote,
												 web_address,
												 email,
												 phone,
												 old_password,
												 new_password,
												 paypal_id).execute();
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
			Util_Class.show_Toast(con.getResources().getString(R.string.data_uploaded_successfull), con);

			((Activity) con).finish();
			
		}
		else if(message.equals("Failure"))
		{
			Util_Class.show_global_dialog(con,con.getResources().getString(R.string.unsuccessfull));
		}
		else if(message.equals("Failure1"))
		{
			Util_Class.show_global_dialog(con,"Passwords did not match.");
		}
	}

	public String update_profile()
	{
		HttpPost httppost = new HttpPost(Util_Class.update_profile);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			
			if(Update_Profile.update_profile_bitmap != null)
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Update_Profile.update_profile_bitmap.compress(CompressFormat.PNG, 100, baos);
				byte[] byte_arr = baos.toByteArray();
				ByteArrayBody bab = new ByteArrayBody(byte_arr, "profile_pic.png");
				reqEntity.addPart("profile_image", bab);
			}
			else
			{
				InputStream in = new URL(rem_pref.getString("profile_image", "")).openStream();
				Bitmap bmp = BitmapFactory.decodeStream(in);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.PNG, 100, baos);
				byte[] byte_arr = baos.toByteArray();
				ByteArrayBody bab = new ByteArrayBody(byte_arr, "profile_pic.png");
				reqEntity.addPart("profile_image", bab);
			}
			reqEntity.addPart("full_name", new StringBody(update_full_name));
			//reqEntity.addPart("user_name", new StringBody(update_user_name));
			reqEntity.addPart("user_description", new StringBody(quote));
			reqEntity.addPart("web_address", new StringBody(web_address));
			
			reqEntity.addPart("key", new StringBody(email));
			reqEntity.addPart("phone_number", new StringBody(phone));
			reqEntity.addPart("old_password", new StringBody(old_password));
			reqEntity.addPart("new_password", new StringBody(new_password));



			
			reqEntity.addPart("privacy_status", new StringBody(rem_pref.getString("privacy_status","")));
			reqEntity.addPart("device_id", new StringBody(rem_pref.getString("GCM_Reg_id", "")));
			reqEntity.addPart("user_id", new StringBody(rem_pref.getString("user_id", "")));

			reqEntity.addPart("paypal_id", new StringBody(paypal_id));

			//Log.e("param", "" + reqEntity.getContent().toString());

			httppost.setEntity(reqEntity);
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
