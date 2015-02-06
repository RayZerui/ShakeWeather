package com.ma.zerui.weatherdemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.location.Location;
import android.util.Log;

public class WeatherHttpClient {
	private static String CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?units=imperial&";

	private static String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?units=imperial&cnt=4&";

	public String getWeatherData(Location location) {
		HttpURLConnection connection = null;
		InputStream stream = null;

		try {
			String url = CURRENT_URL + "lat=" + location.getLatitude() + "&"
					+ "lon=" + location.getLongitude();

			int responseCode = -1;

			connection = (HttpURLConnection) (new URL(url)).openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Read the response
				StringBuffer buffer = new StringBuffer();
				stream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(stream));
				String line = null;
				while ((line = reader.readLine()) != null)
					buffer.append(line + "\r\n");

				stream.close();
				connection.disconnect();

				return buffer.toString();
			} else {
				Log.i("Current_HTTP_Response", "Unsuccesful Http Response Code: "
						+ responseCode);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Throwable t) {
			}
			try {
				connection.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}

	public String getForecastWeatherData(Location location) {
		HttpURLConnection connection = null;
		InputStream stream = null;

		try {

			// Forecast
			String url = FORECAST_URL + "lat=" + location.getLatitude() + "&"
					+ "lon=" + location.getLongitude();

			int responseCode = -1;

			connection = (HttpURLConnection) (new URL(url)).openConnection();
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// Read the forcast response
				StringBuffer buffer = new StringBuffer();
				stream = connection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						stream));
				String line1 = null;
				while ((line1 = reader.readLine()) != null)
					buffer.append(line1 + "\r\n");

				stream.close();
				connection.disconnect();

				return buffer.toString();
			}else{
				Log.i("Forcast_HTTP_Response", "Unsuccesful Http Response Code: "
						+ responseCode);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				stream.close();
			} catch (Throwable t) {
			}
			try {
				connection.disconnect();
			} catch (Throwable t) {
			}
		}

		return null;

	}
}
