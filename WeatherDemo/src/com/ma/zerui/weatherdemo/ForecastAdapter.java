package com.ma.zerui.weatherdemo;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ma.zerui.weatherdemo.model.Weather;
import com.ma.zerui.weatherdemo.model.WeatherForecast;
import com.ma.zerui.weatherdemo.util.DateUtil;
import com.ma.zerui.weatherdemo.util.WeatherIconUtil;

public class ForecastAdapter extends BaseAdapter {

	private static final int FORECAST_LIST_NUM = 3;

	private WeatherForecast mListData;
	private Context mContext;
	private List<Integer> mColor;
	private Typeface mWeatherFont;

	public ForecastAdapter(Context context, WeatherForecast listData,
			List<Integer> color, Typeface weatherFont) {
		this.mContext = context;
		this.mListData = listData;
		this.mColor = color;
		this.mWeatherFont = weatherFont;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return FORECAST_LIST_NUM;
	}

	@Override
	public Weather getItem(int position) {

		return mListData.getForecast(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void setListData(WeatherForecast data) {
		mListData = data;
	}

	public void setColor(List<Integer> color) {
		mColor = color;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.forcast_item, parent, false);
		}
		TextView icon = (TextView) convertView.findViewById(R.id.forcast_icon);
		TextView description = (TextView) convertView
				.findViewById(R.id.forcast_description);
		TextView date = (TextView) convertView.findViewById(R.id.forcast_date);
		TextView temp = (TextView) convertView.findViewById(R.id.forecast_temp);
		TextView tempRange = (TextView) convertView
				.findViewById(R.id.forecast_temp_range);
		icon.setTypeface(mWeatherFont);

		convertView.setBackgroundColor(mColor.get(position + 1));

		icon.setText(WeatherIconUtil.setWeatherIcon(getItem(position)
				.getWeatherId(), mContext));
		description.setText(getItem(position).getCondition());
		date.setText(DateUtil.getDate(getItem(position).getTimeStamp()));
		temp.setText(getItem(position).getTemp() + " °„F");
		tempRange.setText(getItem(position).getMinTemp() + "°„F/"
				+ getItem(position).getMaxTemp() + "°„F");

		return convertView;
	}

}
