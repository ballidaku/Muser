package com.example.classes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;

import com.ameba.muser.R;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class CannonballTwitterLoginButton extends TwitterLoginButton {
    public CannonballTwitterLoginButton(Context context) {
        super(context);
        init();
    }

    public CannonballTwitterLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CannonballTwitterLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @SuppressWarnings("deprecation")
	@SuppressLint("ResourceAsColor")
	private void init() {
        if (isInEditMode()){
            return;
        }
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
      //  setBackgroundColor(android.R.color.transparent);
       // setBackgroundDrawable(getResources().getDrawable(R.drawable.tw_btn));
       //setBackgroundResource(R.drawable.tw_btn);
       setText("");
      // setGravity(Gravity.CENTER);
       
       
      
    }
}