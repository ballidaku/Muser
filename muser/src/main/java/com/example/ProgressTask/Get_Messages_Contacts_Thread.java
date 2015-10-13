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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ameba.muser.Captured_Image;
import com.ameba.muser.Messages;
import com.example.classes.Util_Class;
//import com.google.gson.JsonObject;

public class Get_Messages_Contacts_Thread
{
	Context con;
	public ArrayList<HashMap<String, String>> list;// = new ArrayList<HashMap<String, String>>();
	SharedPreferences rem_pref;
	Messages con2;

	public Get_Messages_Contacts_Thread(Context con, Messages con2)
	{

		this.con = con;
		this.con2=con2;
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
				HttpPost httppost = new HttpPost(Util_Class.get_messages_contacts);
				
				HttpParams httpParameters = new BasicHttpParams();
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				
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
				Log.e("Get_Messages_Contacts_Thread", "Thread  exception " + t);
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

		
		private final Handler handler = new Handler()
		{

			public void handleMessage(Message msg)
			{
				

				String response = msg.getData().getString("message");

				Log.e("Msg response",""+response);

				if((null != response))
				{
					try
					{
						String msg_json = new JSONObject(response).getString("status");
						if(msg_json.equals("Success"))
						{
							list = new ArrayList<>();
							JSONArray jo = new JSONObject(response).getJSONArray("recent_chat");

							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("last_message", obj.getString("last_message"));
								map.put("read_status", obj.getString("read_status"));
								map.put("timestamp", obj.getString("timestamp"));
								
								JSONObject obj2=obj.getJSONObject("user_info");
								map.put("other_user_id", obj2.getString("user_id"));
								map.put("other_user_name", obj2.getString("user_name"));
								map.put("other_profile_image", obj2.getString("profile_image"));
								
								list.add(map);
							}
							
							con2.add_list(list);


						}
						else
						{
							((Messages)con2).on_Failure();
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
