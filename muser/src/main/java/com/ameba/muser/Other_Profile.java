package com.ameba.muser;



import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.example.Adapter.PagerAdapter;
import com.example.ProgressTask.Add_To_Favorites_ProgressTask;
import com.example.ProgressTask.Get_Funds_Thread;
import com.example.ProgressTask.Get_Profile_ProgressTask;
import com.example.ProgressTask.Search_Hashtags_User_ProgressTask;
import com.example.ProgressTask.Set_Block_Follow_Users_ProgressTask;
import com.example.ProgressTask.Subscribe_Unsubscribe_Trainer_ProgressTask;
import com.example.Tabs.Tab_My_Profile_Pictures;
import com.example.Tabs.Tab_My_Profile_Sessions;
import com.example.Tabs.Tab_My_Profile_Videos;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.rockerhieu.emojicon.EmojiconTextView;

public class Other_Profile extends FragmentActivity implements TabHost.OnTabChangeListener,ViewPager.OnPageChangeListener,OnClickListener
{

	static Context						con;
	private TabHost						mTabHost;
	private ViewPager					mViewPager;
	private HashMap<String, TabInfo>	mapTabInfo								= new HashMap<String, Other_Profile.TabInfo>();
	private PagerAdapter				mPagerAdapter;
	LinearLayout						following_layout, followers_layout;
	//View rootView = null;
	public RoundedCornersGaganImg		profile_image;
	public TextView						user_name;
	public TextView					/*post_value,*/followers_value;
	public TextView						following_value;
	public TextView						web_address;
	public EmojiconTextView				description;
	SharedPreferences					rem_pref;
	public PullToRefreshScrollView		mPullRefreshScrollView;
	ScrollView							mScrollView;
	TextView							/*update_profile,*/back;

	public Spinner						spinner;
	public boolean				/*is_follow=false,*/is_add_to_favorite	= false;

	String								user_id;
	Other_Profile						con2;

	private class TabInfo
	{
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args)
		{
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}
	}

	class TabFactory implements TabContentFactory
	{
		private final Context mContext;

		public TabFactory(Context context)
		{
			mContext = context;
		}

		public View createTabContent(String tag)
		{
			Log.e("createTabContent", "" + tag);
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_other_profile);

		con = this;
		con2=this;
		

		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		profile_image = (RoundedCornersGaganImg) findViewById(R.id.profile_image);
		back = (TextView) findViewById(R.id.back);
		user_name = (TextView) findViewById(R.id.user_name);
		// post_value = (TextView) findViewById(R.id.post_value);
		followers_value = (TextView) findViewById(R.id.followers_value);
		following_value = (TextView) findViewById(R.id.following_value);
		(description = (EmojiconTextView )findViewById(R.id.description)).setOnClickListener(this);
		web_address = (TextView) findViewById(R.id.web_address);

		//		update_profile = (TextView) findViewById(R.id.update_profile);
		following_layout = (LinearLayout) findViewById(R.id.following_layout);
		followers_layout = (LinearLayout) findViewById(R.id.followers_layout);
		count = 0;

		spinner = (Spinner) findViewById(R.id.spinner);
		
		
		
		
		

		
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		

		spinner.setOnItemSelectedListener(new OnItemSelectedListener()
		{

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				Log.e(""+arg2,""+ arg2);
				switch (arg2)
				{
					case 1:
						final HashMap<String, String> map1 = new HashMap<String, String>();
						map1.put("user_id", Global.get_user_id());
					//	final String follow=is_follow?"U":"F";
						
						if(spinner.getSelectedItem().toString().equals("Follow"))
						{
							new Set_Block_Follow_Users_ProgressTask(con, map1,"F","Other_Profile").execute();
						}
						else
						{
							OnClickListener y1 = new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Util_Class.dialog_text_yes_no.dismiss();
									new Set_Block_Follow_Users_ProgressTask(con, map1,"U","Other_Profile").execute();
								}
							};
							Util_Class.show_dialog_text_yes_no(con, "Lost interest ehhh?", y1);
						}
							
						 
						 
						 
					/*	if(follow.equals("F"))
						{
							new Set_Block_Follow_Users_ProgressTask(con, map1,follow,"Other_Profile").execute();
						}
						else
						{
							OnClickListener y1 = new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Util_Class.dialog_text_yes_no.dismiss();
									new Set_Block_Follow_Users_ProgressTask(con, map1,follow,"Other_Profile").execute();
								}
							};
							Util_Class.show_dialog_text_yes_no(con, "Lost interest ehhh?", y1);
						}*/
						
						
						break;
					case 2:
						
						OnClickListener y = new OnClickListener()
						{
							@Override
							public void onClick(View v)
							{
								Util_Class.dialog_text_yes_no.dismiss();

								HashMap<String, String> map2 = new HashMap<String, String>();
								map2.put("user_id", Global.get_user_id());
								new Set_Block_Follow_Users_ProgressTask(con, map2,"B","Other_Profile").execute();
							}
						};
						Util_Class.show_dialog_text_yes_no(con, "Things just got serious,are you sure you want to block the selected user?", y);
						
						
						break;
					case 3:
						final String add_to_fav = is_add_to_favorite ? "N" : "Y";
						final HashMap<String, String> map = new HashMap<String, String>();
						map.put("post_id", Global.get_user_id());
						map.put("post_type", "U");
						map.put("status", add_to_fav);
						
						if(add_to_fav.equals("Y"))
						{
							new Add_To_Favorites_ProgressTask(con, map).execute();
						}
						else
						{
							OnClickListener y1 = new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Util_Class.dialog_text_yes_no.dismiss();
									new Add_To_Favorites_ProgressTask(con, map).execute();
								}
							};
							Util_Class.show_dialog_text_yes_no(con, "Are you sure, you want to remove this users from your favorites?", y1);
						}

						break;
					case 4:
						
						Intent i=new Intent(con,Chat_sharan.class);
						i.putExtra("img",Get_Profile_ProgressTask.profile_image_url);
						i.putExtra("id",Global.user_id  );
						con.startActivity(i);

						break;
					case 5: //Train with or Remove Training With

						if(spinner.getSelectedItem().toString().equals("Train With"))
						{
							OnClickListener proceed = new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Util_Class.super_dialog.dismiss();
									
									new Get_Funds_Thread(con,handler);
									/*Paypal_idea p = new Paypal_idea(con, "5");
									p.onBuyPressed();*/

								}
							};
							Util_Class.show_super_dialog(con, proceed,map_new.get("user_name"));
						}
						else
						{
							//String val=spinner.getSelectedItem().toString().equals("Train With")?"S":"U";
							new Subscribe_Unsubscribe_Trainer_ProgressTask(con, Global.get_user_id(), "U").execute();
						}

						break;
						
						
					/*case 6: //Recommend Trainer or Remove Recommendation

						String value=spinner.getSelectedItem().toString().equals("Remove Recommendation")?"N":"Y";
						new Subscribe_Unsubscribe_recommend_Trainer_ProgressTask(con, Global.get_user_id(), value,"Recommend").execute();
						break;
*/
					default:
						break;
				}
				
				
				spinner.setSelection(0);
	
				

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub

			}
		});



		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		this.initialiseTabHost(savedInstanceState);
		this.intialiseViewPager();

		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>()
		{
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				//new Get_Profile_ProgressTask(con).execute();
				refresh();
			}
		});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		
		user_id=Global.get_user_id();
		
		

		
		following_layout.setOnClickListener(this);
		followers_layout.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	
	
	private Handler	handler	= new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			Bundle bundle = msg.getData();
			String balance= bundle.getString("balance");
			int b=Math.round(Float.parseFloat(balance));

			if(b >= 5)
			{
				new Subscribe_Unsubscribe_Trainer_ProgressTask(con, user_id, "S").execute();
			}
			else
			{
				Util_Class.show_Toast("Insufficient Balance", con);
			}
		}

	};

	HashMap<String, String> map_new;
	public void set_data(HashMap<String, String> map)
	{
		this.map_new=map;
		//Log.e("dusyfguyref", "fiudgiuewrfgiew");
		mPullRefreshScrollView.onRefreshComplete();

		profile_image.setImageUrl(con, map.get("profile_image_url"));
		user_name.setText( map.get("user_name"));
		followers_value.setText( map.get("followers_count"));
		following_value.setText( map.get("following_count"));

		try
		{
			description.setText(Util_Class.emoji_decode( map.get("user_description")));
		}
		catch(UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		web_address.setText( map.get("web_address"));
	}
	
	
	@Override
	protected void onResume()
	{
		

		con = this;
		Global.set_user_id(user_id);


		//new Get_Profile_ProgressTask(con).execute();

		refresh();

		super.onResume();
	}

	public void refresh()
	{
		if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB )
		{
			new Get_Profile_ProgressTask(con).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		}
		else
		{
			new Get_Profile_ProgressTask(con).execute();
		}

	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			/*case R.id.description:
				try
				{
					Util_Class.show_description_dialog(con,description.getText().toString(), screenWidth);
				}
				catch(UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;*/
			case R.id.following_layout:
				startActivity(new Intent(con, Following.class));
				break;
			case R.id.followers_layout:
				startActivity(new Intent(con, Followers.class));
				break;
			case R.id.back:
				Other_Profile.this.finish();
				break;

			default:
				break;
		}
	}

	public void onSaveInstanceState(Bundle outState)
	{
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager()
	{
		List<Fragment> fragments = new Vector<Fragment>();
		// fragments.add(Fragment.instantiate(con,
		// Tab_My_Profile_Details.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Pictures.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Videos.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Sessions.class.getName()));
		mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		mViewPager = null;
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);

	}

//	TODO:i am extremly sorry for the jugad sir ,but new year party start hon vali h..
	//public static String USER_ID_Gagan="";

	private void initialiseTabHost(Bundle args)
	{
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;

	//	USER_ID_Gagan=Global.get_user_id();

		Other_Profile.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Pictures").setIndicator(""), (tabInfo = new TabInfo("Pictures", Tab_My_Profile_Pictures.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Other_Profile.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Videos").setIndicator(""), (tabInfo = new TabInfo("Videos", Tab_My_Profile_Videos.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Other_Profile.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Session").setIndicator(""), (tabInfo = new TabInfo("Session", Tab_My_Profile_Sessions.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);

		mTabHost.setOnTabChangedListener(this);
	}

	static int count = 0;
	static String[] tab_names = { "Pictures", "Videos", "Session" };

	private static void AddTab(Other_Profile activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo)
	{
		tabSpec.setContent(activity.new TabFactory(con)).setIndicator(tab_names[count]);
		tabHost.addTab(tabSpec);
		count++;
	}

	public void onTabChanged(String tag)
	{
		int pos = this.mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);
	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{
		//mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
	}

	@Override
	public void onPageSelected(int position)
	{
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
	}
	

	
	
}
