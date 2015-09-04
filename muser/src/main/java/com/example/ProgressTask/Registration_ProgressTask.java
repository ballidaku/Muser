package com.example.ProgressTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.classes.Util_Class;
import com.ameba.muser.Drawer;
import com.ameba.muser.R;
import com.ameba.muser.Registration;




public class Registration_ProgressTask extends AsyncTask<String, Void, Void> 
{
	Context con;
	private ProgressDialog dialog;
	String which, message=null,user_id,msg_json,key;
	String password,user_name,full_name,profile_pic_path,identifier,optional_fb_name,optional_fb_phone;
	String optional_tw_name,optional_tw_phone,optional_ig_name,optional_ig_phone,member_type,privacy_status;
	SharedPreferences rem_pref;

	 
   public Registration_ProgressTask(Context con,
		   							String key,
								    String password,
								    String user_name,
								    String full_name,
								    String profile_pic_path,
								    String identifier,
								    String optional_fb_name,
								    String optional_fb_phone,
								    String optional_tw_name,
								    String optional_tw_phone,
								    String optional_ig_name,
								    String optional_ig_phone,
								    String member_type,
								    String privacy_status)
   {
	    this.con=con;
	    this.key				=key;
	    this.password			=password;
	    this.user_name			=user_name;
	    this.full_name			=full_name;
	    this.profile_pic_path	=profile_pic_path;
	    this.identifier			=identifier;
	    this.optional_fb_name	=optional_fb_name;
	    this.optional_fb_phone	=optional_fb_phone;
	    this.optional_tw_name	=optional_tw_name;
	    this.optional_tw_phone	=optional_tw_phone;
	    this.optional_ig_name	=optional_ig_name;
	    this.optional_ig_phone	=optional_ig_phone;
	    this.member_type		=member_type;
	    this.privacy_status		=privacy_status;
	    
	    Log.e("Registration", "key:"+key +"password "+password+"user_name "+user_name+"full_name "+full_name+"profile_pic_path "+profile_pic_path+"identifier"+identifier+"optional_fb_name"+optional_fb_name+"optional_fb_phone"+optional_fb_phone+"optional_tw_name"+optional_tw_name+"optional_tw_phone"+optional_tw_phone+"optional_ig_name"+optional_ig_name+"optional_ig_phone"+optional_ig_phone+"member_type"+member_type+"privacy_status"+privacy_status);
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
		String response = registration();
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
					Log.e("msg_json", msg_json);
					if(msg_json.equals("Success"))
					{
						message = "Success";
						JSONObject jo = new JSONObject(response).getJSONObject("user_info");
						rem_pref.edit().putString("user_id", jo.getString("user_id")).commit();
						rem_pref.edit().putString("key", jo.getString("key")).commit();
//						rem_pref.edit().putString("password", jo.getString("password")).commit();
						rem_pref.edit().putString("user_name", jo.getString("user_name")).commit();
						rem_pref.edit().putString("privacy_status", jo.getString("privacy_status")).commit();
						rem_pref.edit().putString("user_description", jo.getString("user_description")).commit();
						rem_pref.edit().putString("profile_image", jo.getString("profile_image")).commit();
						rem_pref.edit().putString("web_address", jo.getString("web_address")).commit();
						rem_pref.edit().putString("phone_number", jo.getString("phone_number")).commit();
						rem_pref.edit().putString("registration_type", jo.getString("registration_type")).commit();
						rem_pref.edit().putString("member_type", jo.getString("member_type")).commit();
						rem_pref.edit().putString("full_name", jo.getString("full_name")).commit();
						
						JSONObject jo2 = jo.getJSONObject("optional_info");
						
						rem_pref.edit().putString("facebook_name", jo2.getString("facebook_name")).commit();
						rem_pref.edit().putString("facebook_phone", jo2.getString("facebook_phone")).commit();
						rem_pref.edit().putString("instagram_name", jo2.getString("instagram_name")).commit();
						rem_pref.edit().putString("instagram_phone", jo2.getString("instagram_phone")).commit();
						rem_pref.edit().putString("twitter_name", jo2.getString("twitter_name")).commit();
						rem_pref.edit().putString("twitter_phone", jo2.getString("twitter_phone")).commit();
						
						JSONObject jo3 = jo.getJSONObject("profile_counts");
						
						rem_pref.edit().putString("followers_count", jo3.getString("followers_count")).commit();
						rem_pref.edit().putString("following_count", jo3.getString("following_count")).commit();
						rem_pref.edit().putString("post_count", jo3.getString("post_count")).commit();
						
						Log.e("Preference", "" + rem_pref.getAll());
					}
					else if(msg_json.equals("Failure"))
					{
						message = "Failure";
					}
					else if(msg_json.equals("Failure1"))
					{
						message = "Failure1";
					}
					else if(msg_json.equals("Failure2"))
					{
						message = "Failure2";
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
       
       OnClickListener retry=new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Util_Class.internet_dialog.dismiss();
				
				new Registration_ProgressTask( con,
												user_id,
						        				password,
						        				user_name,
						        				full_name,
						        				profile_pic_path,
						        				identifier,
						        				optional_fb_name,
						        				optional_fb_phone,
						        				optional_tw_name,
						        				optional_tw_phone,
						        				optional_ig_name,
						        				optional_ig_phone,
						        				member_type,
						        				privacy_status).execute();
			
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
			Util_Class.show_Toast("Successfully registered.", con);
			rem_pref.edit().putString("current_frag", "Home").apply();
			con.startActivity(new Intent(con, Drawer.class));
			((Activity) con).finish();
		}
		else if(message.equals("Failure"))
		{
			if(identifier.equals("M"))
			{
				Util_Class.show_global_dialog(con, con.getResources().getString(R.string.already_registered_email));
			}
			else
			{
				Util_Class.show_global_dialog(con, con.getResources().getString(R.string.already_registered_social));
			}
		}
		else if(message.equals("Failure1"))
		{
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.unsuccessfull_registration));
		}
		else if(message.equals("Failure2"))
		{
			Util_Class.show_global_dialog(con, con.getResources().getString(R.string.user_already_registered));
		}
	}

	public String registration()
	{
		HttpPost httppost = new HttpPost(Util_Class.registration);
		HttpParams httpParameters = new BasicHttpParams();
		//HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
		//HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			reqEntity.addPart("key", new StringBody(key));
			reqEntity.addPart("password", new StringBody(password));
			reqEntity.addPart("user_name", new StringBody(user_name));
			reqEntity.addPart("full_name", new StringBody(full_name));
			if(Registration.profile_pic_bitmap != null)
			{
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Registration.profile_pic_bitmap.compress(CompressFormat.PNG, 100, baos);
				byte[] byte_arr = baos.toByteArray();
				ByteArrayBody bab = new ByteArrayBody(byte_arr, "profile_pic.png");
				reqEntity.addPart("profile_image", bab);
			}
			else
			{
				InputStream in = new URL(profile_pic_path).openStream();
				Bitmap bmp = BitmapFactory.decodeStream(in);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.PNG, 100, baos);
				byte[] byte_arr = baos.toByteArray();
				ByteArrayBody bab = new ByteArrayBody(byte_arr, "profile_pic.png");
				reqEntity.addPart("profile_image", bab);
			}
			reqEntity.addPart("privacy_status", new StringBody(privacy_status));
			reqEntity.addPart("device_id", new StringBody(rem_pref.getString("GCM_Reg_id", "")));
			reqEntity.addPart("registration_type", new StringBody(identifier));
			reqEntity.addPart("facebook_name", new StringBody(optional_fb_name));
			reqEntity.addPart("facebook_phone", new StringBody(optional_fb_phone));
			reqEntity.addPart("twitter_name", new StringBody(optional_tw_name));
			reqEntity.addPart("twitter_phone", new StringBody(optional_tw_phone));
			reqEntity.addPart("instagram_name", new StringBody(optional_ig_name));
			reqEntity.addPart("instagram_phone", new StringBody(optional_ig_phone));
			reqEntity.addPart("member_type", new StringBody(member_type));
			reqEntity.addPart("privacy_status", new StringBody(privacy_status));
			reqEntity.addPart("device_type", new StringBody("A"));
			Log.e("reqEntity", "" + reqEntity);
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
