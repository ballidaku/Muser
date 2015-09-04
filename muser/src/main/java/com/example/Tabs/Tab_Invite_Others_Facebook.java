package com.example.Tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ameba.muser.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;

public
class Tab_Invite_Others_Facebook extends Fragment
{
	//public static UiLifecycleHelper uiHelper;

	LoginButton facebook;
	TextView invite;
	View rootView;

	AccessToken accessToken;
	CallbackManager  sCallbackManager;

	@Override public
	void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
//		uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
//		uiHelper.onCreate(savedInstanceState);
	}

	public
	View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		/*if (savedInstanceState == null)
		{*/
			rootView = inflater.inflate(R.layout.tab_invite_others_facebook, container, false);

		invite = (TextView) rootView.findViewById(R.id.invite);
		FacebookSdk.sdkInitialize(this.getActivity());
		sCallbackManager = CallbackManager.Factory.create();

		//accessToken = AccessToken.getCurrentAccessToken();

		invite.setOnClickListener(new View.OnClickListener()
		{
			@Override public
			void onClick(View view)
			{
				sendRequestDialog();

			}
		});


	/*		facebook = (LoginButton) rootView.findViewById(R.id.facebook);
//			facebook.setApplicationId(getResources().getString(R.string.app_id));
			facebook.setReadPermissions("public_profile", "email", "user_friends", "user_birthday", "read_friendlists");
			facebook.setFragment(this);
			// data_layout=(LinearLayout)rootView.findViewById(R.id.data_layout);
			//
			// ListView facebook_listView=(ListView)rootView.findViewById(R.id.facebook_listView);
			//
			// Invite_Others_Facebook_Adapter adapter=new Invite_Others_Facebook_Adapter(getActivity());
			// facebook_listView.setAdapter(adapter);

			invite = (TextView) rootView.findViewById(R.id.invite);



	//	}
		invite.setOnClickListener(new OnClickListener()
		{

			@Override
			public
			void onClick(View arg0)
			{
				*//*Session fbsession = Session.getActiveSession();

				System.out.println(fbsession);

				if (fbsession.isOpened())
				{

					sendRequestDialog();

				}
				else if (fbsession.isClosed())
				{
					facebook.performClick();
				}
				else
				{
					facebook.performClick();
				}*//*

			}
		});*/



		return rootView;
	}


	private
	void sendRequestDialog()
	{

		String appLinkUrl, previewImageUrl;

		appLinkUrl = "https://fb.me/1455292581462657";
		previewImageUrl = "https://lh3.googleusercontent.com/eOk8cN7fGTU88SllEyAhGZjbnXiEmH3xJJw6dU8PL1SsvcdHe8HaY7aN76jPRzZNigQ=w300-rw";

		if (AppInviteDialog.canShow())
		{
			AppInviteContent content = new AppInviteContent.Builder()
					.setApplinkUrl(appLinkUrl)
					.setPreviewImageUrl(previewImageUrl)
					.build();
			AppInviteDialog.show(Tab_Invite_Others_Facebook.this, content);
		}



		/*Bundle params = new Bundle();
		params.putString("title", "Muser");
		params.putString("link", "https://play.google.com/store/apps/details?id=com.app.messenger&hl=en");
		params.putString("message", "Join me on Muser ! - I'm on Muser. Install the app. https://play.google.com/store/apps/details?id=com.ameba.muser&hl=fr_CA");




		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(getActivity(), Session.getActiveSession(), params)).setOnCompleteListener(new OnCompleteListener()
		{

			@Override
			public
			void onComplete(Bundle values, FacebookException error)
			{
				if (error != null)
				{
					if (error instanceof FacebookOperationCanceledException)
					{
						Toast.makeText(getActivity().getApplicationContext(), "Request cancelled", Toast.LENGTH_SHORT).show();
					}
					else
					{
						//System.out.println(""+error);
						Toast.makeText(getActivity().getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
					}
				}
				else
				{
					final String requestId = values.getString("request");
					if (requestId != null)
					{
						Toast.makeText(getActivity().getApplicationContext(), "Request sent", Toast.LENGTH_SHORT).show();

					}
					else
					{
						Toast.makeText(getActivity().getApplicationContext(), "Request cancelled", Toast.LENGTH_SHORT).show();

					}
				}

				*//*System.out.println(error.toString());
				Log.e("error.toString",error.toString());*//*

			}

		}).build();
		requestsDialog.show();*/
	}

	/*private Session.StatusCallback statusCallback = new Session.StatusCallback()
	{
		@SuppressWarnings("deprecation")
		@Override
		public
		void call(Session session, SessionState state, Exception exception)
		{
			Log.e("Facebook12", "Session State: " + session.getState());
			if (state.isOpened())
			{
				sendRequestDialog();

			}
			else if (state.isClosed())
			{

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
		//uiHelper.onPause();
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

	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		System.out.println("sharan :" + requestCode +"......"+ resultCode);
		Log.e("sharan_fragment" + requestCode, "" + resultCode);


		super.onActivityResult(requestCode, resultCode, data);
		//uiHelper.onActivityResult(requestCode, resultCode, data);
		//Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);


	}




}