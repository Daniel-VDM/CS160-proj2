package com.example.proj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "ADAPTER LOG";
    private final double chargePercentage = Math.random();

    private JSONArray currStations;
    private Context context;
    private LatLng currloc;
    private String car;

    RecyclerViewAdapter(Context context, JSONArray currStations, LatLng currloc, String car){
        this.currStations = currStations;
        this.context = context;
        this.currloc = currloc;
        this.car = car;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_listitem, parent, false);
        return new ViewHolder(view);
    }

    private String read(String httpUrl) {
        String httpData = "";
        InputStream stream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            stream = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buf = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            httpData = buf.toString();
            reader.close();
        } catch (Exception e) {
            Log.e("HttpRequestHandler", Objects.requireNonNull(e.getMessage()));
        } finally {
            try {
                assert stream != null;
                stream.close();
                urlConnection.disconnect();
            } catch (Exception e) {
                Log.e("HttpRequestHandler", Objects.requireNonNull(e.getMessage()));
            }
        }
        return httpData;
    }


    private float calcDistance(double lat, double lng){
        float[] results = new float[1];
        Location.distanceBetween(currloc.latitude, currloc.longitude, lat, lng, results);
        return new BigDecimal(results[0] * 0.000621371)
                .setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "And we're in...");
        try {
            JSONObject jsonObject = (JSONObject) currStations.get(position);

            // Distance
            double lat = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getDouble("lat");
            double lng = jsonObject.getJSONObject("geometry")
                    .getJSONObject("location").getDouble("lng");
            float dist = calcDistance(lat, lng);
            holder.Distance.setText("Distance: " + dist + " mi");

        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return currStations.length();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView PlaceName;
        TextView PlaceTime;
        TextView ChargeTime;
        TextView Distance;
        TextView Rating;
        TextView RatingCount;
        ImageView bar;
        ImageView openIcon;
        ImageView timeIcon;
        ImageView carIcon;
        ImageView open;
        ImageView close;
        List<ImageView> StarFull;
        List<ImageView> StarHalf;
        Button More;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            PlaceName = itemView.findViewById(R.id.PlaceName);
            PlaceTime = itemView.findViewById(R.id.PlaceTime);
            ChargeTime = itemView.findViewById(R.id.ChargeTime);
            Distance = itemView.findViewById(R.id.Distance);
            Rating = itemView.findViewById(R.id.Rating);
            RatingCount = itemView.findViewById(R.id.RatingCount);
            bar = itemView.findViewById(R.id.bar);
            openIcon = itemView.findViewById(R.id.openIcon);
            timeIcon = itemView.findViewById(R.id.timeIcon);
            carIcon = itemView.findViewById(R.id.carIcon);
            open = itemView.findViewById(R.id.open);
            close = itemView.findViewById(R.id.close);
            More = itemView.findViewById(R.id.More);

            final View view = itemView;
            StarFull = new ArrayList<ImageView>(){{
                view.findViewById(R.id.StarFull0);
                view.findViewById(R.id.StarFull1);
                view.findViewById(R.id.StarFull2);
                view.findViewById(R.id.StarFull3);
                view.findViewById(R.id.StarFull4);
            }};
            StarHalf = new ArrayList<ImageView>(){{
                view.findViewById(R.id.StarHalf0);
                view.findViewById(R.id.StarHalf1);
                view.findViewById(R.id.StarHalf2);
                view.findViewById(R.id.StarHalf3);
                view.findViewById(R.id.StarHalf4);
            }};

        }
    }

}
