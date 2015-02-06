package com.ma.zerui.weatherdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ma.zerui.weatherdemo.model.Weather;
import com.ma.zerui.weatherdemo.model.WeatherForecast;
import com.ma.zerui.weatherdemo.util.DateUtil;
import com.ma.zerui.weatherdemo.util.ShakeDetectorUtil;
import com.ma.zerui.weatherdemo.util.ShakeDetectorUtil.OnShakeListener;
import com.ma.zerui.weatherdemo.util.WeatherIconUtil;

public class MainActivity extends ActionBarActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private LocationManager mLocManager;
	public Location mLocation;
	private String mBestProvider;
	private LocationListener mLocListener;

	public Typeface mWeatherFont;

	private TextView mCity;
	private TextView mDate;
	private TextView mDetails;
	private TextView mDescription;
	private TextView mTemperature;
	private TextView mWeatherIcon;
	private RelativeLayout mFrame;

	private WeatherForecast mListData;
	private ListView mForecastList;
	private ForecastAdapter mAdapter;

	private List<Integer> mColor;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetectorUtil mShakeDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mWeatherFont = Typeface.createFromAsset(getAssets(), "weather.ttf");

		mCity = (TextView) findViewById(R.id.current_weather_city);
		mDate = (TextView) findViewById(R.id.current_weather_date);
		mDetails = (TextView) findViewById(R.id.current_weather_details);
		mDescription = (TextView) findViewById(R.id.current_weather_description);
		mTemperature = (TextView) findViewById(R.id.current_weather_temp);
		mWeatherIcon = (TextView) findViewById(R.id.current_weather_icon);
		mFrame = (RelativeLayout) findViewById(R.id.main_frame);
		mForecastList = (ListView) findViewById(R.id.forcast_list);

		mWeatherIcon.setTypeface(mWeatherFont);

		initColor();
		mFrame.setBackgroundColor(mColor.get(0));

		mLocManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		initProvider();
		initLocationListener();
		locationTracker();

		executeTasks();

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mShakeDetector = new ShakeDetectorUtil (new OnShakeListener() {

			public void onShake() {
				changeColor();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mLocation == null) {
			mLocManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			initProvider();
			initLocationListener();
		}
		locationTracker();

		executeTasks();
		
		mSensorManager.registerListener(mShakeDetector, mAccelerometer, 
    			SensorManager.SENSOR_DELAY_UI);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.shake, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.change_color) {
			changeColor();
		}
		return false;
	}

	private void changeColor() {
		Collections.shuffle(mColor);
		mFrame.setBackgroundColor(mColor.get(0));
		mAdapter.setColor(mColor);
		mAdapter.notifyDataSetChanged();
	}

	private void initColor() {
		mColor = new ArrayList<Integer>();
		Integer purple = getResources().getColor(R.color.purple);
		Integer orange = getResources().getColor(R.color.orange);
		Integer blue = getResources().getColor(R.color.blue);
		Integer green = getResources().getColor(R.color.green);

		mColor.add(purple);
		mColor.add(orange);
		mColor.add(blue);
		mColor.add(green);
	}

	/* Initiate location service provider. */
	private void initProvider() {
		mBestProvider = null;
		Criteria criteria = new Criteria();
		mBestProvider = mLocManager.getBestProvider(criteria, true);
		if (mBestProvider == null) {
			Toast.makeText(this, R.string.error_no_provider, Toast.LENGTH_LONG)
					.show();
		}

	}

	private void initLocationListener() {
		mLocListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {
				initProvider();
				locationTracker();
				executeTasks();
			}

			@Override
			public void onProviderDisabled(String provider) {

			}

			@Override
			public void onLocationChanged(Location location) {
				mLocation = mLocManager.getLastKnownLocation(mBestProvider);
				executeTasks();
			}
		};

	}

	/* Track user location with specific time interval. */
	private void locationTracker() {
		mLocManager.requestLocationUpdates(mBestProvider, 5 * 1000, 100,
				mLocListener);
		mLocation = mLocManager.getLastKnownLocation(mBestProvider);

		/* mandatory null location check */
		if (mLocation == null) {
			Log.i(TAG, "Bestlocation returns null location");
			if (mBestProvider.equals(LocationManager.GPS_PROVIDER)) {
				mBestProvider = null;
				mLocManager.removeUpdates(mLocListener);

				mBestProvider = LocationManager.NETWORK_PROVIDER;
				initLocationListener();
				locationTracker();
			} else if (mBestProvider.equals(LocationManager.NETWORK_PROVIDER)) {
				mBestProvider = null;
				mLocManager.removeUpdates(mLocListener);

				mBestProvider = LocationManager.PASSIVE_PROVIDER;
				initLocationListener();
				locationTracker();
			} else if (mBestProvider.equals(LocationManager.PASSIVE_PROVIDER)) {
				Toast.makeText(this, R.string.error_cannot_get_location,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();

		boolean isAvailable = false;
		if (networkInfo != null && networkInfo.isConnected()) {
			isAvailable = true;
		}
		return isAvailable;
	}

	@Override
	protected void onStop() {
		super.onStop();
		// stop the location listener
		if (mLocManager != null && mLocListener != null) {
			mLocManager.removeUpdates(mLocListener);
		}
	}
	
	@Override
    public void onPause() {
    	super.onPause();
    	mSensorManager.unregisterListener(mShakeDetector);
    }

	private void executeTasks() {
		RetrieveWeatherTask currentWeather = new RetrieveWeatherTask();
		ForecastWeatherTask forecastWeather = new ForecastWeatherTask();
		if (mLocation != null && isNetworkAvailable()) {
			currentWeather.execute(mLocation);
			forecastWeather.execute(mLocation);
		} else {
			Toast.makeText(this, R.string.error_network_status,
					Toast.LENGTH_LONG).show();
		}

	}

	private class RetrieveWeatherTask extends
			AsyncTask<Location, Void, Weather> {

		@Override
		protected Weather doInBackground(Location... params) {
			Weather weather = new Weather();
			String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONParser.getWeather(data);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return weather;

		}

		@Override
		protected void onPostExecute(Weather weather) {
			super.onPostExecute(weather);

			mCity.setText(weather.getCity() + ", " + weather.getCountry());
			mDate.setText(DateUtil.getDate(weather.getTimeStamp()));
			mDetails.setText("Humidity: %" + weather.getHumidity());
			mTemperature.setText(weather.getTemp() + " °„F");
			mDescription.setText(weather.getCondition());
			mWeatherIcon.setText(WeatherIconUtil.setWeatherIcon(
					weather.getWeatherId(), MainActivity.this));
		}
	}

	private class ForecastWeatherTask extends
			AsyncTask<Location, Void, WeatherForecast> {

		@Override
		protected WeatherForecast doInBackground(Location... params) {

			String data = ((new WeatherHttpClient())
					.getForecastWeatherData(params[0]));
			WeatherForecast forecast = new WeatherForecast();
			try {
				forecast = JSONParser.getForecastWeather(data);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return forecast;
		}

		@Override
		protected void onPostExecute(WeatherForecast forecastWeather) {
			super.onPostExecute(forecastWeather);

			mListData = forecastWeather;

			mAdapter = new ForecastAdapter(MainActivity.this, mListData,
					mColor, mWeatherFont);
			mForecastList.setAdapter(mAdapter);
		}
	}

}
