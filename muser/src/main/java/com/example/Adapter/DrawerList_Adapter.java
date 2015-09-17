package com.example.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.classes.RoundedCornersGaganImg;
import com.ameba.muser.R;

public class DrawerList_Adapter extends BaseAdapter
{

	Context					con;

	LayoutInflater			inflater;
	LinearLayout lay_notification;
	TextView				txtNearby,badge;
	ImageView				img_icon;
	RoundedCornersGaganImg				profile_image;
//	public static boolean	clicked			= false;
//	public static int		pos				= 0;
	SharedPreferences		rem_pref;
//	ImageLoader				imageLoader		= ImageLoader.getInstance();

	public static String[]	values			= { "My Profile", "Home", "Trending", "Recommended", "Notification", "Messages", "Search", "My Favorites", "Invite Friends", "Find Friends", "Wallet",
			"Logout"						};

	public static int[]		images			= { 0, R.drawable.home_icon, R.drawable.trending, R.drawable.recomended, R.drawable.notification_icon, R.drawable.message, R.drawable.search_icon,
			R.drawable.favourite_selected, R.drawable.invite_icon, R.drawable.find_friend, R.drawable.wallet, R.drawable.logout };

	public static int[]		images_selected	= { 0, R.drawable.home_icon_selected, R.drawable.trending_selected, R.drawable.recomended_selected, R.drawable.notification_icon_selected,
			R.drawable.message_selected, R.drawable.search_icon_selected, R.drawable.favourite, R.drawable.invite_icon_selected, R.drawable.find_friend_selected, R.drawable.wallet_selected,
			R.drawable.logout_selected		};

	public DrawerList_Adapter(Context con)
	{
		this.con = con;
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
	}

	@Override
	public int getCount()
	{
		return values.length;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@SuppressLint("ViewHolder")
	public View getView(final int position, View row, ViewGroup parent)
	{
		inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(position == 0)
		{
			row = inflater.inflate(R.layout.custom_drawer_item_first, parent, false);
			profile_image = (RoundedCornersGaganImg) row.findViewById(R.id.profile_pic_drawer);
			txtNearby = (TextView) row.findViewById(R.id.name_drawer);

			//profile_image.setImageUrl(rem_pref.getString("profile_image", ""));

//			DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.no_media)
//					.showImageOnFail(R.drawable.no_media).showImageOnLoading(R.drawable.loading_image).build();
//
//			imageLoader.init(ImageLoaderConfiguration.createDefault(con));
			profile_image.setImageUrl(con, rem_pref.getString("profile_image", ""));
			
//			imageLoader.displayImage(rem_pref.getString("profile_image", ""), profile_image, options);
			txtNearby.setText(rem_pref.getString("user_name", ""));
		}
		else
		{
			row = inflater.inflate(R.layout.custom_drawer_item, parent, false);

			img_icon = (ImageView) row.findViewById(R.id.image_icon);
			txtNearby = (TextView) row.findViewById(R.id.title);
			badge = (TextView) row.findViewById(R.id.badge);
			lay_notification=(LinearLayout)row.findViewById(R.id.lay_notification);

			TextView txtv_like=(TextView)row.findViewById(R.id.txtv_like);
			TextView txtv_comment=(TextView)row.findViewById(R.id.txtv_comment);
			TextView txtv_session=(TextView)row.findViewById(R.id.txtv_session);
			TextView txtv_connection=(TextView)row.findViewById(R.id.txtv_connection);

			LinearLayout lay_like=(LinearLayout)row.findViewById(R.id.lay_like);
			LinearLayout lay_comment=(LinearLayout)row.findViewById(R.id.lay_comment);
			LinearLayout lay_session=(LinearLayout)row.findViewById(R.id.lay_session);
			LinearLayout lay_connection=(LinearLayout)row.findViewById(R.id.lay_connection);



			//if(clicked == true && position == pos)
			if(rem_pref.getString("current_frag", "").equals(values[position]))
			{

				//row.setBackgroundResource(R.drawable.menu_selected);
				img_icon.setImageResource(images_selected[position]);
				txtNearby.setText(values[position]);
				txtNearby.setTextColor(Color.parseColor("#ffffff"));

			}
			else
			{
				//row.setBackgroundResource(R.drawable.menu_unselected);
				img_icon.setImageResource(images[position]);
				txtNearby.setText(values[position]);
			}



			if(position==4)  //Notification
			{
				lay_notification.setVisibility(View.VISIBLE);
				//lay_notification.setPadding(0,10,0,0);


				if(rem_pref.getInt("L", 0)!=0) // Likes
				{
					txtv_like.setText(""+rem_pref.getInt("L", 0));
				}
				else
				{
					lay_like.setVisibility(View.GONE);
				}

				if(rem_pref.getInt("C", 0)!=0) // comments
				{
					txtv_comment.setText("" + rem_pref.getInt("C", 0));
				}
				else
				{
					lay_comment.setVisibility(View.GONE);
				}

				if(rem_pref.getInt("S", 0)!=0) //  sessions
				{
					txtv_session.setText(""+rem_pref.getInt("S", 0));
				}
				else
				{
					lay_session.setVisibility(View.GONE);
				}

				if(rem_pref.getInt("FC", 0)!=0) //  Friend connection
				{
					txtv_connection.setText(""+rem_pref.getInt("FC", 0));
				}
				else
				{
					lay_connection.setVisibility(View.GONE);
				}

				//row.setPadding(0,20,0,15);
			}
			else if(position==5)  // messages
			{
				if(rem_pref.getInt("message_count",0)==0)
				{
					badge.setVisibility(View.GONE);
				}
				else
				{
					badge.setVisibility(View.VISIBLE);
					badge.setText(""+rem_pref.getInt("message_count",0));

				}
			}

		}

		return row;
	}
}
