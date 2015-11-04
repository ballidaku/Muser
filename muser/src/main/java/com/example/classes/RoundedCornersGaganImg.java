package com.example.classes;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ameba.muser.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class RoundedCornersGaganImg extends ImageView
{

	private boolean				mBlockLayout;

	private int					RADIUS		= 0;

	private ImageLoader			imageLoader	= ImageLoader.getInstance();
	private DisplayImageOptions	options;

	public RoundedCornersGaganImg(Context context)
	{
		super(context);

	}

	public RoundedCornersGaganImg(Context context, AttributeSet attrs)
	{
		super(context, attrs);

	}

	public RoundedCornersGaganImg(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);

	}

	@Override
	public void onDraw(Canvas canvas)
	{
		Drawable maiDrawable = getDrawable();

		if(maiDrawable != null && maiDrawable instanceof BitmapDrawable && RADIUS > 0)
		{
			Paint paint = ((BitmapDrawable) maiDrawable).getPaint();
			final int color = 0xff000000;
			Rect bitmapBounds = maiDrawable.getBounds();
			final RectF rectF = new RectF(bitmapBounds);
			// Create an off-screen bitmap to the PorterDuff alpha blending to
			// work right
			int saveCount = canvas.saveLayer(rectF, null, Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG | Canvas.CLIP_TO_LAYER_SAVE_FLAG);
			// Resize the rounded rect we'll clip by this view's current bounds
			// (super.onDraw() will do something similar with the drawable to
			// draw)
			getImageMatrix().mapRect(rectF);

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, RADIUS, RADIUS, paint);

			Xfermode oldMode = paint.getXfermode();
			// This is the paint already associated with the BitmapDrawable that
			// super draws
			paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			super.onDraw(canvas);
			paint.setXfermode(oldMode);
			canvas.restoreToCount(saveCount);
		}
		else
		{
			super.onDraw(canvas);
		}
	}

	public void setRadius(int radius)
	{
		this.RADIUS = radius;
	}

	@Override
	public void requestLayout()
	{

		if(!mBlockLayout)
		{

			super.requestLayout();

		}

	}

	@Override
	public void setImageResource(int resId)
	{

		mBlockLayout = true;

		super.setImageResource(resId);

		mBlockLayout = false;

	}

	@Override
	public void setImageURI(Uri uri)
	{

		mBlockLayout = true;

		super.setImageURI(uri);

		mBlockLayout = false;

	}

	@Override
	public void setImageDrawable(Drawable drawable)
	{

		mBlockLayout = true;

		super.setImageDrawable(drawable);

		mBlockLayout = false;

	}

	@Override
	public void setImageBitmap(Bitmap bm)
	{
		mBlockLayout = true;

		super.setImageBitmap(bm);

		mBlockLayout = false;

	}

	public void setImageUrl(Context con, String URL)
	{

		options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true).showImageForEmptyUri(R.drawable.no_media).showImageOnFail(R.drawable.no_media).showImageOnLoading(R.drawable.loading_image).build();
		
		if(!imageLoader.isInited())
		{
		
			imageLoader.init(ImageLoaderConfiguration.createDefault(((ContextWrapper) con).getBaseContext()));
		}

		imageLoader.displayImage(URL, this, options);




	}

}
