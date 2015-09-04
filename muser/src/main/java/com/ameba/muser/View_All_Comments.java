package com.ameba.muser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Adapter.Comments_Adapter;
import com.example.ProgressTask.Add_Like_Comment_Repost_ProgressTask;
import com.example.ProgressTask.Get_Comment_Likes_ProgressTask;
import com.example.classes.Util_Class;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class View_All_Comments extends FragmentActivity  implements OnClickListener , EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener
{

	public static PullToRefreshListView comment_listview;
	ImageView send,keyboard,smilly;
	EmojiconEditText comment_editText;
	TextView back;
	Context con;
	//HashMap<String, String> hashMap;
	int limit=1;
	public static ArrayList<HashMap<String, String>> list;
	SharedPreferences rem_pref;
	Util_Class util=new Util_Class();
	
	
	String post_id="";
	FrameLayout				emojicons;
	
	View root=null;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_all_comments);
		con=this;
		
		root = getLayoutInflater().inflate(R.layout.activity_view_all_comments, null);
				
		rem_pref	 		= con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		
		list = new ArrayList<HashMap<String, String>>();
		
		/*Intent intent = getIntent();
	    hashMap = (HashMap<String, String>)intent.getSerializableExtra("map");*/
		
		post_id = getIntent().getStringExtra("post_id");
		
		
	    comment_listview = (PullToRefreshListView) findViewById(R.id.comment_listview);
		(send=(ImageView)findViewById(R.id.send)).setOnClickListener(this);
		comment_editText=(EmojiconEditText)findViewById(R.id.comment_editText);
		(back= (TextView) findViewById(R.id.back)).setOnClickListener(this);
		
		(smilly				= (ImageView) findViewById(R.id.smilly)).setOnClickListener(this);
		(keyboard				= (ImageView) findViewById(R.id.keyboard)).setOnClickListener(this);
		

		
		comment_listview.setOnRefreshListener(new OnRefreshListener()
		{
			@Override
			public void onRefresh(PullToRefreshBase refreshView)
			{
				if( Util_Class.checknetwork(con))
				   {
					limit++;
					   new Get_Comment_Likes_ProgressTask(con,"C",post_id,limit,"View_All_Comments").execute();
				   }

			}
		});
		
		
		list.addAll(Image_Video_Details.comment_list);
		Comments_Adapter adp = new Comments_Adapter(con,list,"View_All_Comments");
		
		View_All_Comments.comment_listview.setAdapter(adp);

		
		emojicons=(FrameLayout)findViewById(R.id.emojicons);
		
	
		View activityRootView = findViewById(R.id.main);
		checkKeyboardHeight(activityRootView);
		
		setEmojiconFragment();
	}

	private int	keyboardHeight;

	boolean		isKeyBoardVisible;

	int			previousHeightDiffrence	= 0;
	
	private void changeKeyboardHeight(int height)
	{

		if(height > 100)
		{
			keyboardHeight = height;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, keyboardHeight);
			emojicons.setLayoutParams(params);
		}

	}
	

	private void checkKeyboardHeight(final View parentLayout)
	{

		parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
		{

			@Override
			public void onGlobalLayout()
			{

				Rect r = new Rect();
				parentLayout.getWindowVisibleDisplayFrame(r);

				int screenHeight = parentLayout.getRootView().getHeight();
				int heightDifference = screenHeight - (r.bottom);

				
				Log.e("heightDiff", ""+heightDifference);
				if(previousHeightDiffrence - heightDifference > 50)
				{
					//popupWindow.dismiss();
				}

				previousHeightDiffrence = heightDifference;
				if(heightDifference > 100)
				{

					isKeyBoardVisible = true;
					changeKeyboardHeight(heightDifference);

				}
				else
				{

					isKeyBoardVisible = false;

				}

			}
		});

	}
	

	 @Override
	    public void onEmojiconClicked(Emojicon emojicon) {
	        EmojiconsFragment.input(comment_editText, emojicon);
	    }

	    @Override
	    public void onEmojiconBackspaceClicked(View v) {
	        EmojiconsFragment.backspace(comment_editText);
	    }
	    
	    private void setEmojiconFragment()
		{
			boolean useSystemDefault=false;
			getSupportFragmentManager().beginTransaction().replace(R.id.emojicons, EmojiconsFragment.newInstance(useSystemDefault)).commit();
		}
	    
	    
	    

	
	
	@Override
	public void onClick(View v)
	{
		
		switch (v.getId())
		{
			
			case R.id.send:
				
				
				
				String comment = comment_editText.getText().toString().trim();
				if(comment.length() > 0)
				{
					hide();
					
					try
					{
						comment=Util_Class.emoji_encode(comment);
					}
					catch(UnsupportedEncodingException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//comment_list.add(comment);
					//adapter.notifyDataSetChanged();
					//check_list_size();

					//Log.e("List", "" + comment_list);HashMap<String, String> map = new HashMap<String, String>();
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("post_id", post_id);
					map.put("data", comment);
					map.put("date", util.get_current_date_time());
					
					map.put("user_id", rem_pref.getString("user_id", ""));
					map.put("user_name", rem_pref.getString("user_name", ""));
					map.put("profile_image", rem_pref.getString("profile_image", ""));
					
					/*if(Get_Comment_Likes_ProgressTask.list.size() == 0)
					{*/
						list.add(map);
						Comments_Adapter adp = new Comments_Adapter(con, list,"View_All_Comments");
						comment_listview.setAdapter(adp);
						//check_list_size();
				/*	}
					else
					{
						Get_Comment_Likes_ProgressTask.list.add(map);
						Get_Comment_Likes_ProgressTask.adp.notifyDataSetChanged();
						check_list_size();
					}*/
					
					
					comment_editText.setText("");

					HashMap<String, String> map2 = new HashMap<String, String>();
					map2.put("post_id", post_id);
					map2.put("action", "C");
					map2.put("data", comment);
					
					
					new Add_Like_Comment_Repost_ProgressTask(con, map2).execute();
				}

				break;
				
			case R.id.smilly:

				emoji_keyboard("emoji");
				
				break;
				
				
			case R.id.keyboard:
				comment_editText.requestFocus();
				emoji_keyboard("keyboard");
				
				
				//show_keyboard();

				
				break;

			case R.id.back:

				this.finish();

				break;

			default:
				break;
		}

	}
	

	@Override
	public void onBackPressed()
	{
		
		
	}

	public void HideEmoji()
	{
		emojicons.setVisibility(View.GONE);
		smilly.setVisibility(View.VISIBLE);
		keyboard.setVisibility(View.GONE);
	}
	
	public void hide()
	{
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(comment_editText.getWindowToken(), 0);
		
		emojicons.setVisibility(View.GONE);
		
		smilly.setVisibility(View.VISIBLE);
		keyboard.setVisibility(View.GONE);
	}
	
	
	public void emoji_keyboard(String s)
	{
		if(s.equals("keyboard"))
		{
			keyboard.setVisibility(View.GONE);
			smilly.setVisibility(View.VISIBLE);
			
		    final InputMethodManager imm = (InputMethodManager) con.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInputFromWindow(comment_editText.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//			
			emojicons.setVisibility(View.INVISIBLE);
			
		}
		else
		{
			
			keyboard.setVisibility(View.VISIBLE);
			smilly.setVisibility(View.GONE);
			
			emojicons.setVisibility(View.VISIBLE);
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(comment_editText.getWindowToken(), 0);
			
			
			
		}
		
	}
	
	

}
