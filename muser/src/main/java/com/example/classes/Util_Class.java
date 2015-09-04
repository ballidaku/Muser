package com.example.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ameba.muser.Drawer;
import com.ameba.muser.Other_Profile;
import com.ameba.muser.R;
import com.ameba.muser.Trainers;
import com.ameba.muser.Update_Profile;
import com.ameba.muser.Wallet;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util_Class
{
	//static String				main								= "http://muser.amebasoftware.com";
	
	 //static String				main="http://108.179.199.92:8025";
	 static String main="http://muser.amebatechnologies.com";

	public static String		registration						= main + "/user.php?muser=register";
	public static String		login								= main + "/user.php?muser=login";
	public static String		forgot_password						= main + "/user.php?muser=forgot";
	public static String		get_pictures_videos					= main + "/user.php?muser=getimagevideo";
	public static String		update_profile						= main + "/user.php?muser=update";
	public static String		get_f_f_b							= main + "/user.php?muser=get_block_follow_user";
	public static String		get_profile							= main + "/user.php?muser=getprofile";
	public static String		get_categories						= main + "/user.php?muser=getvideocategory";
	public static String		get_trainers						= main + "/user.php?muser=get_trainer";
	public static String		upload_image_video					= main + "/user.php?muser=upimgvd";
	public static String		get_trending_pic_videos				= main + "/user.php?muser=tranding";
	public static String		get_area_of_focus					= main + "/user.php?muser=getareaoffocus";
	public static String		search								= main + "/user.php?muser=usr_search_tag";

	public static String		search_child						= main + "/user.php?muser=usr_post_search";
	public static String		get_favourite_i_v_u					= main + "/user.php?muser=get_favourites";
	public static String		get_recommended						= main + "/user.php?muser=get_recommended_trainers";
	public static String		add_like_comment_repost				= main + "/user.php?muser=set_lcs";
	public static String		get_comments_likes					= main + "/user.php?muser=get_comments_likes";
	public static String		get_notifications					= main + "/user.php?muser=get_notification";
	public static String		get_find_friends					= main + "/user.php?muser=get_recommended_friends";
	public static String		set_block_follow_users				= main + "/user.php?muser=set_block_follow_user";
	public static String		add_to_favorites					= main + "/user.php?muser=add_favourites";
	public static String		get_home_posts						= main + "/user.php?muser=get_home_posts";
	public static String		get_trending_friendactivities		= main + "/user.php?muser=get_activities";
	public static String		send_invite_twitter_email_contact	= main + "/user.php?muser=send_msg_request";
	public static String		delete_report_post					= main + "/user.php?muser=report_as_span";
	public static String		subscribe_unsubscribe_trainer		= main + "/user.php?muser=subscribe_trainer";
	public static String		get_subscribed_trainer_session		= main + "/user.php?muser=get_subscribed_trainers_session";
	public static String		get_fitness_goal					= main + "/user.php?muser=getfitnessgoals";
	public static String		make_public_private					= main + "/user.php?muser=change_status";
	public static String		change_username						= main + "/user.php?muser=change_username";
	public static String		change_password						= main + "/user.php?muser=change_password";
	public static String		get_messages_contacts				= main + "/user.php?muser=get_chats";
	public static String		accept_reject_request				= main + "/user.php?muser=approve_following_status";
	public static String		deactivate							= main + "/user.php?muser=dissable_account";
	public static String		set_recommended						= main + "/user.php?muser=set_recommended";
	public static String		send_invite_contact					= main + "/user.php?muser=invite_others";
	public static String		report_problem						= main + "/user.php?muser=report_a_problem";
	public static String		get_messages						= main + "/user.php?muser=get_messages";
	public static String        save_messages						= main + "/user.php?muser=save_messages";
	public static String 		get_funds							= main + "/user.php?muser=get_fund";
	public static String 		add_fund							= main + "/user.php?muser=add_fund";
	public static String 		withdraw_funds						= main + "/user.php?muser=withdraw_fund";
	public static String 		withdraw_get_trainers				= main + "/user.php?muser=gettrainer";
	public static String		cancel_subscription					= main + "/user.php?muser=cancel_subscription";
	public static String		get_subscription					= main + "/user.php?muser=get_subscription";
	
	public static final String	SENDER_ID							= "423011141634";

	ImageButton					img_btn_cross;
	public static boolean		global_value						= false;
	public static double		latitude							= 0.0, longitude = 0.0;

	public static Dialog		internet_dialog						= null, global_dialog = null, forgot_dialog = null, dialog_text_yes_no = null, super_dialog = null;
	static Toast				t									= null;

	GPSTracker					gps;


	public static final String BROADCAST_REFRESH_DRAWER="com.balli.refresh_drawer";

	//My
//	private static final String TWITTER_KEY = "JO07kjMCimwgsTPwrDsnWwgj4";
//	private static final String TWITTER_SECRET = "KEbCQycWSUCmA5cMCFmAKlhi6JGn0WRO0Zi6XseaHHdeuwqjZP";


	//Client
	public static final String TWITTER_KEY = "patOLLgA9Z8B1zgiNvj8mCGyp";
	public static final String TWITTER_SECRET = "g2uiJqmrgIgYDbwBZxjIGHIw2DpUBFEOqPIpAwDQyNa5CPw3B3";

	public static void dismiss_dialog()
	{
		if(global_dialog != null)
		{
			global_dialog.dismiss();
		}
		if(internet_dialog != null)
		{
			internet_dialog.dismiss();
		}

		if(dialog_text_yes_no != null)
		{
			dialog_text_yes_no.dismiss();
		}
	}




	public void get_Lat_Long(Context con)
	{
		gps = new GPSTracker(con);
		if(gps.canGetLocation())
		{

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

			//Log.e(""+latitude, ""+longitude);

		}
		else
		{
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}
	}

	String	addrss	= "";

	public
	boolean check_gps(Context con)
	{
		final LocationManager manager = (LocationManager) con.getSystemService(Context.LOCATION_SERVICE);

		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			buildAlertMessageNoGps(con);
			return false;
		}
		else
		{
			return true;
		}


	}

	private
	void buildAlertMessageNoGps(final Context con)
	{
		final AlertDialog.Builder builder = new AlertDialog.Builder(con);
		builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener()
				{
					public
					void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id)
					{
						con.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener()
				{
					public
					void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id)
					{
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	public String get_location_name(final Activity con)
	{
		addrss = "";
		Log.e("weqrwerw1", "werwqer1");

		/*((Activity) con).runOnUiThread(new Runnable()
		{
			public void run()
			{*/
		get_Lat_Long(con);

		Geocoder coder = new Geocoder(con, Locale.getDefault());
		try
		{
			List<Address> address = coder.getFromLocation(latitude, longitude, 1);

			int g = address.get(0).getMaxAddressLineIndex();

			for(int j = 0; j < g; j++)
			{
				addrss = addrss + " " + address.get(0).getAddressLine(j);
			}
			//Log.e("addrss", ""+addrss);

		}

		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*	}
		});
		
		Log.e("addrss", ""+addrss);*/

		return addrss;
	}


	public static boolean isValidEmail(String email)
	{
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static void get_Hash_key(Context con)
	{
		try
		{

			PackageInfo info = con.getPackageManager().getPackageInfo(con.getPackageName(), PackageManager.GET_SIGNATURES);

			for(Signature signature : info.signatures)
			{
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}

		}
		catch(NameNotFoundException e)
		{
			Log.e("name not found", e.toString());
		}
		catch(NoSuchAlgorithmException e)
		{
			Log.e("no such an algorithm", e.toString());
		}
	}

	public static void copyStream(InputStream input, OutputStream output) throws IOException
	{
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1)
		{
			output.write(buffer, 0, bytesRead);
		}
	}

	public static void show_global_dialog(final Context con, String text/*
																		 * ,
																		 * OnClickListener
																		 * oc
																		 */)
	{
		global_dialog = new Dialog(con);
		global_dialog.setContentView(R.layout.dialog_global);
		global_dialog.setTitle("Confirmation");

		TextView tex = (TextView) global_dialog.findViewById(R.id.text);
		Button ok = (Button) global_dialog.findViewById(R.id.ok);
		// Button cancel=(Button)global_dialog.findViewById(R.id.cancel);

		tex.setText(text);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(global_dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		global_dialog.show();
		global_dialog.getWindow().setAttributes(lp);

		// ok.setOnClickListener(oc);
		ok.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				global_dialog.dismiss();

			}
		});

	}

	public static void show_internet_dialog(final Context con, OnClickListener ret)
	{
		internet_dialog = new Dialog(con);
		internet_dialog.setContentView(R.layout.dialog_no_internet_connection);
		internet_dialog.setTitle("Connection Failed");

		// TextView tex=(TextView)internet_dialog.findViewById(R.id.text);
		Button retry = (Button) internet_dialog.findViewById(R.id.retry);
		Button cancel = (Button) internet_dialog.findViewById(R.id.cancel);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(internet_dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		internet_dialog.show();
		internet_dialog.getWindow().setAttributes(lp);

		retry.setOnClickListener(ret);

		cancel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				internet_dialog.dismiss();

			}
		});

	}

	public static void show_dialog_text_yes_no(final Context con, String texts, OnClickListener y)
	{
		dialog_text_yes_no = new Dialog(con);
		dialog_text_yes_no.setContentView(R.layout.dialog_text_yes_no);
		dialog_text_yes_no.setTitle("Confirmation");

		TextView text = (TextView) dialog_text_yes_no.findViewById(R.id.text);
		Button yes = (Button) dialog_text_yes_no.findViewById(R.id.yes);
		Button no = (Button) dialog_text_yes_no.findViewById(R.id.no);

		text.setText(texts);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog_text_yes_no.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		dialog_text_yes_no.show();
		dialog_text_yes_no.getWindow().setAttributes(lp);

		yes.setOnClickListener(y);

		no.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				dialog_text_yes_no.dismiss();

			}
		});

	}

	public static void show_super_dialog(final Context con, OnClickListener y, String trainer_name )
	{
		super_dialog = new Dialog(con);
		super_dialog.setContentView(R.layout.dialog_super);
		

		TextView text = (TextView) super_dialog.findViewById(R.id.text);
		Button yes = (Button) super_dialog.findViewById(R.id.yes);
		Button no = (Button) super_dialog.findViewById(R.id.no);

		if(con instanceof Other_Profile || con instanceof Trainers)
		{
			super_dialog.setTitle("Confirmation");
			text.setText("You are about to subscribe to "+trainer_name+" by paying USD 5.00. Would you like to proceed with this transaction?");
			yes.setText("Proceed");
			no.setText("Forgive Me");
		}
		else if(con instanceof Update_Profile)
		{
			super_dialog.setTitle("Confirmation");
			text.setText("Are you sure, you want to deactivate your account?");
			yes.setText("Deactivate");
			no.setText("Cancel");
		}
		else if(con instanceof Drawer)
		{
			super_dialog.setTitle("Confirmation");
			text.setText("Are you sure, you want to logout?");
			yes.setText("Logout");
			no.setText("Cancel");
		}
		

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(super_dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		super_dialog.show();
		super_dialog.getWindow().setAttributes(lp);

		yes.setOnClickListener(y);

		no.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				super_dialog.dismiss();

			}
		});

	}
	
	
	public static EditText show_fragment_super_dialog(final Context con,Fragment fragment, OnClickListener y,String where)
	{
		super_dialog = new Dialog(con);
		super_dialog.setContentView(R.layout.dialog_wallet_add_funds);
		

		TextView text = (TextView) super_dialog.findViewById(R.id.text);
		EditText amount_edittext=(EditText)super_dialog.findViewById(R.id.amount_edittext);
		Button yes = (Button) super_dialog.findViewById(R.id.yes);
		Button no = (Button) super_dialog.findViewById(R.id.no);

		 if(fragment instanceof Wallet)
		{
		
			if(where.equals("amount_edittext"))
			{
				super_dialog.setTitle("Add to Wallet");
				text.setText("Enter the amount you want to add to your wallet:");
				yes.setText("Done");
				no.setText("Cancel");
			}
			else
			{
				super_dialog.setTitle("Withdraw from Wallet");
				text.setText("Enter the amount you want to withdraw from your wallet:");
				yes.setText("Done");
				no.setText("Cancel");
			}
		}
		

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(super_dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		super_dialog.show();
		super_dialog.getWindow().setAttributes(lp);

		yes.setOnClickListener(y);

		no.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				super_dialog.dismiss();

			}
		});
		return amount_edittext;

	}

	public static void show_report_problem_dialog(final Context con, final Handler handler)
	{
		final Dialog report_problem_dialog = new Dialog(con);
		report_problem_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		report_problem_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		report_problem_dialog.setContentView(R.layout.report_problem);

		final EditText subject = (EditText) report_problem_dialog.findViewById(R.id.subject);
		final EditText message = (EditText) report_problem_dialog.findViewById(R.id.message);

		TextView send = (TextView) report_problem_dialog.findViewById(R.id.send_);
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(report_problem_dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		report_problem_dialog.show();
		report_problem_dialog.getWindow().setAttributes(lp);

		send.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				String s = subject.getText().toString().trim();
				String m = message.getText().toString().trim();
				if(s.length() > 0 && m.length() > 0)
				{

					Message msg = new Message();
					Bundle bundle = new Bundle();

					bundle.putString("subject", s);
					bundle.putString("message", m);
					msg.setData(bundle);

					handler.sendMessage(msg);
					report_problem_dialog.dismiss();
				}
				else
				{
					
					String text=s.length()==0?"Subject":"Message";
					show_Toast("Please enter "+text, con);
				}

			}
		});

	}

	public static void show_description_dialog(Context con, String text, int screenWidth) throws UnsupportedEncodingException
	{

		final Dialog dialog = new Dialog(con, R.style.Theme_Dialog);
		dialog.setContentView(R.layout.dialog_description);
		dialog.setCanceledOnTouchOutside(true);

		EmojiconTextView tex = (EmojiconTextView) dialog.findViewById(R.id.text);

		tex.setText(emoji_decode(text));

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.gravity = Gravity.TOP /*| Gravity.RIGHT*/;
		lp.x = 120;
		lp.y = 230;

		lp.width = screenWidth - 170;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		dialog.show();
		dialog.getWindow().setAttributes(lp);

	}

	public static EditText show_forgot_dialog(final Context con, OnClickListener oc)
	{

		forgot_dialog = new Dialog(con);
		forgot_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		forgot_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		forgot_dialog.setContentView(R.layout.forgot_password_dialog);

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(forgot_dialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

		forgot_dialog.show();
		forgot_dialog.getWindow().setAttributes(lp);

		Button send = (Button) forgot_dialog.findViewById(R.id.send);
		final EditText forgot_password_email;
		(forgot_password_email = (EditText) forgot_dialog.findViewById(R.id.forgot_password_email)).addTextChangedListener(new Text(forgot_password_email));

		send.setOnClickListener(oc);

		return forgot_password_email;
	}

	public static void show_Toast(String text, Context con)
	{
		if(t != null)
		{
			t.cancel();
		}
		t = Toast.makeText(con, text, Toast.LENGTH_SHORT);

		t.show();

	}



	public static class Text implements TextWatcher
	{
		EditText	ed;

		public Text(EditText ed)
		{
			this.ed = ed;
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
		{
			String str = arg0.toString();
			//Log.e("sf", str);
			if(str.length() > 0 && str.endsWith(" "))
			{
				//Log.e("", "Cannot begin with space");
				ed.setText(str.trim());
				ed.setSelection(str.length() - 1);
			}
			//Log.e("///////////////", "ITHEEEEEEEEEEEEEEEEEEEEE");

		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
		{

		}

		@Override
		public void afterTextChanged(Editable arg0)
		{
			// TODO Auto-generated method stub

		}
	}

	public static boolean checknetwork(Context con)
	{
		ConnectivityManager connMgr = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfo = connMgr.getAllNetworkInfo();
		if(networkInfo != null)
		{
			for(int i = 0; i < networkInfo.length; i++)
			{
				if(networkInfo[i].getState() == NetworkInfo.State.CONNECTED)
				{
					return true;
				}

			}
		}
		return false;
	}

	public static Bitmap decodeFile(String filePath) throws IOException
	{

		// Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, o);
		// The new size we want to scale to
		final int REQUIRED_SIZE = 1024;
		// Find the correct scale value. It should be the power of 2.
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true)
		{
			if(width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		Bitmap photoPreview = BitmapFactory.decodeFile(filePath, o2);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		photoPreview.compress(Bitmap.CompressFormat.PNG, 100, baos);

		ExifInterface ei = new ExifInterface(filePath);
		int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		Matrix matrix = new Matrix();
		switch (orientation)
		{
			case ExifInterface.ORIENTATION_ROTATE_90:
				matrix.postRotate(90);
				photoPreview = Bitmap.createBitmap(photoPreview, 0, 0, photoPreview.getWidth(), photoPreview.getHeight(), matrix, true);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				matrix.postRotate(180);
				photoPreview = Bitmap.createBitmap(photoPreview, 0, 0, photoPreview.getWidth(), photoPreview.getHeight(), matrix, true);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix.postRotate(270);
				photoPreview = Bitmap.createBitmap(photoPreview, 0, 0, photoPreview.getWidth(), photoPreview.getHeight(), matrix, true);
				break;
			default:
				photoPreview = Bitmap.createBitmap(photoPreview, 0, 0, photoPreview.getWidth(), photoPreview.getHeight(), matrix, true);
				break;
		}
		return photoPreview;

	}

	public static boolean checkFolder(File filepath, String string)
	{
		boolean checkf = false;
		try
		{
			File dbfile = new File(filepath.getAbsolutePath() + "/" + string + "/");
			checkf = dbfile.exists();
			Log.e("info", "Folder  exist");
		}
		catch(Exception e)
		{
			Log.e("info", "Folder doesn't exist");
			e.printStackTrace();
		}
		return checkf;
	}

	public final static long	ONE_SECOND	= 1000;
	public final static long	SECONDS		= 60;

	public final static long	ONE_MINUTE	= ONE_SECOND * 60;
	public final static long	MINUTES		= 60;

	public final static long	ONE_HOUR	= ONE_MINUTE * 60;
	public final static long	HOURS		= 24;

	public final static long	ONE_DAY		= ONE_HOUR * 24;
	public final static long	MONTHS		= ONE_DAY * 30;

	public String millisToLongDHMS(long duration)
	{
		StringBuffer res = new StringBuffer();
		long temp = 0;
		if(duration >= ONE_SECOND)
		{

			temp = duration / MONTHS;
			if(temp > 0)
			{
				duration -= temp * MONTHS;
				res.append(temp).append(" month").append(temp > 1 ? "s" : "");
				return res.toString() + " ago";
				// .append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_DAY;
			if(temp > 0)
			{
				duration -= temp * ONE_DAY;
				res.append(temp).append(" day").append(temp > 1 ? "(s)" : "");
				return res.toString() + " ago";
				// .append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_HOUR;
			if(temp > 0)
			{
				duration -= temp * ONE_HOUR;
				res.append(temp).append(" hour").append(temp > 1 ? "s" : "");
				return res.toString() + " ago";
				// .append(duration >= ONE_MINUTE ? ", " : "");
			}

			temp = duration / ONE_MINUTE;
			if(temp > 0)
			{
				duration -= temp * ONE_MINUTE;
				res.append(temp).append(" minute").append(temp > 1 ? "s" : "");
				return res.toString() + " ago";
			}

			if(!res.toString().equals("") && duration >= ONE_SECOND)
			{
				res.append(" and ");
			}

			temp = duration / ONE_SECOND;
			if(temp > 0)
			{
				res.append(temp).append(" second").append(temp > 1 ? "s" : "");
				return res.toString() + " ago";
			}
			return res.toString();
		}
		else
		{
			return "0 second";
		}
	}

	public String get_time(String date)
	{
		String time = null;
		Calendar c = Calendar.getInstance();
		SimpleDateFormat dfJOBJ = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
		String formattedDateJOBJ = dfJOBJ.format(c.getTime());

		java.util.Date currentdate;
		try
		{
			currentdate = dfJOBJ.parse(formattedDateJOBJ);
			java.util.Date other_date = dfJOBJ.parse(date);

			long diff = currentdate.getTime() - other_date.getTime();

			time = millisToLongDHMS(diff);
		}
		catch(ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return time;
	}

	public String get_time2(String date)
	{
		//Log.e("date",""+date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		String time = null;
		Calendar c = Calendar.getInstance();
		String formattedDateJOBJ = simpleDateFormat.format(c.getTime());

		Date server_date_time = null;
		try
		{
			server_date_time = simpleDateFormat.parse(date);
			//Log.e("server_date_time",""+server_date_time);

			Date current_date_time = simpleDateFormat.parse(formattedDateJOBJ);
			//Log.e("current_date_time",""+current_date_time);

			long diff = current_date_time.getTime() - server_date_time.getTime()-120000-32000;
			time = millisToLongDHMS(diff);
		}
		catch(ParseException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return time;
	}

	public String get_current_date_time()
	{
		String date_time = "";

		Date now = new Date(); // java.util.Date, NOT java.sql.Date or java.sql.Timestamp!
		return date_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);

	}

	public static String emoji_decode(String text) throws UnsupportedEncodingException
	{
		try
		{
			return URLDecoder.decode(text, "UTF-8");
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			return "";
		}
	}

	public static String emoji_encode(String text) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(text, "UTF-8");
	}

}
