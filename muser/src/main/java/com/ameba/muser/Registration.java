package com.ameba.muser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.BreakIterator;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.example.ProgressTask.Registration_ProgressTask;
import com.example.classes.Global;
import com.example.classes.RoundedCornersGaganImg;
import com.example.classes.Util_Class;
import com.example.classes.Util_Class.Text;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.appsroid.fxpro.PhotoActivity;
import org.appsroid.fxpro.library.Constants;

import eu.janmuller.android.simplecropimage.CropImage;

public class Registration extends Activity implements OnClickListener
{
    LinearLayout user_lay, trainer_lay, public_lay, private_lay, optional_fb, optional_tw, optional_ig;
    //Button trainer_lay;
    TextView user_textview, trainer_textview, public_textview, private_textview, txt_discription, optional_info;
    ImageView img_btn_cross, img_btn_right;
    Context con;

    RoundedCornersGaganImg profile_pic;
    public AlertDialog myAlertDialog;

    String identifier, member_type, privacy_status, user_id = "";
    EditText user_name, full_name, email, optional_fb_name, optional_tw_name, optional_ig_name, password, confirm_password, edtxt_paypalid;
    EditText optional_fb_phone, optional_tw_phone, optional_ig_phone;

    //boolean is_profile_pic_there = false;
    GoogleCloudMessaging gcm;
    //	String GCM_Reg_id = "";

    Registration_ProgressTask registration;
    String profile_pic_path = "";
    SharedPreferences rem_pref;

    public static Bitmap profile_pic_bitmap = null;

    //	ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter);

        con = this;

        rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);

        Intent i = getIntent();
        identifier = i.getStringExtra("identifier");

        (profile_pic = (RoundedCornersGaganImg) findViewById(R.id.profile_pic)).setOnClickListener(this);

        user_lay = (LinearLayout) findViewById(R.id.user_lay);
        trainer_lay = (LinearLayout) findViewById(R.id.trainer_lay);
        public_lay = (LinearLayout) findViewById(R.id.public_lay);
        private_lay = (LinearLayout) findViewById(R.id.private_lay);
        optional_fb = (LinearLayout) findViewById(R.id.optional_fb);
        optional_tw = (LinearLayout) findViewById(R.id.optional_tw);
        optional_ig = (LinearLayout) findViewById(R.id.optional_ig);

        user_textview = (TextView) findViewById(R.id.user_textview);
        trainer_textview = (TextView) findViewById(R.id.trainer_textview);
        public_textview = (TextView) findViewById(R.id.public_textview);
        private_textview = (TextView) findViewById(R.id.private_textview);
        txt_discription = (TextView) findViewById(R.id.txt_discription);
        optional_info = (TextView) findViewById(R.id.optional_info);

        img_btn_cross = (ImageView) findViewById(R.id.image_btn_cross);
        img_btn_right = (ImageView) findViewById(R.id.image_btn_right);

        user_name = (EditText) findViewById(R.id.user_name);/*).addTextChangedListener(new Text(user_name));*/
        full_name = (EditText) findViewById(R.id.full_name);//).addTextChangedListener(new Text(full_name));
        (email = (EditText) findViewById(R.id.email)).addTextChangedListener(new Text(email));
        (password = (EditText) findViewById(R.id.password)).addTextChangedListener(new Text(password));
        (confirm_password = (EditText) findViewById(R.id.confirm_password)).addTextChangedListener(new Text(confirm_password));

        edtxt_paypalid = (EditText) findViewById(R.id.edtxt_paypalid);

        optional_fb_name = (EditText) findViewById(R.id.optional_fb_name);
        optional_tw_name = (EditText) findViewById(R.id.optional_tw_name);
        optional_ig_name = (EditText) findViewById(R.id.optional_ig_name);

        optional_fb_phone = (EditText) findViewById(R.id.optional_fb_phone);
        optional_tw_phone = (EditText) findViewById(R.id.optional_tw_phone);
        optional_ig_phone = (EditText) findViewById(R.id.optional_ig_phone);

        //txt_discription.setText(Html.fromHtml("<i><font color=\"#c5c5c5\">" + "By tapping to continue,you are indicating that you have read the " + "</font></i>" +"<font color=\"#00abe5\">" + "<a href=\"\">Terms of Service</a>"+"</font>"+"<font color=\"#c5c5c5\">" + "\n and agree to the"+"</font>"+"<font color=\"#00abe5\">" + "\n <a href=\"\">Privacy Policy</a>" + "</font>"));

        user_lay.setOnClickListener(this);
        trainer_lay.setOnClickListener(this);
        public_lay.setOnClickListener(this);
        private_lay.setOnClickListener(this);
        img_btn_cross.setOnClickListener(this);
        img_btn_right.setOnClickListener(this);

        //        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
        //				.cacheOnDisc(true).resetViewBeforeLoading(true)
        //				.showImageForEmptyUri(R.drawable.no_media)
        //				.showImageOnFail(R.drawable.no_media)
        //				.showImageOnLoading(R.drawable.loading_image).build();
        //
        //
        //		imageLoader.init(ImageLoaderConfiguration.createDefault(con));

        if (identifier.equals("F")) // Facebook
        {
            //profile_pic.setRadius(90);
            profile_pic_path = i.getStringExtra("image_path");
            //profile_pic.setImageUrl(profile_pic_path);
            //is_profile_pic_there=true;
            //        	imageLoader.displayImage(profile_pic_path, profile_pic, options);

            profile_pic.setImageUrl(con, profile_pic_path);

            email.setHint("Not Applicable");
            email.setEnabled(false);
            password.setHint("Not Applicable");
            password.setEnabled(false);
            confirm_password.setHint("Not Applicable");
            confirm_password.setEnabled(false);
            user_id = i.getStringExtra("user_id");
            //user_name.setText(i.getStringExtra("user_name"));
            full_name.setText(i.getStringExtra("full_name"));

            optional_fb_name.setText(i.getStringExtra("full_name"));

            setOptVisibility("F");
        }
        else if (identifier.equals("T")) // Twitter
        {
            //profile_pic.setRadius(90);
            //Log.e("path", ""+i.getStringExtra("image_path"));
            profile_pic_path = i.getStringExtra("image_path");
            //profile_pic.setImageUrl(profile_pic_path);
            //is_profile_pic_there=true;

            profile_pic.setImageUrl(con, profile_pic_path);
            //        	imageLoader.displayImage(profile_pic_path, profile_pic, options);

            email.setHint("Not Applicable");
            email.setEnabled(false);
            password.setHint("Not Applicable");
            password.setEnabled(false);
            confirm_password.setHint("Not Applicable");
            confirm_password.setEnabled(false);
            user_id = i.getStringExtra("user_id");
            user_name.setText(i.getStringExtra("user_name"));
            full_name.setText(i.getStringExtra("full_name"));

            optional_tw_name.setText(i.getStringExtra("full_name"));

            setOptVisibility("T");
        }
        else if (identifier.equals("I")) //Instagram
        {
            //Log.e("path", ""+i.getStringExtra("image_path"));
            //profile_pic.setRadius(90);
            profile_pic_path = i.getStringExtra("image_path");
            //profile_pic.setImageUrl(profile_pic_path);
            //is_profile_pic_there=true;
            //        	imageLoader.displayImage(profile_pic_path, profile_pic, options);

            profile_pic.setImageUrl(con, profile_pic_path);

            email.setHint("Not Applicable");
            email.setEnabled(false);
            password.setHint("Not Applicable");
            password.setEnabled(false);
            confirm_password.setHint("Not Applicable");
            confirm_password.setEnabled(false);
            user_id = i.getStringExtra("user_id");
            user_name.setText(i.getStringExtra("user_name"));
            full_name.setText(i.getStringExtra("full_name"));

            optional_ig_name.setText(i.getStringExtra("full_name"));

            setOptVisibility("I");
        }
        else if (identifier.equals("M"))
        {
            profile_pic_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_iamge);
            //is_profile_pic_there=false;
            setOptVisibility("M");
            optional_info.setVisibility(View.GONE);
        }

        if (Util_Class.checknetwork(con) && !rem_pref.contains("GCM_Reg_id"))
        {
            registerInBackground();
        }

        color_changer("user");
        color_changer2("public");

        String s = "By tapping to continue,you are indicating that you have read the Terms of Service and agree to the Privacy Policy";

        txt_discription.setMovementMethod(LinkMovementMethod.getInstance());

        txt_discription.setText(s, BufferType.SPANNABLE);
        Spannable     spans    = (Spannable) txt_discription.getText();
        BreakIterator iterator = BreakIterator.getWordInstance(Locale.US);
        iterator.setText(s);
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next())
        {
            Log.e("start" + start, "end" + end);

            String possibleWord = s.substring(start, end);
            Log.e("possibleWord", "" + possibleWord);

            if (possibleWord.matches("Terms|of|Service|Privacy|Policy"))
            {
                b = true;
            }

            if (Character.isLetterOrDigit(possibleWord.charAt(0)) && b == true)
            {
                ClickableSpan clickSpan = getClickableSpan(possibleWord);
                spans.setSpan(clickSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                b = false;
            }
        }
    }

    boolean b = false;

    private ClickableSpan getClickableSpan(final String word)
    {
        return new ClickableSpan()
        {
            @Override
            public void onClick(View widget)
            {
                Log.e("word", word);
                if (word.matches("Terms|of|Service"))
                {
                    Intent i1 = new Intent(con, Global_WebView.class);
                    i1.putExtra("title", "Terms Services");
                    i1.putExtra("url", "file:///android_asset/terms_and_conditions.html");
                    startActivity(i1);
                }
                else
                {
                    Intent i1 = new Intent(con, Global_WebView.class);
                    i1.putExtra("title", "Privacy Policy");
                    i1.putExtra("url", "file:///android_asset/privacy_policy.html");
                    startActivity(i1);
                }

            }

            public void updateDrawState(TextPaint ds)
            {
                super.updateDrawState(ds);
            }
        };
    }

    private void setOptVisibility(String string)
    {
        if (string.equals("F"))
        {
            optional_fb.setVisibility(View.VISIBLE);
            optional_tw.setVisibility(View.GONE);
            optional_ig.setVisibility(View.GONE);
        }
        else if (string.equals("T"))
        {
            optional_fb.setVisibility(View.GONE);
            optional_tw.setVisibility(View.VISIBLE);
            optional_ig.setVisibility(View.GONE);
        }
        else if (string.equals("I"))
        {
            optional_fb.setVisibility(View.GONE);
            optional_tw.setVisibility(View.GONE);
            optional_ig.setVisibility(View.VISIBLE);
        }
        else if (string.equals("M"))
        {
            optional_fb.setVisibility(View.GONE);
            optional_tw.setVisibility(View.GONE);
            optional_ig.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v)
    {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(con.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        switch (v.getId())
        {
            case R.id.profile_pic:
                profile_pic.setEnabled(false);
                selectImage();
                break;

            case R.id.user_lay:
                color_changer("user");
                hide_paypal_edittext();
                break;

            case R.id.trainer_lay:
                color_changer("trainer");

                Intent i = new Intent(con, Dialog_Insert_Paypal.class);
                i.putExtra("paypal_email",edtxt_paypalid.getText().toString());
                startActivityForResult(i, 55);

                break;

            case R.id.public_lay:
                color_changer2("public");
                break;

            case R.id.private_lay:
                color_changer2("private");
                break;

            case R.id.image_btn_cross:

                // TODO Auto-generated method stub
                Util_Class.global_value = true;
                Log.e("valuesss", "" + Util_Class.global_value);
                startActivity(new Intent(con, Login.class));
                Registration.this.finish();
                // overridePendingTransition(R.anim.enter, R.anim.exit);
          
			/*Intent slideactivity = new Intent(Registration_Screen.this,Login_Screen.class);
            Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.animation2,R.anim.animation).toBundle();
			startActivity(slideactivity, bndlanimation);
			*/
                break;

            case R.id.image_btn_right:

                if (get_Check())
                {

                    if (identifier.equals("M"))
                    {
                        user_id = email.getText().toString().trim();
                    }

                    if (rem_pref.contains("GCM_Reg_id"))
                    {

                        registration = new Registration_ProgressTask(con,
                                                                      user_id,
                                                                      password.getText().toString(),
                                                                      user_name.getText().toString(),
                                                                      full_name.getText().toString(),
                                                                      profile_pic_path,
                                                                      identifier,
                                                                      optional_fb_name.getText().toString(),
                                                                      optional_fb_phone.getText().toString(),
                                                                      optional_tw_name.getText().toString(),
                                                                      optional_tw_phone.getText().toString(),
                                                                      optional_ig_name.getText().toString(),
                                                                      optional_ig_phone.getText().toString(),
                                                                      member_type,
                                                                      privacy_status,
                                                                      edtxt_paypalid.getText().toString());

                        registration.execute();
                    }
                    else
                    {
                        registerInBackground();
                        System.out.println("Attention" + "Registration Id not found");
                    }

                }

                break;

            default:
                break;
        }
    }


    public void hide_paypal_edittext()
    {

        edtxt_paypalid.setText("");
        edtxt_paypalid.setVisibility(View.GONE);

    }



    private boolean get_Check()
    {
		/*if(is_profile_pic_there==false)
		{
			Util_Class.show_Toast("Please select profile pic.", con);
		}
		else*/
        if (email.getText().toString().trim().length() == 0 && identifier.equals("M"))
        {
            Util_Class.show_Toast("Please enter email.", con);
        }
        else if (!Util_Class.isValidEmail(email.getText().toString()) && identifier.equals("M"))
        {
            Util_Class.show_Toast("Please enter valid email.", con);
        }
        else if (user_name.getText().toString().trim().length() == 0)
        {
            Util_Class.show_Toast("Please enter username.", con);
        }
        else if (full_name.getText().toString().trim().length() == 0)
        {
            Util_Class.show_Toast("Please enter full name.", con);
        }
        else if (password.getText().toString().trim().length() == 0 && identifier.equals("M"))
        {
            Util_Class.show_Toast("Please enter password.", con);
        }
        else if (password.getText().toString().trim().length() < 5 && identifier.equals("M"))
        {
            Util_Class.show_Toast("Password should be minimum of 5 digits.", con);
        }
        else if (confirm_password.getText().toString().trim().length() == 0 && identifier.equals("M"))
        {
            Util_Class.show_Toast("Please enter confirm password.", con);
        }
        else if (confirm_password.getText().toString().trim().length() < 5 && identifier.equals("M"))
        {
            Util_Class.show_Toast("Confirm password should be minimum of 5 digits.", con);
        }
        else if (!password.getText().toString().equals(confirm_password.getText().toString()) && identifier.equals("M"))
        {
            Util_Class.show_Toast("Password and confirm password did not match.", con);
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

    public void color_changer(String string)
    {

        if (string.equals("user"))
        {
            //			user_lay.setBackgroundColor(getResources().getColor(R.color.GrayHot));//Resource(R.color.GrayHot);
            //			trainer_lay.setDrawingCacheBackgroundColor(getResources().getColor(R.color.White));
            user_lay.setBackgroundResource(R.drawable.user_colored_bg);
            trainer_lay.setBackgroundResource(R.drawable.trainer_white_bg);
            user_textview.setTextColor(Color.parseColor("#ffffff"));
            trainer_textview.setTextColor(getResources().getColor(R.color.GrayHot));
            member_type = "U";
        }
        else
        {
            user_lay.setBackgroundResource(R.drawable.user_white_bg);
            trainer_lay.setBackgroundResource(R.drawable.trainer_coloured_bg);
            trainer_textview.setTextColor(Color.parseColor("#ffffff"));
            user_textview.setTextColor(getResources().getColor(R.color.GrayHot));
            member_type = "T";
        }

    }

    public void color_changer2(String string)
    {

        if (string.equals("public"))
        {
            //			user_lay.setBackgroundColor(getResources().getColor(R.color.GrayHot));//Resource(R.color.GrayHot);
            //			trainer_lay.setDrawingCacheBackgroundColor(getResources().getColor(R.color.White));
            public_lay.setBackgroundResource(R.drawable.user_colored_bg);
            private_lay.setBackgroundResource(R.drawable.trainer_white_bg);
            public_textview.setTextColor(Color.parseColor("#ffffff"));
            private_textview.setTextColor(getResources().getColor(R.color.GrayHot));
            privacy_status = "PB";
        }
        else
        {
            public_lay.setBackgroundResource(R.drawable.user_white_bg);
            private_lay.setBackgroundResource(R.drawable.trainer_coloured_bg);
            private_textview.setTextColor(Color.parseColor("#ffffff"));
            public_textview.setTextColor(getResources().getColor(R.color.GrayHot));
            privacy_status = "PR";
        }

    }

    protected void selectImage()
    {

        final CharSequence[] options = {"Take Picture", "Gallery", "Cancel"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(con);

        builder.setTitle("Add Profile Picture");

        builder.setItems(options, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int item)
            {

                if (options[item].equals("Take Picture"))

                {

                    //is_selectable=false;
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, getTempUri());

                    // cameraIntent.putExtra("return-data", true);
                    // cameraIntent.putExtra(MediaStore.extra_, 1);
                    startActivityForResult(cameraIntent, 1);
                    profile_pic.setEnabled(true);
                    myAlertDialog.dismiss();
                }

                else if (options[item].equals("Gallery"))
                {
                    //is_selectable=false;
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);
                    profile_pic.setEnabled(true);
                    myAlertDialog.dismiss();
                }

                else if (options[item].equals("Cancel"))
                {
                    myAlertDialog.dismiss();
                    profile_pic.setEnabled(true);
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
                profile_pic.setEnabled(true);
            }
        });

    }

    private Uri getTempUri()
    {
        return Uri.fromFile(getTempFile());
    }

    public static final String TEMP_PHOTO_FILE = "temporary_holder.jpg";

    private File getTempFile()
    {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {

            File file = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
            }

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
        if (requestCode == 1)
        {
            try
            {
                String filePath = null;
                filePath = Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
                if (filePath != null)
                {

                    performCrop(filePath);
                }

            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        else if (requestCode == 2 && data.getData() != null) //Gallery
        {

            try
            {
                String filePathG = Environment.getExternalStorageDirectory() + "/" + TEMP_PHOTO_FILE;
                InputStream inputStream;
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
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else if (requestCode == 77)
        {
            try
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    String path = data.getStringExtra(CropImage.IMAGE_PATH);
                    if (path != null)
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
        else if (requestCode == 55 && data != null)
        {

            Log.e("Intent Data", "" + data.getStringExtra("paypal_email"));

            edtxt_paypalid.setVisibility(View.VISIBLE);

            edtxt_paypalid.setText(data.getStringExtra("paypal_email"));

        }
        else if (requestCode == 55 && data == null)
        {
            user_lay.performClick();
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

        //base64_image = UtilClass.BitMapToString(photoPreview);
        //	Bitmap bit = com.game.Adapter.RoundedCornerBitmap.getCroppedBitmap(photoPreview, 1024);
        //profile_pic.setRadius(90);
        profile_pic.setImageBitmap(pic);

        profile_pic_bitmap = pic;

        File tempFile = getTempFile();
        if (tempFile.exists())
        {
            tempFile.delete();
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerInBackground()
    {
        new AsyncTask()
        {
            @Override
            protected Void doInBackground(Object... params)
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

                    System.out.println("GCM_Reg_id===" + GCM_Reg_id);

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
