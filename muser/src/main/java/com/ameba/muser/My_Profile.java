package com.ameba.muser;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.example.Adapter.PagerAdapter;
import com.example.ProgressTask.Get_Profile_ProgressTask;
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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class My_Profile extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, OnClickListener
{
	static Context						con;
	private TabHost						mTabHost;
	private ViewPager					mViewPager;
	private HashMap<String, TabInfo>	mapTabInfo	= new HashMap<String, My_Profile.TabInfo>();
	private PagerAdapter				mPagerAdapter;
	LinearLayout						following_layout, followers_layout;
	View								rootView	= null;
	public RoundedCornersGaganImg		profile_image;
	public TextView						user_name;
	public TextView					/*post_value,*/followers_value;
	public TextView						following_value;
	public TextView						web_address;
	public EmojiconTextView				description;
	SharedPreferences					rem_pref;
	public PullToRefreshScrollView		mPullRefreshScrollView;
	//ScrollView							mScrollView;
	TextView							update_profile;
	My_Profile							con2;

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

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		con = getActivity();
		con2=this;

		if(savedInstanceState==null)
		{

			Display display = getActivity().getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;

			rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

			rootView = inflater.inflate(R.layout.my_profile, container, false);
			profile_image = (RoundedCornersGaganImg) rootView.findViewById(R.id.profile_image);
			user_name = (TextView) rootView.findViewById(R.id.user_name);
			// post_value = (TextView) rootView.findViewById(R.id.post_value);
			followers_value = (TextView) rootView.findViewById(R.id.followers_value);
			following_value = (TextView) rootView.findViewById(R.id.following_value);
			(description = (EmojiconTextView) rootView.findViewById(R.id.description)).setOnClickListener(this);
			web_address = (TextView) rootView.findViewById(R.id.web_address);
			// horizontal_tab = (HorizontalScrollView)
			// rootView.findViewById(R.id.horizontal_tab);
			update_profile = (TextView) rootView.findViewById(R.id.update_profile);
			following_layout = (LinearLayout) rootView.findViewById(R.id.following_layout);
			followers_layout = (LinearLayout) rootView.findViewById(R.id.followers_layout);
			count = 0;
			//profile_image.setImageUrl(rem_pref.getString("profile_image", ""));


//		Drawer.imageLoader.displayImage(rem_pref.getString("profile_image", ""), profile_image, Drawer.options);


			ViewTreeObserver vto = this.description.getViewTreeObserver();
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{

				@Override
				public
				void onGlobalLayout()
				{
					ViewTreeObserver obs = description.getViewTreeObserver();
					obs.removeGlobalOnLayoutListener(this);
					if (description.getLineCount() > 4)
					{
						int lineEndIndex = description.getLayout().getLineEnd(3);
						String text = description.getText().subSequence(0, lineEndIndex - 3) + "...";
						description.setText(text);
					}
				}
			});


			// }
			//screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
			this.initialiseTabHost(savedInstanceState);
			this.intialiseViewPager();
			mPullRefreshScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.pull_refresh_scrollview);
			mPullRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>()
			{
				@Override
				public
				void onRefresh(PullToRefreshBase<ScrollView> refreshView)
				{
					Global.set_user_id(rem_pref.getString("user_id", ""));
					new Get_Profile_ProgressTask(con, con2).execute();
				}
			});


			ViewTreeObserver observer = mPullRefreshScrollView.getViewTreeObserver();
			observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener()
			{

				@Override
				public
				void onGlobalLayout()
				{
					mPullRefreshScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});

			Global.set_user_id(rem_pref.getString("user_id", ""));
			new Get_Profile_ProgressTask(con, con2).execute();

			update_profile.setOnClickListener(this);
			following_layout.setOnClickListener(this);
			followers_layout.setOnClickListener(this);

		}
		return rootView;
	}
	
	
	@Override
	public void onResume()
	{
		Global.set_user_id(rem_pref.getString("user_id", ""));
		
		set_data();
		
		super.onResume();
	}
	
	
	public void set_data()
	{
		profile_image.setImageUrl(con, rem_pref.getString("profile_image", ""));
		user_name.setText(rem_pref.getString("user_name", ""));
		// post_value.setText(rem_pref.getString("post_count", ""));
		followers_value.setText(rem_pref.getString("followers_count", ""));
		following_value.setText(rem_pref.getString("following_count", ""));
		
		
		
		try
		{
			description.setText(Util_Class.emoji_decode(rem_pref.getString("user_description", "")));
		}
		catch(UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
		web_address.setText(rem_pref.getString("web_address", ""));
	}
	
	
	
	
	

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			/*case R.id.description:
				try
				{
					Util_Class.show_description_dialog(con, description.getText().toString(), screenWidth);
				}
				catch(UnsupportedEncodingException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;*/

			case R.id.update_profile:
				startActivity(new Intent(con, Update_Profile.class));
				break;
			case R.id.following_layout:
				startActivity(new Intent(con, Following.class));
				break;
			case R.id.followers_layout:
				startActivity(new Intent(con, Followers.class));
				break;
			default:
				break;
		}
	}

	

	public void onSaveInstanceState(Bundle outState)
	{
		/*outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected*/
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager()
	{

		// Other_Profile.USER_ID_Gagan="";



		List<Fragment> fragments = new Vector<Fragment>();
		// fragments.add(Fragment.instantiate(con,
		// Tab_My_Profile_Details.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Pictures.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Videos.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Sessions.class.getName()));
		mPagerAdapter = new PagerAdapter(super.getFragmentManager(), fragments);
		mViewPager = null;
		mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	
	}

	private void initialiseTabHost(Bundle args)
	{
		mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		
		My_Profile.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Pictures").setIndicator(""), (tabInfo = new TabInfo("Pictures", Tab_My_Profile_Pictures.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		My_Profile.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Videos").setIndicator(""), (tabInfo = new TabInfo("Videos", Tab_My_Profile_Videos.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		My_Profile.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Session").setIndicator(""), (tabInfo = new TabInfo("Session", Tab_My_Profile_Sessions.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
	
		mTabHost.setOnTabChangedListener(this);
	}


	static int count = 0;
	static String[] tab_names = { "Pictures", "Videos", "Session" };

	private static void AddTab(My_Profile activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo)
	{
		
	    
		tabSpec.setContent(activity.new TabFactory(con)).setIndicator(tab_names[count]);
//		TextView x = (TextView) tabHost.getTabWidget().getChildAt(count).findViewById(android.R.id.title);
//	    x.setTextSize(14);
//	    x.setText(tab_names[count]);
		tabHost.addTab(tabSpec);
		TextView x = (TextView) tabHost.getTabWidget().getChildAt(count).findViewById(android.R.id.title);
	    x.setTextSize(12);
		count++;
	}

	public void onTabChanged(String tag)
	{
		// TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);
		// Log.e("tag", "" + tag);
		// change_pic(pos);
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
