package com.example.ProgressTask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.classes.Util_Class;

public class Delete_Report_Thread
{
	Context con;
	String post_id,from_where;
	SharedPreferences rem_pref;

	public Delete_Report_Thread(Context con,String post_id,String from_where)
	{
		this.con = con;
		this.post_id=post_id;
		this.from_where=from_where;
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
				HttpPost httppost = new HttpPost(Util_Class.delete_report_post);
				DefaultHttpClient httpclient = new DefaultHttpClient();
				
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				param.add(new BasicNameValuePair("post_id", post_id));
				
				httppost.setEntity(new UrlEncodedFormEntity(param));
				HttpResponse response = httpclient.execute(httppost);
				String data = EntityUtils.toString(response.getEntity());
				Log.e("DATAAA", data);
				threadMsg(data);

			}
			catch(Throwable t)
			{
				// just end the background thread
				Log.e("Delete ", "Thread  exception " + t);
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
							String text=from_where.equals("Delete")?"Post deleted successfully.":"Post reported as spam successfully.";
							Util_Class.show_Toast(text, con);
							
							if(from_where.equals("Delete"))
							((Activity) con).finish();

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
