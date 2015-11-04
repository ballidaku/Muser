package com.ameba.muser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.ProgressTask.Change_Username_Password_Thread;
import com.example.ProgressTask.Deactivate_Account_Thread;
import com.example.ProgressTask.Make_User_Public_Private_Thread;
import com.example.ProgressTask.Report_Problem_Thread;
import com.example.ProgressTask.Update_Profile_ProgressTask;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import eu.janmuller.android.simplecropimage.CropImage;

/*
 * import com.rockerhieu.emojicon.EmojiconEditText; import com.rockerhieu.emojicon.EmojiconsFragment; import com.rockerhieu.emojicon.EmojiconGridFragment; import
 * com.rockerhieu.emojicon.emoji.Emojicon;
 */

public class Update_Profile extends FragmentActivity implements OnClickListener,EmojiconGridFragment.OnEmojiconClickedListener,EmojiconsFragment.OnEmojiconBackspaceClickedListener
{
	//LinearLayout			public_lay, private_lay, optional_fb, optional_tw, optional_ig;
	TextView				/*public_textview, private_textview, txt_discription, optional_info, member_type, registered_from, */back;
	TextView				block_peoples, deactivate, report_problem,linked_accounts;
	String					privacy_status;
	RoundedCornersGaganImg	update_profile_image;
	EditText				update_full_name/*, optional_fb_name, optional_fb_phone, optional_tw_name, optional_tw_phone, optional_ig_name, optional_ig_phone*/;
	EditText				web_address, email, phone;

	ToggleButton			public_private_toggle, notification_toggle;
	ImageView				update, smilly;
	EmojiconEditText		quote;
	public static EditText	update_user_name, confirm_password, old_password, new_password,edtxt_paypalid;
	FrameLayout				emojicons;
	public static Bitmap	update_profile_bitmap	= null;

	//EmojiView emojiView;

	Context					con;
	SharedPreferences		rem_pref;
	public AlertDialog		myAlertDialog;

	//GoogleCloudMessaging	gcm;
	//String					GCM_Reg_id				= "";
	//ImageLoader				imageLoader				= ImageLoader.getInstance();
	String					old, ne, confirm;

	ScrollView				scrollview;
	LinearLayout emo_lay;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_profile);

		con = this;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

		(back = (TextView) findViewById(R.id.back)).setOnClickListener(this);
		(block_peoples = (TextView) findViewById(R.id.block_peoples)).setOnClickListener(this);

		(deactivate = (TextView) findViewById(R.id.deactivate)).setOnClickListener(this);
		(report_problem = (TextView) findViewById(R.id.report_problem)).setOnClickListener(this);
		((TextView) findViewById(R.id.privacy_policy)).setOnClickListener(this);
		((TextView) findViewById(R.id.terms_conditions)).setOnClickListener(this);
		((TextView) findViewById(R.id.linked_accounts)).setOnClickListener(this);
		
		scrollview = (ScrollView) findViewById(R.id.scrollview);
		(update_profile_image = (RoundedCornersGaganImg) findViewById(R.id.update_profile_image)).setOnClickListener(this);
		update_full_name = (EditText) findViewById(R.id.update_full_name);
		update_user_name = (EditText) findViewById(R.id.update_user_name);/*).addTextChangedListener(new Text(update_user_name));*/
		quote = (EmojiconEditText) findViewById(R.id.quote);
		web_address = (EditText) findViewById(R.id.web_address);

		email = (EditText) findViewById(R.id.email);
		phone = (EditText) findViewById(R.id.phone);
		old_password = (EditText) findViewById(R.id.old_password);
		new_password = (EditText) findViewById(R.id.new_password);
		confirm_password = (EditText) findViewById(R.id.confirm_password);

		edtxt_paypalid= (EditText) findViewById(R.id.edtxt_paypalid);

		(update = (ImageView) findViewById(R.id.update)).setOnClickListener(this);
		(smilly = (ImageView) findViewById(R.id.smilly)).setOnClickListener(this);

		//emojicons			=(FrameLayout)findViewById(R.id.emojicons);

		public_private_toggle = (ToggleButton) findViewById(R.id.public_private_toggle);
		notification_toggle = (ToggleButton) findViewById(R.id.notification_toggle);

		/*this.emojiView = (EmojiView) this.findViewById(R.id.emojiView);
		this.emojiView.setEditText(quote);*/

		emo_lay=(LinearLayout)findViewById(R.id.emo_lay);
		emojicons = (FrameLayout) findViewById(R.id.emojicons);
		/*emojicons.setOnTouchListener(new View.OnTouchListener()
		{

			public boolean onTouch(View view, MotionEvent event)
			{
				// TODO Auto-generated method stub
				if(view.getId() == R.id.emojicons)
				{
					scrollview.requestDisallowInterceptTouchEvent(true);
					switch (event.getAction() & MotionEvent.ACTION_MASK)
					{
						case MotionEvent.ACTION_UP:
							scrollview.requestDisallowInterceptTouchEvent(false);
							break;
					}
				}
				return false;
			}
		});*/
		
		/*emojicons.setOnTouchListener(new View.OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				int action = event.getAction();
				Log.e("action", ""+action);
				System.out.println(action);
				switch (action)
				{
					case MotionEvent.ACTION_DOWN:
						// Disallow ScrollView to intercept touch events.
						v.getParent().requestDisallowInterceptTouchEvent(true);
						break;

					case MotionEvent.ACTION_UP:
						// Allow ScrollView to intercept touch events.
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
				}

				// Handle ListView touch events.
				v.onTouchEvent(event);
				return true;
			}
		});*/

		update_profile_image.setImageUrl(con, rem_pref.getString("profile_image", ""));

		//update_profile_image.setImageUrl(rem_pref.getString("profile_image", "")); 

		update_full_name.setText(rem_pref.getString("full_name", ""));
		update_user_name.setText(rem_pref.getString("user_name", ""));

		setEmojiconFragment();

		try
		{
			quote.setText(Util_Class.emoji_decode(rem_pref.getString("user_description", "")));
		}
		catch(UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		web_address.setText(rem_pref.getString("web_address", ""));

		phone.setText(rem_pref.getString("phone_number", ""));

		if(rem_pref.getString("privacy_status", "").equals("PB"))
		{
			public_private_toggle.setChecked(false);
			privacy_status = "PB";
		}
		else
		{
			public_private_toggle.setChecked(true);
			privacy_status = "PR";
		}

		if(rem_pref.getBoolean("is_notification_on", true))
		{
			notification_toggle.setChecked(true);
		}
		else
		{
			notification_toggle.setChecked(false);

		}

		if(!rem_pref.getString("registration_type", "").equals("M"))
		{
			email.setVisibility(View.GONE);
			old_password.setVisibility(View.GONE);
			new_password.setVisibility(View.GONE);
			confirm_password.setVisibility(View.GONE);

		}
		else
		{
			email.setText(rem_pref.getString("key", ""));
		}

		edtxt_paypalid.setText(rem_pref.getString("paypal_id", ""));

		update_user_name.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void afterTextChanged(Editable s)
			{
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if(!update_user_name.getText().toString().trim().equals(rem_pref.getString("user_name", "")))
				{
					update_user_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_name, 0, R.drawable.right_gray, 0);

				}
				else
				{
					update_user_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_name, 0, R.drawable.right_green, 0);
				}

			}
		});

		update_user_name.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					if(event.getRawX() >= (update_user_name.getRight() - update_user_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
					{

						if(!update_user_name.getText().toString().trim().equals(rem_pref.getString("user_name", "")))
						{
							InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
							inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
							update_user_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user_name, 0, R.drawable.timer, 0);
							new Change_Username_Password_Thread(con, update_user_name.getText().toString().trim(), "", "");
						}

						return true;
					}
				}
				return false;
			}
		});

		new_password.addTextChangedListener(textWatcher);
		confirm_password.addTextChangedListener(textWatcher);

		confirm_password.setOnTouchListener(new OnTouchListener()
		{
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				final int DRAWABLE_LEFT = 0;
				final int DRAWABLE_TOP = 1;
				final int DRAWABLE_RIGHT = 2;
				final int DRAWABLE_BOTTOM = 3;

				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					if(event.getRawX() >= (update_user_name.getRight() - update_user_name.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
					{
						old = old_password.getText().toString().trim();
						ne = new_password.getText().toString().trim();
						confirm = confirm_password.getText().toString().trim();

						InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

						if(ne.equals(confirm) && ne.length() >= 5)
						{

							confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.timer, 0);
							new Change_Username_Password_Thread(con, "", old, ne);
						}
						else if(!ne.equals(confirm))
						{
							Util_Class.show_Toast("Password and confirm password did not match.", con);
						}
						else
						{
							Util_Class.show_Toast("Password should be minimum of 5 digits.", con);
						}

						return true;
					}
				}
				return false;
			}
		});

		public_private_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				privacy_status = isChecked ? "PR" : "PB";

				new Make_User_Public_Private_Thread(con, privacy_status);
			}
		});

		notification_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				boolean is_on = isChecked ? true : false;

				rem_pref.edit().putBoolean("is_notification_on", is_on).commit();

				Util_Class.show_Toast(is_on ? "You will now receive MUSER notifications." : "You will no longer receive any notifications.", con);

			}
		});

	}

	@Override
	public void onEmojiconClicked(Emojicon emojicon)
	{
		EmojiconsFragment.input(quote, emojicon);
	}

	@Override
	public void onEmojiconBackspaceClicked(View v)
	{
		EmojiconsFragment.backspace(quote);
	}

	private final TextWatcher	textWatcher			= new TextWatcher()
		{
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

				old = old_password.getText().toString().trim();
				ne = new_password.getText().toString().trim();
				confirm = confirm_password.getText().toString().trim();

				if(!old.isEmpty() && !ne.isEmpty() && !confirm.isEmpty())
				{

					if(ne.equals(confirm))
					{
						confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, R.drawable.right_gray, 0);
					}
					else
					{
						confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, 0, 0);
					}
				}

				if(!ne.equals(confirm))
				{
					confirm_password.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password, 0, 0, 0);
				}
			}

			public void afterTextChanged(Editable s)
			{

			}
		};

	/*	public void toggleEmojiView(View view)
		{
			this.emojiView.toggle();
		}*/

	/*
		 private void setEmojiconFragment(boolean useSystemDefault) 
		 {
		        getSupportFragmentManager()
		                .beginTransaction()
		                .replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault))
		                .commit();
		  }*/

	boolean						emojicons_visible	= false;



	@Override
	public void onClick(View v)
	{
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		switch (v.getId())
		{

			case R.id.block_peoples:
				Global.set_user_id(rem_pref.getString("user_id", ""));
				startActivity(new Intent(con, Blocks.class));
				break;

			case R.id.update_profile_image:
				update_profile_image.setEnabled(false);
				selectImage();
				break;

			case R.id.back:
				((Activity) con).finish();
				break;

			case R.id.smilly:

				emojicons_visible = !emojicons_visible;
				int view_value = emojicons_visible == true ? View.VISIBLE : View.GONE;
				emojicons.setVisibility(view_value);


				break;

			case R.id.update:

				//Log.e("quote", quote.getText().toString().trim());





				if(rem_pref.getString("registration_type", "").equals("M"))
				{

					if(rem_pref.getString("member_type", "").equals("T") )
					{
						if(get_check())
						{
							update_manual_profile();
						}
					}
					else
					{
						update_manual_profile();
					}


				}
				else
				{

					if(rem_pref.getString("member_type", "").equals("T") )
					{
						if(get_check())
						{
							update_social_profile();
						}
					}
					else
					{
						update_social_profile();
					}

				}

				break;

			case R.id.linked_accounts:
				
				startActivity(new Intent(con,Linked_Accounts.class));
				break;
			case R.id.report_problem:

				Util_Class.show_report_problem_dialog(con, handler);
				
				

				break;
			case R.id.privacy_policy:
				//CopyReadAssets("privacy_policy.docx");
				Intent i1=new Intent(con,Global_WebView.class);
				i1.putExtra("title", "Privacy Policy");
				i1.putExtra("url","file:///android_asset/privacy_policy.html");
				startActivity(i1);
				
				break;

			case R.id.terms_conditions:
				//CopyReadAssets("terms_and_conditions.docx");
				Intent i2=new Intent(con,Global_WebView.class);
				i2.putExtra("title", "Terms Services");
				i2.putExtra("url","file:///android_asset/terms_and_conditions.html");
				startActivity(i2);

				break;

			case R.id.deactivate:
				OnClickListener deactivate = new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Util_Class.super_dialog.dismiss();
						new Deactivate_Account_Thread(con);
					} 
				};
				Util_Class.show_super_dialog(con, deactivate,"");

				break;

			default:
				break;
		}
	}


	public boolean get_check()
	{

		if(edtxt_paypalid.getText().toString().trim().length()==0 )
		{
			Util_Class.show_Toast("Please enter paypal email.", con);

			return false;
		}
		else if(!Util_Class.isValidEmail(edtxt_paypalid.getText().toString()))
		{
			Util_Class.show_Toast("Please enter valid paypal email.", con);
			return false;
		}
		else
		{
			return true;

		}

	}

	public void update_manual_profile()
	{

		String quote_string = "";
		try
		{
			quote_string = URLEncoder.encode(quote.getText().toString(), "UTF-8").trim();
		}
		catch(UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Update_Profile_ProgressTask(con,
				  update_full_name.getText().toString().trim(),
				  quote_string,
				  web_address.getText().toString().trim(),
				  email.getText().toString().trim(),
				  phone.getText().toString().trim(),
				  "",
				  "",
				  edtxt_paypalid.getText().toString().trim()).execute();
	}


	public void update_social_profile()
	{

		String quote_string = "";
		try
		{
			quote_string = URLEncoder.encode(quote.getText().toString(), "UTF-8").trim();
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		new Update_Profile_ProgressTask(con,
				  update_full_name.getText().toString().trim(),
				  quote_string, web_address.getText().toString(),
				  "",
				  phone.getText().toString(),
				  "",
				  "",
				  edtxt_paypalid.getText().toString().trim()).execute();
	}

	String			s		= "", m = "";

	
//	Gagan Handler
	private Handler	handler	= new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			Bundle bundle = msg.getData();
			s= bundle.getString("subject");
			m= bundle.getString("message");

			new Report_Problem_Thread(con, s, m);
		}

	};
//	End Gagan Handler


	private void setEmojiconFragment()
	{
		boolean useSystemDefault = false;
		getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
	}

	private boolean get_Check()
	{

		if(old_password.getText().toString().trim().length() < 5)
		{
			Util_Class.show_Toast("Old password should be minimum of 5 digits.", con);
		}
		else if(new_password.getText().toString().trim().length() == 0)
		{
			Util_Class.show_Toast("Please enter new password.", con);
		}
		else if(new_password.getText().toString().trim().length() < 5)
		{
			Util_Class.show_Toast("New password should be minimum of 5 digits.", con);
		}
		else if(confirm_password.getText().toString().trim().length() == 0)
		{
			Util_Class.show_Toast("Please enter confirm password.", con);
		}
		else if(confirm_password.getText().toString().trim().length() < 5)
		{
			Util_Class.show_Toast("Confirm password should be minimum of 5 digits.", con);
		}
		else if(!new_password.getText().toString().equals(confirm_password.getText().toString()))
		{
			Util_Class.show_Toast("Password and confirm password did not match.", con);
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

	protected void selectImage()
	{

		final CharSequence[] options = { "Take Picture", "Gallery", "Cancel" };

		final AlertDialog.Builder builder = new AlertDialog.Builder(con);

		builder.setTitle("Add Profile Picture");

		builder.setItems(options, new DialogInterface.OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int item)
			{

				if(options[item].equals("Take Picture"))

				{

					//is_selectable=false;
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getTempUri());

					// cameraIntent.putExtra("return-data", true);
					// cameraIntent.putExtra(MediaStore.extra_, 1);
					startActivityForResult(cameraIntent, 1);
					update_profile_image.setEnabled(true);
					myAlertDialog.dismiss();
				}

				else if(options[item].equals("Gallery"))
				{
					//is_selectable=false;
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

					startActivityForResult(intent, 2);
					update_profile_image.setEnabled(true);
					myAlertDialog.dismiss();
				}

				else if(options[item].equals("Cancel"))
				{
					myAlertDialog.dismiss();
					update_profile_image.setEnabled(true);
				}
			}
		});
		myAlertDialog = builder.create();
		myAlertDialog.show();
		myAlertDialog.setCanceledOnTouchOutside(false);

		myAlertDialog.setOnCancelListener(new OnCancelListener()
		{
			@Override
			public void onCancel(DialogInterface arg0)
			{
				update_profile_image.setEnabled(true);
			}
		});

	}

	private Uri getTempUri()
	{
		return Uri.fromFile(getTempFile());
	}

	public static final String	TEMP_PHOTO_FILE	= "temporary_holder.jpg";

	private File getTempFile()
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1)
		{
			try
			{
				String filePath = null;
				filePath = Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
				if(filePath != null)
				{
					Bitmap pic = Util_Class.decodeFile(filePath);
					performCrop(filePath);
				}

			}
			catch(RuntimeException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

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
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
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
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	private void performCrop(String filePath)
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

	public void Get_pic(String filePath) throws IOException
	{
		Bitmap pic = Util_Class.decodeFile(filePath);
		// base64_image = UtilClass.BitMapToString(photoPreview);
		// Bitmap bit =
		// com.game.Adapter.RoundedCornerBitmap.getCroppedBitmap(photoPreview,
		// 1024);
		// update_profile_image.setRadius(90);
		update_profile_image.setImageBitmap(pic);

		update_profile_bitmap = pic;
		File tempFile = getTempFile();
		if(tempFile.exists())
		{
			tempFile.delete();
		}
	}

	/*	@Override
		public void onEmojiconBackspaceClicked(View arg0)
		{
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onEmojiconClicked(Emojicon arg0)
		{
			// TODO Auto-generated method stub
			
		}*/

}
