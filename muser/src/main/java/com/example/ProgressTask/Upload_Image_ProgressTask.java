package com.example.ProgressTask;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.classes.Util_Class;
import com.ameba.muser.R;

public class Upload_Image_ProgressTask extends AsyncTask<String, Void, Void>
{
	Context con;
	//private ProgressDialog dialog;
	String message = null, msg_json/*,video_duration*/;
	SharedPreferences rem_pref;
	ArrayList<HashMap<String, String>> list;
	//String image_path, captions, tag, trainer_id, fitness_goal,category_id,latitude,longitude,type;
	
	HashMap<String, String> map;//=new HashMap<String, String>();

	public Upload_Image_ProgressTask(Context con,HashMap<String, String> map)
	{
		this.con = con;
		
		this.map=map;
		
		
		Log.e("map", ""+map);
		
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		if(map.get("type").equals("I"))
		{
			((Activity) con).finish();
			Util_Class.show_Toast("Uploading please wait", con);
		}

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
		String response = upload();
		Log.e("response",""+response);
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
						message = msg_json;
						// message = null;
					}
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
				new Upload_Image_ProgressTask(con,map).execute();
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

			rem_pref.edit().putString("current_frag","My_Profile").commit();
			Util_Class.show_Toast(con.getResources().getString(R.string.data_uploaded_successfull), con);
			
			//
			
		}
		else if(message.equals("Failure"))
		{
			Util_Class.show_global_dialog(con,con.getResources().getString(R.string.unsuccessfull));
		}
		/*else if(message.equals("Failure1"))
		{
			Util_Class.show_global_dialog(con,"Passwords did not match.");
		}*/
	}

	public String upload()
	{
		HttpPost httppost = new HttpPost(Util_Class.upload_image_video);
		HttpParams httpParameters = new BasicHttpParams();
//		HttpConnectionParams.setConnectionTimeout(httpParameters, 4000);
//		HttpConnectionParams.setSoTimeout(httpParameters, 4000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
		try
		{
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			
			if(map.get("type").equals("V"))
			{
				/* File file = new File(map.get("image_path"));
		         FileBody fileBody = new FileBody(file);
		         reqEntity.addPart("data", fileBody);*/

				reqEntity.addPart("video_name", new StringBody(map.get("video_name")));
		         
				/*MediaPlayer mp = MediaPlayer.create(con.getApplicationContext(), Uri.parse(file.getAbsolutePath()));
				video_duration = String.valueOf(mp.getDuration()/1000);
				mp.release();
				Log.e("duration", "" + video_duration);*/
			}
			else
			{
				Bitmap bmp = BitmapFactory.decodeFile(map.get("image_path"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bmp.compress(CompressFormat.PNG, 100, baos);
				byte[] byte_arr = baos.toByteArray();
				ByteArrayBody bab = new ByteArrayBody(byte_arr, "profile_pic.png");
				reqEntity.addPart("data", bab);
//				video_duration="";
			}
		
		
			reqEntity.addPart("user_id", new StringBody(rem_pref.getString("user_id", "")));
			reqEntity.addPart("trainer_id", new StringBody(map.get("trainer_id")));
			reqEntity.addPart("category_id", new StringBody(map.get("category_id")));
			reqEntity.addPart("focus_id", new StringBody(map.get("focus_id")));
			
			reqEntity.addPart("data_type", new StringBody(map.get("type")));
			reqEntity.addPart("caption", new StringBody(map.get("captions_s")));
			reqEntity.addPart("tag", new StringBody(map.get("tag")));
			
			reqEntity.addPart("latitude", new StringBody(map.get("latitude")));
			reqEntity.addPart("longitude", new StringBody(map.get("longitude")));
			reqEntity.addPart("location", new StringBody(map.get("location")));
			reqEntity.addPart("fitness_id", new StringBody(map.get("fitness_id")));
			reqEntity.addPart("duration", new StringBody(map.get("video_duration")));
			
			if(map.get("type").equals("I"))
			{
				reqEntity.addPart("thumbnail", new StringBody(""));
			}
			else
			{
				Bitmap bit = ThumbnailUtils.createVideoThumbnail(map.get("image_path"),MediaStore.Images.Thumbnails.MINI_KIND);
				ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
				bit.compress(CompressFormat.PNG, 100, baos1);
				byte[] byte_arr1 = baos1.toByteArray();
				ByteArrayBody bab1 = new ByteArrayBody(byte_arr1, "profile_pic.png");
				reqEntity.addPart("thumbnail", bab1);
				
			}
			
			
		
			
			
			httppost.setEntity(reqEntity);
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
