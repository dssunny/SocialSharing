package com.dssunny.share;

public class Config {

	public static String LINKEDIN_CONSUMER_KEY = "75p7yn094r97ob";
	public static String LINKEDIN_CONSUMER_SECRET = "qSj81y4wy8X7dFsE";
	public static String scopeParams = "rw_nus+r_basicprofile";
	
	public static String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static String OAUTH_CALLBACK_HOST = "callback";
	public static String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
}
