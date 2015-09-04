package com.example.ProgressTask;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
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
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.Adapter.Notification_Adapter;
import com.example.Tabs.Tab_Notification_Connects;
import com.example.Tabs.Tab_Notification_Pictures;
import com.example.Tabs.Tab_Notification_Videos;
import com.example.classes.Util_Class;
import com.ameba.muser.Cancel_Sessions;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;
import com.ameba.muser.Trainers;

public class Subscribe_Unsubscribe_Trainer_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	//private ProgressDialog dialog;
	String message = null, msg_json,msg=""/*,paypal_json=""*/;
	SharedPreferences rem_pref;

	String trainer_id,action;

	public Subscribe_Unsubscribe_Trainer_ProgressTask(Context con, String trainer_id,String action/*,String paypal_json*/)
	{
		this.con = con;
		this.trainer_id = trainer_id;
		this.action=action;
//		this.paypal_json=paypal_json;
		Log.e("trainer_id", trainer_id);
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	protected void onPreExecute()
	{
		Util_Class.dismiss_dialog();
		/*dialog = ProgressDialog.show(con, "", "");
		dialog.setContentView(R.layout.main);
		dialog.show();*/
	}

	@Override
	protected Void doInBackground(String... params)
	{
		String response = get_subscribe_unsubscribe_trainer();
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
					}
					else
					{
						message = "Failure";
						// message = null;
					}
					
					msg = new JSONObject(response).getString("message");
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
		/*if(dialog.isShowing())
		{
			dialog.dismiss();
		}*/
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				new Subscribe_Unsubscribe_Trainer_ProgressTask(con, trainer_id,action/*paypal_json*/).execute();
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
			
			Util_Class.show_Toast(msg, con);
			/*if(from_where.equals("Trainer"))
			{
				Util_Class.show_Toast(msg, con);
				String val=action.equals("U")?("Train With"):"Remove Training With";
				Get_Profile_ProgressTask.item.set(5, val);
				
				Get_Profile_ProgressTask.spinner_adapter.notifyDataSetChanged();
			}
			else
			{
				Util_Class.show_Toast(msg, con);
				
				String val=action.equals("Y")?("Remove Recommendation"):"Recommend Trainer";
				Get_Profile_ProgressTask.item.set(6, val);
				
				Log.e("Get_Profile_ProgressTask.item", ""+Get_Profile_ProgressTask.item);
				
				Get_Profile_ProgressTask.spinner_adapter.notifyDataSetChanged();
			}*/
			
			if(con instanceof Trainers)
			{
				((Trainers) con).refresh();
			}
			else if(con instanceof Other_Profile)
			{
				new Get_Profile_ProgressTask(con).execute();
			}
			else if(con instanceof Cancel_Sessions)
			{
				((Cancel_Sessions) con).refresh();
			}
			
			
			
		/*	if(paypal_json.isEmpty())
			{
				new Get_Profile_ProgressTask(con).execute();
			}*/
			
			
			
			
		}
		else if(message.equals("Failure"))
		{ 
			Util_Class.show_Toast(msg, con);
			
		}
	}

	public String get_subscribe_unsubscribe_trainer()
	{
		
		//String url=from_where.equals("Trainer")?Util_Class.subscribe_unsubscribe_trainer:Util_Class.set_recommended;
		
		HttpPost httppost = new HttpPost(Util_Class.subscribe_unsubscribe_trainer);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			param.add(new BasicNameValuePair("trainer_id", trainer_id));
			param.add(new BasicNameValuePair("status", action));
			//param.add(new BasicNameValuePair("transactionConfirmation", paypal_json));
			
			Log.e("subscribe trainer  param", param.toString());
			httppost.setEntity(new UrlEncodedFormEntity(param));
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
