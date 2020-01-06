package com.gmail.pavkascool.h7_weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocatorActivity extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private WeatherAdapter weatherAdapter;
    private ImageButton search;
    private Button save;
    private EditText city;
    private TextView time, temp, desc;
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
        city = findViewById(R.id.city);
        time = findViewById(R.id.time);
        temp = findViewById(R.id.temp);
        desc = findViewById(R.id.desc);
        weatherImage = findViewById(R.id.weather);

        search.setOnClickListener(this);
        save.setOnClickListener(this);

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
            Weather w = weathers.get(position);
            weatherViewHolder.time.setText(w.getTime());
            weatherViewHolder.temp.setText(w.getTemp());
            weatherViewHolder.desc.setText(w.getDesc());
            weatherViewHolder.weath.setImageBitmap(w.getWeatherImage());
        }

        @Override
        public int getItemCount() {
            return weathers.size();
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
            try {
                searchWeatherForLocation(loc);
                searchForecastForLocation(loc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else saveLocation(loc);
    }

    private void searchWeatherForLocation(final String location) throws IOException {

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
//                String answer = response.body().string();
//
//                Date date = new Date();
//                final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm").format(date);
//
//                try {
//                    JSONObject object = new JSONObject(answer);
//
//                    JSONArray weatherArray = object.getJSONArray("weather");
//                    final String description = weatherArray.getJSONObject(0).getString("description");
//
//                    String icon = weatherArray.getJSONObject(0).getString("icon");
//                    final String iconUrl = "http://openweathermap.org/img/wn/" + icon + "@2x.png";
//
//                    JSONObject main = object.getJSONObject("main");
//                    final Double temperature = main.getDouble("temp");
//
//
//
//                    LocatorActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            desc.setText(description);
//
//                            String temper = "t = " + Math.round(temperature - 273.15) + " C";
//                            temp.setText(temper);
//
//                            time.setText(t);
//
//                            Picasso.with(LocatorActivity.this).load(iconUrl).into(weatherImage);
//                        }
//                    });
//
//                } catch (JSONException e) {
//                    LocatorActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(LocatorActivity.this, "Wrong Request or Response", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                }

                createWeather(location, 0);
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
                final String t = "Time: " + new SimpleDateFormat("dd.MM HH.mm").format(date);

                try {
                    JSONObject object = new JSONObject(answer);
                    JSONArray list = object.getJSONArray("list");
                    for(int i = 0; i < list.length(); i++) {
                        Weather w = new Weather(location);
                        JSONObject main = list.getJSONObject(i).getJSONObject("main");
                        String temper = main.getString("temp");
                        w.setTemp(temper);
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
