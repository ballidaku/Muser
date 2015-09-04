package com.example.classes;

public class Global
{
	
	public static String user_id;
	public static String friend_id;
	public static String fund;
	//public static String is_public_or_private="";
	//public static String is_follow="Y";
	

	
/*	public static String getIs_follow()
	{
		return is_follow;
	}

	public static void setIs_follow(String is_follow)
	{
		Global.is_follow = is_follow;
	}*/


	/*public static String getIs_public_or_private()
	{
		return is_public_or_private;
	}

	
	public static void setIs_public_or_private(String is_public_or_private)
	{
		Global.is_public_or_private = is_public_or_private;
	}*/

	/**
	 * @return the fund
	 */
	public static String getFund()
	{
		return fund;
	}

	/**
	 * @param fund the fund to set
	 */
	public static void setFund(String fund)
	{
		Global.fund = fund;
	}

	public static void set_user_id(String s)
	{
		user_id=s;	
	}
	
	public static String get_user_id()
	{
		return user_id;
	}
	
	
	public static void set_friend_id(String f)
	{
		friend_id=f;	
	}
	
	public static String get_friend_id()
	{
		return friend_id;
	}

}
