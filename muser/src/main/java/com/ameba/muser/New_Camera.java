package com.ameba.muser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.example.classes.Util_Class;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class New_Camera extends Activity implements OnClickListener/*
																	 * ,
																	 * OnTouchListener
																	 */
{
	private SurfaceView preview = null;
	private SurfaceHolder previewHolder = null;
	private Camera camera = null;
	private boolean inPreview = false;
	private boolean cameraConfigured = false;
	ImageView button_capture;
	private int outputOrientation = -1;
	private int lastPictureOrientation = -1;
	String fileName;
	Context con;
	Uri outuri;
	int no_of_capture = 0;
	// public static ArrayList<String> images_list ;
	// ImageView image;
	Bitmap b;
	static String user_id, tour_id;
	boolean pic_clicked = false;
	int width;
	boolean pressed = false;
	OnOrientationChange onOrientationChange;
	private int front_or_back_camera;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.picture_fragment);
		con = this;
		/*
		 * Util_Class util = new Util_Class(); util.get_Lat_Long(con);
		 */
		preview = (SurfaceView) findViewById(R.id.preview);
		(button_capture = (ImageView) findViewById(R.id.button_capture)).setOnClickListener(this);
		((ImageView) findViewById(R.id.button_switch_camera)).setOnClickListener(this);
		// (flash = (ImageView)
		// findViewById(R.id.flash)).setOnClickListener(this);
		((TextView) findViewById(R.id.back)).setOnClickListener(this);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		LayoutParams params = preview.getLayoutParams();
		params.height = width;
		params.width = width;
		preview.setLayoutParams(params);
		// preview.setOnTouchListener(this);
		onOrientationChange = new OnOrientationChange(con);
		front_or_back_camera = CameraInfo.CAMERA_FACING_BACK;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		onOrientationChange.enable();
		Log.e("ttttttt", "gggggggg");
		previewHolder = preview.getHolder();
		previewHolder.addCallback(surfaceCallback);
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		if(camera == null)
		{
			camera = Camera.open();
		}
		setCameraDisplayOrientation(New_Camera.this, front_or_back_camera, camera);
		cameraConfigured = false;
		Camera.Parameters pictureParams = camera.getParameters();
		setCameraPictureOrientation(pictureParams);
	
	}

	@Override
	public void onPause()
	{
		onOrientationChange.disable();
		if(inPreview)
		{
			camera.stopPreview();
		}
		camera.release();
		camera = null;
		inPreview = false;
		super.onPause();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.button_capture:
			pressed = true;
			camera.takePicture(shutterCallback, rawCallback, jpegCallback);
			pic_clicked = true;
			break;
		case R.id.button_switch_camera:
			switchCamera();
			break;
		default:
			break;
		}
	}

	private void switchCamera()
	{
		if(camera != null)
		{
			camera.stopPreview(); // stop preview
			camera.release(); // release previous camera
		}
		if(front_or_back_camera == CameraInfo.CAMERA_FACING_BACK)
		{
			front_or_back_camera = CameraInfo.CAMERA_FACING_FRONT;
		}
		else
		{
			front_or_back_camera = CameraInfo.CAMERA_FACING_BACK;
		}
		// Create an instance of Camera
		camera = getCameraInstance(front_or_back_camera);
		if(camera == null)
			return;
		refresh_camera();
	}

	private Camera getCameraInstance(int type)
	{
		Camera c = null;
		int numberOfCameras = Camera.getNumberOfCameras();
		for(int i = 0; i < numberOfCameras; i++)
		{
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if(info.facing == type)
			{
				try
				{
					c = Camera.open(i); // attempt to get a Camera instance
				}
				catch(Exception e)
				{
					// Camera is not available
					Util_Class.show_Toast("Camera not available.", con);
				}
				break;
			}
		}
		return c;
	}

	public void refresh_camera()
	{
		cameraConfigured = false;
		setCameraDisplayOrientation(New_Camera.this, front_or_back_camera, camera);
		initPreview(width, width);
		startPreview();
	}

	/*
	 * @Override public void onBackPressed() { if(pic_clicked==true) {
	 * startActivity(new Intent(con,Camera_Images.class)); this.finish(); } else
	 * { Custom_camera.this.finish(); } }
	 */
	private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters)
	{
		Camera.Size result = null;
		for(Camera.Size size : parameters.getSupportedPreviewSizes())
		{
			if(size.width <= width && size.height <= height)
			{
				if(result == null)
				{
					result = size;
				}
				else
				{
					int resultArea = result.width * result.height;
					int newArea = size.width * size.height;
					if(newArea > resultArea)
					{
						result = size;
					}
				}
			}
		}
		return (result);
	}

	private void initPreview(int width, int height)
	{
		if(camera != null && previewHolder.getSurface() != null)
		{
			try
			{
				camera.setPreviewDisplay(previewHolder);
			}
			catch(Throwable t)
			{
				Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
				Toast.makeText(New_Camera.this, t.getMessage(), Toast.LENGTH_LONG).show();
			}
			if(!cameraConfigured)
			{
				Camera.Parameters parameters = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, parameters);
				if(size != null)
				{
					parameters.setPreviewSize(size.width, size.height);
					camera.setParameters(parameters);
					cameraConfigured = true;
				}
			}
		}
	}

	private void startPreview()
	{
		if(cameraConfigured && camera != null)
		{
			camera.startPreview();
			inPreview = true;
		}
	}

	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback()
	{
		public void surfaceCreated(SurfaceHolder holder)
		{
			// int
			// angleToRotate=CommonMethods.getRoatationAngle(New_Camera.this,
			// Camera.CameraInfo.CAMERA_FACING_FRONT);
			Log.e("surfaceCreated........................", "surfaceCreated");
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			initPreview(width, height);
			startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder holder)
		{
			// no-op
		}
	};

	private class OnOrientationChange extends OrientationEventListener
	{
		private boolean isEnabled = false;

		public OnOrientationChange(Context context)
		{
			super(context);
			disable();
		}

		@Override
		public void onOrientationChanged(int orientation)
		{
			// Log.e("OnOrientationChange........................",
			// ""+orientation);
			if(camera != null && orientation != ORIENTATION_UNKNOWN)
			{
				int newOutputOrientation = getCameraPictureRotation(orientation);
				if(newOutputOrientation != outputOrientation)
				{
					outputOrientation = newOutputOrientation;
					Log.e("outputOrientation........................", "" + outputOrientation);
					Camera.Parameters params = camera.getParameters();
					params.setRotation(outputOrientation);
					try
					{
						camera.setParameters(params);
						// camera.setDisplayOrientation(outputOrientation);
						lastPictureOrientation = outputOrientation;
						Log.e("lastPictureOrientation", "" + lastPictureOrientation);
						// setCameraDisplayOrientation(New_Camera.this,
						// CameraInfo.CAMERA_FACING_BACK, camera);
						/*
						 * Camera.Parameters
						 * pictureParams=camera.getParameters();
						 * 
						 * 
						 * setCameraPictureOrientation(pictureParams);
						 */
					}
					catch(Exception e)
					{
						Log.e(getClass().getSimpleName(), "Exception updating camera parameters in orientation change", e);
						// TODO: get this info out to hosting app
					}
				}
			}
		}
	}

	private int getCameraPictureRotation(int orientation)
	{
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(front_or_back_camera, info);
		int rotation = 0;
		orientation = (orientation + 45) / 90 * 90;
		if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
		{
			rotation = (info.orientation - orientation + 360) % 360;
		}
		else
		{ // back-facing camera
			rotation = (info.orientation + orientation) % 360;
		}
		return (rotation);
	}

	private void setCameraPictureOrientation(Camera.Parameters params)
	{
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(front_or_back_camera, info);
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
		{
			outputOrientation = getCameraPictureRotation(getWindowManager().getDefaultDisplay().getOrientation());
		}
		else if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
		{
			outputOrientation = (360 - lastPictureOrientation) % 360;
		}
		else
		{
			outputOrientation = lastPictureOrientation;
		}
		if(lastPictureOrientation != outputOrientation)
		{
			params.setRotation(outputOrientation);
			lastPictureOrientation = outputOrientation;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera)
	{
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		switch (rotation)
		{
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result;
		if(info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
		{
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		}
		else
		{ // back-facing
			result = (info.orientation - degrees + 360) % 360;
		}
		try
		{
			Log.e("rotation11111........................", "" + rotation);
			Log.e("result........................", "" + result);
			camera.setDisplayOrientation(result);
		}
		catch(NullPointerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	ShutterCallback shutterCallback = new ShutterCallback()
	{
		public void onShutter()
		{
			// Log.d(TAG, "onShutter'd");
		}
	};
	PictureCallback rawCallback = new PictureCallback()
	{
		public void onPictureTaken(byte[] data, Camera camera)
		{
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};
	PictureCallback jpegCallback = new PictureCallback()
	{
		public void onPictureTaken(byte[] data, Camera camera)
		{
			try
			{
				Calendar c = Calendar.getInstance();
				int day = c.get(Calendar.DAY_OF_MONTH);
				int month = c.get(Calendar.MONTH) + 1;
				int year = c.get(Calendar.YEAR);
				int hour = c.get(Calendar.HOUR_OF_DAY);
				int minute = c.get(Calendar.MINUTE);
				int second = c.get(Calendar.SECOND);
				String n = day + "" + month + "" + year + "_" + hour + "" + minute + "" + second;
				String image_name = n + ".jpg";
				File filepath = Environment.getExternalStorageDirectory();
				if(!checkFolder(filepath, "Muser"))
				{
					File dir = new File(filepath.getAbsolutePath() + "/Muser/");
					dir.mkdirs();
				}
				File dir = new File("/sdcard/Muser/");
				File file = new File(dir, image_name);
				BitmapFactory.Options opt;
				opt = new BitmapFactory.Options();
				opt.inTempStorage = new byte[16 * 1024];
				Parameters parameters = camera.getParameters();
				Size size = parameters.getPictureSize();
				int height11 = size.height;
				int width11 = size.width;
				float mb = (width11 * height11) / 1024000;
				if(mb > 4f)
					opt.inSampleSize = 4;
				else if(mb > 3f)
					opt.inSampleSize = 2;
				try
				{
					b = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
				}
				catch(OutOfMemoryError e)
				{
					e.printStackTrace();
				}
				Matrix matrix = new Matrix();
				matrix.postRotate(lastPictureOrientation);
				Bitmap rotated_bmp = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
				resetCam();
				new SaveToSdCard(rotated_bmp, file).execute();
				pressed = false;
			}
			finally
			{}
		}
	};

	private void resetCam()
	{
		camera.startPreview();
		// previewHolder.setCamera(camera);
	}

	/*
	 * @Override public void onConfigurationChanged(Configuration newConfig) {
	 * super.onConfigurationChanged(newConfig);
	 * 
	 * // setCameraDisplayOrientation(New_Camera.this,
	 * CameraInfo.CAMERA_FACING_BACK, camera); }
	 */
	public boolean checkFolder(File filepath, String string)
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

	private class SaveToSdCard extends AsyncTask<Void, Void, Void>
	{
		Bitmap bmp = null;
		File file = null;
		FileOutputStream outStream = null;

		public SaveToSdCard(Bitmap bmp, File file)
		{
			this.bmp = bmp;
			this.file = file;
		}

		protected void onPreExecute()
		{
		}

		@Override
		protected Void doInBackground(Void... arg0)
		{
			try
			{
				outStream = new FileOutputStream(file);
				bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				outStream.flush();
				outStream.close();
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			((Activity) con).finish();
			Intent i = new Intent(con, Captured_Image.class);
			i.putExtra("image", file.getAbsolutePath().toString());
			i.putExtra("type", "I");
			startActivity(i);
		}
	}
	/*
	 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
	 * Auto-generated method stub return false; }
	 */
}
