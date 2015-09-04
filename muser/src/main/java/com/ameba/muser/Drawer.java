package com.ameba.muser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Adapter.DrawerList_Adapter;
import com.example.ProgressTask.Add_Funds_Thread;
import com.example.Tabs.Tab_Invite_Others_Twitter;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.SlidingMenuLayout;
import com.example.classes.Util_Class;
import com.example.classes.Video_Thumbnails;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import eu.janmuller.android.simplecropimage.CropImage;
import io.fabric.sdk.android.Fabric;

public
class Drawer extends FragmentActivity
{

    public static ListView drawer_list;
    public static DrawerList_Adapter adapter;
    static SlidingMenuLayout slidingmenu_layout;
    Context con;
    ImageView toggle, upload;
    //Button								lk_profile_filter_btn;
    //TextView							title_header;
    //FragmentTransaction					fragmentTransaction;
    SharedPreferences rem_pref;
    public static Drawer con2;

    File root;
    ArrayList<Video_Thumbnails> fileList;
    int time = 0;

    MyBroadcastReceiver refresh_adapter_Receiver;
    private boolean mIsReceiverRegistered = false;
    TextView txtv_menu_bubble;


    @Override
    public
    void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //FacebookSdk.sdkInitialize(this.getApplicationContext());
//		System.out.println("savedInstanceState----->"+savedInstanceState);
        con = this;
        con2 = this;
        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);


        Intent intent = getIntent();

        try
        {
            String action = intent.getAction();
            Log.e("Hello", "0");

            if(action != null)
            {

                Log.e("action", action);

                if(action.equals("Messages"))
                {
                    rem_pref.edit().putString("current_frag", "Messages").commit();
                    Log.e("Hello", "1");
                }
                if(action.equals("Notification"))
                {
                    rem_pref.edit().putString("current_frag", "Notification").commit();
                    Log.e("Hello", "2");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        if(savedInstanceState == null)
        {

            FacebookSdk.sdkInitialize(this.getApplicationContext());

            TwitterAuthConfig authConfig = new TwitterAuthConfig(Util_Class.TWITTER_KEY, Util_Class.TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig));

            slidingmenu_layout = (SlidingMenuLayout) this.getLayoutInflater().inflate(R.layout.drawer, null);

            setContentView(slidingmenu_layout);

            upload = (ImageView) findViewById(R.id.upload);

            txtv_menu_bubble = (TextView) findViewById(R.id.txtv_menu_bubble);


            //title_header = (TextView) findViewById(R.id.title_header);

            toggle = (ImageView) findViewById(R.id.toggle);

            toggle.setOnClickListener(new OnClickListener()
            {
                @Override
                public
                void onClick(View v)
                {
                    // Show/hide the menu
                    drawer_list.smoothScrollToPosition(0);
                    adapter.notifyDataSetChanged();
                    toggleMenu(v);

                }
            });

            upload.setOnClickListener(new OnClickListener()
            {
                @Override
                public
                void onClick(View v)
                {
                    //startActivity(new Intent(con,Camera.class));
                    selectImage();
                }
            });

            drawer_list = (ListView) findViewById(R.id.list);

            adapter = new DrawerList_Adapter(con);
            drawer_list.setAdapter(adapter);


            time = rem_pref.getString("member_type", "").equals("T") ? 30 * 60 * 1000 : 15000;

            root = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            fileList = new ArrayList<Video_Thumbnails>();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new GetVideos_ProgressTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            {

                new GetVideos_ProgressTask().execute();
            }
            change_frangement(rem_pref.getString("current_frag", ""));

        }


        Log.e("Drawer", "onCreate");


        drawer_list.setOnItemClickListener(new OnItemClickListener()
        {

            @Override
            public
            void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                onMenuItemClick(parent, view, position, id);
//				DrawerList_Adapter.clicked = true;
//				DrawerList_Adapter.pos = position;
                adapter.notifyDataSetInvalidated();

                //displayView(position);

            }
        });
    }


    //static String old_fragment="Home";

    @Override
    protected
    void onResume()
    {
        super.onResume();
        refresh_menu_logo();


        Log.e("Drawer", "onResume");
        if(!mIsReceiverRegistered)
        {
            if(refresh_adapter_Receiver == null)
            {
                refresh_adapter_Receiver = new MyBroadcastReceiver();
            }
            registerReceiver(refresh_adapter_Receiver, new IntentFilter(Util_Class.BROADCAST_REFRESH_DRAWER));
            mIsReceiverRegistered = true;
        }

        overridePendingTransition(R.anim.exit, R.anim.enter);

    }

    private
    class MyBroadcastReceiver extends BroadcastReceiver
    {

        @Override
        public
        void onReceive(Context context, Intent intent)
        {
            adapter.notifyDataSetInvalidated();

            refresh_menu_logo();




           /* TextView badge = (TextView) drawer_list.getChildAt(5).findViewById(R.id.badge);

            if(rem_pref.getInt("message_count", 0) == 0)
            {
                badge.setVisibility(View.GONE);
            }
            else
            {
                badge.setVisibility(View.VISIBLE);
                badge.setText("" + rem_pref.getInt("message_count", 0));

            }*/


        }
    }


    public
    void refresh_menu_logo()
    {
        int msg_count = rem_pref.getInt("message_count", 0);
        int noti_count = rem_pref.getInt("notification_count", 0);

        if(msg_count != 0 || noti_count != 0)
        {
            txtv_menu_bubble.setVisibility(View.VISIBLE);

            String msg = ((msg_count != 0 ? msg_count + " Message(s)" : "") + "\n" + (noti_count != 0 ? noti_count + " Notification(s)" : "")).trim();
            txtv_menu_bubble.setText(msg);
        }
        else
        {
            txtv_menu_bubble.setVisibility(View.GONE);
        }
    }


    @Override protected
    void onPause()
    {
        super.onPause();

        if(mIsReceiverRegistered)
        {
            unregisterReceiver(refresh_adapter_Receiver);
            refresh_adapter_Receiver = null;
            mIsReceiverRegistered = false;
        }
    }

    private
    void onMenuItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String selectedItem = DrawerList_Adapter.values[position];
        //	String currentItem = title_header.getText().toString();

        // Do nothing if selectedItem is currentItem
        /*if(selectedItem.compareTo(selectedItem) == 0)
        {
			slidingmenu_layout.toggleMenu();
			return;
		}*/

        change_frangement(selectedItem);

        // Hide menu anyway
        slidingmenu_layout.toggleMenu();

    }

    public
    void change_frangement(String selectedItem)
    {
        FragmentManager fm = Drawer.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;

        if(/*selectedItem.compareTo("My_Profile") == 0 ||*/ selectedItem.compareTo("My Profile") == 0)
        {
            fragment = new My_Profile();
        }
        else if(selectedItem.compareTo("Home") == 0)
        {
            fragment = new Home();
        }
        else if(selectedItem.compareTo("Trending") == 0)
        {
            fragment = new Trending();
        }
        else if(selectedItem.compareTo("Recommended") == 0)
        {
            fragment = new Recomended();
        }
        else if(selectedItem.compareTo("Notification") == 0)
        {
            fragment = new Notification();
        }
        else if(selectedItem.compareTo("Messages") == 0)
        {
            fragment = new Messages();
        }
        else if(selectedItem.compareTo("Search") == 0)
        {
            fragment = new Search();
        }
        else if(selectedItem.compareTo("My Favorites") == 0)
        {
            fragment = new My_Favourites();
        }
        else if(selectedItem.compareTo("Invite Friends") == 0)
        {
            fragment = new Invite_Others();
        }
        else if(selectedItem.compareTo("Find Friends") == 0)
        {
            fragment = new Find_Friends();
        }
        else if(selectedItem.compareTo("Wallet") == 0)
        {
            fragment = new Wallet();
        }
        else if(selectedItem.compareTo("Logout") == 0)
        {

            show_logout_dialog();

        }


        if(fragment != null)
        {
            //detach_frag();
            //replaceFragment(fragment);
            //Log.e("sharanN", fragment.getClass().getName().replace("com.ameba.muser.", ""));
            String backStateName = fragment.getClass().getName();

            //rem_pref.edit().putString("current_frag",fragment.getClass().getName().replace("com.ameba.muser.", "")).commit();

            rem_pref.edit().putString("current_frag", selectedItem).commit();
            //Log.e("sharanPref", rem_pref.getString("current_frag",""));
            ft.replace(R.id.lk_profile_fragment, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();

            //title_header.setText(selectedItem);
        }
    }




	/*private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

		FragmentManager manager = getSupportFragmentManager();
		boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

		if (!fragmentPopped){ //fragment not in back stack, create it.
			FragmentTransaction ft = manager.beginTransaction();
			ft.replace(R.id.lk_profile_fragment, fragment);
			ft.addToBackStack(backStateName);
			ft.commit();
		}
	}*/

/*	public void detach_frag()
	{
		FragmentManager fm = getSupportFragmentManager();

		for(int entry = 0; entry < fm.getBackStackEntryCount(); entry++)
		{
			Log.e(TAG, "Found fragment: " + fm.getBackStackEntryAt(entry).getName());



//			FragmentManager.BackStackEntry backEntry=fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
//			String str=backEntry.getName();
		//	Log.e("Found fragment Name: " ,str);


		}
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}*/

    private
    void show_logout_dialog()
    {
        OnClickListener logout = new OnClickListener()
        {
            @Override
            public
            void onClick(View v)
            {
                Util_Class.super_dialog.dismiss();
                try
                {


                    Twitter.getInstance();
                    Twitter.getSessionManager().clearActiveSession();
                    Twitter.logOut();

                    //	callFacebookLogout(con);


                    if(AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null)
                    {
                        Log.e("Drawer", "IN FB LOGOUT");
                        LoginManager.getInstance().logOut();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


                rem_pref.edit().remove("user_id").commit();
                Intent i = new Intent(con, Login.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                Drawer.this.finish();


            }
        };
        Util_Class.show_super_dialog(con, logout, "");

    }

	/*public static void callFacebookLogout(Context context)
	{
		Session session = Session.getActiveSession();
		if(session != null)
		{

			if(!session.isClosed())
			{
				session.closeAndClearTokenInformation();
				//clear your preferences if saved
			}
		}
		else
		{

			session = new Session(context);
			Session.setActiveSession(session);

			session.closeAndClearTokenInformation();
			//clear your preferences if saved

		}

	}*/

    public static
    void toggleMenu(View v)
    {
        slidingmenu_layout.toggleMenu();
    }

    public
    void click()
    {

        change_frangement(DrawerList_Adapter.values[0]);
		/*drawer_list.performItemClick(adapter.getView(0, null, null), 0, drawer_list.getItemIdAtPosition(0));
		
		slidingmenu_layout.toggleMenu();*/
    }

    @Override
    public
    void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //super.onActivityResult(requestCode, resultCode, data);
        Log.e("sharan :" + requestCode, "" + resultCode);
        Log.e("sharan :", "" + requestCode);
		/*if(requestCode == 391886)  //facebook
		{
			super.onActivityResult(requestCode, resultCode, data);
			Tab_Invite_Others_Facebook.uiHelper.onActivityResult(requestCode, resultCode, data);
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		}
		else*/
        if(requestCode == 140)  //twitter
        {

            super.onActivityResult(requestCode, resultCode, data);

            Tab_Invite_Others_Twitter.twitter.onActivityResult(requestCode, resultCode, data);

        }
        else if(requestCode == 2 && data != null)
        {
            String filePathG = Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
            InputStream inputStream;
            try
            {
                inputStream = getContentResolver().openInputStream(data.getData());
                FileOutputStream fileOutputStream = new FileOutputStream(filePathG);
                Util_Class.copyStream(inputStream, fileOutputStream);
                fileOutputStream.close();
                inputStream.close();
                performCrop(filePathG);

            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else if(requestCode == 77)
        {
            try
            {
                if(resultCode == Activity.RESULT_OK)
                {
                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if(path != null)
                    {
                        Get_pic(path);
                    }
                }
                else
                {
                    String filePathC = Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
                    Get_pic(filePathC);

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if(requestCode == REQUEST_CODE_PAYMENT)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(confirm != null)
                {
                    try
                    {
                        Log.e(TAG, confirm.toJSONObject().toString(4));

                        JSONObject jsonObj = new JSONObject(confirm.toJSONObject().toString());

                        if(jsonObj.getJSONObject("response").getString("state").equals("approved"))
                        {

                            Log.e("Reeeeeeeeeee", "" + jsonObj.getJSONObject("response"));

                            new Add_Funds_Thread(con, Global.getFund(), jsonObj.getJSONObject("response").toString());

                        }

                        String paymentId = jsonObj.getJSONObject("response").getString("id");    //payment id jo krna kar lao ehda..

                        //Toast.makeText(con, paymentId, Toast.LENGTH_LONG).show();

                    }
                    catch (JSONException e)
                    {
                        Toast.makeText(con, "An Error has occur while adding money..please try again later..!", Toast.LENGTH_LONG).show();

                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(con, "Request canceled..!", Toast.LENGTH_LONG).show();

            }
            else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                Toast.makeText(con, "An invalid Payment was submitted. Please see the docs for more details.", Toast.LENGTH_LONG).show();

            }


            stopService(new Intent(con, PayPalService.class));
        }
        else //facebook
        {
            Log.e("Facebook sharan", "Navjot");
            super.onActivityResult(requestCode, resultCode, data);
            //Tab_Invite_Others_Facebook.uiHelper.onActivityResult(requestCode, resultCode, data);
            //Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }
    }

    private static final int REQUEST_CODE_PAYMENT = 1;

    private static final String TAG = "payment";

    public static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";

	/*private File getTempFile()
	{

		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{

			File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);
			try
			{
				file.createNewFile();
			}
			catch(IOException e)
			{}

			return file;
		}
		else
		{

			return null;
		}
	}*/

    private
    void performCrop(String filePath)
    {

        Intent intent = new Intent(con, CropImage.class);

        // tell CropImage activity to look for image to crop

        intent.putExtra(CropImage.IMAGE_PATH, filePath);

        intent.putExtra(CropImage.SCALE, true);

        // if the aspect ratio is fixed to ratio 3/2
        intent.putExtra(CropImage.ASPECT_X, 1);
        intent.putExtra(CropImage.ASPECT_Y, 1);

        startActivityForResult(intent, 77);

    }

    public
    void Get_pic(String filePath) throws IOException
    {
        //Bitmap pic = Util_Class.decodeFile(filePath);
        //
        //		update_profile_image.setImageBitmap(pic);
        //
        //		update_profile_bitmap = pic;
        Log.e("filePath", filePath);

        Intent i = new Intent(con, Captured_Image.class);
        i.putExtra("image", filePath);
        i.putExtra("type", "I");
        startActivity(i);

		/*File tempFile = getTempFile();
		if(tempFile.exists())
		{
			tempFile.delete();
		}*/
    }

    public AlertDialog myAlertDialog;

    protected
    void selectImage()
    {

        final CharSequence[] options = {"Gallery Images", "Gallery Videos", "Camera", "Cancel"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(con);

        builder.setTitle("Choose Media...");

        builder.setItems(options, new DialogInterface.OnClickListener()
        {

            @Override
            public
            void onClick(DialogInterface dialog, int item)
            {

                if(options[item].equals("Camera"))

                {

                    startActivity(new Intent(con, Camera.class));
                    myAlertDialog.dismiss();

					/*final int REQUEST_VIDEO_CAPTURE = 1;


					Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

					takeVideoIntent .putExtra("android.intent.extra.durationLimit","15000");

					if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
					    startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
					}*/

                }

                else if(options[item].equals("Gallery Images"))
                {
                    //is_selectable=false;
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                    myAlertDialog.dismiss();
                }
                else if(options[item].equals("Gallery Videos"))
                {
                    myAlertDialog.dismiss();
                    show_photos_dialog();
                }
                else if(options[item].equals("Cancel"))
                {
                    myAlertDialog.dismiss();

                }
            }
        });
        myAlertDialog = builder.create();
        myAlertDialog.show();
        myAlertDialog.setCanceledOnTouchOutside(false);

		/*myAlertDialog.setOnCancelListener(new OnCancelListener() 
		{
			@Override
			public void onCancel(DialogInterface arg0) 
			{
				
			}
		});*/

    }

    Dialog video_dialog;

    private
    void show_photos_dialog()
    {
        video_dialog = new Dialog(con);
        video_dialog.setContentView(R.layout.activity_custom_gallery_videos);
        video_dialog.setTitle("Videos");

        GridView gridView1 = (GridView) video_dialog.findViewById(R.id.gridView1);

        gridView1.setAdapter(videos_adapter);

        TextView emptyText = (TextView) video_dialog.findViewById(R.id.empty);

        if(gridView1.getAdapter().getCount() == 0)
        {
            emptyText.setVisibility(View.VISIBLE);
        }


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(video_dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        video_dialog.show();
        video_dialog.getWindow().setAttributes(lp);

    }

    BaseAdapter videos_adapter = new BaseAdapter()
    {
        @Override
        public
        View getView(final int position, View row, ViewGroup arg2)
        {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            row = inflater.inflate(R.layout.custom_gallery_video_item, arg2, false);

            RoundedCornersGaganImg image = (RoundedCornersGaganImg) row.findViewById(R.id.thumbImage);
            TextView duration = (TextView) row.findViewById(R.id.duration);

            image.setImageBitmap(fileList.get(position).getThumbnail());
            duration.setText(secondsToString(fileList.get(position).getDuration() / 1000));

            row.setOnClickListener(new OnClickListener()
            {

                @Override
                public
                void onClick(View v)
                {
                    video_dialog.dismiss();
                    Intent i = new Intent(con, Captured_Image.class);
                    i.putExtra("image", fileList.get(position).getVideo_path());
                    i.putExtra("type", "V");
                    startActivity(i);

                }
            });

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
            return fileList.size();
        }
    };

    private
    String secondsToString(int pTime)
    {
        return String.format("%02d:%02d", pTime / 60, pTime % 60);
    }

    public
    class GetVideos_ProgressTask extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected
        Void doInBackground(Void... params)
        {
            getfile(root);
            return null;
        }

        @Override
        protected
        void onPostExecute(Void result)
        {

            super.onPostExecute(result);

        }
    }

    public
    void getfile(File dir)
    {

        File listFile[] = dir.listFiles();
        if(listFile != null && listFile.length > 0)
        {
            for(int i = 0; i < listFile.length; i++)
            {

                if(listFile[i].isDirectory())
                {
                    //	fileList.add(listFile[i]);
                    getfile(listFile[i]);

                }
                else
                {
                    if(listFile[i].getName().endsWith(".3gp") || listFile[i].getName().endsWith(".mp4") || listFile[i].getName().endsWith(".ts") || listFile[i].getName().endsWith(".webm") || listFile[i].getName().endsWith(".mkv"))
                    {
                        //int duration=getRealPathFromURI(con,listFile[i].getAbsolutePath());

                        final String s = listFile[i].getAbsolutePath();

                        try
                        {
                            if(listFile[i].getAbsolutePath().length() != 0)
                            {
                                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                                retriever.setDataSource(s);
                                int d = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

                                if(d <= time && d != 0)
                                {
                                    Video_Thumbnails video_data = new Video_Thumbnails();
                                    video_data.setThumbnail(ThumbnailUtils.createVideoThumbnail(s, MediaStore.Images.Thumbnails.MINI_KIND));
                                    video_data.setVideo_path(s);
                                    video_data.setDuration(d);

                                    fileList.add(video_data);
                                }
                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();

							/*Video_Thumbnails video_data = new Video_Thumbnails();
							video_data.setThumbnail(ThumbnailUtils.createVideoThumbnail(s, MediaStore.Images.Thumbnails.MINI_KIND));
							video_data.setVideo_path(s);
							video_data.setDuration(0);

							fileList.add(video_data);*/
                        }

                    }
                }

            }
        }

    }

    int b = 0;

    @Override
    public
    void onBackPressed()
    {
        if(b != 0)
        {
            this.finish();
        }
        else
        {
            c.start();
            Util_Class.show_Toast("Press again to exit.", con);
            b++;
        }

    }

    CountDownTimer c = new CountDownTimer(2000, 1000)
    {

        public
        void onTick(long millisUntilFinished)
        {
        }

        public
        void onFinish()
        {
            b = 0;
        }
    };

}
