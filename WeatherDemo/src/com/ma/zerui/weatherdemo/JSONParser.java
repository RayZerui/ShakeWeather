package com.ma.zerui.weatherdemo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ma.zerui.weatherdemo.model.Weather;
import com.ma.zerui.weatherdemo.model.WeatherForecast;

public class JSONParser {
	
	public static Weather getWeather(String data) throws JSONException  {
		Weather weather = new Weather();
		Log.i("JSONObject", "Data ["+data+"]");
		//Creates JSONObject from the data
		JSONObject jObj = new JSONObject(data);
		weather.setCity(getString("name", jObj));
		weather.setTimeStamp(getLong("dt", jObj));
				
		JSONObject sysObj = getObject("sys", jObj);
		weather.setCountry(getString("country", sysObj));
		
		// Weather is listed in array
		JSONArray jArr = jObj.getJSONArray("weather");
				
		JSONObject JSONWeather = jArr.getJSONObject(0);
		weather.setWeatherId(getInt("id", JSONWeather));
		weather.setCondition(getString("description", JSONWeather));
		
		JSONObject mainObj = getObject("main", jObj);
		weather.setHumidity(getInt("humidity", mainObj));
		weather.setMaxTemp(getFloat("temp_max", mainObj));
		weather.setMinTemp(getFloat("temp_min", mainObj));
		weather.setTemp(getFloat("temp", mainObj));
				
		return weather;
	}
	
public static WeatherForecast getForecastWeather(String data) throws JSONException  {
		
		WeatherForecast forecast = new WeatherForecast();
		
		Log.i("ForecastJSONObject", "Buffer ["+data+"]");
		
		JSONObject jObj = new JSONObject(data);

		JSONArray jArr = jObj.getJSONArray("list"); 
		//start with second object since the first is today's weather forecast
		for (int i=1; i < jArr.length(); i++) {
			JSONObject jForecast = jArr.getJSONObject(i);	
			
			Weather weather = new Weather();
			// Retrieve the timestamp (dt)
			weather.setTimeStamp(getLong("dt", jForecast));
			
			// Temp is an object
			JSONObject jTempObj = jForecast.getJSONObject("temp");
			
			weather.setTemp(getFloat("day", jTempObj)); // Used day temp to represent the overall temp
			weather.setMaxTemp(getFloat("max", jTempObj));
			weather.setMinTemp(getFloat("min", jTempObj));
			
			weather.setHumidity(getInt("humidity", jForecast));
			// the weather object
			JSONArray jWeatherArr = jForecast.getJSONArray("weather");
			JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
			weather.setWeatherId(getInt("id", jWeatherObj));
			weather.setCondition(getString("description", jWeatherObj));
			
			forecast.addForecast(weather);
		}
		
		return forecast;
	}
	
	private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
		JSONObject subObj = jObj.getJSONObject(tagName);
		return subObj;
	}
	
	private static String getString(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getString(tagName);
	}

	private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
		return (float) jObj.getDouble(tagName);
	}
	
	private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getInt(tagName);
	}
	
	private static long getLong(String tagName, JSONObject jObj) throws JSONException {
		return jObj.getLong(tagName);
	}
	
	
}
