package com.example.proj;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Detail extends AppCompatActivity {

    private static final String TAG = "Detail";

    LatLng currLoc;
    JSONObject jsonObjectSearch;
    JSONObject jsonObjectDetail;
    String carSelected;
    String id;
    String placeId;

    ImageView imageViewDetail;
    ImageView closeDetail;
    ImageView openDetail;
    ImageView openMaybeDetail;
    TextView Title;
    TextView addressDetail;
    TextView rating;
    TextView ratingCountDetail;
    TextView priceDetail;
    TextView driveDetail;
    List<ImageView> PriceRating;
    List<ImageView> StarFull;
    List<ImageView> StarHalf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageViewDetail = findViewById(R.id.imageViewDetail);
        closeDetail = findViewById(R.id.closeDetail);
        openDetail = findViewById(R.id.openDetail);
        openMaybeDetail = findViewById(R.id.openMaybeDetail);
        Title = findViewById(R.id.Title);
        addressDetail = findViewById(R.id.addressDetail);
        rating = findViewById(R.id.rating);
        ratingCountDetail = findViewById(R.id.ratingCountDetail);
        priceDetail = findViewById(R.id.priceDetail);
        driveDetail = findViewById(R.id.driveDetail);

        PriceRating = new ArrayList<>();
        PriceRating.add((ImageView) findViewById(R.id.money0));
        PriceRating.add((ImageView) findViewById(R.id.money1));
        PriceRating.add((ImageView) findViewById(R.id.money2));

        StarFull = new ArrayList<>();
        StarFull.add((ImageView) findViewById(R.id.starFull0));
        StarFull.add((ImageView) findViewById(R.id.starFull1));
        StarFull.add((ImageView) findViewById(R.id.starFull2));
        StarFull.add((ImageView) findViewById(R.id.starFull3));
        StarFull.add((ImageView) findViewById(R.id.starFull4));

        StarHalf = new ArrayList<>();
        StarHalf.add((ImageView) findViewById(R.id.starHalf0));
        StarHalf.add((ImageView) findViewById(R.id.starHalf1));
        StarHalf.add((ImageView) findViewById(R.id.starHalf2));
        StarHalf.add((ImageView) findViewById(R.id.starHalf3));
        StarHalf.add((ImageView) findViewById(R.id.starHalf4));

        processIntent();
        getDetail();
        setDetailsHeader();
    }

    private void processIntent() {
        try {
            Intent intent = getIntent();
            carSelected = intent.getStringExtra("CAR");
            String jsonObjectString = intent.getStringExtra("CURR_STATION");
            double lng = intent.getDoubleExtra("CURR_LONG", 0);
            double lat = intent.getDoubleExtra("CURR_LAT", 0);
            jsonObjectSearch = new JSONObject(jsonObjectString);
            currLoc = new LatLng(lat, lng);
            id = jsonObjectSearch.getString("id");
            placeId = jsonObjectSearch.getString("place_id");
        } catch (JSONException | NullPointerException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void getDetail() {
        try {
            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/" +
                    "maps/api/place/details/json?")
                    .append("place_id=").append(placeId)
                    .append("&key=").append(MapsActivity.key);
            String response = read(googlePlacesUrl.toString());
            JSONObject responseObject = new JSONObject(response);
            jsonObjectDetail = responseObject.getJSONObject("result");
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDetailsHeader(){
        try {
            if (jsonObjectSearch.has("photos")){
                JSONObject photoObject = (JSONObject) jsonObjectSearch
                        .getJSONArray("photos").get(0);
                String photoRef = photoObject.getString("photo_reference");
                StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/" +
                        "maps/api/place/photo?")
                        .append("maxwidth=420")
                        .append("&photoreference=").append(photoRef)
                        .append("&key=").append(MapsActivity.key);
                URL url = new URL(googlePlacesUrl.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                imageViewDetail.setImageBitmap(myBitmap);
            }

            Title.setText(jsonObjectDetail.getString("name"));
            addressDetail.setText(jsonObjectDetail.getString("formatted_address"));
            if (jsonObjectSearch.has("opening_hours")){
                if (jsonObjectSearch.getJSONObject("opening_hours").getBoolean("open_now")){
                   openDetail.setAlpha(1.0f);
                   closeDetail.setAlpha(0.0f);
                   openMaybeDetail.setAlpha(0.0f);
                } else {
                    openDetail.setAlpha(0.0f);
                    closeDetail.setAlpha(1.0f);
                    openMaybeDetail.setAlpha(0.0f);
                }

            } else {
                openDetail.setAlpha(0.0f);
                closeDetail.setAlpha(0.0f);
                openMaybeDetail.setAlpha(1.0f);
            }

            int ratingVal = jsonObjectSearch.has("rating") ?
                    jsonObjectSearch.getInt("rating") : 0;
            int ratingCount = jsonObjectSearch.has("user_ratings_total") ?
                    jsonObjectSearch.getInt("user_ratings_total") : 0;
            rating.setText(ratingVal + "");
            for (int i = 0; i < 5; i ++){
                if (i < ratingVal){
                    ImageView full = StarFull.get(i);
                    ImageView half = StarHalf.get(i);
                    full.setAlpha(1.0f);
                    half.setAlpha(0.0f);
                } else {
                    ImageView full = StarFull.get(i);
                    ImageView half = StarHalf.get(i);
                    full.setAlpha(0.0f);
                    half.setAlpha(1.0f);
                }
            }
            ratingCountDetail.setText(ratingCount + " reviews");

            int pricePerKwh = (int)(jsonObjectSearch.getDouble("price-per-kWh") * 100);
            priceDetail.setText(pricePerKwh + " Cents per kWh");
            if (8 <= pricePerKwh && pricePerKwh <= 10.66){  // Hardcoded random prices
                PriceRating.get(0).setAlpha(1.0f);
                PriceRating.get(1).setAlpha(0.2f);
                PriceRating.get(2).setAlpha(0.2f);
            } else if (10.66 <= pricePerKwh && pricePerKwh <= 13.33){
                PriceRating.get(0).setAlpha(1.0f);
                PriceRating.get(1).setAlpha(1.0f);
                PriceRating.get(2).setAlpha(0.2f);
            } else {
                PriceRating.get(0).setAlpha(1.0f);
                PriceRating.get(1).setAlpha(1.0f);
                PriceRating.get(2).setAlpha(1.0f);
            }

            double dist = jsonObjectSearch.getDouble("distance");
            double etaINHr = Math.max(new BigDecimal( dist / 25)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue(), 0.02);
            if (etaINHr > 1){
                if (etaINHr >= 2) {
                    driveDetail.setText("ETA: " + etaINHr + " hrs");
                } else {
                    driveDetail.setText("ETA: " + etaINHr + " hr");
                }
            } else {
                double val = new BigDecimal( etaINHr * 60)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
                driveDetail.setText("ETA: " + val + " mins");
            }

        } catch (JSONException | IOException e){
            Log.e(TAG, e.toString());
        }
    }

    private void setDetailBody(){

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
}

