package com.ameba.muser;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

import com.example.Adapter.PagerAdapter;
import com.example.Tabs.Tab_Invite_Others_Contacts;
import com.example.Tabs.Tab_Invite_Others_Email;
import com.example.Tabs.Tab_Invite_Others_Facebook;
import com.example.Tabs.Tab_Invite_Others_Twitter;

public class Invite_Others extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener 
{
	static Context con;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, Invite_Others.TabInfo>();
	private PagerAdapter mPagerAdapter;
	HorizontalScrollView horizontalScrollView;
//	static int count =0;
	View rootView =null;
	/*public static int[] images = {  R.drawable.fb, 
									R.drawable.instagram,
									R.drawable.email,
									R.drawable.contacts};

	public static int[] images_selected = { R.drawable.fb_selected, 
											R.drawable.instagram_selected,
											R.drawable.email_selected,
											R.drawable.contacts_selected};*/

	
	
	
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
		
		/*if (savedInstanceState != null) 
		{
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); 
		}*/
			count = 0;

			horizontalScrollView = (HorizontalScrollView) rootView.findViewById(R.id.horizontalScrollView);
			this.initialiseTabHost(savedInstanceState);

			this.intialiseViewPager();
		}
		//change_pic(0);
		
		
		return rootView;
	}

	public void onSaveInstanceState(Bundle outState) {
		/*outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected*/
		super.onSaveInstanceState(outState);
	}

	private void intialiseViewPager() 
	{

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(con, Tab_Invite_Others_Facebook.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_Invite_Others_Twitter.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_Invite_Others_Email.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_Invite_Others_Contacts.class.getName()));
		mPagerAdapter = new PagerAdapter(super.getFragmentManager(), fragments);
		
		mViewPager=null;
		mViewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initialiseTabHost(Bundle args) 
	{
		mTabHost = (TabHost)rootView.findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabInfo tabInfo = null;
		Invite_Others.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Facebook").setIndicator(""),(tabInfo = new TabInfo("Facebook", Tab_Invite_Others_Facebook.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Invite_Others.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Twitter").setIndicator(""),(tabInfo = new TabInfo("Twitter", Tab_Invite_Others_Twitter.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Invite_Others.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Email").setIndicator(""),(tabInfo = new TabInfo("Email", Tab_Invite_Others_Email.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		Invite_Others.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Contacts").setIndicator(""),(tabInfo = new TabInfo("Contacts", Tab_Invite_Others_Contacts.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		mTabHost.setOnTabChangedListener(this);
	}

	static int count =0;
	static String[] tab_names = { "facebook", "twitter", "email","contacts" };
	

	private static void AddTab(Invite_Others activity, TabHost tabHost,TabHost.TabSpec tabSpec, TabInfo tabInfo) 
	{
		tabSpec.setContent(activity.new TabFactory(con)).setIndicator(tab_names[count]);
		tabHost.addTab(tabSpec);
		TextView x = (TextView) tabHost.getTabWidget().getChildAt(count).findViewById(android.R.id.title);
	    x.setTextSize(12);
	    
		 //tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.pictures);
		 //tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.pictures);
		// Log.e("count home", ""+count);
		 count++;
	}

	public void onTabChanged(String tag) 
	{
		//TabInfo newTab = this.mapTabInfo.get(tag);
		
		int pos = this.mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);
	}
	
	/*public void change_pic(int pos)
	{
		mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(images[0]);
		mTabHost.getTabWidget().getChildAt(1).setBackgroundResource(images[1]);
		mTabHost.getTabWidget().getChildAt(2).setBackgroundResource(images[2]);
		mTabHost.getTabWidget().getChildAt(3).setBackgroundResource(images[3]);
		
		mTabHost.getTabWidget().getChildAt(pos).setBackgroundResource(images_selected[pos]);
	}*/


	@Override
	public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) 
	{ View tabView = mTabHost.getTabWidget().getChildAt(position);
    if (tabView != null)
    {
        final int width = horizontalScrollView.getWidth();
        final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
        horizontalScrollView.scrollTo(scrollPos, 0);
    } else {
    	horizontalScrollView.scrollBy(positionOffsetPixels, 0);
    }}

	@Override
	public void onPageSelected(int position) 
	{
		this.mTabHost.setCurrentTab(position);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}
}
