package com.ameba.muser;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;

import com.example.Adapter.PagerAdapter;
import com.example.Tabs.Tab_Home_Pictures;
import com.example.Tabs.Tab_Home_Sessions;
import com.example.Tabs.Tab_Home_Videos;
import com.example.Tabs.Tab_My_Profile_Sessions;


public class Home extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
{
	static Context con;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, Home.TabInfo>();
	private PagerAdapter mPagerAdapter;

	View rootView =null;
	//SharedPreferences rem_pref;
/*	public static int[] images = {  R.drawable.pictures, 
									R.drawable.videos,
									R.drawable.sessions};

	public static int[] images_selected = { R.drawable.pictures_selected, 
											R.drawable.videos_selected,
											R.drawable.sessions_selected};
*/

	Tab_Home_Pictures tab_home_pictures;
	Tab_Home_Videos tab_home_videos;

/*	public
	Home(Tab_Home_Pictures tab_home_pictures,Tab_Home_Videos tab_home_videos,Fragment tab_home_sessions)
	{
		this.tab_home_pictures=tab_home_pictures;
		this.tab_home_videos=tab_home_videos;
	}*/

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
			Log.e("createTabContent", ""+tag);
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}


	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{

		con=getActivity();

		if(savedInstanceState==null)
		{
			rootView = inflater.inflate(R.layout.tabs_common_layout, container, false);

			//	rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		/*if (savedInstanceState != null)
		{
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); 
		}*/
			count = 0;


			this.initialiseTabHost(savedInstanceState);

			this.intialiseViewPager();

			//Global.set_user_id(rem_pref.getString("user_id", ""));

		}
	
		
		
		return rootView;
	}

	public void onSaveInstanceState(Bundle outState) {
//		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
//																// selected
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() 
	{

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(con, Tab_Home_Pictures.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_Home_Videos.class.getName()));

		//***********************************I changed Tab_Home_Sessions to Tab_My_Profile_Sessions******************************************
		fragments.add(Fragment.instantiate(con, Tab_My_Profile_Sessions.class.getName()));

		//***********************************************************************************************************************************
		mPagerAdapter = new PagerAdapter(super.getFragmentManager(), fragments);
		
		mViewPager=null;
		mViewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);

	/*	FragmentManager manager = getActivity().getSupportFragmentManager();
		manager.beginTransaction().addToBackStack(Tab_Home_Pictures.class.getName()).commit();
		manager.beginTransaction().addToBackStack(Tab_Home_Videos.class.getName()).commit();
		manager.beginTransaction().addToBackStack(Tab_Home_Sessions.class.getName()).commit();*/



	}

	private void initialiseTabHost(Bundle args) 
	{
		mTabHost = (TabHost)rootView.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabInfo tabInfo = null;
		Home.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Pictures").setIndicator(""),(tabInfo = new TabInfo("Pictures", Tab_Home_Pictures.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Home.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Videos").setIndicator(""),(tabInfo = new TabInfo("Videos", Tab_Home_Videos.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Home.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Session").setIndicator(""),(tabInfo = new TabInfo("Session", Tab_My_Profile_Sessions.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		mTabHost.setOnTabChangedListener(this);
	}

	static int count =0;
	static String[] tab_names = { "Pictures", "Videos", "Session" };

	private static void AddTab(Home activity, TabHost tabHost,TabHost.TabSpec tabSpec, TabInfo tabInfo) 
	{
		tabSpec.setContent(activity.new TabFactory(con)).setIndicator(tab_names[count]);
		tabHost.addTab(tabSpec);
		TextView x = (TextView) tabHost.getTabWidget().getChildAt(count).findViewById(android.R.id.title);
	    x.setTextSize(12);
		
	//	tabSpec.setContent(activity.new TabFactory(con)).setIndicator(tab_names[count]);
	//	 tabHost.getTabWidget().getChildAt(count).setBackgroundResource(tab_names[count]);
		 //tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.pictures);
		 //tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.pictures);
		// Log.e("count home", ""+count);
		 count++;
	}

	public void onTabChanged(String tag) 
	{
		
		
		int pos = this.mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);
		//TabInfo newTab = this.mapTabInfo.get(tag);
		
		/*int pos = this.mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);
		Log.e("tag", ""+tag);*/
		//change_pic(pos);
	}
	
	/*public void change_pic(int pos)
	{
		mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(images[0]);
		mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(images[1]);
		mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(images[2]);
		
		
		mTabHost.getTabWidget().getChildAt(pos).setBackgroundResource(images_selected[pos]);
	}*/


	@Override
	public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) 
	{}

	@Override
	public void onPageSelected(int position) 
	{
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}
}
