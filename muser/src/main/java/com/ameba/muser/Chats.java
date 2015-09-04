package com.ameba.muser;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ProgressTask.Get_unreadChat;
import com.example.classes.Chat_data;
import com.example.classes.Data_list;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class Chats extends FragmentActivity implements OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener
{

	static LinearLayout		layoutChat;
	ScrollView				scrollChat;
	static String			DateTemp	= "";

	EmojiconEditText				edMSG;
	ImageView				btnSendChat,keyboard,smilly;
	TextView				back;

	public static boolean	chats		= false;

	String					userID		= "", frndID = "", user_img = "", my_img = "", usr_name = "";

	
	FrameLayout				emojicons;
	
	@Override
	public void onBackPressed()
	{
	}

	@Override
	protected void onStop()
	{

		chats = false;
		super.onStop();
	}

	@Override
	protected void onPause()
	{
		chats = false;
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		chats = true;

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			new Get_unreadChat(responseHandlerChat, userID, Chats.this, frndID,"U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{

			new Get_unreadChat(responseHandlerChat, userID, Chats.this, frndID,"U").execute();
		}
		super.onResume();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		layoutChat = (LinearLayout) findViewById(R.id.layoutChatContainer);
		scrollChat = (ScrollView) findViewById(R.id.scrollViewChat);
		edMSG = (EmojiconEditText) findViewById(R.id.editT_msg);
		btnSendChat=(ImageView)findViewById(R.id.send);
		(back=(TextView)findViewById(R.id.back)).setOnClickListener(this);
		
		
		(smilly				= (ImageView) findViewById(R.id.smilly)).setOnClickListener(this);
		(keyboard				= (ImageView) findViewById(R.id.keyboard)).setOnClickListener(this);
		
		
		emojicons=(FrameLayout)findViewById(R.id.emojicons);
		
		btnSendChat.setOnClickListener(this);
		
		SharedPreferences preferences = getSharedPreferences("Remember", Context.MODE_WORLD_READABLE);
		 userID = preferences.getString("user_id", "1");
		// my_img = preferences.getString("profile_pic", "profile_pic");

		my_img=preferences.getString("profile_image", "");
	
		
//		GET FROM INTENT
		user_img=getIntent().getStringExtra("img");
		frndID = getIntent().getStringExtra("id");

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			new Get_unreadChat(responseHandlerChat, userID, Chats.this, frndID,"A").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{

			new Get_unreadChat(responseHandlerChat, userID, Chats.this, frndID,"A").execute();
		}
		
		
		
		back.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				finish();
				
			}
		});
		
		
		View activityRootView = findViewById(R.id.main);
		checkKeyboardHeight(activityRootView);
		
		

	}
	
	@Override
	protected void onResumeFragments()
	{
		setEmojiconFragment();
		super.onResumeFragments();
	}
	
	private int	keyboardHeight;

	boolean		isKeyBoardVisible;

	int			previousHeightDiffrence	= 0;
	
	private void changeKeyboardHeight(int height)
	{

		if(height > 100)
		{
			keyboardHeight = height;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, keyboardHeight);
			emojicons.setLayoutParams(params);
		}

	}
	

	private void checkKeyboardHeight(final View parentLayout)
	{

		parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{

			@Override
			public void onGlobalLayout()
			{

				Rect r = new Rect();
				parentLayout.getWindowVisibleDisplayFrame(r);

				int screenHeight = parentLayout.getRootView().getHeight();
				int heightDifference = screenHeight - (r.bottom);

				
				Log.e("heightDiff", ""+heightDifference);
				if(previousHeightDiffrence - heightDifference > 50)
				{
					//popupWindow.dismiss();
				}

				previousHeightDiffrence = heightDifference;
				if(heightDifference > 100)
				{

					isKeyBoardVisible = true;
					changeKeyboardHeight(heightDifference);

				}
				else
				{

					isKeyBoardVisible = false;

				}

			}
		});

	}
	

	 @Override
	    public void onEmojiconClicked(Emojicon emojicon) {
	        EmojiconsFragment.input(edMSG, emojicon);
	    }

	    @Override
	    public void onEmojiconBackspaceClicked(View v) {
	        EmojiconsFragment.backspace(edMSG);
	    }
	    
	    private void setEmojiconFragment()
		{
			boolean useSystemDefault=false;
			getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
		}
	    

	Handler	responseHandlerChat	= new Handler()
								{
									public void handleMessage(Message msg)
									{

										Data_list data = (Data_list) msg.getData().get("Chat");

										Showmsgs(data.getList());

										if(chats)
										{
											if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
											{
												new Get_unreadChat(responseHandlerChat, userID, Chats.this, frndID, "U").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
											}
											else
											{

												new Get_unreadChat(responseHandlerChat, userID, Chats.this, frndID, "U").execute();
											}

										}

									};
								};

	class sendChat extends AsyncTask<Void, Void, Void>
	{
		String		jsonStr		= "";

		String		message		= "";
		String		identifier	= "";

		Chat_data	chatData;

		public sendChat(String msg)
		{
			
			
			try
			{
				this.message = Util_Class.emoji_encode(msg);
			}
			catch(UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			};

		}

		@Override
		protected void onPreExecute()
		{

			Calendar c = Calendar.getInstance();
			SimpleDateFormat dfJOBJ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDateJOBJ = dfJOBJ.format(c.getTime());

			chatData = new Chat_data(frndID, message, formattedDateJOBJ);

			List<Chat_data> list = new ArrayList<Chat_data>();
			list.add(chatData);
			Showmsgs(list);
			edMSG.setText("");

			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params)
		{

			try
			{
				
				
				
				
				

				List<NameValuePair> param = new ArrayList<NameValuePair>();
				param.add(new BasicNameValuePair("sender_id", userID));
				param.add(new BasicNameValuePair("receiver_id", frndID));
				param.add(new BasicNameValuePair("message", message));
				// param.add(new BasicNameValuePair("timezone",
				// UtilClass.timezone));

				
				
				HttpParams httpParams = new BasicHttpParams();
				httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

				HttpClient httpClient = new DefaultHttpClient(httpParams);

				HttpEntity httpEntity = null;
				HttpResponse httpResponse = null;

				HttpPost httpPost = new HttpPost(Util_Class.save_messages);
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

						Log.e("Response", "" + jsonStr);

						JSONObject jsonobject = new JSONObject(jsonStr);

						message = jsonobject.getString("message");

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

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{

			try
			{

				JSONObject jsonobject = new JSONObject(jsonStr);

				String response = jsonobject.getString("status");
//				{"status":"Success","message_info":{"message_id":39,"sender_id":"2","receiver_id":"1","message":"G","read_status":"N","added_date":"2015-05-14 11:44:48"},"message":"Message save successfully."}
				if(response.equalsIgnoreCase("Success"))
				{

				}

			}
			catch(Exception e)
			{

				e.printStackTrace();
			}

			super.onPostExecute(result);
		}

	}

	public static String parseDateToddMMyyyy(String time)
	{
		String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "dd-MMMM";
		SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

		Date date = null;
		String str = null;

		try
		{
			date = inputFormat.parse(time);
			str = outputFormat.format(date);
		}
		catch(ParseException e)
		{

			e.printStackTrace();
		}
		catch(java.text.ParseException e)
		{

			e.printStackTrace();
		}
		return str;
	}

	private void Showmsgs(List<Chat_data> list)
	{

		for(int i = 0; i < list.size(); i++)
		{

			String Friend_id = list.get(i).getFriend_id();
			final String message = list.get(i).getMessage();
			final String date = list.get(i).getAdded_date();

			String formtDate = parseDateToddMMyyyy(date);

			if(!Friend_id.equalsIgnoreCase(userID))
			{
				formtDate = "";
				formtDate = parseDateToddMMyyyy(date);
			}

			if(DateTemp.equalsIgnoreCase(formtDate))
			{

			}
			else
			{
				LinearLayout.LayoutParams lpDate = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lpDate.gravity = Gravity.CENTER;

				TextView tvdate = new TextView(Chats.this);
				tvdate.setLayoutParams(lpDate);
				tvdate.setText(formtDate + "");
				tvdate.setTextColor(Color.BLACK);
				tvdate.setTextSize(15);
				tvdate.setPadding(5, 5, 5, 5);

				// tvdate.setBackgroundColor(R.drawable.btn_gray_pressed);
				tvdate.setGravity(Gravity.CENTER);
				layoutChat.addView(tvdate);
			}

			DateTemp = formtDate;

			LinearLayout layoutMsgContainer = new LinearLayout(Chats.this);

			if(Friend_id.equalsIgnoreCase(userID))
			{

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.LEFT;
				layoutMsgContainer.setLayoutParams(lp);

				View view = LayoutInflater.from(Chats.this).inflate(R.layout.chat_my, layoutMsgContainer);

				EmojiconTextView tvMSG = (EmojiconTextView) view.findViewById(R.id.txtV_MyChatmsg);

				RoundedCornersGaganImg DP = (RoundedCornersGaganImg) view.findViewById(R.id.fb_MyChat);
				LinearLayout FramemsgLayout = (LinearLayout) view.findViewById(R.id.FrameLayoutMyChat);

				TextView tvTime = (TextView) view.findViewById(R.id.txtV_MyChatDate);

				FrameLayout imageContainer = (FrameLayout) view.findViewById(R.id.FrameLayoutIMAGE);

				 DP.setImageUrl(Chats.this,user_img);

				tvTime.setText(date);

				FramemsgLayout.setVisibility(View.VISIBLE);
				imageContainer.setVisibility(View.GONE);
				// youVideo.setVisibility(View.GONE);

				try
				{
					tvMSG.setText(Util_Class.emoji_decode(message));
				}
				catch(UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tvTime.setText(date);

				// layoutMsgContainer.addView(imgv);

			}

			else
			{

				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				lp.gravity = Gravity.RIGHT;
				layoutMsgContainer.setLayoutParams(lp);

				View viewOther = LayoutInflater.from(Chats.this).inflate(R.layout.other_chat, layoutMsgContainer);

				EmojiconTextView tvMSG = (EmojiconTextView) viewOther.findViewById(R.id.txtV_otherChatmsg);

				RoundedCornersGaganImg DP = (RoundedCornersGaganImg) viewOther.findViewById(R.id.fb_otherChat);
				LinearLayout FramemsgLayout = (LinearLayout) viewOther.findViewById(R.id.FrameLayoutotherChat);

				TextView tvTime = (TextView) viewOther.findViewById(R.id.txtV_otherChatDate);

				FrameLayout imageContainer = (FrameLayout) viewOther.findViewById(R.id.FrameLayoutIMAGE_Other);

				 DP.setImageUrl(Chats.this,my_img);

				tvTime.setText(date);

				FramemsgLayout.setVisibility(View.VISIBLE);
				imageContainer.setVisibility(View.GONE);

				try
				{
					tvMSG.setText(Util_Class.emoji_decode(message));
				}
				catch(UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tvTime.setText(date);

			}

			layoutChat.addView(layoutMsgContainer);

			scrollChat.post(new Runnable()
			{

				@Override
				public void run()
				{
					scrollChat.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});

		}

	}

	@Override
	public void onClick(View arg0)
	{
		
		switch (arg0.getId())
		{
		case R.id.send:
			
			if(!edMSG.getText().toString().trim().equals(""))
			{

				if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
				{

					new sendChat(edMSG.getText().toString() ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				}
				else
				{

					new sendChat(edMSG.getText().toString() ).execute();
				}
			
			}
			else
			{
				edMSG.setText("");
				edMSG.setHintTextColor(Color.RED);
			}
			
			
			break;
			
		case R.id.smilly:

			emoji_keyboard("emoji");
			
			break;
			
			
		case R.id.keyboard:
			edMSG.requestFocus();
			emoji_keyboard("keyboard");
			
			
			//show_keyboard();

			
			break;
			
			
		case R.id.back:

			chats = false;
			this.finish();

			break;

		default:
			
			break;
		}
		
	}
	
	
	
	
	public void emoji_keyboard(String s)
	{
		if(s.equals("keyboard"))
		{
			keyboard.setVisibility(View.GONE);
			smilly.setVisibility(View.VISIBLE);
			
		    final InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInputFromWindow(edMSG.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//			
			emojicons.setVisibility(View.INVISIBLE);
			
		}
		else
		{
			
			keyboard.setVisibility(View.VISIBLE);
			smilly.setVisibility(View.GONE);
			
			emojicons.setVisibility(View.VISIBLE);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edMSG.getWindowToken(), 0);
			
			
			
		}
		
	}
	
	
	public void hide()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edMSG.getWindowToken(), 0);
		
		emojicons.setVisibility(View.GONE);
		
		smilly.setVisibility(View.VISIBLE);
		keyboard.setVisibility(View.GONE);
	}
	
	public void HideEmoji()
	{
		emojicons.setVisibility(View.GONE);
		smilly.setVisibility(View.VISIBLE);
		keyboard.setVisibility(View.GONE);
	}
	
	


}
