package com.ameba.muser;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.classes.Util_Class;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;


public class Splash extends Activity
{

	
	protected int _splashTime = 800; 
	private Thread splashTread;
	SharedPreferences rem_pref;
	Context con;
	int height;
	
	public int getStatusBarHeight() 
	{ 
	      int result = 0;
	      int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = getResources().getDimensionPixelSize(resourceId);
	      } 
	      return result;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		LinearLayout logo_lay=(LinearLayout)findViewById(R.id.logo_lay);
		
		Display display =getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		height = size.y-getStatusBarHeight();/**/
		
		LayoutParams params =  logo_lay.getLayoutParams();
		int l=(int) (height*0.40);
		params.height = l;/**/
		//params.width = width;
		logo_lay.setLayoutParams(params);

		

		
		con=this;
		//Util_Class.get_Hash_key(con);
		
		rem_pref=getSharedPreferences("Remember", MODE_WORLD_READABLE);
		
		splashTread = new Thread()
	    {
	        @Override
	        public void run() 
	        {
	            try 
	            {	            	
	            	synchronized(this)
	            	{
	            		wait(_splashTime);
	            	}
	            	
	            }
	            catch(InterruptedException e) {} 
	            finally 
	            {
	            	Log.e("Preference", rem_pref.getAll().toString());
	 	           
	            	if(!rem_pref.contains("user_id"))
	            	{
	            		Log.e("which", "1");
		            	startActivity(new Intent(Splash.this,Login.class)); 
		            	Util_Class.global_value=false;
		            	 c.start();
	            	}
	            	else if(!rem_pref.getString("user_id","user_id").equals(""))
            		{
            		    Log.e("which", "2");
						rem_pref.edit().putString("current_frag","Home").commit();

						startActivity(new Intent(con,Drawer.class));
            		   c.start();
            		}
	            	else
	            	{
	            		Log.e("which", "3");
		            	startActivity(new Intent(Splash.this,Login.class));
		            	Util_Class.global_value=false;
		            	 c.start();
	            	}
            		
	            }
	        }
	    };
	    
	    splashTread.start();
	}
	
	
	CountDownTimer c=new CountDownTimer(2000, 1000) {

        public void onTick(long millisUntilFinished)
        {      
        }

        public void onFinish() 
        {
        	 finish();
        }
     }  ; 
}
