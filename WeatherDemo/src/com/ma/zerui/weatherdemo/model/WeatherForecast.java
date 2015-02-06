package com.ma.zerui.weatherdemo.model;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecast {
	private List<Weather> weatherForecast = new ArrayList<Weather>();

	public List<Weather> getWeatherForecast() {
		return weatherForecast;
	}

	public void setWeatherForecast(List<Weather> weatherForecast) {
		this.weatherForecast = weatherForecast;
	}
	
	public void addForecast(Weather forecast) {
		weatherForecast.add(forecast);
	}
	
	public Weather getForecast(int postion){
		return weatherForecast.get(postion);
	}
	
	
}
