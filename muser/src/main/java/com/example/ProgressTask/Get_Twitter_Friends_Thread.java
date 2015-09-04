package com.example.ProgressTask;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.example.Tabs.Tab_Invite_Others_Twitter;
import com.example.classes.Authenticated;
import com.google.gson.Gson;
//import twitter4j.Twitter;

public class Get_Twitter_Friends_Thread
{
	Tab_Invite_Others_Twitter con;
	

	String name,next_cursor;

	public Get_Twitter_Friends_Thread(Tab_Invite_Others_Twitter con, String name,String next_cursor)
	{

		this.con = con;
		this.name = name;
		this.next_cursor=next_cursor;
		background.start();

	}

	Thread	background	= new Thread(new Runnable()
						{

							public void run()
							{
								try
								{

									String i = getTwitterStream(name);
									//b();
									//System.out.println(i);
									//Log.e("MMMMMMMMMMMMMMMMMMMMMMMM", ""+i);
									threadMsg(i);

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

	
		
		private final Handler handler = new Handler()
		{

			public void handleMessage(Message msg)
			{

				String response = msg.getData().getString("message");

				if((null != response))
				{
					//Log.e("response", response);

					System.out.println("response--->>>"+ response);

					try
					{


							String next_cursor=new JSONObject(response).getString("next_cursor_str");
							JSONArray jo = new JSONObject(response).getJSONArray("users");
							
							 ArrayList<HashMap<String, String>> list=new ArrayList<>();

							for(int i = 0; i < jo.length(); i++)
							{
								JSONObject obj = jo.getJSONObject(i);
								HashMap<String, String> map = new HashMap<String, String>();
								map.put("screen_name", obj.getString("screen_name"));
								map.put("id_str", obj.getString("id_str"));
								map.put("profile_image_url", obj.getString("profile_image_url").replace("_normal", ""));
								map.put("name", obj.getString("name"));
								
								list.add(map);
							}
							
							((Tab_Invite_Others_Twitter) con).set_data(list,next_cursor);
							
							
						
					
					}
					catch(JSONException e)
					{
						
						e.printStackTrace();
					}

				}


			}
		};

	});





	private String getTwitterStream(String screenName) {
		String results = null;

		try {
			//Ameba
//			String urlApiKey = URLEncoder.encode("JO07kjMCimwgsTPwrDsnWwgj4", "UTF-8");
//			String urlApiSecret = URLEncoder.encode("KEbCQycWSUCmA5cMCFmAKlhi6JGn0WRO0Zi6XseaHHdeuwqjZP", "UTF-8");

			//Client _MUSER__

			String urlApiKey = URLEncoder.encode("FQr4TxIrANTAutbAvfxzIMaxQ", "UTF-8");
			String urlApiSecret = URLEncoder.encode("Dk9dYCjLCyNoSGr6j20mAQWfGQqB1smY6IPOM13rPARU68pp61", "UTF-8");

			String combined = urlApiKey + ":" + urlApiSecret;

		
			String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

			
			HttpPost httpPost = new HttpPost("https://api.twitter.com/oauth2/token");
			httpPost.setHeader("Authorization", "Basic " + base64Encoded);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpPost.setEntity(new StringEntity("grant_type=client_credentials"));

			
			String rawAuthorization = getResponseBody(httpPost);
			Authenticated auth = jsonToAuthenticated(rawAuthorization);

			if(auth != null && auth.token_type.equals("bearer"))
			{

				//To get all followes and following
				//HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/friends/list.json?cursor=-1&screen_name=" + screenName+"&skip_status=true&include_user_entities=false");

				HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/followers/list.json?cursor="+next_cursor+"&screen_name="+screenName+"&count=10");
				
				
				httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
				httpGet.setHeader("Content-Type", "application/json");

				results = getResponseBody(httpGet);


			}
		} catch (UnsupportedEncodingException ex) {
		} catch (IllegalStateException ex1) {
		}
		return results;
	}
	
	
	
	
	private String getResponseBody(HttpRequestBase request) {
		StringBuilder sb = new StringBuilder();
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			String reason = response.getStatusLine().getReasonPhrase();

			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				String line = null;
				while ((line = bReader.readLine()) != null) {
					sb.append(line);
				}
			} else {
				sb.append(reason);
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (ClientProtocolException ex1) {
		} catch (IOException ex2) {
		}
		return sb.toString();
	}
	
	
	private Authenticated jsonToAuthenticated(String rawAuthorization) {
		Authenticated auth = null;
		if (rawAuthorization != null && rawAuthorization.length() > 0) {
			try {
				Gson gson = new Gson();
				auth = gson.fromJson(rawAuthorization, Authenticated.class);
			} catch (IllegalStateException ex) {
				// just eat the exception
			}
		}
		return auth;
	}
	
	
	
	
	/*  public boolean shouldOverrideUrlLoading(WebView view, final String url) {
		  if (url.startsWith("http")) {
		    // authorization complete hide webview for now.
		    final Uri uri = Uri.parse(url);
		    final String verifier = uri.getQueryParameter("oauth_verifier");
		    Log.i("1", "1-->" + verifier);
		    if (verifier != null) {
		      new Thread(new Runnable() {
		        @Override
		        public void run() {
		      // TODO Auto-generated method stub
		      Verifier v = new Verifier(verifier);
		      Token accessToken = s.getAccessToken(requestToken, v);
		      // host twitter detected from callback
		      // oauth://twitter
		      if (uri.getHost().equals("www.google.com")) {
		        try {
		          OAuthRequest req = new OAuthRequest(Verb.POST,"https://api.twitter.com/1.1/statuses/update_with_media.json");
		          s.signRequest(accessToken, req);
		          File outputDir = Environment.getExternalStorageDirectory();
		          File tempFile = File.createTempFile("temp_inner_twitter", ".jpg", outputDir);
		          Bitmap bitmap = createBitmap();
		          FileOutputStream fOut = new FileOutputStream(tempFile);
		          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		          Log.i("asds", tempFile.length() + "," + tempFile.getAbsolutePath());
		          fOut.flush();
		          fOut.close();
		          bitmap.recycle();
		          MultipartEntity entity = new MultipartEntity();
		          String urii = Uri.parse(createShareLink(urlwost)).toString();
		          entity.addPart("status", new StringBody("Vote here ww.abc.com\n Download WO? App http://www.google.com/"));
		          entity.addPart("media", new FileBody(tempFile, "image/jpeg"));
		          entity.addPart("access_token", new StringBody(accessToken.getToken()));
		          ByteArrayOutputStream out = new ByteArrayOutputStream();
		          entity.writeTo(out);
		          req.addPayload(out.toByteArray());
		          req.addBodyParameter("status", "I have uploaded test image on twitter");
		          req.addHeader(entity.getContentType().getName(), entity.getContentType().getValue());
		          org.scribe.model.Response response = req.send();
		          JSONObject json = new JSONObject(response.getBody());
		          Log.i("Twitter Response", json.toString());
		          if (!json.has("errors")) {
		            Toast
		            .makeText(getApplicationContext(), "Image shared on twitter successfully", Toast.LENGTH_LONG)
		            .show();
		          } else {
		            Toast.makeText(getApplicationContext(), "Error while sharing application on twitter",
		            Toast.LENGTH_LONG).show();
		          }
		        } catch (JSONException e) {
		          e.printStackTrace();
		          Toast.makeText(getApplicationContext(), "Error while sharing application on twitter",
		          Toast.LENGTH_LONG).show();
		        } catch (Exception e) {
		          // TODO: handle exception
		          e.printStackTrace();
		          Toast.makeText(getApplicationContext(), "Error while sharing application on twitter",
		          Toast.LENGTH_LONG).show();
		        }
		      }
		        }
		      }).run();
		      webView1.setVisibility(View.GONE);
		      // save this token for practical use.
		    }
		    return true;
		  }
		  return super.shouldOverrideUrlLoading(view, url);
		    }
		
		  new Thread(new Runnable() {
		    @Override
		    public void run() {
		  // TODO Auto-generated method stub
		  s = new ServiceBuilder().provider(TwitterApi.class).apiKey(APIKEY).apiSecret(APISECRET).callback(CALLBACK)
		      .build();
		  requestToken = s.getRequestToken();
		  // send user to authorization page
		  runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		      // TODO Auto-generated method stub
		      final String authURL = s.getAuthorizationUrl(requestToken);
		      Log.i("2", "--" + authURL);
		      webView1.loadUrl(authURL);
		      progress.dismiss();
		    }
		  });
		    } }).start();
		  
	*/
	
	
	
	

}
