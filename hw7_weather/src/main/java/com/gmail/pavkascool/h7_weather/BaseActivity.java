package com.gmail.pavkascool.h7_weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private Button add;
    private RecyclerView recyclerView;
    private LocAdapter locAdapter;
    private List<String> locations;
    private static int REQ_CODE = 1;
    private WeatherDataBase db = WeatherApplication.getInstance().getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        add = findViewById(R.id.add);
        add.setOnClickListener(this);

        recyclerView = findViewById(R.id.rec);
        int cols = getResources().getConfiguration().orientation;
        GridLayoutManager manager = new GridLayoutManager(this, cols);
        recyclerView.setLayoutManager(manager);
        locAdapter = new BaseActivity.LocAdapter();
        recyclerView.setAdapter(locAdapter);

        locations = (List<String>)getLastCustomNonConfigurationInstance();
        if(locations == null) {
            locations = new ArrayList<String>();
        }
    }

    private class LocAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.loc, parent, false);
            return new BaseActivity.LocViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            LocViewHolder locViewHolder = (LocViewHolder)holder;
            String s = BaseActivity.this.locations.get(position);
            locViewHolder.local.setText(s);
        }

        @Override
        public int getItemCount() {
            if(BaseActivity.this.locations != null) {
                return BaseActivity.this.locations.size();
            }
            return 0;
        }
    }

    private class LocViewHolder extends RecyclerView.ViewHolder {
        private TextView local;
        private Button remove;
        public LocViewHolder(@NonNull View itemView) {
            super(itemView);
            local = itemView.findViewById(R.id.local);
            remove = itemView.findViewById(R.id.remove);
            local.setOnClickListener(BaseActivity.this);
            remove.setOnClickListener(BaseActivity.this);

        }
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, LocatorActivity.class);
        if(v.getId() == R.id.add) {

            startActivityForResult(intent, 1);
        }
        else {
            int pos = recyclerView.getChildLayoutPosition((LinearLayout)(v.getParent()));
            if(v instanceof Button) {
                Toast.makeText(BaseActivity.this, "Button " + pos, Toast.LENGTH_SHORT).show();
                locations.remove(pos);
                locAdapter.notifyDataSetChanged();

            }
            else {
                Toast.makeText(BaseActivity.this, "Field " + pos, Toast.LENGTH_SHORT).show();
                String loc = ((TextView)v).getText().toString();
                intent.putExtra("location", loc);
                startActivityForResult(intent, 2);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(data != null && resultCode == RESULT_OK) {
            String loc = data.getStringExtra("location").toUpperCase();
            for(String s: locations) {
                if(s.equals(loc)) {
                    Toast.makeText(BaseActivity.this, "The Location is already in the list", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            final Locations newLocation = new Locations();
            newLocation.setLocation(loc);
            final LocationDao dao = db.locationDao();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    dao.insert(newLocation);
                }
            });
            t.start();
            locations.add(loc);
            locAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return locations;
    }
}
