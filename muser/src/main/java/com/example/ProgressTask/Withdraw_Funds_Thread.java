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

import com.ameba.muser.Wallet;
import com.example.classes.Util_Class;

public class Withdraw_Funds_Thread
{
	Context con;
	Fragment con2;
	SharedPreferences rem_pref;
	String withdraw_amount;
	
	public Withdraw_Funds_Thread(Context con, Fragment con2, String withdraw_amount)
	{

		this.con = con;
		this.con2 = con2;
		this.withdraw_amount=withdraw_amount;
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
				HttpPost httppost = new HttpPost(Util_Class.withdraw_funds);
				
				HttpParams httpParameters = new BasicHttpParams();
				
				DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
				
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				param.add(new BasicNameValuePair("fund", withdraw_amount));
				
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
							String balance = new JSONObject(response).getString("balance");
							Wallet.set_balance(balance);
						}
						else if(msg_json.equals("Failure1"))
						{
							String message = new JSONObject(response).getString("message");
							Util_Class.show_Toast(message, con);
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
