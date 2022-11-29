package com.example.group9_hw10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TripRecyclerViewAdapter extends RecyclerView.Adapter<TripRecyclerViewAdapter.TripViewHolder> {

    ArrayList<Trip> trips;
    ITripRecycler iTripRecycler;

    public TripRecyclerViewAdapter(ArrayList<Trip> data, ITripRecycler iTripRecycler) {
        this.trips = data;
        this.iTripRecycler = iTripRecycler;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_row_item, parent, false);
        TripViewHolder tripViewHolder = new TripViewHolder(view, iTripRecycler);
        return tripViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface ITripRecycler {
        void goToDetails(Trip trip);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {

        View rootView;
        ITripRecycler iTripRecycler;

        TextView textViewTripName;
        TextView textViewTripStartDate;
        TextView textViewTripCompletedDate;
        TextView textViewTripStatus;
        TextView textViewTripDistance;

        int position;
        ArrayList<Trip> trips;
        Trip trip;

        public TripViewHolder(@NonNull View itemView, ITripRecycler iTripRecycler) {
            super(itemView);

            rootView = itemView;
            this.iTripRecycler = iTripRecycler;

            textViewTripName = itemView.findViewById(R.id.textViewTripName);
            textViewTripStartDate = itemView.findViewById(R.id.textViewTripStartDate);
            textViewTripCompletedDate = itemView.findViewById(R.id.textViewTripCompletedDate);
            textViewTripStatus = itemView.findViewById(R.id.textViewTripStatus);
            textViewTripDistance = itemView.findViewById(R.id.textViewTripDistance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    trip = trips.get(position);
                    iTripRecycler.goToDetails(trip);
                }
            });
        }
    }
}
