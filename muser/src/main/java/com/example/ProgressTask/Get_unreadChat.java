package com.example.ProgressTask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ameba.muser.Chat_sharan;
import com.example.classes.Chat_data;
import com.example.classes.Data_list;
import com.example.classes.Util_Class;

public class Get_unreadChat extends AsyncTask<Void, Void, String>
{

	private final Handler	replyTo;
	private final String	userid;
	private final String	frndID;
	Context					conn;

	String					message	= "", type;

	String					jsonStr	= "";

	Data_list				dataList;
	ArrayList<Chat_data>			list;

	public Get_unreadChat(Handler replyTo, String userID, Context con, String frndID, String type)
	{
		this.replyTo = replyTo;
		this.userid = userID;
		this.frndID = frndID;
		this.conn = con;
		this.type = type;

		list = new ArrayList<Chat_data>();
	}

	@Override
	protected void onPreExecute()
	{
		System.gc();
		
		
		if(type.equals("A"))
		{
//			show progress
		}
		
		super.onPreExecute();

	}

	@Override
	protected String doInBackground(Void... params)
	{

		try
		{

			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("sender_id", userid));
			param.add(new BasicNameValuePair("receiver_id", frndID));
			param.add(new BasicNameValuePair("type", type));

			//			userid, receiver_id, type (A/U A=> get all message, U get Unread

			// param.add(new BasicNameValuePair("timezone",
			// UtilClass.timezone));

			//Log.e("Get Unread messages",""+param.toString());

			HttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpClient httpClient = new DefaultHttpClient(httpParams);

			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			HttpPost httpPost = new HttpPost(Util_Class.get_messages);
			if(params != null)
			{
				httpPost.setEntity(new UrlEncodedFormEntity(param));
			}

			httpResponse = httpClient.execute(httpPost);
			httpEntity = httpResponse.getEntity();
			jsonStr = EntityUtils.toString(httpEntity);

			// jsonStr = sh.makeServiceCall(UtilClass.url + "/chat/unread",
			// 2, param);

			if(jsonStr != null)
			{

				try
				{

					JSONObject jsonobject = new JSONObject(jsonStr);

					message = jsonobject.getString("status");

				}
				catch(Exception e)
				{

					message = "Error";
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return jsonStr;
	}

	@Override
	protected void onPostExecute(String result)
	{

		try
		{

			Log.e("result nav",""+result);

			if(message.equalsIgnoreCase("Success"))
			{

				JSONObject jsonobject = new JSONObject(result);

				JSONArray arraymsg = jsonobject.getJSONArray("msg_info");

				if(arraymsg.length() > 0)
				{

					for(int jArray = 0; jArray < arraymsg.length(); jArray++)
					{

						JSONObject inner = arraymsg.getJSONObject(jArray);

						Chat_data ChatData = new Chat_data(inner.getString("receiver_id"), inner.getString("message"),inner.getString("message_id"), inner.getString("added_date"));
						list.add(ChatData);
					}

					dataList = new Data_list(list);

					Bundle data = new Bundle();
					data.putSerializable("Chat", dataList);

					Message msg = Message.obtain();
					msg.setData(data);
					replyTo.sendMessage(msg);
				}
				else
				{
					if(Chat_sharan.chats)
					{

						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
						{
							new Get_unreadChat(replyTo, userid, conn, frndID, "U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
						}
						else
						{

							new Get_unreadChat(replyTo, userid, conn, frndID, "U").execute();
						}
					}
				}

			}
			else
			{

				if(Chat_sharan.chats)
				{

					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					{
						new Get_unreadChat(replyTo, userid, conn, frndID, "U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					}
					else
					{

						new Get_unreadChat(replyTo, userid, conn, frndID, "U").execute();
					}
				}

			}

		}
		catch(Exception e)
		{
			if(Chat_sharan.chats)
			{

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				{
					new Get_unreadChat(replyTo, userid, conn, frndID, "U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else
				{

					new Get_unreadChat(replyTo, userid, conn, frndID, "U").execute();
				}
			}
			e.printStackTrace();
		}

		super.onPostExecute(result);
	}
}
