package com.example.proj;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "ADAPTER LOG";

    private JSONArray currStations = new JSONArray();
    private Context context;

    public RecyclerViewAdapter(Context context, JSONArray currStations){
        this.currStations = currStations;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "And we're in...");
        // TODO data here...
    }

    @Override
    public int getItemCount() {
        return currStations.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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

        public ViewHolder(@NonNull View itemView) {
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
