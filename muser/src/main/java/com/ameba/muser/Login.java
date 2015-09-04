package com.ameba.muser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.ProgressTask.Forgot_Password_ProgressTask;
import com.example.ProgressTask.Login_ProgressTask;
import com.example.classes.CannonballTwitterLoginButton;
import com.example.classes.CustomService;
import com.example.classes.Util_Class;
import com.example.classes.Util_Class.Text;
import com.example.instagram.ApplicationData;
import com.example.instagram.InstagramApp;
import com.example.instagram.InstagramApp.OAuthAuthenticationListener;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import io.fabric.sdk.android.Fabric;
// import android.os.Handler.Callback;
// Callback;

@SuppressLint("ClickableViewAccessibility")
public
class Login extends FragmentActivity implements OnClickListener, OnTouchListener
{
    LinearLayout swipe_lay, logo_lay, anim_sceond_lay, linear_lay, second_lay, bottom_lay;
    EditText email, password;
    ImageView /* remember_checkbox, */login,/* twitter_image, */
            instagram, fb_logo;
    TextView forgot_password, faq, contact_us;
    Intent intent;
    Animation animALpha, animALpha_second, animALpha_third, animsecond, anim, anim_third;
    Context con = null;

    LoginButton facebook;
    ProfileTracker profileTracker;
    //AccessTokenTracker accessTokenTracker;
    //UiLifecycleHelper uiHelper;

	/* private static Twitter twitter; private static RequestToken requestToken; private static SharedPreferences mSharedPreferences; ProgressDialog pDialog; */

    static public InstagramApp mApp;
    // private TwitterLoginButton loginButton;

    CannonballTwitterLoginButton twitter_image;

    // Note: Your consumer key and secret should be obfuscated in your source
    // code before shipping.


    GoogleCloudMessaging gcm;
    //String GCM_Reg_id = ""/* ,identifier */;
    Login_ProgressTask login_ProgressTask;
    SharedPreferences rem_pref;
    int height;

    public
    int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        con = this;

        //************************************************Facebook**************************************

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        //**********************************************************************************************

        Log.e("erfwe23", "....." + getStatusBarHeight());

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        height = size.y - getStatusBarHeight();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Util_Class.TWITTER_KEY, Util_Class.TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

//		uiHelper = new UiLifecycleHelper(this, statusCallback);
//		uiHelper.onCreate(savedInstanceState);

//		Session fbsession = Session.getActiveSession();
//
//		if(fbsession != null)
//		{
//			fbsession.closeAndClearTokenInformation();
//		}
        setContentView(R.layout.activity_login);


        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        swipe_lay = (LinearLayout) findViewById(R.id.swipe_lay);
        logo_lay = (LinearLayout) findViewById(R.id.logo_lay);
        bottom_lay = (LinearLayout) findViewById(R.id.bottom_lay);
        anim_sceond_lay = (LinearLayout) findViewById(R.id.anim_sceond_lay);
        linear_lay = (LinearLayout) findViewById(R.id.linear_lay);
        second_lay = (LinearLayout) findViewById(R.id.second_lay);

        animALpha_second = AnimationUtils.loadAnimation(this, R.anim.alpha_second);
        animsecond = AnimationUtils.loadAnimation(this, R.anim.translate_second);
        animALpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        animALpha_third = AnimationUtils.loadAnimation(this, R.anim.alpha_third);
        anim_third = AnimationUtils.loadAnimation(this, R.anim.translate_third);

        (email = (EditText) findViewById(R.id.email)).addTextChangedListener(new Text(email));
        (password = (EditText) findViewById(R.id.password)).addTextChangedListener(new Text(password));

		/* remember_checkbox= (ImageView)findViewById(R.id.remember_checkbox); */
        (login = (ImageView) findViewById(R.id.login)).setOnClickListener(this);
        facebook = (LoginButton) findViewById(R.id.facebook);
        fb_logo = (ImageView) findViewById(R.id.fb_logo);

        twitter_image = (CannonballTwitterLoginButton) findViewById(R.id.twitter);
        instagram = (ImageView) findViewById(R.id.instagram);

        forgot_password = (TextView) findViewById(R.id.forgot_password);
        (faq = (TextView) findViewById(R.id.faq)).setOnClickListener(this);
        (contact_us = (TextView) findViewById(R.id.contact_us)).setOnClickListener(this);

        LayoutParams params = logo_lay.getLayoutParams();
        int l = (int) (height * 0.40);
        params.height = l;
        //params.width = width;
        logo_lay.setLayoutParams(params);

        LayoutParams params2 = swipe_lay.getLayoutParams();
        int s = (int) (height * 0.07);
        params2.height = s;
        //params.width = width;
        swipe_lay.setLayoutParams(params2);

        LayoutParams params3 = bottom_lay.getLayoutParams();
        int b = (int) (height * 0.53);
        params3.height = b;
        //params.width = width;
        bottom_lay.setLayoutParams(params3);

        Log.e("Heignt", height + "...." + l + "...." + s + "...." + b);

        LayoutParams params4 = anim_sceond_lay.getLayoutParams();
        int b1 = (int) (b * 0.55);
        params4.height = b1;
        //params.width = width;
        anim_sceond_lay.setLayoutParams(params4);

        LayoutParams params5 = linear_lay.getLayoutParams();
        int b2 = (int) (b * 0.18);
        params5.height = b2;
        //params.width = width;
        linear_lay.setLayoutParams(params5);

        LayoutParams params6 = second_lay.getLayoutParams();
        int b3 = (int) (b * 0.22);
        params6.height = b3;
        //params.width = width;
        second_lay.setLayoutParams(params6);

        Log.e("B", "" + b + ".." + b1 + ".." + b2 + ".." + b3);

        StartAnimations_imageview();
        StartAnimations();
        swipe_lay.setVisibility(View.INVISIBLE);


        //************************************************Facebook**********************************

        //facebook.setApplicationId(getResources().getString(R.string.app_id));
        //facebook.setReadPermissions("public_profile", "email", "user_friends", "user_birthday", "read_friendlists");
        facebook.setReadPermissions("user_friends");
        //fb_logo.setBackgroundResource(R.drawable.fb_btn);

        LayoutParams params7 = fb_logo.getLayoutParams();

        params7.height = b3;
        params7.width = b3;
        fb_logo.setLayoutParams(params7);

        //*********************************************************************************************

        fb_logo.setOnClickListener(this);
        swipe_lay.setOnTouchListener(this);
        instagram.setOnClickListener(this);
        twitter_image.setOnClickListener(this);
        forgot_password.setOnClickListener(this);

        password.setOnEditorActionListener(new OnEditorActionListener()
        {
            public
            boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    login.performClick();
                    return true;
                }
                return false;
            }
        });

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
        mApp.setListener(new OAuthAuthenticationListener()
        {

            @Override
            public
            void onSuccess()
            {
                // tvSummary.setText("Connected as " + mApp.getUserName());
                // btnConnect.setText("Disconnect");
                // llAfterLoginView.setVisibility(View.VISIBLE);
                // userInfoHashmap = mApp.
                mApp.fetchUserName(handler);
            }

            @Override
            public
            void onFail(String error)
            {
                Toast.makeText(con, error, Toast.LENGTH_SHORT).show();
            }
        });

        twitter_image.setBackgroundResource(R.drawable.tw_btn);
        LayoutParams params8 = twitter_image.getLayoutParams();

        params8.height = b3;
        params8.width = b3;
        twitter_image.setLayoutParams(params8);
        twitter_image.setCallback(new Callback<TwitterSession>()
        {
            @Override
            public
            void success(Result<TwitterSession> result)
            {
                // Log.e("Twitter",
                // ""+result.data.getUserName()+""+result.data.getUserId()+""+result.data.getUserName());
                TwitterSession session = Twitter.getSessionManager().getActiveSession();

                getTwitterData(session);
            }

            @Override
            public
            void failure(TwitterException exception)
            {
                // Do something on failure
            }
        });

        LayoutParams params9 = instagram.getLayoutParams();

        params9.height = b3;
        params9.width = b3;
        instagram.setLayoutParams(params9);

        if (Util_Class.checknetwork(con) && !rem_pref.contains("GCM_Reg_id"))
        {
            registerInBackground();
        }

        if (mApp.hasAccessToken())
        {
            mApp.resetAccessToken();
        }


        //************************************************Facebook****************************************


        //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));


		/*facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override public
			void onSuccess(LoginResult loginResult)
			{
				Log.e("registerCallback", "onSuccess");
			}

			@Override public
			void onCancel()
			{
				Log.e("registerCallback", "onCancel");
			}

			@Override public
			void onError(FacebookException error)
			{
				Log.e("registerCallback", "onError");
			}
		});*/
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>()
                {
                    @Override
                    public
                    void onSuccess(LoginResult loginResult)
                    {
                        Log.e("onSuccess", "onSuccess");

                        Log.e("onSuccess", "1" + loginResult.getAccessToken());
                        Log.e("onSuccess", "2" + loginResult.getRecentlyDeniedPermissions());
                        Log.e("onSuccess", "3" + loginResult.getRecentlyGrantedPermissions());

                        if( Profile.getCurrentProfile() != null)
                        {
                            Log.e("onSuccess", "4" + Profile.getCurrentProfile().getName()) ;
                            Log.e("onSuccess", "5" + Profile.getCurrentProfile().getId()) ;
                            Log.e("onSuccess", "6" + Profile.getCurrentProfile().getProfilePictureUri(500, 500)) ;

                            HashMap<String,String>map=new HashMap<>();
                            map.put("id",Profile.getCurrentProfile().getId());
                            map.put("image",Profile.getCurrentProfile().getProfilePictureUri(500, 500).toString());
                            map.put("name",Profile.getCurrentProfile().getName());

                            login_through_fb(map);
                        }
                        else
                        {
                            profileTracker.startTracking();
                        }



                    }

                    @Override
                    public
                    void onCancel()
                    {
                        Log.e("onCancel", "Hello");
                    }

                    @Override
                    public
                    void onError(FacebookException exception)
                    {
                        Log.e("onError", "Hello");
                    }
                });

      /*  accessTokenTracker = new AccessTokenTracker()
        {
            @Override
            protected
            void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken)
            {
                // Set the access token using
                // currentAccessToken when it's loaded or set.

            }
        };*/


        profileTracker = new ProfileTracker()
        {
            @Override
            protected
            void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile)
            {
                Log.e("AAAAAAAAAAAAAA", "Hello");

                if (Profile.getCurrentProfile() != null)
                {
                   // Log.e("currentProfile", "Hello" + currentProfile.getName() + "...." + currentProfile.getProfilePictureUri(100, 100) + "...." + currentProfile.getId());



                    Log.e("onSuccess", "7" + currentProfile.getName()) ;
                    Log.e("onSuccess", "8" + currentProfile.getId()) ;
                    Log.e("onSuccess", "9" + currentProfile.getProfilePictureUri(500, 500).toString()) ;


                    HashMap<String,String>map=new HashMap<>();
                    map.put("id", currentProfile.getId());
                    map.put("image", currentProfile.getProfilePictureUri(500, 500).toString());
                    map.put("name", currentProfile.getName());

                    login_through_fb(map);

                   /* if (rem_pref.contains("GCM_Reg_id"))
                    {

                        new Login_ProgressTask(con, currentProfile.getId(), "", "F", currentProfile.getProfilePictureUri(500, 500).toString(), currentProfile.getName().replace(" ", ""), currentProfile.getName()).execute();

                    }
                    else
                    {
                        registerInBackground();
                        System.out.println("Attention" + "Registration Id not found");
                    }*/
                }
                else
                {
                    stop_fb();
                }
            }
        };



	/*	facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
		{
			@Override
			public
			void onSuccess(LoginResult loginResult)
			{
				// App code
			}

			@Override
			public
			void onCancel()
			{
				// App code
			}

			@Override
			public
			void onError(FacebookException exception)
			{
				// App code
			}
		});*/
    }


    public void login_through_fb(HashMap<String,String> map)
    {

        if (rem_pref.contains("GCM_Reg_id"))
        {

            new Login_ProgressTask(con, map.get("id"), "", "F", map.get("image"), map.get("name").replace(" ", ""), map.get("name")).execute();

        }
        else
        {
            registerInBackground();
            Log.e("Attention" , "Registration Id not found");
        }
    }

    public
    void stop_fb()
    {
        Log.e("Logout", "Logout");


        if(profileTracker.isTracking())
        {
            profileTracker.stopTracking();
        }

      /*  if(accessTokenTracker.isTracking())
        {
            accessTokenTracker.stopTracking();
        }*/


        try
        {
            AccessToken.setCurrentAccessToken(null);
            Profile.setCurrentProfile(null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


       /* if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null)
        {
            LoginManager.getInstance().logOut();
        }*/

    }

    CallbackManager callbackManager;

    //**********************************************************************************************************
    //********************************************TWITTER*****************************************************
    //*********************************************************************************************************

    public
    void getTwitterData(final TwitterSession session)
    {
        MyTwitterApiClient tapiclient = new MyTwitterApiClient(session);
        tapiclient.getCustomService().show(session.getUserId(), new Callback<User>()
        {
            @Override
            public
            void success(Result<User> result)
            {
                // Do something with result, which provides a Tweet
                // inside of result.data
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
				/* TwitterAuthToken authToken = session.getAuthToken(); String token = authToken.token; String secret = authToken.secret; Log.e("name",""+result.data.name);
				 * Log.e("email",""+result.data.email); Log.e("id",""+result.data.id); Log.e("profileImageUrl" ,""+result.data.profileImageUrl);
				 * 
				 * 
				 * intent = new Intent(Login.this,Registration.class); intent.putExtra("image_path", result.data.profileImageUrl); intent.putExtra("user_id",""+result.data.id);
				 * intent.putExtra("user_name", result.data.screenName); intent.putExtra("full_name", result.data.name);
				 * 
				 * intent.putExtra("identifier", "T"); identifier="T"; startActivity(intent);
				 * 
				 * ((Activity) con).finish(); */

                if (rem_pref.contains("GCM_Reg_id"))
                {
                    Log.e("result.data.screenName", "" + result.data.screenName);
                    new Login_ProgressTask(con, "" + result.data.id, "", "T", result.data.profileImageUrl.replace("_normal", ""), result.data.screenName, result.data.name).execute();
                }
                else
                {
                    registerInBackground();
                    System.out.println("Attention" + "Registration Id not found");
                }

            }

            public
            void failure(TwitterException exception)
            {
                // Do something on failure
            }
        });
    }

    class MyTwitterApiClient extends TwitterApiClient
    {
        public
        MyTwitterApiClient(TwitterSession session)
        {
            super(session);
        }

        public
        CustomService getCustomService()
        {
            return getService(CustomService.class);
        }
    }


    //**********************************************************************************************************
    //********************************************INSTAGRAM*****************************************************
    //*********************************************************************************************************


    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public
        boolean handleMessage(Message msg)
        {
            if (msg.what == InstagramApp.WHAT_FINALIZE)
            {
                userInfoHashmap = mApp.getUserInfo();
                Log.e("GRF", "" + userInfoHashmap);

                if (rem_pref.contains("GCM_Reg_id"))
                {
                    new Login_ProgressTask(con, userInfoHashmap.get("id"), "", "I", userInfoHashmap.get("profile_picture"), userInfoHashmap.get("username"), userInfoHashmap.get("full_name"))
                            .execute();
                }
                else
                {
                    registerInBackground();
                    System.out.println("Attention" + "Registration Id not found");
                }

            }
            else if (msg.what == InstagramApp.WHAT_FINALIZE)
            {
                Util_Class.show_Toast("Internet is not available", con);
            }
            return false;
        }
    });

    float initialX;
    boolean will_start_anim = true;

    @Override
    public
    boolean onTouch(View arg0, MotionEvent event)
    {
        int action = event.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                initialX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float finalX = event.getX();
                float diff = finalX - initialX;
                Log.e("diff", "" + diff);
                // if(initialX < finalX && diff > 50 && will_start_anim == true)
                if ((diff > 50 || diff < -50) && will_start_anim == true)
                {
                    Log.e("ergerthreth", "" + initialX + "...." + finalX);
                    StartAnimations_dwon();
                    will_start_anim = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected
    void onResume()
    {

        if (Util_Class.global_value)
        {
            overridePendingTransition(R.anim.exit, R.anim.enter);
        }
        else
        {
            overridePendingTransition(R.anim.alpha_splash, R.anim.alpha_splsh_reverse);
        }

        //uiHelper.onResume();
        super.onResume();
    }

    //**********************************************************************************************************
    //********************************************FACEBOOK*****************************************************
    //*********************************************************************************************************

/*	private Session.StatusCallback statusCallback = new Session.StatusCallback()
	{
		@SuppressWarnings("deprecation")
		@Override
		public void call(Session session, SessionState state, Exception exception)
		{


			Log.e("session",""+session);
			if(state.isOpened())
			{
				Request.executeMeRequestAsync(session, new Request.GraphUserCallback()
				{
					public void onCompleted(GraphUser user, Response response)
					{
						if(response != null)
						{

							try
							{
								String image_path = "https://graph.facebook.com/" + user.getId() + "/picture?type=large";


								if(rem_pref.contains("GCM_Reg_id"))
								{
									new Login_ProgressTask(con, user.getId(), "", "F", image_path, user.getName().replace(" ", ""), user.getName()).execute();
								}
								else
								{
									registerInBackground();
									System.out.println("Attention"+ "Registration Id not found");
								}
							}
							catch(Exception e)
							{
								e.printStackTrace();
							}
						}
					}
				});

			}
			else if(state.isClosed())
			{

			}
		}
	};*/

    @Override
    public
    void onPause()
    {
        super.onPause();
        //uiHelper.onPause();
    }

    @Override
    public
    void onDestroy()
    {
        super.onDestroy();
     /*   if (accessTokenTracker.isTracking())
            accessTokenTracker.stopTracking();


        if (profileTracker.isTracking())
            profileTracker.stopTracking();*/
    }

    @Override
    public
    void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("sharan :" + requestCode, "" + resultCode);

        if (requestCode == 64206)   //FACEBOOK
        {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            //	uiHelper.onActivityResult(requestCode, resultCode, data);
            //Session.getActiveSession().onActivityResult(Login.this, requestCode, resultCode, data);
        }
        else if (requestCode == 140) //TWITTER
        {
            super.onActivityResult(requestCode, resultCode, data);
            twitter_image.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public
    void onSaveInstanceState(Bundle savedState)
    {
        super.onSaveInstanceState(savedState);
        //uiHelper.onSaveInstanceState(savedState);
    }

    private
    void StartAnimations_imageview()
    {
        animALpha_second.reset();

        animsecond.reset();

        if (Util_Class.global_value == false)
        {
            logo_lay.clearAnimation();
            logo_lay.startAnimation(animsecond);
        }
    }

    private
    void StartAnimations()
    {

        animALpha.reset();

        anim.reset();

        second_lay.bringToFront();
        // ImageView image_view_animation = (ImageView)
        // findViewById(R.id.image_view_animation);
        anim_sceond_lay.clearAnimation();
        anim_sceond_lay.startAnimation(anim);
        linear_lay.clearAnimation();
        linear_lay.startAnimation(anim);
        second_lay.clearAnimation();
        second_lay.startAnimation(anim);

        anim.setAnimationListener(new AnimationListener()
        {
            @Override
            public
            void onAnimationStart(Animation animation)
            {
            }

            @Override
            public
            void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public
            void onAnimationEnd(Animation animation)
            {
                swipe_lay.setAnimation(animALpha);
                swipe_lay.startAnimation(animALpha);
                swipe_lay.setVisibility(View.VISIBLE);
            }
        });

    }

    OnClickListener oc;
    EditText forgot_password_email;

    @Override
    public
    void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId())
        {

            case R.id.fb_logo:
                facebook.performClick();
                break;

            case R.id.instagram:
                connectOrDisconnectUser();
                break;

			/* case R.id.facebook:
			 * 
			 * if(!Util_Class.checknetwork(con)) { Util_Class.show_Toast(con.getResources().getString(R.string.no_internet_connection), con); } break; */
            case R.id.twitter:

                if (!Util_Class.checknetwork(con))
                {
                    Util_Class.show_Toast(con.getResources().getString(R.string.no_internet_connection), con);
                }
                break;

            case R.id.forgot_password:

                oc = new OnClickListener()
                {
                    @Override
                    public
                    void onClick(View v)
                    {
                        String email_s = forgot_password_email.getText().toString().trim();

                        if (email_s.length() == 0)
                        {
                            Util_Class.show_Toast(con.getResources().getString(R.string.enter_email), con);
                        }
                        else if (Util_Class.isValidEmail(email_s))
                        {
                            Log.e("forgot", email_s);
                            Util_Class.forgot_dialog.dismiss();

                            new Forgot_Password_ProgressTask(con, email_s).execute();
                        }
                        else
                        {
                            Util_Class.show_Toast(con.getResources().getString(R.string.valid_email), con);
                        }
                    }
                };
                forgot_password_email = Util_Class.show_forgot_dialog(con, oc);

                break;

            case R.id.login:

                if (get_Check())
                {
                    if (rem_pref.contains("GCM_Reg_id"))
                    {
                        login_ProgressTask = new Login_ProgressTask(con, email.getText().toString(), password.getText().toString(), "M", "", "", "");
                        login_ProgressTask.execute();
                    }
                    else
                    {
                        registerInBackground();
                        System.out.println("Attention" + "Registration Id not found");
                    }


                }
                // StartAnimations_dwon();
                // startActivity(new Intent(con,Drawer.class));
                break;

            case R.id.faq:
                Intent i = new Intent(con, Global_WebView.class);
                i.putExtra("title", "FAQ");
                i.putExtra("url", "file:///android_asset/faq.html");
                startActivity(i);
                break;

            case R.id.contact_us:

                Intent i1 = new Intent(con, Global_WebView.class);
                i1.putExtra("title", "Contact Us");
                i1.putExtra("url", "http://muserinspiredfitness.com/?page_id=34");
                //i.putExtra("url","file:///android_asset/terms and conditions.html");
                startActivity(i1);

                break;

            default:
                break;
        }

    }

    private
    boolean get_Check()
    {
        if (email.getText().toString().trim().length() == 0)
        {
            Util_Class.show_Toast("Please enter email.", con);
        }
        else if (!Util_Class.isValidEmail(email.getText().toString()))
        {
            Util_Class.show_Toast("Please enter valid email.", con);
        }
        else if (password.getText().toString().trim().length() == 0)
        {
            Util_Class.show_Toast("Please enter password.", con);
        }
        else if (password.getText().toString().trim().length() < 5)
        {
            Util_Class.show_Toast("Password minimum of 5 digits.", con);
        }
        else if (!Util_Class.checknetwork(con))
        {
            Util_Class.show_Toast("Internet is not available", con);
        }
        else
        {
            return true;
        }
        return false;
    }

    private
    void connectOrDisconnectUser()
    {
        if (mApp.hasAccessToken())
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(con);
            builder.setMessage("Disconnect from Instagram?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public
                void onClick(DialogInterface dialog, int id)
                {
                    mApp.resetAccessToken();
                    // btnConnect.setVisibility(View.VISIBLE);
                    // llAfterLoginView.setVisibility(View.GONE);
                    // btnConnect.setText("Connect");
                    // tvSummary.setText("Not connected");
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener()
            {
                public
                void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            mApp.authorize();
        }

    }

    public
    void onSwipeRight()
    {
        swipe_lay.setVisibility(View.INVISIBLE);
        logo_lay.setVisibility(View.INVISIBLE);
        intent = new Intent(Login.this, Registration.class);
        intent.putExtra("identifier", "M");

        startActivity(intent);

        ((Activity) con).finish();
    }

    private
    void StartAnimations_dwon()
    {

        animALpha_third.reset();

        anim_third.reset();

        second_lay.bringToFront();
        // ImageView image_view_animation = (ImageView)
        // findViewById(R.id.image_view_animation);
        anim_sceond_lay.clearAnimation();
        anim_sceond_lay.startAnimation(anim_third);
        linear_lay.clearAnimation();
        linear_lay.startAnimation(anim_third);
        second_lay.clearAnimation();
        second_lay.startAnimation(anim_third);

        anim_third.setAnimationListener(new AnimationListener()
        {
            @Override
            public
            void onAnimationStart(Animation animation)
            {
                // TODO Auto-generated method stub
                swipe_lay.setVisibility(View.INVISIBLE);
                logo_lay.setVisibility(View.INVISIBLE);
            }

            @Override
            public
            void onAnimationRepeat(Animation animation)
            {
            }

            @Override
            public
            void onAnimationEnd(Animation animation)
            {
                onSwipeRight();
				/* linear_layout_swipe.setAnimation(animALpha); linear_layout_swipe.startAnimation(animALpha); */
                anim_sceond_lay.setVisibility(View.INVISIBLE);
                linear_lay.setVisibility(View.INVISIBLE);
                second_lay.setVisibility(View.INVISIBLE);
            }
        });

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private
    void registerInBackground()
    {
        new AsyncTask()
        {
            @Override
            protected
            Void doInBackground(Object... params)
            {
                String msg = "";
                try
                {
                    if (gcm == null)
                    {
                        gcm = GoogleCloudMessaging.getInstance(con);
                    }
                    String GCM_Reg_id = gcm.register(Util_Class.SENDER_ID);

                    rem_pref.edit().putString("GCM_Reg_id", GCM_Reg_id).commit();

//					System.out.println("GCM_Reg_id===" + GCM_Reg_id);
                }
                catch (IOException ex)
                {
                    registerInBackground();
                    msg = "Error :" + ex.getMessage();
                }
                return null;
            }

        }.execute(null, null, null);
    }


}
