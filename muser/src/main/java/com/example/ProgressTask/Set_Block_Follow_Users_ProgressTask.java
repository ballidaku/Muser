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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.classes.Util_Class;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;

public class Set_Block_Follow_Users_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	SharedPreferences rem_pref;
	HashMap<String, String> map; 

	String message = null, msg_json,status,from_where,msg="";

	ProgressDialog dialog;
	public Set_Block_Follow_Users_ProgressTask(Context con,HashMap<String, String> map,String status,String from_where)
	{
		this.con = con;
		this.map = map;
		this.status=status;
		this.from_where=from_where;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
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
		String response = set_block_follow_users();
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
					/*if(msg_json.equals("Success"))
					{
						message = "Success";

					}
					else
					{
						message = "Failure";
						// message = null;
					}*/
					message=msg_json;
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
		if(dialog.isShowing())
		{
			dialog.dismiss();
		}
		Util_Class.dismiss_dialog();
		OnClickListener retry = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Util_Class.internet_dialog.dismiss();
				 new Set_Block_Follow_Users_ProgressTask(con, map,status,from_where).execute();
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
			if(from_where.equals("Find_Friends"))
			{
				new Get_Find_Friends_ProgressTask(con).execute();
			}
			
			if(status.matches("F|U") && from_where.equals("Other_Profile"))
			{
				Util_Class.show_Toast(msg, con);
				new Get_Profile_ProgressTask(con).execute();
			}
//			else if(status.equals("U") && from_where.equals("Other_Profile"))
//			{
////				Other_Profile.is_follow=false;
////				Get_Profile_ProgressTask.item.set(1,"Follow");
////				Get_Profile_ProgressTask.spinner_adapter.notifyDataSetChanged();
//				Util_Class.show_Toast(msg, con);
//			}
			else if(status.equals("B") && from_where.equals("Other_Profile"))
			{
				Util_Class.show_Toast(msg, con);
				//Util_Class.show_Toast("Selected user has been blocked.", con);
				((Activity) con).finish();
			}
			else if(from_where.equals("Blocks_Adapter"))
			{
				Util_Class.show_Toast("Selected user has been unblocked successfully.", con);
				new Get_Followes_Following_Blocks_ProgressTask(con, "B").execute();
			}
			else if(from_where.equals("Following_Adapter"))
			{
				Util_Class.show_Toast("Selected user has been unfollow successfully.", con);
				new Get_Followes_Following_Blocks_ProgressTask(con, "FG").execute();
			}
			else if(from_where.equals("Followers_Adapter"))
			{
				Util_Class.show_Toast("Selected user has been followed successfully.", con);
				new Get_Followes_Following_Blocks_ProgressTask(con, "F").execute();
			}
		}
		else if(message.equals("Failure1"))
		{
			Util_Class.show_Toast(msg, con);
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
		else if(message.equals("Failure2"))
		{
			Util_Class.show_Toast(msg, con);
			//Util_Class.show_global_dialog(con, con.getResources().getString(R.string.no_data_found));
		}
		else if(message.equals("requested") && from_where.equals("Followers_Adapter"))
		{
			Util_Class.show_Toast(msg, con);
			new Get_Followes_Following_Blocks_ProgressTask(con, "F").execute();
		}
		else if(message.equals("requested") && from_where.equals("Following_Adapter"))
		{
			Util_Class.show_Toast(msg, con);
			new Get_Followes_Following_Blocks_ProgressTask(con, "FG").execute();
		}
		else if(message.equals("requested")  && from_where.equals("Other_Profile"))
		{
			Util_Class.show_Toast(msg, con);
			new Get_Profile_ProgressTask(con).execute();
		}
		else if(from_where.equals("Find_Friends") && message.equals("requested"))
		{
			Util_Class.show_Toast(msg, con);
			new Get_Find_Friends_ProgressTask(con).execute();
		}	
		else if(message.equals("requested"))
		{
			Util_Class.show_Toast(msg, con);
		}
	}

	public String set_block_follow_users()
	{
		HttpPost httppost = new HttpPost(Util_Class.set_block_follow_users);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{

			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("user_id", rem_pref.getString("user_id", "")));
			param.add(new BasicNameValuePair("friend_id", map.get("user_id")));
			param.add(new BasicNameValuePair("status", status));
			
			
			Log.e("search child param", param.toString());
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
