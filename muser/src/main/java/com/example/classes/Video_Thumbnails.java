package com.example.classes;

import android.graphics.Bitmap;

public class Video_Thumbnails
{

	Bitmap thumbnail;
	String video_path;
	int duration;
	/**
	 * @return the thumbnail
	 */
	public Bitmap getThumbnail()
	{
		return thumbnail;
	}
	/**
	 * @param thumbnail the thumbnail to set
	 */
	public void setThumbnail(Bitmap thumbnail)
	{
		this.thumbnail = thumbnail;
	}
	/**
	 * @return the video_path
	 */
	public String getVideo_path()
	{
		return video_path;
	}
	/**
	 * @param video_path the video_path to set
	 */
	public void setVideo_path(String video_path)
	{
		this.video_path = video_path;
	}
	/**
	 * @return the duration
	 */
	public int getDuration()
	{
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	
	
	
}
