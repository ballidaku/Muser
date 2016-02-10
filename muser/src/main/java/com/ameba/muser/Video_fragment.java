package com.ameba.muser;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import com.example.classes.Util_Class;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Video_fragment extends Fragment implements OnClickListener, OnTouchListener/*, SurfaceHolder.Callback*/
{
	private CamcorderProfile	camcorderProfile;
	ImageView					button_capture;
	private Camera				camera				= null;
	MediaRecorder				recorder;
	boolean						recording			= false;
	boolean						usecamera			= true;
	boolean						previewRunning		= false;
	private SurfaceHolder		holder				= null;
	private SurfaceView			preview				= null;
	private int					front_or_back_camera;
	private boolean				cameraConfigured	= false;
	int							width;
	ImageView					flash;
	private boolean				isLighOn			= false;
	Parameters					p;
	String						path;
	ProgressBar					progressBar;
	CountDownTimer				counter;
	RelativeLayout				progress_relative_lay;
	OnOrientationChange			onOrientationChange;
	Context						con;
	SharedPreferences			rem_pref;
	TextView					timer_text;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		con=getActivity();
		rem_pref = con.getSharedPreferences("Remember", con.MODE_WORLD_READABLE);
		
		com.ameba.muser.Camera.which_fragment="video";
		
		View rootView = inflater.inflate(R.layout.video_fragment, container, false);
		camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
		preview = (SurfaceView) rootView.findViewById(R.id.preview);
		(button_capture = (ImageView) rootView.findViewById(R.id.button_capture)).setOnClickListener(this);
		progress_relative_lay = (RelativeLayout) rootView.findViewById(R.id.progress_relative_lay);
		(progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar)).setOnClickListener(this);
		((ImageView) rootView.findViewById(R.id.button_switch_camera)).setOnClickListener(this);
		(flash = (ImageView) rootView.findViewById(R.id.flash)).setOnClickListener(this);
		((TextView) rootView.findViewById(R.id.back)).setOnClickListener(this);
		((TextView)rootView. findViewById(R.id.go_to_picture)).setOnClickListener(this);
		timer_text=(TextView)rootView. findViewById(R.id.timer_text);
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		LayoutParams params = preview.getLayoutParams();
		params.height = width;
		params.width = width;
		preview.setLayoutParams(params);
		preview.setOnTouchListener(this);
		
		
		onOrientationChange = new OnOrientationChange(con);
		front_or_back_camera = CameraInfo.CAMERA_FACING_BACK;
		
		
		return rootView;
	}

	
	@Override
	public void onResume()
	{
		super.onResume();
		onOrientationChange.enable();
		//Log.e("ttttttt", "gggggggg");
		holder = preview.getHolder();
		holder.addCallback(surfaceCallback);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
	//	camera = getCameraInstance(front_or_back_camera);
		
		if(camera == null)
		{
			camera = Camera.open();
		}
		
	/*Camera.Parameters pictureParams = camera.getParameters();
		setCameraPictureOrientation(pictureParams);*/
		
		refresh_camera();
		
	}
	
	
	@Override
	public void onPause()
	{


		if(recording)
		{
			start_work();
		}
		else
		{
			onOrientationChange.disable();
			if (camera != null)
			{
				camera.stopPreview();
				camera.setPreviewCallback(null);
				camera.lock();
				camera.release();
				camera = null;
			}
		}





		super.onPause();
	}
	
	public void refresh_camera()
	{
		
		cameraConfigured=false;
		setCameraDisplayOrientation(getActivity(), front_or_back_camera, camera);
		initPreview(width, width);
		startPreview();
		
		
	}
	
	private void startPreview()
	{
		if(cameraConfigured && camera != null)
		{
			camera.startPreview();
			
			
		}
	}
	private void initPreview(int width, int height)
	{
		if(camera != null && holder.getSurface() != null)
		{
			try
			{
				Log.e("initPreview", "initPreview");
				camera.setPreviewDisplay(holder);
			}
			catch(Throwable t)
			{
				Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
				Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
			}
			if(!cameraConfigured)
			{
				p = camera.getParameters();
				Camera.Size size = getBestPreviewSize(width, height, p);
				if(size != null)
				{
					p.setPreviewSize(size.width, size.height);
					p.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
					p.setPreviewFrameRate(camcorderProfile.videoFrameRate);
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
					cameraConfigured = true;
				}
			}
		}
	}
	
/*	private void setCameraPictureOrientation(Camera.Parameters params)
	{
		Camera.CameraInfo info = new Camera.CameraInfo();
		Camera.getCameraInfo(front_or_back_camera, info);
		if(getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
		{
			outputOrientation = getCameraPictureRotation(getActivity().getWindowManager().getDefaultDisplay().getOrientation());
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
	}*/
	
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

	int result;

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera)
	{
		android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
		android.hardware.Camera.getCameraInfo(cameraId, info);
		int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
		int degrees = 0;
		Log.e("rotation........................", "" + rotation);
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
		Log.e("degrees........................", "" + degrees);
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
			Log.e("result........................", "" + result);
			camera.setDisplayOrientation(result);
		}
		catch(NullPointerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*private Camera getCameraInstance(int type)
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
					Util_Class.show_Toast("Camera not available.",getActivity());
				}
				break;
			}
		}
		return c;
	}*/
	
	
	SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback()
	{
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
		{
			Log.e("surfaceChanged", "surfaceChanged");
			refresh_recoder();
		}

		@Override
		public void surfaceCreated(SurfaceHolder arg0)
		{
			Log.e("surfaceCreated", "surfaceCreated");

			previewRunning = true;

		}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0)
	{
		
		Log.e("surfaceDestroyed", "surfaceDestroyed");
		Log.e("Hello", ""+recording);
		if(recorder!=null)
		{
			Log.e("Hello", ""+recording);
			if(recording)
			{

				recorder.stop();
				recording = false;
			}
			recorder.release();
		}
		
		
		
		
		
		if(usecamera)
		{
			previewRunning = false;
			//camera.stopPreview();
			/*camera.setPreviewCallback(null);
			camera.lock();
			camera.release();
			camera = null;*/
		}
		//finish();
	}
	
};

public void refresh_recoder()
{
	if(!recording && usecamera)
	{
		if(previewRunning)
		{
			camera.stopPreview();
		}
		try
		{
			
		   /* p = camera.getParameters();
			p.setPreviewSize(camcorderProfile.videoFrameWidth, camcorderProfile.videoFrameHeight);
			p.setPreviewFrameRate(camcorderProfile.videoFrameRate);
			p.setFlashMode(Parameters.FLASH_MODE_OFF);
			camera.setParameters(p);*/
			setCameraDisplayOrientation(getActivity(), front_or_back_camera, camera);
			camera.setPreviewDisplay(holder);
			camera.startPreview(); 
			previewRunning = true;
		}
		catch(IOException e)
		{
			Log.e("LOGTAG", e.getMessage());
			e.printStackTrace();
		}
		prepareRecorder();
	}
}

	float initialX;
	
	@Override
	public boolean onTouch(View arg0, MotionEvent event)
	{
		int action = event.getAction();
		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			initialX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			float finalX = event.getX();
			float diff = finalX - initialX;
			Log.e("diff", "" + diff);
			// if(initialX < finalX && diff > 50 && will_start_anim == true)
			if(diff > 50)
			{
//				Log.e("ergerthreth", "" + initialX + "...." + finalX);
//				FragmentManager fragmentManager2 = getFragmentManager();
//				fragmentManager2.popBackStack();
				switch_to_picture();
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		default:
			break;
		}
		return true;
	}
	
	
	public void switch_to_picture()
	{
		FragmentManager fragmentManager2 = getFragmentManager();
		fragmentManager2.popBackStack();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		
		case R.id.back:
			  getActivity().finish();
				break;
				
		case R.id.button_switch_camera:
			
			if(recording==false)
			{
				switchCamera(); 
			}
			
			
			break;
			
		case R.id.progressBar:
			start_work();
			
			
			break;
			
		case R.id.button_capture:
			start_work();
			break;
			
		case R.id.go_to_picture:
			switch_to_picture();
				break;
			
		case R.id.flash:
			flash_on_off();
		default:
			break;
		}
	}
	
	
	private void flash_on_off()
	{
		Log.e("mCurrentCamera", ""+front_or_back_camera);
		if(isLighOn && front_or_back_camera==0)
		{
			Log.i("info", "torch is turn off!");
		
			if(!recording)
			{
				camera.lock();
			}
			p.setFlashMode(p.getFlashMode().equals(Parameters.FLASH_MODE_TORCH) ? Parameters.FLASH_MODE_OFF : Parameters.FLASH_MODE_TORCH);
			camera.setParameters(p);
			flash.setBackgroundResource(R.drawable.noflash);
			if(!recording)
			{
				camera.unlock();
			}
			isLighOn = false;
		}
		else if(front_or_back_camera==0)
		{
			Log.i("info", "torch is turn on!");
			if(!recording)
			{
				camera.lock();
			}
			p.setFlashMode(p.getFlashMode().equals(Parameters.FLASH_MODE_TORCH) ? Parameters.FLASH_MODE_OFF : Parameters.FLASH_MODE_TORCH);
			camera.setParameters(p);
			flash.setBackgroundResource(R.drawable.flash);
			if(!recording)
			{
				camera.unlock();
			}
			isLighOn = true;
		}
		
	}
	
	private void switchCamera()
	{
		if(Camera.getNumberOfCameras() >= 2)
		{
			if(camera != null)
			{
				camera.stopPreview();
				camera.setPreviewCallback(null);
				camera.lock();
				camera.release();
				camera = null;
				
				
				
			}
			if(front_or_back_camera == CameraInfo.CAMERA_FACING_BACK)
			{
				front_or_back_camera = CameraInfo.CAMERA_FACING_FRONT;
			}
			else
			{
				front_or_back_camera = CameraInfo.CAMERA_FACING_BACK;
			}
			
			/*holder.removeCallback(surfaceCallback);
			holder = preview.getHolder();
			holder.addCallback(surfaceCallback);
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);*/
		//	camera = getCameraInstance(front_or_back_camera);
			
			if(camera == null)
			{
				camera = Camera.open(front_or_back_camera);
			}
			
			refresh_camera();
			
			
			
				
			/*if(recording)
			{
				recorder.stop();
				recording = false;
			}
			recorder.reset();
			recorder.release();
			//recorder = null;
			
			if(usecamera)
			{
				previewRunning = true;
				// camera.lock();
				//camera.release();
			}*/
			
			refresh_recoder();
		
		}
	}
	static int time;
	private void start_work()
	{
		if(recording)
		{
			counter.cancel();
			
//*******************************************Stop Timer*****************************************************
			mHandler.removeCallbacks(startTimer);
			stopped = true;
//************************************************************************************************
			
			ok_tata();
			
			Log.v("LOGTAG", "Recording Stopped");
		}
		else
		{
			recording = true;
			progress_relative_lay.setVisibility(View.VISIBLE);
			button_capture.setVisibility(View.GONE);
			
			
			recorder.start();
			Log.v("LOGTAG", "Recording Started");
			
//*******************************************Start Timer*****************************************************
			if(stopped)
			{
				startTime = System.currentTimeMillis() - elapsedTime;
			}
			else
			{
				startTime = System.currentTimeMillis();
			}
			mHandler.removeCallbacks(startTimer);
			mHandler.postDelayed(startTimer, 0);
			
//***************************************************************************************************
			time=rem_pref.getString("member_type", "").equals("T")?30*60*1000:15000;
			
			counter = new CountDownTimer(time, time/100)
			{
				
				
				int progress=0;
				@Override
				public void onTick(long millisUntilFinished)
				{
					
					Log.e("millisUntilFinished", ""+millisUntilFinished);
					Log.e("time", ""+progress);
					
					progress +=1;
					Log.e("time", ""+progress);
					progressBar.setProgress(progress);
				}

				@Override
				public void onFinish()
				{
					ok_tata();
				}
			}.start();
		}
	}
	
	 
	
	
	
	
	public void ok_tata()
	{
		

		try
		{
			recorder.stop();

            camera.stopPreview();
            camera.setPreviewCallback(null);
            camera.lock();
            camera.release();
            camera = null;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		recording = false;
		Intent i = new Intent(getActivity(), Captured_Image.class);
		i.putExtra("image", path);
		i.putExtra("type", "V");

		recorder = null;

		startActivity(i);
		getActivity().finish();
	}
	
	public void finish_on_spot()   // accessed on back pressed in Camera Activity
	{
		if(recording)
		{
			counter.cancel();

			//*******************************************Stop Timer*****************************************************
			mHandler.removeCallbacks(startTimer);
			stopped = true;
			//************************************************************************************************


			try
			{

				recorder.stop();
				recorder.release();
				recorder = null;
				camera.reconnect();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			recording = false;

		}

		getActivity().finish();
	}
	

	private int outputOrientation = -1;
	private int lastPictureOrientation = -1;
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
			// Log.e("OnOrientationChange........................",""+orientation);
			if(camera != null && orientation != ORIENTATION_UNKNOWN)
			{
				int newOutputOrientation = getCameraPictureRotation(orientation);
				if(newOutputOrientation != outputOrientation)
				{
					outputOrientation = newOutputOrientation;
					Log.e("outputOrientation........................", "" + outputOrientation);
					//Camera.Parameters params = camera.getParameters();
					
					
					
					
					// p = camera.getParameters();
						
						//camera.setParameters(p);
					//p = camera.getParameters();
				//	p.setRotation(outputOrientation);
					try
					{
					//	camera.setParameters(p);
						lastPictureOrientation = outputOrientation;
						Log.e("lastPictureOrientation", "" + lastPictureOrientation);
					
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
	
	private void prepareRecorder()
	{
		
		Log.e("prepareRecorder:result",""+ result);
		recorder = new MediaRecorder();
		
		if(front_or_back_camera == CameraInfo.CAMERA_FACING_BACK)
		{
			try
			{
				recorder.setOrientationHint(outputOrientation) ;
			}
			catch(Exception e)
			{
				recorder.setOrientationHint(result) ;
				e.printStackTrace();
			}
		}
		else
		{
			recorder.setOrientationHint(270) ;
		}
		
		recorder.setPreviewDisplay(holder.getSurface());
		if(usecamera)
		{
			camera.unlock();
			recorder.setCamera(camera);
			
		}
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		recorder.setProfile(camcorderProfile);
		
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH) + 1;
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		String n = day + "" + month + "" + year + "_" + hour + "" + minute + "" + second;
		
		
		File filepath = Environment.getExternalStorageDirectory();
		
		if(!Util_Class.checkFolder(filepath, "Muser"))
		{
			File dir = new File(filepath.getAbsolutePath() + "/Muser/");
			dir.mkdirs();
		}
		File dir = new File("/sdcard/Muser/");
		/*// This is all very sloppy
		if(camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP )
		{
			try
			{
				File newFile = File.createTempFile("evideocapture", ".3gp", Environment.getExternalStorageDirectory());
				recorder.setOutputFile(newFile.getAbsolutePath());
			}
			catch(IOException e)
			{
				Log.v("LOGTAG", "Couldn't create file");
				e.printStackTrace();
				//finish();
			}
		}
		else if(camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4 )
		{*/
			try
			{
				File newFile = File.createTempFile(n.replaceAll("-", ""), ".mp4",dir);
				path=newFile.getAbsolutePath();
				recorder.setOutputFile(newFile.getAbsolutePath());
			}
			catch(IOException e)
			{
				Log.v("LOGTAG", "Couldn't create file");
				e.printStackTrace();
				//finish();
			}
	/*	}
		else 
		{
			try
			{
				File newFile = File.createTempFile("qvideocapture", ".mp4", Environment.getExternalStorageDirectory());
				recorder.setOutputFile(newFile.getAbsolutePath());
			}
			catch(IOException e)
			{
				Log.v("LOGTAG", "Couldn't create file");
				e.printStackTrace();
				//finish();
			}
		}*/
		// recorder.setMaxDuration(20000); //
		// recorder.setMaxFileSize(50000000); // Approximately 5 megabytes
		try
		{
			recorder.prepare();
		}
		catch(IllegalStateException e)
		{
			e.printStackTrace();
			//finish();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			//finish();
		}
	}
	
	
	private void updateTimer(float time)
	{
		secs = (long) (time / 1000);
		mins = (long) ((time / 1000) / 60);
		//hrs = (long) (((time / 1000) / 60) / 60);
		/* Convert the seconds to String * and format to ensure it has * a leading zero when required */
		secs = secs % 60;
		seconds = String.valueOf(secs);
		if(secs == 0)
		{
			seconds = "00";
		}
		if(secs < 10 && secs > 0)
		{
			seconds = "0" + seconds;
		}
		/* Convert the minutes to String and format the String */
		mins = mins % 60;
		minutes = String.valueOf(mins);
		if(mins == 0)
		{
			minutes = "00";
		}
		if(mins < 10 && mins > 0)
		{
			minutes = "0" + minutes;
		}
		/* Convert the hours to String and format the String */
		/*hours = String.valueOf(hrs);
		if(hrs == 0)
		{
			hours = "00";
		}
		if(hrs < 10 && hrs > 0)
		{
			hours = "0" + hours;
		}*/
		
		/* Although we are not using milliseconds on the timer in this example * I included the code in the event that you wanted to include it on your own */
	/*	milliseconds = String.valueOf((long) time);
		if(milliseconds.length() == 2)
		{
			milliseconds = "0" + milliseconds;
		}
		if(milliseconds.length() <= 1)
		{
			milliseconds = "00";
		}
		milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2);*/
		/* Setting the timer text to the elapsed time */
		
		timer_text.setText(/*hours + ":" +*/ minutes + ":" + seconds);
		Log.e("zzzzzzzzz",/*hours + ":" +*/ minutes + ":" + seconds);
//		Log.e("wwwww.","" + milliseconds);
	}
	
	private Runnable	startTimer		= new Runnable()
	{
		public void run()
		{
			elapsedTime = System.currentTimeMillis() - startTime;
			updateTimer(elapsedTime);
			mHandler.postDelayed(this, REFRESH_RATE);
		}
	};


	private Handler mHandler = new Handler(); 
	private long startTime; 
	private long elapsedTime; 
	private final int REFRESH_RATE = 100; 
	private String /*hours,*/minutes,seconds/*,milliseconds*/; 
	private long secs,mins/*,hrs,msecs*/; 
	private boolean stopped = false;



	
	
	
	

}
