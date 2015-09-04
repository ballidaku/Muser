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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ameba.muser.Cancel_Sessions;
import com.ameba.muser.Total_Clients;
import com.example.classes.Util_Class;

public class Get_Toatal_Clients_Thread
{
	Context con;
	SharedPreferences rem_pref;
	
	public Get_Toatal_Clients_Thread(Context con)
	{
		this.con = con;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		background.start();
	}

	Thread background = new Thread(new Runnable()
	{

		// After call for background.start this run method call
		public void run()
		{
			try
			{
				HttpPost httppost = new HttpPost(Util_Class.get_subscription);
				
				HttpParams httpParameters = new BasicHttpParams();
				
				DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
				
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				param.add(new BasicNameValuePair("status", "U"));
				
				httppost.setEntity(new UrlEncodedFormEntity(param));
				
				HttpResponse response = httpclient.execute(httppost);
				String data = EntityUtils.toString(response.getEntity());
				
				System.out.println("DATAAA   :  "+data);
				
				Log.e("DATAAA", data);
				threadMsg(data);

			}
			catch(Throwable t)
			{
				// just end the background thread
				Log.e("get_funds", "Thread  exception " + t);
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
					try
					{
						String msg_json = new JSONObject(response).getString("status");
						if(msg_json.equals("Success"))
						{
							
							JSONArray jo 	= new JSONObject(response).getJSONArray("user_info");
							ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
							
							
							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("user_id", obj.getString("user_id"));
								map.put("user_name", obj.getString("user_name"));
								map.put("profile_image", obj.getString("profile_image"));
								
								
								list.add(map);
							}
							
							((Total_Clients) con).set_data(list);
							
						
						}
						else if(msg_json.equals("Failure"))
						{
							((Total_Clients) con).on_Failure();
						}
					}
					catch(JSONException e)
					{
						e.printStackTrace();
					}

				}
				else
				{

					//Toast.makeText(con, "Not Got Response From Server.", Toast.LENGTH_SHORT).show();
				}

			}
		};

	});
}
