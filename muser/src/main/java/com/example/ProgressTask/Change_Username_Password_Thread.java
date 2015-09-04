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
import android.util.Log;

import com.example.classes.Util_Class;
import com.ameba.muser.R;
import com.ameba.muser.Update_Profile;

public class Change_Username_Password_Thread
{
	Context con;
	String user_name,old_password,new_password;
	SharedPreferences rem_pref;
	
	public Change_Username_Password_Thread(Context con,String user_name,String old_password,String new_password)
	{
		
		this.con = con;
		
		
		this.user_name=user_name;
		this.old_password=old_password;
		this.new_password=new_password;
		
		rem_pref=con.getSharedPreferences("Remember",con.MODE_WORLD_READABLE);
		 
		background.start();	 
	}

	Thread background = new Thread(new Runnable()
	{

		

		// After call for background.start this run method call
		public void run()
		{
			try
			{
				String url= (!user_name.isEmpty())?Util_Class.change_username:Util_Class.change_password;
				
				HttpPost httppost = new HttpPost(url);
				
				
				HttpParams httpParameters = new BasicHttpParams();
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
				
				if(!user_name.isEmpty())
				{
					param.add(new BasicNameValuePair("user_name", user_name));
				}
				else
				{
					param.add(new BasicNameValuePair("old_password", old_password));
					param.add(new BasicNameValuePair("new_password", new_password));
				}
				
				Log.e("param", ""+param);
				
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
				Log.e("Change_Username_Password_Thread", "Thread  exception " + t);
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
							if(!user_name.isEmpty())
							{
								rem_pref.edit().putString("user_name", user_name).commit();
								Update_Profile.update_user_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_name, 0, R.drawable.right_green, 0);
								Util_Class.show_Toast("UserName changed successfully.", con);
							}
							else
							{
								Update_Profile.old_password.setText("");
								Update_Profile.new_password.setText("");
								Update_Profile.confirm_password.setText("");
								Update_Profile.confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, 0, 0);
								Util_Class.show_Toast("Password changed successfully.", con);
							}
						}
						else
						{
							if(!user_name.isEmpty())
							{
								Util_Class.show_Toast("UserName already exists.", con);
							}
							else
							{
								Update_Profile.confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.right_gray, 0);
								Util_Class.show_Toast("Password did not match with saved password.", con);
							}
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
