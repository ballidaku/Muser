package com.ameba.muser;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;

import com.ameba.muser.Trending.TabFactory;
import com.example.Adapter.PagerAdapter;
import com.example.Tabs.Tab_My_Favourite_Pictures;
import com.example.Tabs.Tab_My_Favourite_User;
import com.example.Tabs.Tab_My_Favourite_Videos;

public class My_Favourites extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener 
{
	static Context con;
	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, My_Favourites.TabInfo>();
	private PagerAdapter mPagerAdapter;

/*	static int count =0;
	
	public static int[] images = {  R.drawable.pictures, 
									R.drawable.videos,
									R.drawable.trainer};

	public static int[] images_selected = { R.drawable.pictures_selected, 
											R.drawable.videos_selected,
											R.drawable.trainer_selected};
*/
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
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}
	View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) 
	{
		con=getActivity();
		count =0;
		/*if (savedInstanceState != null)
		{
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); 
		}*/


		if(savedInstanceState==null)
		{
			rootView = inflater.inflate(R.layout.tabs_common_layout, container, false);
			this.mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
			mTabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);


			this.initialiseTabHost(savedInstanceState);

			this.intialiseViewPager();

			///change_pic(0);
		}
		
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
		fragments.add(Fragment.instantiate(con, Tab_My_Favourite_Pictures.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Favourite_Videos.class.getName()));
		fragments.add(Fragment.instantiate(con, Tab_My_Favourite_User.class.getName()));
		this.mPagerAdapter = new PagerAdapter(super.getFragmentManager(), fragments);
		//
		
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	private void initialiseTabHost(Bundle args) 
	{
		mTabHost.setup();
		
		TabInfo tabInfo = null;
		My_Favourites.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab1").setIndicator(""),(tabInfo = new TabInfo("Tab1", Tab_My_Favourite_Pictures.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		My_Favourites.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab2").setIndicator(""),(tabInfo = new TabInfo("Tab2", Tab_My_Favourite_Videos.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		My_Favourites.AddTab(this, this.mTabHost,this.mTabHost.newTabSpec("Tab3").setIndicator(""),(tabInfo = new TabInfo("Tab3", Tab_My_Favourite_User.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		
		mTabHost.setOnTabChangedListener(this);
	}

	static int count =0;
	static String[] tab_names = { "Pictures", "Videos", "Users" };
	private static void AddTab(My_Favourites activity, TabHost tabHost,TabHost.TabSpec tabSpec, TabInfo tabInfo) 
	{

		tabSpec.setContent(activity.new TabFactory(con)).setIndicator(tab_names[count]);
		tabHost.addTab(tabSpec);
		TextView x = (TextView) tabHost.getTabWidget().getChildAt(count).findViewById(android.R.id.title);
	    x.setTextSize(12);
		 //tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.pictures);
		 //tabHost.getTabWidget().getChildAt(2).setBackgroundResource(R.drawable.pictures);
		count++;
	}

	public void onTabChanged(String tag) 
	{
		int pos = this.mTabHost.getCurrentTab();
		mViewPager.setCurrentItem(pos);
		//change_pic(pos);
	}
/*	
	public void change_pic(int pos)
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
