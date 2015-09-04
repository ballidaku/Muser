package com.example.ProgressTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.classes.Util_Class;

public class Make_User_Public_Private_Thread
{
	Context con;
	String public_private;
	SharedPreferences rem_pref;
	
	public Make_User_Public_Private_Thread(Context con, String public_private)
	{
		
		this.con = con;
		background.start();
		this.public_private=public_private;
		 rem_pref=con.getSharedPreferences("Remember",con.MODE_WORLD_READABLE);
	}

	Thread background = new Thread(new Runnable()
	{

		

		// After call for background.start this run method call
		public void run()
		{
			try
			{
				HttpPost httppost = new HttpPost(Util_Class.make_public_private);
				HttpParams httpParameters = new BasicHttpParams();
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				param.add(new BasicNameValuePair("status", public_private));
				
				httppost.setEntity(new UrlEncodedFormEntity(param));
				
				DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse response = httpclient.execute(httppost);
				String data = EntityUtils.toString(response.getEntity());
				Log.e("DATAAA", data);
				threadMsg(data);

			}
			catch(Throwable t)
			{
				// just end the background thread
				Log.i("Make_User_Public_Private_Thread", "Thread  exception " + t);
			}
		}

		private void threadMsg(String msg)
		{

			if(!msg.equals(null) && !msg.equals(""))
			{
				Message msgObj = handler.obtainMessage();
				Bundle b = new Bundle();
				b.putString("message", msg);
				msgObj.setData(b);
				handler.sendMessage(msgObj);
			}
		}

		// Define the Handler that receives messages from the thread and update the progress
		private final Handler handler = new Handler()
		{

			public void handleMessage(Message msg)
			{

				String response = msg.getData().getString("message");

				if((null != response))
				{
					//Log.e("response", response);
					try
					{
						String msg_json = new JSONObject(response).getString("status");
						if(msg_json.equals("Success"))
						{
							rem_pref.edit().putString("privacy_status",public_private).commit();
							Util_Class.show_Toast(public_private.equals("PB")?"Your profile is now visible to the wondering eyes.":"Your profile is now private.", con);

						}
					}
					catch(JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				else
				{
//					Toast.makeText(con, "Not Got Response From Server.", Toast.LENGTH_SHORT).show();
				}

			}
		};

	});

	
}
