package com.example.proj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.Random;

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
    ImageView imageViewAAA;
    ImageView imageViewBBB;
    ImageView imageViewCCC;
    ImageView imageViewAA;
    ImageView imageViewBB;
    ImageView imageViewCC;
    ImageView imageViewA;
    ImageView imageViewB;
    ImageView imageViewC;
    TextView Title;
    TextView addressDetail;
    TextView rating;
    TextView ratingCountDetail;
    TextView priceDetail;
    TextView driveDetail;
    TextView phoneNumber;
    TextView textViewA;
    TextView textViewB;
    TextView textViewC;
    TextView ChargeinputA;
    TextView ChargeinputB;
    TextView ChargeinputC;
    TextView queueTimeA;
    TextView queueTimeB;
    TextView queueTimeC;
    TextView chargeTime;
    Button link;
    String buttonUrl = null;
    Button reviewButton;
    Button sendToMaps;
    FloatingActionButton floatingActionButton3;

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
        phoneNumber = findViewById(R.id.phoneNumber);
        textViewA = findViewById(R.id.textViewA);
        textViewB = findViewById(R.id.textViewB);
        textViewC = findViewById(R.id.textViewC);
        ChargeinputA = findViewById(R.id.ChargeinputA);
        ChargeinputB = findViewById(R.id.ChargeInputB);
        ChargeinputC = findViewById(R.id.ChargeInputC);
        queueTimeA = findViewById(R.id.queueTimeA);
        queueTimeB = findViewById(R.id.queueTimeB);
        queueTimeC = findViewById(R.id.queueTimeC);
        chargeTime = findViewById(R.id.chargTime);
        imageViewAAA = findViewById(R.id.imageViewAAA);
        imageViewBBB = findViewById(R.id.imageViewBBB);
        imageViewCCC = findViewById(R.id.imageViewCCC);
        imageViewAA = findViewById(R.id.imageViewAA);
        imageViewBB = findViewById(R.id.imageViewBB);
        imageViewCC = findViewById(R.id.imageViewCC);
        imageViewA = findViewById(R.id.imageViewA);
        imageViewB = findViewById(R.id.imageViewB);
        imageViewC = findViewById(R.id.imageViewC);
        link = findViewById(R.id.link);
        reviewButton = findViewById(R.id.reviewButton);
        floatingActionButton3 = findViewById(R.id.floatingActionButton3);
        sendToMaps = findViewById(R.id.sendToMaps);

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
        setDetailsBody();
        setNextPageListeners();
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
    private void setDetailsHeader() {
        try {
            if (jsonObjectSearch.has("photos")) {
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
            if (jsonObjectSearch.has("opening_hours")) {
                if (jsonObjectSearch.getJSONObject("opening_hours").getBoolean("open_now")) {
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
            for (int i = 0; i < 5; i++) {
                if (i < ratingVal) {
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

            int pricePerKwh = (int) (jsonObjectSearch.getDouble("price-per-kWh") * 100);
            priceDetail.setText(pricePerKwh + " Cents per kWh");
            if (8 <= pricePerKwh && pricePerKwh <= 10.66) {  // Hardcoded random prices
                PriceRating.get(0).setAlpha(1.0f);
                PriceRating.get(1).setAlpha(0.2f);
                PriceRating.get(2).setAlpha(0.2f);
            } else if (10.66 <= pricePerKwh && pricePerKwh <= 13.33) {
                PriceRating.get(0).setAlpha(1.0f);
                PriceRating.get(1).setAlpha(1.0f);
                PriceRating.get(2).setAlpha(0.2f);
            } else {
                PriceRating.get(0).setAlpha(1.0f);
                PriceRating.get(1).setAlpha(1.0f);
                PriceRating.get(2).setAlpha(1.0f);
            }

            double dist = jsonObjectSearch.getDouble("distance");
            switch (carSelected) {
                case "model3":
                    if (dist > RecyclerViewAdapter.currModel3Range) {
                        driveDetail.setText("Out of range");
                        return;
                    }
                case "modelS":
                    if (dist > RecyclerViewAdapter.currModelSRange) {
                        driveDetail.setText("Out of range");
                        return;
                    }
                case "i3":
                    if (dist > RecyclerViewAdapter.curri3Range) {
                        driveDetail.setText("Out of range");
                        return;
                    }
            }
            double etaINHr = Math.max(new BigDecimal(dist / 25)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue(), 0.02);
            if (etaINHr > 1) {
                if (etaINHr >= 2) {
                    driveDetail.setText("ETA: " + etaINHr + " hrs");
                } else {
                    driveDetail.setText("ETA: " + etaINHr + " hr");
                }
            } else {
                double val = new BigDecimal(etaINHr * 60)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();
                driveDetail.setText("ETA: " + val + " mins");
            }

        } catch (JSONException | IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    private void setDetailsBody() {
        try {
            Random rand = new Random();
            if (jsonObjectDetail.has("international_phone_number")) {
                phoneNumber.setText(jsonObjectDetail.getString("international_phone_number"));
            } else if (jsonObjectDetail.has("formatted_phone_number")) {
                phoneNumber.setText(jsonObjectDetail.getString("formatted_phone_number"));
            } else {
                phoneNumber.setText("Unknown Phone Number");
            }

            if (jsonObjectDetail.has("website")) {
                buttonUrl = jsonObjectDetail.getString("website");
                link.setText(buttonUrl);
            } else {
                link.setText("Unknown Website");
            }

            if (jsonObjectSearch.getString("name").contains("Tesla")) {
                imageViewBBB.setAlpha(0.0f);
                imageViewCCC.setAlpha(0.0f);
                imageViewBB.setAlpha(0.0f);
                imageViewCC.setAlpha(0.0f);
                imageViewB.setAlpha(0.0f);
                imageViewC.setAlpha(0.0f);
                textViewB.setAlpha(0.0f);
                textViewC.setAlpha(0.0f);
                queueTimeB.setAlpha(0.0f);
                queueTimeC.setAlpha(0.0f);
                ChargeinputB.setAlpha(0.0f);
                ChargeinputC.setAlpha(0.0f);
                textViewA.setText("Supercharger");
                if (Math.random() > 0.5) {
                    int curr = (int) (10.0 * Math.random() + 1);
                    ChargeinputA.setText(curr + "/11");
                    queueTimeA.setText("No Queue!");
                } else {
                    int queue = (int) (rand.nextDouble() * 60);
                    ChargeinputA.setText("0/11");
                    queueTimeA.setText("Queue: " + queue + "min");
                }
            } else {
                if (Math.random() > 0.5) {
                    int curr = (int) (5 * rand.nextDouble()) + 1;
                    ChargeinputA.setText(curr + "/6");
                    queueTimeA.setText("No Queue!");
                } else {
                    int queue = (int) (rand.nextDouble() * 60);
                    ChargeinputA.setText("0/6");
                    queueTimeA.setText("Queue: " + queue + "min");
                }
                if (Math.random() > 0.5) {
                    int curr = (int) (6 * rand.nextDouble() + 1);
                    ChargeinputB.setText(curr + "/7");
                    queueTimeB.setText("No Queue!");
                } else {
                    int queue = (int) (rand.nextDouble() * 60);
                    ChargeinputB.setText("0/7");
                    queueTimeB.setText("Queue: " + queue + "min");
                }
                if (Math.random() > 0.5) {
                    int curr = (int) (10.0 * Math.random() + 1);
                    ChargeinputC.setText(curr + "/11");
                    queueTimeC.setText("No Queue!");
                } else {
                    int queue = (int) (rand.nextDouble() * 60);
                    ChargeinputC.setText("0/11");
                    queueTimeC.setText("Queue: " + queue + "min");
                }
            }

            int rangePerHr = jsonObjectSearch.getInt("range-per-hr-i3");
            double leftRange = RecyclerViewAdapter.Model3Range - RecyclerViewAdapter.currModel3Range;
            double timeLeft = new BigDecimal(leftRange / rangePerHr)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue();
            if (timeLeft > 1) {
                chargeTime.setText("Charge: " + timeLeft + " hrs");
            } else {
                chargeTime.setText("Charge: " + timeLeft * 60 + " mins");
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void setNextPageListeners() {
        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent review = new Intent(Detail.this, Review.class);
                String jsonObjectDetailedString = jsonObjectDetail.toString();
                review.putExtra("JSON_OBJECT", jsonObjectDetailedString);
                startActivity(review);
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonUrl != null) {
                    Uri uri = Uri.parse(buttonUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        sendToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri uri = Uri.parse(jsonObjectDetail.getString("url"));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } catch (JSONException ignore) {
                }
            }
        });

        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    double lat = jsonObjectDetail.getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lat");
                    double lng = jsonObjectDetail.getJSONObject("geometry")
                            .getJSONObject("location").getDouble("lng");
                    Uri gmmIntentUri = Uri.parse("google.streetview:cbll=" + lat + "," + lng);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                } catch (JSONException ignore) {
                }
            }
        });
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

