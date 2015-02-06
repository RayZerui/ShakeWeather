package com.ma.zerui.weatherdemo.util;

import android.content.Context;

import com.ma.zerui.weatherdemo.R;

public class WeatherIconUtil {
	public static String setWeatherIcon(int actualId, Context context) {
		int id = actualId / 100;
		String icon = "";
		if (actualId == 800) {
			icon = context.getString(R.string.weather_sunny);
		} else {
			switch (id) {
			case 2:
				icon = context.getString(R.string.weather_thunder);
				break;
			case 3:
				icon = context.getString(R.string.weather_drizzle);
				break;
			case 7:
				icon = context.getString(R.string.weather_foggy);
				break;
			case 8:
				icon = context.getString(R.string.weather_cloudy);
				break;
			case 6:
				icon = context.getString(R.string.weather_snowy);
				break;
			case 5:
				icon = context.getString(R.string.weather_rainy);
				break;
			}
		}
		return icon;
	}
}
