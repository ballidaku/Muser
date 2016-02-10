package com.ameba.muser;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class Camera extends FragmentActivity
{
	
	public static  String which_fragment="";
	Context con;
	Picture_fragment picture_fragment;
	Video_fragment video_fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if(android.os.Build.VERSION.SDK_INT>9)
		 {
			 StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
			 StrictMode.setThreadPolicy(policy);
		 }
		
		con=this;
		 video_fragment = new Video_fragment();
		 picture_fragment=new  Picture_fragment(con,video_fragment);
		
		
        FragmentManager fragmentManager1 = getFragmentManager();
	    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
	    fragmentTransaction1.replace(android.R.id.content,picture_fragment);
	    fragmentTransaction1.addToBackStack(null);
	    fragmentTransaction1.commit();
	
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//	    if (keyCode == KeyEvent.KEYCODE_BACK)
//	    {
//	    	//Log.e("ddddddddddddddddddddd", ""+getFragmentManager().getBackStackEntryCount());
//	        if (getFragmentManager().getBackStackEntryCount() == 1)
//	        {
//	        	Camera.this.finish();
//	            return false;
//	        }
//	        else
//	        {
//	            getFragmentManager().popBackStack();
//	           // removeCurrentFragment();
//
//	            return false;
//	        }
//
//
//
//	    }
//
//	    return super.onKeyDown(keyCode, event);
//	}
	
	

	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		Log.e("onConfigurationChanged", "onConfigurationChanged");
		
		
	/*	Picture_fragment frag=new Picture_fragment();
		frag.refresh();*/
		//setCameraDisplayOrientation(getActivity(), CameraInfo.CAMERA_FACING_BACK, camera);
	}

	/*@Override
	protected void onPause()
	{
		super.onPause();
		video_fragment.finish_on_spot();
	}*/

	@Override
	public void onBackPressed()
	{
		if(which_fragment.equals("camera"))
		{
			finish();
		}
		else
		{
			video_fragment.finish_on_spot();
		}
	}
}
