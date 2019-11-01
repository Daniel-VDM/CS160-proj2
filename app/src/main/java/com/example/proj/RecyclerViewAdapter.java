package com.example.proj;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "ADAPTER LOG";
//    private final double chargePercentage = Math.random();
    static final double chargePercentage = 0.03;
    static final double Model3Range = 260;
    static final double ModelSRange = 300;
    static final double i3Range = 190;
    static final double currModel3Range = chargePercentage * Model3Range;
    static final double currModelSRange = chargePercentage * ModelSRange;
    static final double curri3Range = chargePercentage * i3Range;

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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        try {
            JSONObject jsonObject = (JSONObject) currStations.get(position);

            if (jsonObject.has("name")) {
                holder.PlaceName.setText(jsonObject.getString("name"));
            } else {
                holder.PlaceName.setText("Charge Station");
            }

            if (jsonObject.has("vicinity")) {
                holder.Location.setText(jsonObject.getString("vicinity"));
            } else {
                holder.Location.setText("Address is unknown");
            }

            if (jsonObject.has("opening_hours")){
                if (jsonObject.getJSONObject("opening_hours").getBoolean("open_now")){
                    holder.open.setAlpha(1.0f);
                    holder.close.setAlpha(0.0f);
                    holder.openMaybe.setAlpha(0.0f);
                } else {
                    holder.open.setAlpha(0.0f);
                    holder.close.setAlpha(1.0f);
                    holder.openMaybe.setAlpha(0.0f);
                }
            } else {
                holder.open.setAlpha(0.0f);
                holder.close.setAlpha(0.0f);
                holder.openMaybe.setAlpha(1.0f);
            }

            int rating = jsonObject.has("rating") ? jsonObject.getInt("rating") : 0;
            int ratingCount = jsonObject.has("user_ratings_total") ?
                    jsonObject.getInt("user_ratings_total") : 0;
            holder.Rating.setText(rating + "");
            for (int i = 0; i < 5; i ++){
                if (i < rating){
                    ImageView full = holder.StarFull.get(i);
                    ImageView half = holder.StarHalf.get(i);
                    full.setAlpha(1.0f);
                    half.setAlpha(0.0f);
                } else {
                    ImageView full = holder.StarFull.get(i);
                    ImageView half = holder.StarHalf.get(i);
                    full.setAlpha(0.0f);
                    half.setAlpha(1.0f);
                }
            }
            holder.RatingCount.setText("(" + ratingCount + ")");

            int rangePerHr;
            double leftRange;
            double timeLeft;
            float dist = new BigDecimal(jsonObject.getDouble("distance"))
                    .setScale(2, RoundingMode.HALF_UP).floatValue();
            switch (car){
                case "model3":
                    rangePerHr = jsonObject.getInt("range-per-hr-model3");
                    leftRange = Model3Range - currModel3Range;
                    timeLeft = new BigDecimal( leftRange / rangePerHr)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();
                    if (timeLeft > 1){
                        holder.ChargeTime.setText("Charge Time: " + timeLeft + " hrs");
                    } else {
                        holder.ChargeTime.setText("Charge Time: " + timeLeft*60 + " mins");
                    }
                    if (dist > currModel3Range){
                        holder.Distance.setText("Out of range");
                    } else {
                        holder.Distance.setText("Distance: " + dist + " mi (in range)");
                    }
                    break;
                case "modelS":
                    rangePerHr = jsonObject.getInt("range-per-hr-modelS");
                    leftRange = Model3Range - currModel3Range;
                    timeLeft = new BigDecimal( leftRange / rangePerHr)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();
                    if (timeLeft > 1){
                        holder.ChargeTime.setText("Charge Time: " + timeLeft + " hrs");
                    } else {
                        holder.ChargeTime.setText("Charge Time: " + timeLeft*60 + " mins");
                    }
                    if (dist > currModelSRange){
                        holder.Distance.setText("Out of range");
                    } else {
                        holder.Distance.setText("Distance: " + dist + " mi (in range)");
                    }
                    break;
                case "i3":
                    rangePerHr = jsonObject.getInt("range-per-hr-i3");
                    leftRange = Model3Range - currModel3Range;
                    timeLeft = new BigDecimal( leftRange / rangePerHr)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue();
                    if (timeLeft > 1){
                        holder.ChargeTime.setText("Charge Time: " + timeLeft + " hrs");
                    } else {
                        holder.ChargeTime.setText("Charge Time: " + timeLeft*60 + " mins");
                    }
                    if (dist > curri3Range){
                        holder.Distance.setText("Out of range");
                    } else {
                        holder.Distance.setText("Distance: " + dist + " mi (in range)");
                    }
                    break;
            }

            holder.More.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        JSONObject currStation = currStations.getJSONObject(position);
                        double lat = currStation.getJSONObject("geometry")
                                .getJSONObject("location").getDouble("lat");
                        double lng = currStation.getJSONObject("geometry")
                                .getJSONObject("location").getDouble("lng");
                        URL url = new URL("https://map.openchargemap.io/assets/images/" +
                                "icons/branding/AppIcon_128x128.png");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        ((MapsActivity) context).googleMap.animateCamera(CameraUpdateFactory
                                .newLatLng(new LatLng(lat, lng)));
                        ((MapsActivity) context).googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(lat, lng))
                                .icon(BitmapDescriptorFactory.fromBitmap(myBitmap))
                                .title(currStation.getString("name"))).showInfoWindow();
                        Intent detail = new Intent(context, Detail.class);
                        String serialCurrStation = currStations.get(position).toString();
                        detail.putExtra("CAR", car);
                        detail.putExtra("CURR_LONG", currloc.longitude);
                        detail.putExtra("CURR_LAT", currloc.latitude);
                        detail.putExtra("CURR_STATION", serialCurrStation);
                        context.startActivity(detail);
                    } catch (JSONException | IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });

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
        TextView Location;
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
        ImageView openMaybe;
        List<ImageView> StarFull;
        List<ImageView> StarHalf;
        Button More;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            PlaceName = itemView.findViewById(R.id.PlaceName);
            Location = itemView.findViewById(R.id.Location);
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
            openMaybe = itemView.findViewById(R.id.OpenMaybe);
            More = itemView.findViewById(R.id.More);

            StarFull = new ArrayList<>();
            StarFull.add((ImageView) itemView.findViewById(R.id.StarFull0));
            StarFull.add((ImageView) itemView.findViewById(R.id.StarFull1));
            StarFull.add((ImageView) itemView.findViewById(R.id.StarFull2));
            StarFull.add((ImageView) itemView.findViewById(R.id.StarFull3));
            StarFull.add((ImageView) itemView.findViewById(R.id.StarFull4));

            StarHalf = new ArrayList<>();
            StarHalf.add((ImageView) itemView.findViewById(R.id.StarHalf0));
            StarHalf.add((ImageView) itemView.findViewById(R.id.StarHalf1));
            StarHalf.add((ImageView) itemView.findViewById(R.id.StarHalf2));
            StarHalf.add((ImageView) itemView.findViewById(R.id.StarHalf3));
            StarHalf.add((ImageView) itemView.findViewById(R.id.StarHalf4));

        }
    }

}
