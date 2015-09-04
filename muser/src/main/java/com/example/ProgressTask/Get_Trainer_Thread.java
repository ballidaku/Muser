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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classes.Util_Class;
import com.ameba.muser.Captured_Image;
import com.ameba.muser.R;

 public class Get_Trainer_Thread
{
	Context con;
	public static ArrayList<HashMap<String, String>> list;
	ArrayList<String> NameList;
	String s;

	public Get_Trainer_Thread(Context con, String s)
	{
		
		this.con = con;
		background.start();
		this.s=s;
	}

	Thread background = new Thread(new Runnable()
	{

		

		// After call for background.start this run method call
		public void run()
		{
			try
			{
				HttpPost httppost = new HttpPost(Util_Class.get_trainers);
				HttpParams httpParameters = new BasicHttpParams();
				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("keyword", s));
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
				Log.i("Animation", "Thread  exception " + t);
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
					Log.e("response", response);
					try
					{
						String msg_json = new JSONObject(response).getString("status");
						if(msg_json.equals("Success"))
						{
							
							JSONArray jo 	= new JSONObject(response).getJSONArray("trainer_info");
							list 			= new ArrayList<HashMap<String, String>>();
							NameList		= new ArrayList<String>();
							
							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("user_name", obj.getString("user_name"));
								map.put("full_name", obj.getString("full_name"));
								map.put("trainer_id", obj.getString("trainer_id"));
								
								NameList.add(obj.getString("user_name"));
								list.add(map);
							}
							
							ArrayAdapter<String> adapter1=new ArrayAdapter<String>(con,R.layout.custom_dropdownlist,NameList);
							Captured_Image.trained_with.setAdapter(adapter1);
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
