package com.example.ProgressTask;

import java.util.ArrayList;
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
import android.support.v4.app.Fragment;
import android.util.Log;

import com.example.Tabs.Tab_Notification_Connects;
import com.example.classes.Util_Class;

public class Accept_Reject_Request_Thread
{
	Context con;
	Fragment con2;
	String friend_id,status;
	SharedPreferences rem_pref;
	
	public Accept_Reject_Request_Thread(Context con, Fragment con2, String friend_id,String status)
	{
		
		this.con = con;
		this.con2 = con2;
		background.start();
		this.friend_id=friend_id;
		this.status=status;
		 rem_pref=con.getSharedPreferences("Remember",con.MODE_WORLD_READABLE);
	}

	Thread background = new Thread(new Runnable()
	{

		

		// After call for background.start this run method call
		public void run()
		{
			try
			{
				HttpPost httppost = new HttpPost(Util_Class.accept_reject_request);
				HttpParams httpParameters = new BasicHttpParams();
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				param.add(new BasicNameValuePair("friend_id",friend_id));
				param.add(new BasicNameValuePair("status", status));
				
				Log.e("Accept_Reject_Request_Thread param",""+ param);
				
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
				Log.i("Accept_Reject_Request_Thread", "Thread  exception " + t);
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
							((Tab_Notification_Connects) con2).refresh();
							// Util_Class.show_Toast("", con);
							 //new Get_Notification_ProgressTask(con,"C").execute();
//							rem_pref.edit().putString("privacy_status",public_private).commit();
						

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
