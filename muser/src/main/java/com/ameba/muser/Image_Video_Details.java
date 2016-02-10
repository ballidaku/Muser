package com.ameba.muser;

import java.io.UnsupportedEncodingException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.VideoView;

import com.example.ProgressTask.Add_Like_Comment_Repost_ProgressTask;
import com.example.ProgressTask.Add_To_Favorites_ProgressTask;
import com.example.ProgressTask.Delete_Report_Thread;
import com.example.ProgressTask.Get_Comment_Likes_ProgressTask;
import com.example.ProgressTask.Get_Pictures_Videos_ProgressTask;
import com.example.ProgressTask.Recommend_Unrecommend_Post_ProgressTask;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.rockerhieu.emojicon.EmojiconTextView;

@SuppressLint("WorldReadableFiles")
public class Image_Video_Details extends Activity implements OnClickListener /* ,EndlessListView.EndlessListener */
{

	public static ListView								comments_listview;
	// ListView comments_listview;
	// EditText comment_editText;
	// static ArrayList<String> comment_list = new ArrayList<String>();
	TextView											/* comment, */user_name, time, back, title_header, location, repost_user_name,tagged_peoples;

	public static TextView								view_all_comments, likes_count, train_with, goal, category, area_of_focus;
	ImageView											 add_to_favourites, like_image, repost,recommend;
	ImageView											menu_icon,video_blink_icon,video_play_icon;
	RoundedCornersGaganImg								profile_image,image;
	boolean												is_self_favourite	= false;
	boolean												is_like				= false;
	boolean												is_repost			= false;
	Context												con;
	//HashMap<String, String> hashMap;
	SharedPreferences									rem_pref;
	Util_Class											util;
	int													height				= 0;
	public static ArrayList<HashMap<String, String>>	comment_list;
	HashMap<String, String>								data_hashmap;

	public static ScrollView							scrollview;
	MediaController										mc;
	VideoView											mVideoView;
	//String type;
	RelativeLayout										video_relative;
	LinearLayout										user_layout, location_lay, repost_lay, tagged_peoples_lay;
	int													l_c;

	String												post_id				= "", user_id = "";

	EmojiconTextView									caption;
	int													width;
	TextView 											video_duration;
	RoundedCornersGaganImg								repost_profile_image;
	
	Animation  ani ;

	int count=0;
	boolean double_tap=false;

	@SuppressWarnings({ "unchecked", "static-access", "deprecation" })
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_detail);
		con = this;
		util = new Util_Class();

		comment_list = new ArrayList<HashMap<String, String>>();

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		

		title_header = (TextView) findViewById(R.id.title_header);
		(image = (RoundedCornersGaganImg) findViewById(R.id.image)).setOnClickListener(this);
		video_play_icon=(ImageView) findViewById(R.id.video_play_icon);
		mVideoView = (VideoView) findViewById(R.id.videoView);
		video_duration=(TextView)findViewById(R.id.video_duration);
		profile_image = (RoundedCornersGaganImg) findViewById(R.id.profile_image);
		(like_image = (ImageView) findViewById(R.id.like_image)).setOnClickListener(this);
		repost_profile_image= (RoundedCornersGaganImg) findViewById(R.id.repost_profile_image);
		(menu_icon = (ImageView) findViewById(R.id.menu_icon)).setOnClickListener(this);
		(repost = (ImageView) findViewById(R.id.repost)).setOnClickListener(this);
		(recommend = (ImageView) findViewById(R.id.recommend)).setOnClickListener(this);
		comments_listview = (ListView) findViewById(R.id.comments_listview);
		(view_all_comments = (TextView) findViewById(R.id.view_all_comments)).setOnClickListener(this);
		caption = (EmojiconTextView) findViewById(R.id.caption);
		tagged_peoples = (TextView) findViewById(R.id.tagged_peoples);
		
		train_with = (TextView) findViewById(R.id.train_with);
		goal = (TextView) findViewById(R.id.goal);
		category = (TextView) findViewById(R.id.category);
		area_of_focus = (TextView) findViewById(R.id.area_of_focus);

		user_name = (TextView) findViewById(R.id.user_name);
		repost_user_name= (TextView) findViewById(R.id.repost_user_name);
		time = (TextView) findViewById(R.id.time);
		(likes_count = (TextView) findViewById(R.id.likes_count)).setOnClickListener(this);
		(add_to_favourites = (ImageView) findViewById(R.id.add_to_favourites)).setOnClickListener(this);
		(back = (TextView) findViewById(R.id.back)).setOnClickListener(this);
		video_relative = (RelativeLayout) findViewById(R.id.video_relative);
		video_blink_icon=(ImageView)findViewById(R.id.video_blink_icon);
		(user_layout = (LinearLayout) findViewById(R.id.user_layout)).setOnClickListener(this);
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		
		location_lay=(LinearLayout)findViewById(R.id.location_lay);
		location = (TextView) findViewById(R.id.location);
		repost_lay=(LinearLayout)findViewById(R.id.repost_lay);
		tagged_peoples_lay=(LinearLayout)findViewById(R.id.tagged_peoples_lay);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		
		
		LayoutParams params = image.getLayoutParams();
		params.height = width;
		params.width = width;
		
		image.setLayoutParams(params);
		
		LayoutParams params2 = mVideoView.getLayoutParams();
		params2.height = width;
		params2.width = width;
		
		mVideoView.setLayoutParams(params2);
		
		
		if(getIntent().getStringExtra("from_where").equals("Data"))
		{
			
			try
			{
				set_data((HashMap<String, String>) getIntent().getSerializableExtra("map"));
			}
			catch(UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			post_id = data_hashmap.get("post_id");
			user_id= data_hashmap.get("user_id");
		}
		else if(getIntent().getStringExtra("from_where").equals("Notification"))
		{

			try
			{
				set_data((HashMap<String, String>) getIntent().getSerializableExtra("map"));
			}
			catch(UnsupportedEncodingException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			post_id = getIntent().getStringExtra("post_id");
			user_id= getIntent().getStringExtra("user_id");


			// i inserted the new AsyncTask which gets the data from server and then sets thats why there is no need of using this service here.
			//new Get_Pictures_Videos_ProgressTask(con,"","",post_id,getIntent().getStringExtra("which")).execute();



		}
		else if(getIntent().getStringExtra("from_where").equals("serch_tags"))
		{
			post_id = getIntent().getStringExtra("post_id");
			user_id= getIntent().getStringExtra("user_id");
		    
			new Get_Pictures_Videos_ProgressTask(con,user_id,"",post_id).execute();
		}
		
	
	}

	@Override
	protected void onResume()
	{
		if(Util_Class.checknetwork(con))
		{
			comment_list.clear();
			new Get_Comment_Likes_ProgressTask(con, "C",post_id, limit, "Image_Details").execute();
		}
		else
		{
			// Util_Class.show_Toast("Internet is not http://cg95.com/mobile_apps/project_management/webservices.php", con);
		}
		super.onResume();
	}
	
	public void refresh()// this method only for refresh recommendation in Image Video details
	{
		
		new Get_Pictures_Videos_ProgressTask(con,data_hashmap.get("user_id"),"",post_id,"S").execute();
	}

	int limit = 1;



	@Override
	public void onClick(View v)
	{
		
		switch (v.getId())
		{
	

			case R.id.likes_count:

				Intent ii = new Intent(con, View_All_Likes.class);
				ii.putExtra("post_id", post_id);
				con.startActivity(ii);

				break;
				
			case R.id.menu_icon:
				
				show_menu_dialog();
				
				break;

			
				
			case R.id.add_to_favourites:
				if(is_self_favourite = !is_self_favourite)
				{
					add_to_favourites.setImageResource(R.drawable.favorite);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("post_id", post_id);
					map.put("post_type", data_hashmap.get("data_type"));
					map.put("status", "Y");
					new Add_To_Favorites_ProgressTask(con, map).execute();
				}
				else
				{
					add_to_favourites.setImageResource(R.drawable.unfavorite);

					HashMap<String, String> map = new HashMap<String, String>();
					map.put("post_id", post_id);
					map.put("post_type", data_hashmap.get("data_type"));
					map.put("status", "N");

					new Add_To_Favorites_ProgressTask(con, map).execute();
				}
				break;

			case R.id.like_image:

				set_Like_UnLike();

				break;


			//Double tab on imageview ,like

			case R.id.image:

				if(count == 0)
				{
					count++;
					new MyCountDownTimer(250, 250).start();

				}
				else
				{

					double_tap = true;
				}


				break;

			case R.id.repost:

			//int id = (is_repost = !is_repost == true) ? R.drawable.repost_selected : R.drawable.repost;

				if(data_hashmap.get("is_self_posted").equals("N"))
				{
					repost.setImageResource(R.drawable.repost_selected);
				}

				//repost.setImageResource(R.drawable.repost);
				//repost.setImageResource(id);

				HashMap<String, String> map = new HashMap<>();
				map.put("post_id", post_id);
				map.put("action", "R");
				map.put("data", "");
				//map.put("type", type);
				new Add_Like_Comment_Repost_ProgressTask(con, map).execute();

				break;
				
			case R.id.recommend:


				int  res = data_hashmap.get("is_self_recommended").equals("N") ? R.drawable.recommend_selected : R.drawable.recommend_unselected ;
				recommend.setImageResource(res);
				
				String value=data_hashmap.get("is_self_recommended").equals("Y")?"N":"Y";
				new Recommend_Unrecommend_Post_ProgressTask(con,data_hashmap.get("post_id"), value).execute();
				
				break;

			case R.id.view_all_comments:

				Intent i = new Intent(con, View_All_Comments.class);
				i.putExtra("post_id", post_id);
				i.putExtra("from_where", "Image_Video_Details");
				con.startActivity(i);

				break;

			case R.id.back:

				this.finish();
				break;

			case R.id.user_layout:

				/*Global.set_user_id(user_id);
				Global.set_friend_id(rem_pref.getString("user_id", ""));

				Intent iii = new Intent(con, Other_Profile.class);
				// i.putExtra("user_id", list.get(position).get("user_id"));
				con.startActivity(iii);*/

				Log.e("user_id",""+user_id);
				
				if(user_id.equals(rem_pref.getString("user_id", "")))
				{
					//((Drawer) con).click();
				}
				else
				{
					Global.set_user_id(user_id);
					Global.set_friend_id(rem_pref.getString("user_id", ""));
					
					Intent ij=new Intent(con,Other_Profile.class);
					//i.putExtra("user_id", list.get(position).get("user_id"));
					con.startActivity(ij);
				}

				break;

			default:
				break;
		}

	}


	public class MyCountDownTimer extends CountDownTimer
	{

		public MyCountDownTimer(long startTime, long interval)
		{
			super(startTime, interval);
		}

		@Override
		public void onFinish()
		{
			count=0;

			if(double_tap)
			{

				double_tap=false;

				set_Like_UnLike();
			}
		}

		@Override
		public void onTick(long millisUntilFinished){}
	}


	public void set_Like_UnLike()
	{
		if(is_like = !is_like)
		{
			like_image.setImageResource(R.drawable.like_selected);

			l_c++;
			likes_count.setText(l_c + " like(s)");



			HashMap<String, String> map = new HashMap<String, String>();
			map.put("post_id", post_id);
			map.put("action", "L");
			map.put("data", "");
			//map.put("type", type);
			new Add_Like_Comment_Repost_ProgressTask(con, map).execute();
		}
		else
		{
			like_image.setImageResource(R.drawable.like);

			l_c--;
			likes_count.setText(l_c + " like(s)");



			HashMap<String, String> map = new HashMap<String, String>();
			map.put("post_id", post_id);
			map.put("action", "U");
			map.put("data", "");
			//map.put("type", type);

			new Add_Like_Comment_Repost_ProgressTask(con, map).execute();
		}
	}

	private void show_menu_dialog()
	{
		final Dialog menu_dialog = new Dialog(con);
		menu_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		menu_dialog.setContentView(R.layout.menu_dialog);
		menu_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final TextView delete_report = (TextView) menu_dialog.findViewById(R.id.delete_report);
		
		//final TextView recommend = (TextView) menu_dialog.findViewById(R.id.recommend);
		
		
		
	/*	if(data_hashmap.containsKey("is_self_subscribed"))
		{

			if(data_hashmap.get("is_self_subscribed").equals("Y"))
			{
				String value1 = data_hashmap.get("is_self_recommended").equals("Y") ? "Remove Recommendation" : "Recommend";

				recommend.setText(value1);

			}
			else
			{
				recommend.setVisibility(View.GONE);
			}
		}
		else
		{
			recommend.setVisibility(View.GONE);
		}*/
		
		
		String text=rem_pref.getString("user_id", "").equals(user_id)?"Delete":"Report to spam";
		delete_report.setText(text);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(menu_dialog.getWindow().getAttributes());
//		lp.width = 200;
//		lp.height = 150;
		lp.gravity = Gravity.TOP | Gravity.RIGHT;
		//lp.x = 100;   //x position
		lp.y = 210;   //y position

		menu_dialog.show();
		menu_dialog.getWindow().setAttributes(lp);

		delete_report.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				
				new Delete_Report_Thread(con,post_id,delete_report.getText().toString());
				menu_dialog.dismiss();
			}
		});
		
		/*recommend.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				
				String value=recommend.getText().toString().equals("Remove Recommendation")?"N":"Y";
				new Recommend_Unrecommend_Post_ProgressTask(con,data_hashmap.get("post_id"), value).execute();
				menu_dialog.dismiss();
			}
		});*/
		
		
	}
	
/*	public void set_recommemded_postdata()
	{
		String val=data_hashmap.get("is_self_recommended").equals("Y")?"N":"Y";
		data_hashmap.put("is_self_recommended", val);
		
		int  res = data_hashmap.get("is_self_recommended").equals("Y") ? R.drawable.recommend_selected : R.drawable.recommend_unselected ;
		recommend.setImageResource(res);
		
		Log.e("data_hashmap", ""+data_hashmap);
		
	}*/

	
	


	public static void setListViewHeightBasedOnItems()
	{

		ListAdapter listAdapter = comments_listview.getAdapter();
		if(listAdapter != null)
		{

			int numberOfItems = listAdapter.getCount();

			// Get total height of all items.
			int totalItemsHeight = 0;
			for(int itemPos = 0; itemPos < numberOfItems; itemPos++)
			{
				if(itemPos < 4)
				{
					View item = listAdapter.getView(itemPos, null, comments_listview);
					item.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
					Log.e(""+itemPos, ""+item.getMeasuredHeight() );
					totalItemsHeight += item.getMeasuredHeight();

				}
			}

			// Get total height of all item dividers.
			int totalDividersHeight = comments_listview.getDividerHeight() * (numberOfItems - 1);

			// Set list height.
			ViewGroup.LayoutParams params = comments_listview.getLayoutParams();
			params.height = totalItemsHeight + totalDividersHeight;
			comments_listview.setLayoutParams(params);
			comments_listview.requestLayout();

			//	return true;

		}
		else
		{
			//	return false;
		}

	}
	

	String tagged_user_id="",tagged_user_name="";
	int duration_s=0;
	public void  set_data(HashMap<String, String> hashmap) throws UnsupportedEncodingException
	{
	
		
		data_hashmap=hashmap;
		
		Log.e("data_hashmap", ""+data_hashmap);
		
		Util_Class util=new Util_Class();
		
		
		title_header.setText(data_hashmap.get("data_type").equals("I")? "Image Details" : "Video Details");
		
		
		profile_image.setImageUrl(con, data_hashmap.get("profile_image"));
		
		//Drawer.imageLoader.displayImage(data_hashmap.get("profile_image"), profile_image, Drawer.options);
		//Drawer.imageLoader.displayImage(data_hashmap.get("data"), image, Drawer.options);
	
		user_name.setText(data_hashmap.get("user_name"));
		
		l_c=Integer.parseInt(data_hashmap.get("like_count"));
		
		time.setText(util.get_time2(data_hashmap.get("date")));
		likes_count.setText(""+l_c+ " like(s)");
		
		if(data_hashmap.get("is_tagged").equals("Y"))
		{
			tagged_peoples_lay.setVisibility(View.VISIBLE);
			
			
			//tagged_peoples.setText(data_hashmap.get("tagged_user_name"));
			
			
			tagged_user_id=data_hashmap.get("tagged_user_id");
			tagged_user_name=data_hashmap.get("tagged_user_name");
			
			tagged_peoples.setMovementMethod(LinkMovementMethod.getInstance());
			String c=Util_Class.emoji_decode(data_hashmap.get("tagged_user_name"));
			tagged_peoples.setText(c, BufferType.SPANNABLE);
			Spannable spans = (Spannable) tagged_peoples.getText();
			BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
			iterator.setText(c);
			int start = iterator.first();
			for(int end = iterator.next(); end != BreakIterator.DONE; start = end,end = iterator.next())
			{
				Log.e("start"+start, "end"+end);
				
				String possibleWord = c.substring(start, end);
				Log.e("possibleWord", ""+possibleWord);
				
				if(possibleWord.startsWith("@"))
				{
					b = true;
				}
				
				if(Character.isLetterOrDigit(possibleWord.charAt(0))&& b==true)
				{
					ClickableSpan clickSpan = getClickableSpan(possibleWord,"tag");
					spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					b=false;
				}
			}
			
			
		}
		else
		{
			tagged_peoples_lay.setVisibility(View.GONE);
		}

		if(data_hashmap.get("is_self_posted").equals("Y"))
		{
			repost.setImageResource(R.drawable.repost_selected);
		}
		
		if(data_hashmap.get("is_repost").equals("Y"))
		{
			//Drawer.imageLoader.displayImage(data_hashmap.get("data"), repost_profile_image, Drawer.options);
			
			repost_profile_image.setImageUrl(con, data_hashmap.get("repost_profile_image"));
			repost_user_name.setText(data_hashmap.get("repost_user_name"));
			
			repost_lay.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					/*Global.set_user_id(data_hashmap.get("repost_user_id"));
					Global.set_friend_id(rem_pref.getString("user_id", ""));
					
					Intent i=new Intent(con,Other_Profile.class);
					con.startActivity(i);*/
					
					if(data_hashmap.get("repost_user_id").equals(rem_pref.getString("user_id", "")))
					{
						
					}
					else
					{
						Global.set_user_id(data_hashmap.get("repost_user_id"));
						Global.set_friend_id(rem_pref.getString("user_id", ""));
						
						Intent i=new Intent(con,Other_Profile.class);
						//i.putExtra("user_id", list.get(position).get("user_id"));
						con.startActivity(i);
					}
				}
			});
			
		}
		else
		{
			repost_lay.setVisibility(View.INVISIBLE);
		}
		
		
		String loc=data_hashmap.get("location");
		if(!loc.isEmpty())
		{
			location_lay.setVisibility(View.VISIBLE);
			location.setText(loc);
		}
		else
		{
			location_lay.setVisibility(View.GONE);
			
		}
		
		
		
		//caption.setText(Emoji_UtilClass.replaceCheatSheetEmojis(data_hashmap.get("caption")));
		train_with.setText(data_hashmap.get("trainer_name"));
		goal.setText(data_hashmap.get("fitness_goal"));
		category.setText(data_hashmap.get("category_name"));
		area_of_focus.setText(data_hashmap.get("focus_name"));
		
		
		
		train_with.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if(!data_hashmap.get("trainer_name").isEmpty())
				{
					Global.set_user_id(data_hashmap.get("trainer_id"));
					Global.set_friend_id(rem_pref.getString("user_id", ""));
					
					Intent i=new Intent(con,Other_Profile.class);
					//i.putExtra("user_id", list.get(position).get("user_id"));
					con.startActivity(i);
				}
				
			}
		});
		
		
		
		if(data_hashmap.get("is_self_favourite").equals("Y"))
		{
			is_self_favourite = true;
			add_to_favourites.setImageResource(R.drawable.favorite);
			//Log.i("is_self_liked", "yes");
		}
		else
		{
			is_self_favourite = false;
			add_to_favourites.setImageResource(R.drawable.unfavorite);
			//Log.i("is_self_liked", "No");
		}
		
		
		
		
		
		if(data_hashmap.get("is_self_liked").equals("Y"))
		{
			is_like = true;
			like_image.setImageResource(R.drawable.like_selected);
			//Log.i("is_self_liked", "yes");
		}
		else
		{
			is_like = false;
			like_image.setImageResource(R.drawable.like);
			//Log.i("is_self_liked", "No");
		}
		
		if(data_hashmap.get("data_type").equals("I"))
		{
			image.setImageUrl(con, data_hashmap.get("data"));
		}
		else if(data_hashmap.get("data_type").equals("V"))
		{
			image.setVisibility(View.GONE);
			video_relative.setVisibility(View.VISIBLE);
			String LINK = data_hashmap.get("data");

			duration_s=Integer.parseInt(data_hashmap.get("duration"));
			
			video_duration.setText(secondsToString(duration_s));
		
			
			mc = new MediaController(con);
			mc.setAnchorView(mVideoView);
			mc.setMediaPlayer(mVideoView);
			mc.setVisibility(View.GONE);
			Uri video = Uri.parse(LINK);
			mVideoView.setMediaController(mc);
			mVideoView.setVideoURI(video);
			mVideoView.seekTo(50); 
			
			ani=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
			video_blink_icon.setAnimation(ani);
			//video_blink_icon.startAnimation(ani);
			  
			
			if(duration_s <= 15)
			{
				mVideoView.start();
			}
			else
			{
				add_to_favourites.setVisibility(View.GONE);
				repost.setVisibility(View.GONE);
				
				video_play_icon.setVisibility(View.VISIBLE);
				
				if(data_hashmap.containsKey("is_self_subscribed"))
				{
					if(data_hashmap.get("is_self_subscribed").equals("Y") )
					{
						int  res = data_hashmap.get("is_self_recommended").equals("Y") ? R.drawable.recommend_selected : R.drawable.recommend_unselected ;

						
						recommend.setVisibility(View.VISIBLE);
						recommend.setImageResource(res);

					}
				}
			
			}
			 
			mVideoView.setOnPreparedListener(new OnPreparedListener()
			{

				@SuppressLint("NewApi")
				@Override
				public void onPrepared(MediaPlayer mp)
				{

					mp.setOnInfoListener(new OnInfoListener()
					{

						@Override
						public boolean onInfo(MediaPlayer mp, int what, int extra)
						{
							switch (what)
							{
								case MediaPlayer.MEDIA_INFO_BUFFERING_START:
									video_blink_icon.startAnimation(ani);
									break;
								case MediaPlayer.MEDIA_INFO_BUFFERING_END:
									video_blink_icon.setAnimation(null);
									break;
							}
							return false;
						}
					});

					mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener()
					{

						@Override
						public void onBufferingUpdate(MediaPlayer mp, int percent)
						{
							Log.e("mVideoView.getDuration()", "" + mVideoView.getDuration());
							Log.e("mVideoView.getCurrentPosition()", "" + mVideoView.getCurrentPosition());
							
							if(mVideoView.getCurrentPosition()>15000)
							{
								if(!data_hashmap.get("is_self_subscribed").equals("Y"))
								{
									mVideoView.pause();
									mVideoView.seekTo(50);
									video_play_icon.setVisibility(View.VISIBLE);
								}
							}

						}
					});

				}
			});
			
		
			
			
			mVideoView.setOnCompletionListener(new OnCompletionListener()
			{
				
				@Override
				public void onCompletion(MediaPlayer mp)
				{
					video_blink_icon.setVisibility(View.GONE);
					
					if(duration_s > 15)
					{
						video_play_icon.setVisibility(View.VISIBLE);
					}
					
				}
			});
			
		
			mVideoView.setOnTouchListener(new OnTouchListener()
			{

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1)
				{
					if(mVideoView.isPlaying())
					{
						mVideoView.pause();
					}
					else 
					{
						mVideoView.start();
					}
					
					video_blink_icon.setVisibility(View.VISIBLE);
					video_play_icon.setVisibility(View.GONE);
					return false;
				}
			});
		}
		
		
		
		caption.setMovementMethod(LinkMovementMethod.getInstance());
		String c=Util_Class.emoji_decode(data_hashmap.get("caption"));
		caption.setText(c, BufferType.SPANNABLE);
		Spannable spans = (Spannable) caption.getText();
		BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
		iterator.setText(c);
		int start = iterator.first();
		for(int end = iterator.next(); end != BreakIterator.DONE; start = end,end = iterator.next())
		{
			Log.e("start"+start, "end"+end);
			
			String possibleWord = c.substring(start, end);
			Log.e("possibleWord", ""+possibleWord);
			
			if(possibleWord.startsWith("#"))
			{
				b = true;
			}
			
			if(Character.isLetterOrDigit(possibleWord.charAt(0))&& b==true)
			{
				ClickableSpan clickSpan = getClickableSpan(possibleWord,"caption");
				spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				b=false;
			}
		}
	}
	
	

	
	private String secondsToString(int pTime)
	{
		return String.format("%02d:%02d", pTime / 60, pTime % 60);
	}
	boolean b=false;
	
	private ClickableSpan getClickableSpan(final String word, final String from)
	{
		return new ClickableSpan()
		{
			
			final String	mWord= word;
			final String	mfrom= from;
			

			@Override
			public void onClick(View widget)
			{
				Log.e(from, word);
				if(from.equals("caption"))
				{
					Intent intent = new Intent(con, Search_Hashtags_Child.class);
					intent.putExtra("value", mWord);
					con.startActivity(intent);
					Log.e(from, word);
				}
				else
				{
					String str[]=tagged_user_name.split(",");
					String id[]=tagged_user_id.split(",");
					
					
					

					for(int i = 0; i < str.length; i++)
					{
						if(word.equals(str[i].replace("@", "")))
						{
							/*Global.set_user_id(id[i]);
							Global.set_friend_id(rem_pref.getString("user_id", ""));

							Intent iii = new Intent(con, Other_Profile.class);
							con.startActivity(iii);*/
							
							
							if(id[i].equals(rem_pref.getString("user_id", "")))
							{
								//((Drawer) con).click();
							}
							else
							{
								Global.set_user_id(id[i]);
								Global.set_friend_id(rem_pref.getString("user_id", ""));
								
								Intent iii=new Intent(con,Other_Profile.class);
								//i.putExtra("user_id", list.get(position).get("user_id"));
								con.startActivity(iii);
							}
						}
					}
				}

			}

			public void updateDrawState(TextPaint ds)
			{
				super.updateDrawState(ds);
			}
		};
	}

	
	
}
