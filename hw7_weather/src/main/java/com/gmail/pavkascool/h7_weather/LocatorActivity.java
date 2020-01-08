package com.gmail.pavkascool.h7_weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocatorActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private ImageButton search;
    private Button save, back;
    private EditText city;
    private TextView town, time, temp, desc;
    private ImageView weatherImage;
    private IconHolder iconHolder;

    private List<Weather> weathers;

    private final static String TAG = "MyTag";


    private final static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&&APPID=%s";
    private final static String FORECAST = "https://api.openweathermap.org/data/2.5/forecast?q=%s&&APPID=%s";
    private final static String ID = "da89ad0f2cb19fb309267dddfb2ac0a9";

    String test = "https://openweathermap.org/find?q=Moscow";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locator);

        weathers = (List<Weather>)getLastCustomNonConfigurationInstance();
        if(weathers == null) weathers = new ArrayList<Weather>();

        iconHolder = IconHolder.getInstance();

        recyclerView = findViewById(R.id.recycler);
        search = findViewById(R.id.search);
        save = findViewById(R.id.save);
        back = findViewById(R.id.back);
        town = findViewById(R.id.town);
        city = findViewById(R.id.city);
        time = findViewById(R.id.time);
        temp = findViewById(R.id.temp);
        desc = findViewById(R.id.desc);
        weatherImage = findViewById(R.id.weather);

        Log.d(TAG, "ON CREATE, IconHolder size is " + iconHolder.size());
        Log.d(TAG, "ON CREATE, weathers size is " + weathers.size());

        if(!weathers.isEmpty()) {
            Weather current = weathers.get(0);
            town.setText(current.getLocation().toUpperCase());
            time.setText(current.getTime());
            temp.setText(current.getTemp());
            desc.setText(current.getDesc());
            if(iconHolder.get(current.getIconCode()) != null) {
                weatherImage.setImageBitmap(iconHolder.get(current.getIconCode()));
                Log.d(TAG, "ICON Already Exists");
            }
            else {
                Picasso.with(this).load("http://openweathermap.org/img/wn/" + current.getIconCode() + "@2x.png").into(weatherImage);
                Log.d(TAG, "ICON Is To Be Loaded");
            }
        }

        search.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(this, cols);
        recyclerView.setLayoutManager(manager);
        weatherAdapter = new WeatherAdapter();
        recyclerView.setAdapter(weatherAdapter);

    }



    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return weathers;
    }

    private class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new WeatherViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            WeatherViewHolder weatherViewHolder = (WeatherViewHolder)holder;
            Weather w = weathers.get(position + 1);
            weatherViewHolder.time.setText(w.getTime());
            weatherViewHolder.temp.setText(w.getTemp());
            weatherViewHolder.desc.setText(w.getDesc());
            String iconCode = w.getIconCode();
            if(iconHolder.containsKey(iconCode)) {
                weatherViewHolder.weath.setImageBitmap(iconHolder.get(iconCode));
            }
            else {
                Picasso.with(LocatorActivity.this).load("http://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                        .into(weatherViewHolder.weath);
            }
        }

        @Override
        public int getItemCount() {
            return weathers.size() - 1;
        }
    }

    private class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView time, temp, desc;
        private ImageView weath;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            temp = itemView.findViewById(R.id.temp);
            desc = itemView.findViewById(R.id.desc);
            weath = itemView.findViewById(R.id.weather);
        }
    }

    @Override
    public void onClick(View v) {
        String loc = city.getText().toString();

        if(v.getId() == R.id.search) {
            weathers = new ArrayList<Weather>();
            try {
                searchWeatherForLocation(loc);
                searchForecastForLocation(loc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(v.getId() == R.id.save) {
            saveLocation(loc);
            finish();
        }
        else {
            finish();
        }
    }

    private void searchWeatherForLocation(final String location) throws IOException {

        Date date = new Date();
        final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(date);
        Log.d(TAG, "Current Time is " + t);

        final Weather currentWeather = new Weather(location);
        currentWeather.setTime(t);
        Log.d(TAG, "Set Time is " + currentWeather.getTime());

        String address = String.format(BASE_URL, location, ID);
        Log.d(TAG, address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LocatorActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LocatorActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                final String answer = response.body().string();


                try {
                    JSONObject object = new JSONObject(answer);

                    JSONArray weatherArray = object.getJSONArray("weather");
                    final String description = weatherArray.getJSONObject(0).getString("description");
                    currentWeather.setDesc(description);

                    final String iconCode = weatherArray.getJSONObject(0).getString("icon");
                    final String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                    currentWeather.setIconCode(iconCode);

                    JSONObject main = object.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    final String temper = "t = " + Math.round(temperature - 273.15) + " C";
                    currentWeather.setTemp(temper);



                    LocatorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            town.setText(location.toUpperCase());

                            desc.setText(description);

                            temp.setText(temper);

                            time.setText(t);


                            if (!iconHolder.containsKey(iconCode)) {
                                Log.d(TAG, iconHolder.size() + " and doesn't contain this icon");
                                Picasso.with(LocatorActivity.this).load(iconUrl).into(weatherImage);
                                Log.d(TAG, "Finished loading to UI and array size of weathers is " + weathers.size());
                                Picasso.with(LocatorActivity.this).load(iconUrl).into(new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        iconHolder.put(iconCode, bitmap);
                                        Log.d(TAG, iconHolder.size() + " updated");
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {
                                        Log.d(TAG, iconHolder.size() + " not updated");
                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                });
                            }
                            else {
                                weatherImage.setImageBitmap(iconHolder.get(iconCode));
                                Log.d(TAG, iconHolder.size() + " and CONTAINS this icon");
                            }
                        }
                    });

                } catch (JSONException e) {
                    LocatorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LocatorActivity.this, "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                weathers.add(currentWeather);
            }
        });
    }

    private void searchForecastForLocation(final String location) {
        String address = String.format(FORECAST, location, ID);
        Log.d(TAG, address);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String answer = response.body().string();

                Date date = new Date();
                final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(date);

                try {
                    JSONObject object = new JSONObject(answer);
                    JSONArray list = object.getJSONArray("list");
                    for(int i = 0; i < list.length(); i++) {
                        Weather w = new Weather(location);
                        JSONObject main = list.getJSONObject(i).getJSONObject("main");
                        String temper = main.getString("temp");
                        double d = Double.parseDouble(temper);
                        String tt = "t = " + Math.round(d - 273.15) + " C";
                        w.setTemp(tt);
                        JSONArray wthrs = list.getJSONObject(i).getJSONArray("weather");
                        String s = wthrs.getJSONObject(0).getString("description");
                        w.setDesc(s);
                        String iCode = wthrs.getJSONObject(0).getString("icon");
                        w.setIconCode(iCode);

                        long dt = list.getJSONObject(i).getLong("dt") * 1000;
                        Date arrayDate = new Date(dt);
                        String aDate = new SimpleDateFormat("dd.MM HH.mm", new Locale("ru", "BY")).format(arrayDate);
                        w.setTime(aDate);

                        weathers.add(w);
                    }

                    LocatorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            weatherAdapter.notifyDataSetChanged();
                            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(city.getWindowToken(),
                                    InputMethodManager.RESULT_UNCHANGED_SHOWN);
                        }
                    });
                } catch (JSONException e) {
                    LocatorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LocatorActivity.this, "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                LocatorActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LocatorActivity.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    private void saveLocation(String location) {

    }

    private Weather createWeather(String loc, int hour) {
        Weather weather = new Weather(loc);
        if(hour == 0) {
            String address = String.format(BASE_URL, loc, ID);
            Log.d(TAG, address);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(address).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LocatorActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LocatorActivity.this, "Connection Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String answer = response.body().string();
                    Date date = new Date();
                    final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm").format(date);

                    try {
                        JSONObject object = new JSONObject(answer);

                        JSONArray weatherArray = object.getJSONArray("weather");
                        final String description = weatherArray.getJSONObject(0).getString("description");

                        final String icon = weatherArray.getJSONObject(0).getString("icon");
                        final String iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";

                        JSONObject main = object.getJSONObject("main");
                        final double temperature = main.getDouble("temp");

                        LocatorActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                desc.setText(description);

                                String temper = "t = " + Math.round(temperature - 273.15) + " C";
                                temp.setText(temper);

                                time.setText(t);
                                if (!iconHolder.containsKey(icon)) {
                                    Picasso.with(LocatorActivity.this).load(iconUrl).into(weatherImage);
                                    Picasso.with(LocatorActivity.this).load(iconUrl).into(new Target() {
                                        @Override
                                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                            iconHolder.put(icon, bitmap);
                                        }

                                        @Override
                                        public void onBitmapFailed(Drawable errorDrawable) {

                                        }

                                        @Override
                                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                                        }
                                    });
                                }
                                else {
                                    weatherImage.setImageBitmap(iconHolder.get(icon));
                                }

                            }
                        });

                    } catch (JSONException e) {
                        LocatorActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LocatorActivity.this, "Wrong Request or Response", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });
        }
        return weather;
    }
}
