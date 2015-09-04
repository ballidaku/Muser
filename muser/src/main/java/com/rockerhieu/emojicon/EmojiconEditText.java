/*
 * Copyright 2014 Hieu Rocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rockerhieu.emojicon;

import com.ameba.muser.Captured_Image;
import com.ameba.muser.Chats;
import com.ameba.muser.R;
import com.ameba.muser.View_All_Comments;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author Hieu Rocker (rockerhieu@gmail.com).
 */
public class EmojiconEditText extends EditText {
    private int mEmojiconSize;
    private int mEmojiconTextSize;
    private boolean mUseSystemDefault = false;
    Context con;

    public EmojiconEditText(Context context) {
        super(context);
        mEmojiconSize = (int) getTextSize();
        mEmojiconTextSize = (int) getTextSize();
    }

    public EmojiconEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        con=context;
    }

    public EmojiconEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Emojicon);
        mEmojiconSize = (int) a.getDimension(R.styleable.Emojicon_emojiconSize, getTextSize());
        mUseSystemDefault = a.getBoolean(R.styleable.Emojicon_emojiconUseSystemDefault, false);
        a.recycle();
        mEmojiconTextSize = (int) getTextSize();
        setText(getText());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        updateText();
    }

    /**
     * Set the size of emojicon in pixels.
     */
    public void setEmojiconSize(int pixels) {
        mEmojiconSize = pixels;

        updateText();
    }

    private void updateText() {
        EmojiconHandler.addEmojis(getContext(), getText(), mEmojiconSize, mEmojiconTextSize, mUseSystemDefault);
    }

    /**
     * Set whether to use system default emojicon
     */
    public void setUseSystemDefault(boolean useSystemDefault) {
        mUseSystemDefault = useSystemDefault;
    }
    
	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{

			FragmentActivity ac=(FragmentActivity) getContext();
			
			if ( ac instanceof View_All_Comments ) 
			{
				((View_All_Comments) ac).HideEmoji();
			}
			else if ( ac instanceof Chats ) 
			{
				((Chats) ac).HideEmoji();
			}
			else if(con instanceof Captured_Image)
			{
				((Captured_Image) con).HideEmoji();
			}
			

		}
		return false;
	}
    
}
