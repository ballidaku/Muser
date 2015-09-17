package com.ameba.muser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

//import com.aviary.android.feather.FeatherActivity;
import com.example.ProgressTask.Get_Area_Of_Focus_Thread;
import com.example.ProgressTask.Get_Categories_Thread;
import com.example.ProgressTask.Get_Fitness_Goal_Thread;
import com.example.ProgressTask.Get_Trainer_Thread;
import com.example.ProgressTask.Upload_Image_ProgressTask;
import com.example.ProgressTask.Upload_Video_FTP_ProgressTask;
import com.example.classes.CannonballTwitterLoginButton;
import com.example.classes.Util_Class;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/*import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.common.utils.StringUtils;
import com.aviary.android.feather.library.Constants;*/

public
class Captured_Image extends FragmentActivity implements OnClickListener, EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener
{
    LinearLayout edit_image/*, instagram*/;
    Bitmap src;

    Context con;
    public static EmojiconEditText						/* location, */captions/* tag, *//*fitness_goal*/;
    String location_s, captions_s, tag_s/*, fitness_goal_s*/, category_id = "", focus_id = "", fitness_goal_id = "", trainer_id = "", image_path, type,
            latitude, longitude;
    public static AutoCompleteTextView trained_with;
    public static Spinner category_spinner, area_of_focus_spinner, fitness_goal_spinner;
    ;

    boolean selected = false;
    int width;
    Get_Area_Of_Focus_Thread gaof;

    public static ArrayList<HashMap<String, String>> aof_list;
    public static ArrayList<HashMap<String, String>> categories_list;
    public static ArrayList<HashMap<String, String>> fitness_goal_list;
    //String[] fitness_goal_list = { "Build Endurance", "Build muscle", "Burn Fat", "Eat right", "Improve Flexibility", "Improve Lifestyle", "Joint Therapy", "Lose weight", "Reduce Stress", "Stability", "Stamina", "Tone", "Run a mile under 5 mins", "Lower body fat" };

    TextView  tag_people_textview, title;
    ToggleButton location_toggle;
    boolean is_location_on = false;

    Util_Class util = new Util_Class();

    //LoginButton facebook;
    //UiLifecycleHelper									uiHelper;
    Bitmap bit;
    TextView facebook_lay, twitter_text;
    //boolean												is_upload_facebook	= true, is_upload_twitter = false, is_upload_instagram = false;

    CannonballTwitterLoginButton twitter;


    //	InstagramApp										mApp;
    FrameLayout emojicons;
    ImageView smilly;
    EditText tag_people,location_name;

    String video_duration = "";

    public static ArrayList<HashMap<String, String>> tag_people_list = new ArrayList<HashMap<String, String>>();

    SharedPreferences rem_pref;
    ShareButton share;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>()
        {
            @Override public
            void onSuccess(Sharer.Result result)
            {
                Log.e("FB", "onSuccess");
                facebook_lay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb_new_selected, 0, 0, 0);
            }

            @Override public
            void onCancel()
            {
                Log.e("FB", "onCancel");
            }

            @Override public
            void onError(FacebookException error)
            {
                Log.e("FB", "onError");
            }
        });

        setContentView(R.layout.activity_captured_image);

        con = this;

        share = (ShareButton) findViewById(R.id.share);


        title = (TextView) findViewById(R.id.title);


        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        tag_people_list.clear();
        Intent i = getIntent();

        image_path = i.getStringExtra("image");
        type = i.getStringExtra("type");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x / 2;


        if(type.equals("V"))
        {
            title.setText("Loaded Video");
            bit = ThumbnailUtils.createVideoThumbnail(image_path, MediaStore.Images.Thumbnails.MINI_KIND);

        }
        else
        {
            title.setText("Loaded Image");
//			BitmapFactory.Options bounds = new BitmapFactory.Options();
//			bounds.inSampleSize = 1;

            bit = BitmapFactory.decodeFile(image_path);

		/*	BitmapFactory.Options opt;
            opt = new BitmapFactory.Options();
			opt.inTempStorage = new byte[20 * 1024];
			opt.inSampleSize = 4;
			
			bit = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(image_path,opt), 150, 150);*/
        }

        //***********************************facebook**********************************************

        //.Bitmap image =  BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder().setBitmap(bit).build();
        final SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        share.setShareContent(content);


        //******************************************************************************************

        ImageView im = (ImageView) findViewById(R.id.camera_image);

        LayoutParams params = im.getLayoutParams();

        params.height = width;
        params.width = width;
        im.setLayoutParams(params);
        im.setImageBitmap(bit);

        (findViewById(R.id.updoad)).setOnClickListener(this);
    	edit_image = (LinearLayout) findViewById(R.id.edit_image);
        edit_image.setOnClickListener(this);


        (findViewById(R.id.back)).setOnClickListener(this);
        location_name = (EditText) findViewById(R.id.location_name);

        captions = (EmojiconEditText) findViewById(R.id.captions);

        emojicons = (FrameLayout) findViewById(R.id.emojicons);

        (smilly = (ImageView) findViewById(R.id.smilly)).setOnClickListener(this);

        //	tag				=(EditText)findViewById(R.id.tag);
        //	location		=(EditText)findViewById(R.id.location);
        tag_people = (EditText) findViewById(R.id.tag_people);

        (tag_people_textview = (TextView) findViewById(R.id.tag_people_textview)).setOnClickListener(this);
        trained_with = (AutoCompleteTextView) findViewById(R.id.trained_with);
        fitness_goal_spinner = (Spinner) findViewById(R.id.fitness_goal_spinner);
        category_spinner = (Spinner) findViewById(R.id.category_spinner);
        area_of_focus_spinner = (Spinner) findViewById(R.id.area_of_focus);
        location_toggle = (ToggleButton) findViewById(R.id.location_toggle);
		
		
		/*uiHelper = new UiLifecycleHelper(Captured_Image.this, statusCallback);
		uiHelper.onCreate(savedInstanceState);
		*/

        // facebook = (LoginButton) findViewById(R.id.facebook);
        (facebook_lay = (TextView) findViewById(R.id.facebook_lay)).setOnClickListener(this);

//		twitter = (CannonballTwitterLoginButton) findViewById(R.id.twitter);
        (twitter_text = (TextView) findViewById(R.id.twitter_text)).setOnClickListener(this);
//		(instagram=(LinearLayout)findViewById(R.id.instagram)).setOnClickListener(this);


        //facebook.setApplicationId(getResources().getString(R.string.app_id));
        //facebook.setReadPermissions("email","public_profile","user_birthday", "read_friendlists");
        //	facebook.setPublishPermissions(Arrays.asList(new String[]{"email", "publish_actions"}));
		
		
	/*	Fabric.with(this, new TweetComposer());
		
	
		
		
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(con, new Twitter(authConfig));*/

        categories_list = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> c_map = new HashMap<String, String>();
        c_map.put("category_id", "0");
        c_map.put("category_name", "Select category");
        categories_list.add(c_map);
        category_spinner.setAdapter(categories_adapter);

        aof_list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("focus_id", "0");
        map.put("focus_name", "Select area of focus");
        aof_list.add(map);
        area_of_focus_spinner.setAdapter(aof_adapter);


        fitness_goal_list = new ArrayList<>();
        HashMap<String, String> fg_map = new HashMap<String, String>();
        fg_map.put("fitness_id", "0");
        fg_map.put("fitness_name", "Select fitness goal");
        fitness_goal_list.add(fg_map);
        fitness_goal_spinner.setAdapter(fitness_goal_adapter);

        //new Get_Categories_ProgressTask(con).execute();
        new Get_Categories_Thread(con);
        new Get_Area_Of_Focus_Thread(con);
        new Get_Fitness_Goal_Thread(con);

//		ArrayAdapter goal_adapter=new ArrayAdapter(this,R.layout.textview, fitness_goal_list);
//		fitness_goal_spinner.setAdapter(goal_adapter);

        category_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public
            void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                TextView id = (TextView) arg1.findViewById(R.id.category_id);
                category_id = id.getText().toString().trim();
                Log.e("category_id", category_id);
            }

            @Override
            public
            void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        area_of_focus_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public
            void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                TextView id = (TextView) arg1.findViewById(R.id.category_id);
                focus_id = id.getText().toString().trim();
                Log.e("focus_id", focus_id);
            }

            @Override
            public
            void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        fitness_goal_spinner.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public
            void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                TextView id = (TextView) arg1.findViewById(R.id.category_id);
                fitness_goal_id = id.getText().toString().trim().equals("0") ? "" : id.getText().toString().trim();
                Log.e("fitness_goal_id", fitness_goal_id);
            }

            @Override
            public
            void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });


        trained_with.setThreshold(1);

        //   edit_text_autocomplete.setAdapter(adapter1);
        trained_with.addTextChangedListener(new TextWatcher()
        {

            @Override
            public
            void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public
            void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public
            void afterTextChanged(Editable s)
            {
                if(trained_with.getText().toString().length() == 3 && selected == false)
                {
                    new Get_Trainer_Thread(con, s.toString());
                    //new Get_Trainers_ProgressTask(con, s.toString()).execute();
                    //	new Get_Trainer(con,s.toString());
                }
                else if(trained_with.getText().toString().length() < 3 && selected == true)
                {
                    selected = false;
                }
            }
        });


        trained_with.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public
            void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selected = true;

                String selection = (String) parent.getItemAtPosition(position);

                Log.e("selection", "" + selection);

                for(int i = 0; i < Get_Trainer_Thread.list.size(); i++)
                {
                    //Log.e("TESTING", ""+SearchTxt.list.get(i).get("NameE"));

                    if(Get_Trainer_Thread.list.get(i).get("user_name").trim().equals(selection.trim()))
                    {
                        Log.e("FINAL", "" + Get_Trainer_Thread.list.get(i));

                        trainer_id = Get_Trainer_Thread.list.get(i).get("trainer_id");
                        String trainer_name = Get_Trainer_Thread.list.get(i).get("user_name");

                        Log.e("trainer_id", "" + trainer_id);
                        Log.e("trainer_name", "" + trainer_name);
                    }
                }

            }
        });

        location_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
            @Override
            public
            void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                {
                    try
                    {
                        if(util.check_gps(con))
                        {
                            is_location_on = true;
                            Log.e("weqrwerw", "werwqer");
                            location_name.setVisibility(View.VISIBLE);
                            location_name.setText(util.get_location_name(Captured_Image.this));
                        }
                        else
                        {
                            location_toggle.setChecked(false);
                        }
                    }
                    catch (Exception e)
                    {
                        location_name.setVisibility(View.GONE);
                        location_toggle.setChecked(false);
                        e.printStackTrace();
                    }


                }
                else
                {
                    is_location_on = false;
                    location_name.setVisibility(View.GONE);
                }
            }
        });
		
		
		/*twitter.setCallback(new Callback<TwitterSession>()
				{
					@Override
					public void success(Result<TwitterSession> result)
					{
						 Log.e("Twitter","Success");
						// Log.e("Twitter",
						// ""+result.data.getUserName()+""+result.data.getUserId()+""+result.data.getUserName());
						TwitterSession session = Twitter.getSessionManager().getActiveSession();
						
						
					}

					@Override
					public void failure(TwitterException exception)
					{
						Log.e("Twitter","failure");
					}
				});*/
		
		
	/*	mApp = new InstagramApp(this, ApplicationData.CLIENT_ID, ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);
		mApp.setListener(new OAuthAuthenticationListener()
		{

			@Override
			public void onSuccess()
			{
				// tvSummary.setText("Connected as " + mApp.getUserName());
				// btnConnect.setText("Disconnect");
				// llAfterLoginView.setVisibility(View.VISIBLE);
				// userInfoHashmap = mApp.
				mApp.fetchUserName(handler);
			}

			@Override
			public void onFail(String error)
			{
				Toast.makeText(con, error, Toast.LENGTH_SHORT).show();
			}
		});*/

        if(type.equals("V"))
        {
            File file = new File(image_path);
            MediaPlayer mp = MediaPlayer.create(con.getApplicationContext(), Uri.parse(file.getAbsolutePath()));
            video_duration = String.valueOf(mp.getDuration() / 1000);
            mp.release();

        }


        setEmojiconFragment();


    }


    private
    String saveToInternalSorage(Bitmap bitmapImage)
    {

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        String n = day + "" + month + "" + year + "_" + hour + "" + minute + "" + second;
        String image_name = n + ".jpg";

        File filepath = Environment.getExternalStorageDirectory();
        if(!Util_Class.checkFolder(filepath, "Muser"))
        {
            File dir = new File(filepath.getAbsolutePath() + "/Muser/");
            dir.mkdirs();
        }

        File dir = new File("/sdcard/Muser/");

        File file = new File(dir, image_name);


        FileOutputStream fos = null;
        try
        {

            fos = new FileOutputStream(file);


            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    String tagged_peoples_ids = "";

    @Override
    protected
    void onResumeFragments()
    {
        super.onResumeFragments();

        if(tag_people_list.size() > 0)
        {
            String names = "";
            for(int i = 0; i < tag_people_list.size(); i++)
            {
                names += i == 0 ? "@" + tag_people_list.get(i).get("user_name") : ", @" + tag_people_list.get(i).get("user_name");
                tagged_peoples_ids += i == 0 ? tag_people_list.get(i).get("user_id") : "," + tag_people_list.get(i).get("user_id");
            }

            tag_people.setVisibility(View.VISIBLE);
            tag_people.setText(names);
        }
    }

    private
    void setEmojiconFragment()
    {
        boolean useSystemDefault = false;
        getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
    }

    boolean emojicons_visible = false;
    private static final int TWEET_COMPOSER_REQUEST_CODE = 100;

    @Override
    public
    void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch(v.getId())
        {

			case R.id.edit_image:

				//startFeather(Uri.parse(image_path));
				break;

            case R.id.updoad:
                //		location_s		=location.getText().toString().trim();
                captions_s = captions.getText().toString().replace("#", " #").trim();

                Log.e("captions_s", captions_s);
                //		tag_s			=tag.getText().toString().trim();
                //				fitness_goal_s = fitness_goal_spinner.getSelectedItem().toString();

                if(get_Check())
                {


                    HashMap<String, String> map = new HashMap<>();
                    map.put("image_path", image_path);
                    map.put("type", type);

                    if(type.equals("V"))
                    {
                        map.put("video_duration", video_duration);
                    }
                    else
                    {
                        map.put("video_duration", "");
                    }

                    try
                    {
                        map.put("captions_s", Util_Class.emoji_encode(captions_s));
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    map.put("latitude", latitude);
                    map.put("longitude", longitude);
                    map.put("trainer_id", trainer_id);
                    map.put("fitness_id", fitness_goal_id);
                    map.put("category_id", category_id);
                    map.put("focus_id", focus_id);
                    map.put("tag", tagged_peoples_ids);
                    map.put("location", location_name.getText().toString());

					
				/*	Log.e("is_upload_facebook",""+is_upload_facebook);
					if(is_upload_facebook)
					{

						//post_to_facebook();
					}*/

                    if(type.equals("V"))
                    {
                        map.put("type", "V");
                        //new Upload_Video_FTP_ProgressTask(con,map).execute();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        {
                            new Upload_Video_FTP_ProgressTask(con, map).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else
                        {
                            new Upload_Video_FTP_ProgressTask(con, map).execute();
                        }

                    }
                    else
                    {
                        map.put("type", "I");
                        //new Upload_Image_ProgressTask(con, map).execute();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                        {
                            new Upload_Image_ProgressTask(con, map).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        }
                        else
                        {
                            new Upload_Image_ProgressTask(con, map).execute();
                        }

                    }


                }

                break;

            case R.id.smilly:

                emojicons_visible = !emojicons_visible;
                int view_value = emojicons_visible == true ? View.VISIBLE : View.GONE;
                emojicons.setVisibility(view_value);
                break;

            case R.id.back:
                ((Activity) con).finish();
                break;

            case R.id.facebook_lay:


                share.performClick();

				/*Session fbsession = Session.getActiveSession();
				if(fbsession.isOpened())
				{

					//post_to_facebook();
					is_upload_facebook = !is_upload_facebook;

					int c = is_upload_facebook ? R.drawable.fb_new_selected : R.drawable.fb_new;

					facebook_lay.setCompoundDrawablesWithIntrinsicBounds(c, 0, 0, 0);

				}
				else if(fbsession.isClosed())
				{
					is_upload_facebook = true;
					facebook.performClick();
				}
				else
				{
					is_upload_facebook = true;
					facebook.performClick();
				}*/

                break;

            case R.id.twitter_text:

                if(type.equals("V"))
                {

                    File myImageFile = new File(saveToInternalSorage(bit));
                    Uri myImageUri = Uri.fromFile(myImageFile);

                    Intent intent = new TweetComposer.Builder(this)
                            .text(rem_pref.getString("user_name", "") + " posted video on #Muser")
                            .image(myImageUri)
                            .createIntent();

                    startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);
                }
                else
                {
                    File myImageFile = new File(image_path);
                    Uri myImageUri = Uri.fromFile(myImageFile);

                   /* TweetComposer.Builder builder = new TweetComposer.Builder(this)
                            .text(rem_pref.getString("user_name", "") + " posted image on #Muser")
                            .image(myImageUri);

                    builder.show();*/

                    Intent intent = new TweetComposer.Builder(this)
                            .text(rem_pref.getString("user_name", "") + " posted image on #Muser")
                            .image(myImageUri)
                            .createIntent();
                    startActivityForResult(intent, TWEET_COMPOSER_REQUEST_CODE);

                }
				
				
				
			/*	try
				{

					TwitterSession session = Twitter.getSessionManager().getActiveSession();
					//Log.e("session.getUserName()", ""+session.getUserName());
					if(!session.getUserName().equals(""))
					{
						Log.e("Twitter", "" + session.getUserName());
						Log.e("Twitter Token", "" + session.getAuthToken().token);
						Log.e("Twitter Secret", "" + session.getAuthToken().secret);

						//StatusesService statusesService = Twitter.getApiClient(session).getStatusesService();
						//	StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
						 AppSession guestAppSession = (AppSession) result.data;
						    TwitterApiClient twitterApiClient =  TwitterCore.getInstance().getApiClient(guestAppSession);
						    StatusesService statusesService = Twitter.getApiClient().getStatusesService();
						    statusesService.userTimeline(382469423L,null,10,null,null,null,null,null,null, new Callback<List<Tweet>>() {

						        @Override
						        public void success(Result<List<Tweet>> listResult) {
						            setProgressBarIndeterminateVisibility(false);

						            for (Tweet tweet : listResult.data) {
						                myLayout.addView(
						                        new CompactTweetView(Principal.this, tweet));
						            }
						        }

						        @Override
						        public void failure(TwitterException e) {
						            Log.e("Error","Error");
						        }
						    });
						}

						@Override
						public void failure(TwitterException exception) {
						    // unable to get an AppSession with guest auth
						}

						access_token=session.getAuthToken().token;
						access_token_secret=session.getAuthToken().secret;

						//						Uri uri = Uri.fromFile(new File(image_path));

						//						InputStream iStream = getContentResolver().openInputStream(uri);
						//						byte[] inputData = getBytes(iStream);
						//b();
						postTwitterImageText();
						//new DownloadTwitterTask().execute(session.getUserName());

					}

				}
				catch(Exception e)
				{
					Log.e("Twitter", "222222222222222222222");
					twitter.performClick();
					e.printStackTrace();
				}*/

                break;

		/*	case R.id.instagram:
				break;
				*/
            case R.id.tag_people_textview:
                startActivity(new Intent(con, Tag_People.class));

                break;

            default:
                break;
        }

    }


    //TwitterSession session;

//	GAGAN AVAIRY END//	TODO:AAAAH CHAK AVAIRY
//	GAGAN AVAIRY
/*private void startFeather(Uri uri) {

	Intent newIntent = new Intent(this, FeatherActivity.class);

	newIntent.setData(uri);

	// === API KEY SECRET (MANDATORY) ====
	// You must pass your Aviary key secret
	newIntent.putExtra(Constants.EXTRA_IN_API_KEY_SECRET, API_SECRET);


	newIntent.putExtra(Constants.EXTRA_OUTPUT, uri);

	newIntent.putExtra(Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG.name());

	newIntent.putExtra(Constants.EXTRA_OUTPUT_QUALITY, 90);


	newIntent.putExtra(Constants.EXTRA_WHITELABEL, true);

	final DisplayMetrics metrics = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(metrics);
	int max_size = Math.max(metrics.widthPixels, metrics.heightPixels);
	max_size = (int) ((float) max_size / 1.2f);
	newIntent.putExtra(Constants.EXTRA_MAX_IMAGE_SIZE, max_size);

	mSessionId = StringUtils.getSha256(System.currentTimeMillis() + mApiKey);

	newIntent.putExtra(Constants.EXTRA_OUTPUT_HIRES_SESSION_ID, mSessionId);


	newIntent.putExtra(Constants.EXTRA_IN_SAVE_ON_NO_CHANGES, true);

	// ..and start feather
	startActivityForResult(newIntent, 22);
}
	private static final String API_SECRET = "1a98a937-63ec-4bac-9131-f275e0aa5992";
	private String mApiKey;
	private String mSessionId;*/
	
/*	private void connectOrDisconnectUser()
	{
		if(mApp.hasAccessToken())
		{
			final AlertDialog.Builder builder = new AlertDialog.Builder(con);
			builder.setMessage("Disconnect from Instagram?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					mApp.resetAccessToken();
					// btnConnect.setVisibility(View.VISIBLE);
					// llAfterLoginView.setVisibility(View.GONE);
					// btnConnect.setText("Connect");
					// tvSummary.setText("Not connected");
				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
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
	}*/
	
	/*private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
	private Handler handler = new Handler(new android.os.Handler.Callback()
	{
		@Override
		public boolean handleMessage(Message msg)
		{
			if(msg.what == InstagramApp.WHAT_FINALIZE)
			{
				userInfoHashmap = mApp.getUserInfo();
				Log.e("GRF", "" + userInfoHashmap);

				if(GCM_Reg_id.equals(""))
				{
					registerInBackground();
				}

				if(GCM_Reg_id.equals(""))
				{
					Util_Class.show_Toast(con.getResources().getString(R.string.GCM_Reg_id_error), getApplicationContext());
				}
				else
				{
					new Login_ProgressTask(con, userInfoHashmap.get("id"), "", "I", userInfoHashmap.get("profile_picture"), userInfoHashmap.get("username"), userInfoHashmap.get("full_name"))
							.execute();
				}

			}
			else if(msg.what == InstagramApp.WHAT_FINALIZE)
			{
				Util_Class.show_Toast("Internet is not available", con);
			}
			return false;
		}
	});
*/
		
		
	/*String access_token, access_token_secret;
	private void postTwitterImageText() {
		try {
			// StatusUpdate su=new StatusUpdate("");
			// ==================Only Status Update============
			// twitter4j.Status response = twitter.updateStatus(edPublish.getText().toString());

			// ===Image and text Update==========

			
			Uri uri = Uri.fromFile(new File(image_path));
			InputStream iStream = getContentResolver().openInputStream(uri);
			
			//String info1 = firstED.getText().toString();
			//String info2 = secED.getText().toString();
			StatusUpdate statusUpdate = new StatusUpdate("Hello Muser.");

			//InputStream is = getResources().openRawResource(R.drawable.ic_launcher);
			statusUpdate.setMedia("Me.png",iStream);

			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			.setOAuthConsumerKey("B3nRiGsngNy6q0ucSmBEasjqg")
			.setOAuthConsumerSecret("pYZbTq1G6b3hnRbUP2jmgZI7rZaRiFpxiK5Ys0tcFt9XjkKaiU")
			.setOAuthAccessToken("2919415273-XDcEjkFVnRESCkFsIgOpQH3sZWdCjuvzCsgqT3x")
			.setOAuthAccessTokenSecret("rYaYjKTUmjj9E78xcd4f12W0gcMtgybuwaBws2jqQeRxB");
			
			//AccessToken accessToken = new AccessToken(access_token, access_token_secret);
			TwitterFactory tf = new TwitterFactory(cb.build());
			twitter4j.Twitter sender = tf.getInstance();
			
			
			twitter4j.Status imageresponse = sender.updateStatus(statusUpdate);

			Log.i("Status", imageresponse.getText());

			//alert.showAlertDialog(this, "Posting Successfull", "Your Tweet is Posted In your Timeline", true);
		} catch (Exception ex) 
		{
			ex.printStackTrace();
			//alert.showAlertDialog(this, "Posting Successfull", ex.getMessage(), false);
		}

	}
	
	*/
	

/*	public void a()
	{
		AccessToken accessToken = session.getAccessToken();
	    ConfigurationBuilder conf = new ConfigurationBuilder();
	    conf.setOAuthConsumerKey(session.getAuthToken().secret);
	    conf.setOAuthConsumerSecret(session.getAuthToken().token);
	    //conf.setUseSSL(true);
	    conf.setHttpReadTimeout(2400000);
	    conf.setHttpStreamingReadTimeout(2400000);
	    conf.setOAuthAccessToken(accessToken.getToken());
	    conf.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
	    conf.setMediaProviderAPIKey(twitpic_api_key);
	    Configuration configuration = conf.build();
	    OAuthAuthorization auth = new OAuthAuthorization(configuration);
	    ImageUpload uploader = new ImageUploadFactory(configuration)
	    .getInstance(auth);

	    File photo=new File("abc/myimage.png");
	    String status="Checkout my new image";

	    uploader.upload(photo,status);
	}*/
	
	
	/*private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
	//	final static String CONSUMER_KEY = "JO07kjMCimwgsTPwrDsnWwgj4";
		//final static String CONSUMER_SECRET = "KEbCQycWSUCmA5cMCFmAKlhi6JGn0WRO0Zi6XseaHHdeuwqjZP";
		final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
		final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;

			if (screenNames.length > 0) {
				result = getTwitterStream(screenNames[0]);
			}
			return result;
		}

		// onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
		@Override
		protected void onPostExecute(String result) {
			Twitter twits = jsonToTwitter(result);

			// lets write the results to the console as well
			for (Tweet tweet : twits) {
				Log.i(LOG_TAG, tweet.getText());
			}

			// send the tweets to the adapter for rendering
			ArrayAdapter<Tweet> adapter = new ArrayAdapter<Tweet>(activity, android.R.layout.simple_list_item_1, twits);
			setListAdapter(adapter);
		}
	
	
	
	private String getTwitterStream(String screenName) {
		String results = null;

		// Step 1: Encode consumer key and secret
		try {
			// URL encode the consumer key and secret
			String urlApiKey = URLEncoder.encode(TWITTER_KEY, "UTF-8");
			String urlApiSecret = URLEncoder.encode(TWITTER_SECRET, "UTF-8");

			// Concatenate the encoded consumer key, a colon character, and the
			// encoded consumer secret
			String combined = urlApiKey + ":" + urlApiSecret;

			// Base64 encode the string
			String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

			// Step 2: Obtain a bearer token
			HttpPost httpPost = new HttpPost(TwitterTokenURL);
			httpPost.setHeader("Authorization", "Basic " + base64Encoded);
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
			String rawAuthorization = getResponseBody(httpPost);
			Authenticated auth = jsonToAuthenticated(rawAuthorization);

			// Applications should verify that the value associated with the
			// token_type key of the returned object is bearer
			if (auth != null && auth.token_type.equals("bearer")) {

				// Step 3: Authenticate API requests with bearer token
				HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

				// construct a normal HTTPS request and include an Authorization
				// header with the value of Bearer <>
				httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
				httpGet.setHeader("Content-Type", "application/json");
				// update the results with the body of the response
				results = getResponseBody(httpGet);
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (IllegalStateException ex1) {
		}
		return results;
	}
}*/
	/*private Authenticated jsonToAuthenticated(String rawAuthorization) {
		Authenticated auth = null;
		if (rawAuthorization != null && rawAuthorization.length() > 0) {
			try {
				Gson gson = new Gson();
				auth = gson.fromJson(rawAuthorization, Authenticated.class);
			} catch (IllegalStateException ex) {
				// just eat the exception
			}
		}
		return auth;
	}*/

	
	
/*	private String getResponseBody(HttpRequestBase request) {
		StringBuilder sb = new StringBuilder();
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
			HttpResponse response = httpClient.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			String reason = response.getStatusLine().getReasonPhrase();

			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();

				BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				String line = null;
				while ((line = bReader.readLine()) != null) {
					sb.append(line);
				}
			} else {
				sb.append(reason);
			}
		} catch (UnsupportedEncodingException ex) {
		} catch (ClientProtocolException ex1) {
		} catch (IOException ex2) {
		}
		return sb.toString();
	}*/

	
/*	private void updateStatus(String status) {
        final ProgressDialog progressDlg = new ProgressDialog(this);

        progressDlg.setMessage("Sending...");
        progressDlg.setCancelable(false);

        progressDlg.show();

        TwitterRequest request      = new TwitterRequest(session.getAuthToken().secret, session.getAuthToken());

        String updateStatusUrl      = "https://api.twitter.com/1.1/statuses/update.json";

        List<NameValuePair> params  = new ArrayList<NameValuePair>(1);

        params.add(new BasicNameValuePair("status", status));

        request.createRequest("POST", updateStatusUrl, params, new TwitterRequest.RequestListener() {

            @Override
            public void onSuccess(String response) {
               // progressDlg.dismiss();

                showToast(response);
            }

            @Override
            public void onError(String error) {
                showToast(error);

                //progressDlg.dismiss();
            }
        });
    }*/

	
/*	public void abc()
	{
		OAuthRequest request = new OAuthRequest(Verb.POST, "https://upload.twitter.com/1/statuses/update_with_media.json");
	    MultipartEntity entity = new MultipartEntity();
	    try {

	        entity.addPart("status", new StringBody("insert vacuous statement here"));
	        entity.addPart("media", new FileBody(new File("/path/of/your/image/file")));

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        entity.writeTo(out);

	        request.addPayload(out.toByteArray());
	        request.addHeader(entity.getContentType().getName(), entity.getContentType().getValue());

	        service.signRequest(session.getAuthToken(), request);
	        Response response = request.send();

	        if (response.isSuccessful()) {
	            // you're all good
	        }
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	*/
	
/*	private void hello()
	{
//		 TODO Auto-generated method stub
		String url;

		long result = 0;

		//String oth = prefs.getString(OAuth.OAUTH_TOKEN, "");
		//String src = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

		Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("Constants.CONSUMER_KEY").setOAuthConsumerSecret("Constants.CONSUMER_SECRET").setOAuthAccessToken(oth).setOAuthAccessTokenSecret(src).build();
		

		OAuthAuthorization auth = new OAuthAuthorization(conf, conf.getOAuthConsumerKey(), conf.getOAuthConsumerSecret(), new AccessToken(conf.getOAuthAccessToken(), conf.getOAuthAccessTokenSecret()));

		ImageUpload upload = ImageUpload.getTwitpicUploader("Constants.twitpic_api_key", auth);

		//Log.d(main_genral_class.TAG, "Start sending image...");

		try
		{
			url = upload.upload(" ", new URL("http://i.stack.imgur.com/wz0qZ.jpg").openStream(), "some text");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}*/
	
	/*void b() throws ClientProtocolException, IOException,  twitter4j.TwitterException
	{
		
		
		
		twitter4j.Twitter twitter = TwitterFactory.getSingleton();
		    twitter.setOAuthConsumer("JO07kjMCimwgsTPwrDsnWwgj4", "KEbCQycWSUCmA5cMCFmAKlhi6JGn0WRO0Zi6XseaHHdeuwqjZP");
		    RequestToken requestToken = twitter.getOAuthRequestToken();
		    AccessToken accessToken = null;
		    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		    while (null == accessToken) {
		      System.out.println("Open the following URL and grant access to your account:");
		      System.out.println(requestToken.getAuthorizationURL());
		      System.out.print("Enter the PIN(if aviailable) or just hit enter.[PIN]:");
		      String pin = br.readLine();
		      try{
		         if(pin.length() > 0){
		           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
		         }else{
		           accessToken = twitter.getOAuthAccessToken();
		         }
		         
		       
		      } catch (TwitterException te) {
//		        if(401 == te.getStatusCode()){
//		          System.out.println("Unable to get the access token.");
//		        }else{
		          te.printStackTrace();
//		        }
		      }
	}
	}*/

	
	
	/*class MyTwitterApiClient extends TwitterApiClient
	{
		public MyTwitterApiClient(TwitterSession session)
		{
			super(session);
		}

		public CustomService getCustomService()
		{
			return getService(CustomService.class);
		}
	}
	
	*/

    public
    void post_to_facebook()
    {
        Uri uri = Uri.fromFile(new File(image_path));
        InputStream iStream;
        try
        {
            iStream = getContentResolver().openInputStream(uri);
            byte[] inputData = getBytes(iStream);

            Bundle params = new Bundle();
            params.putString("message", "Muser Image.");
            params.putString("caption", captions.getText().toString());
            // params.putParcelable("source", bit);
            params.putByteArray("picture", inputData);
            //params.putString("link", "http://www.google.com");
            //params.putString("picture", "http://www.hdwallpapersinn.com/wp-content/uploads/2015/02/flowers-660x330.jpg");

			
			/*  final List<String> PERMISSIONS = Arrays.asList("publish_stream");

			  if (Session.getActiveSession() != null)
			  {
			        NewPermissionsRequest reauthRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
			         Session.getActiveSession().requestNewPublishPermissions(reauthRequest);
			  }*/

		/*	Request requests1 = new Request(Session.getActiveSession(), "me/photos", null, HttpMethod.POST);

			requests1.setParameters(params);
			requests1.setCallback(new Request.Callback()
			{

				@Override
				public void onCompleted(Response response)
				{
					Log.e("response", "" + response.toString());
			//		Toast.makeText(Captured_Image.this, "Done", Toast.LENGTH_SHORT).show();

				}
			});

			requests1.executeAsync();*/

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public
    byte[] getBytes(InputStream inputStream) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while((len = inputStream.read(buffer)) != -1)
        {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }


    private
    boolean get_Check()
    {

		/* if(Util_Class.latitude == 0.0 && Util_Class.longitude == 0.0) { new Get_Location_ProgressTask(con).execute(); } */

		/* if(captions_s.isEmpty() ) { Util_Class.show_Toast("Please enter captions.", con); } else if(tag_s.isEmpty() ) { Util_Class.show_Toast("Please enter tag .", con); } else
		 * if(trainer_id.isEmpty() ) { Util_Class.show_Toast("Please enter trained with .", con); } else if(fitness_goal_s.isEmpty()) { Util_Class.show_Toast("Please enter fitness goal.", con); } else
		 * if(category_id.isEmpty()) { new Get_Categories_ProgressTask(con).execute(); Util_Class.show_Toast("Category should not be null.", con); } else */
        if(is_location_on)
        {
            latitude = String.valueOf(Util_Class.latitude);
            longitude = String.valueOf(Util_Class.longitude);
        }
        else
        {
            latitude = "";
            longitude = "";
        }

        if(type.equals("V") && category_id.equals("0") && Integer.parseInt(video_duration) > 15)
        {
            Util_Class.show_Toast("Please select category.", con);
        }
        else if(!Util_Class.checknetwork(con))
        {
            Util_Class.show_Toast("Internet is not available", con);
        }
        else
        {

            return true;
        }
        return false;
    }

    BaseAdapter aof_adapter = new BaseAdapter()
    {
        @Override
        public
        View getView(int position, View row, ViewGroup arg2)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.custom_category_textview, arg2, false);

            TextView text = (TextView) row.findViewById(R.id.category_text);
            TextView id = (TextView) row.findViewById(R.id.category_id);

            text.setText(aof_list.get(position).get("focus_name"));
            id.setText(aof_list.get(position).get("focus_id"));
            return row;
        }

        @Override
        public
        long getItemId(int arg0)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public
        Object getItem(int arg0)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public
        int getCount()
        {
            // TODO Auto-generated method stub
            return aof_list.size();
        }
    };

    BaseAdapter categories_adapter = new BaseAdapter()
    {
        @Override
        public
        View getView(int position, View row, ViewGroup arg2)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.custom_category_textview, arg2, false);

            TextView text = (TextView) row.findViewById(R.id.category_text);
            TextView id = (TextView) row.findViewById(R.id.category_id);

            text.setText(categories_list.get(position).get("category_name"));
            id.setText(categories_list.get(position).get("category_id"));
            return row;
        }

        @Override
        public
        long getItemId(int arg0)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public
        Object getItem(int arg0)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public
        int getCount()
        {
            // TODO Auto-generated method stub
            return categories_list.size();
        }
    };


    BaseAdapter fitness_goal_adapter = new BaseAdapter()
    {
        @Override
        public
        View getView(int position, View row, ViewGroup arg2)
        {
            LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.custom_category_textview, arg2, false);

            TextView text = (TextView) row.findViewById(R.id.category_text);
            TextView id = (TextView) row.findViewById(R.id.category_id);

            text.setText(fitness_goal_list.get(position).get("fitness_name"));
            id.setText(fitness_goal_list.get(position).get("fitness_id"));
            return row;
        }

        @Override
        public
        long getItemId(int arg0)
        {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public
        Object getItem(int arg0)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public
        int getCount()
        {
            // TODO Auto-generated method stub
            return fitness_goal_list.size();
        }
    };
	
	
	/*private Session.StatusCallback statusCallback = new Session.StatusCallback()
	{
		@SuppressWarnings("deprecation")
		@Override
		public void call(Session session, SessionState state, Exception exception)
		{
			Log.e("Facebook12", "Session State: " + session.getState());
			if(state.isOpened())
			{
				
				Log.e("qqqqqqqqqqqqqqqqqqq", "qqqqqqqqqqqqqqqqqq");
				if(is_upload_facebook)
				{
//					int c=is_upload_facebook ? R.drawable.fb_new_selected: R.drawable.fb_new;
					
					facebook_lay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fb_new_selected, 0, 0, 0);
				}

			}
			else if(state.isClosed())
			{
				is_upload_facebook=false;
			}
		}
	};*/

    @Override
    public
    void onResume()
    {

        //uiHelper.onResume();
        super.onResume();
    }

    @Override
    public
    void onPause()
    {
        super.onPause();
        //	uiHelper.onPause();
    }

    @Override
    public
    void onDestroy()
    {
        super.onDestroy();
        //uiHelper.onDestroy();
    }

    @Override
    public
    void onSaveInstanceState(Bundle savedState)

    {
        super.onSaveInstanceState(savedState);
        //uiHelper.onSaveInstanceState(savedState);
    }

    @Override
    public
    void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        System.out.println("sharan :" + requestCode + "......" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);

       /* Bundle bun = data.getExtras();
        Log.e("sharan :", "" + bun.);*/

        if(requestCode == 64207)
        {

            callbackManager.onActivityResult(requestCode, resultCode, data);
            //uiHelper.onActivityResult(requestCode, resultCode, data);
            //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
        else if(requestCode == 140)
        {



            Log.e("onActivityResult", "onActivityResult");

            twitter.onActivityResult(requestCode, resultCode, data);

        }
        else if(requestCode == 100)
        {
            if(resultCode == -1)
            {
                twitter_text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.twitter_new_selected, 0, 0, 0);
            }
        }

    }

    @Override
    public
    void onEmojiconClicked(Emojicon emojicon)
    {
        EmojiconsFragment.input(captions, emojicon);
    }

    @Override
    public
    void onEmojiconBackspaceClicked(View v)
    {
        EmojiconsFragment.backspace(captions);
    }

    public
    void HideEmoji()
    {
        emojicons.setVisibility(View.GONE);
    }


}
