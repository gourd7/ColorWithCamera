package com.isvsa.wallpaper;

public interface IViewLife {
	
	public static final int ON_RESUME = 0;
	public static final int ON_STOP = 1;
	public static final int ON_DESTROY = 2;

	public void onResume();

	public void onStop();

	public void onDestroy();
}