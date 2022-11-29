package com.example.group9_hw10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.group9_hw10.databinding.FragmentTripDetailsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailsFragment extends Fragment implements OnMapReadyCallback {

    FragmentTripDetailsBinding binding;
    final String TAG = "test";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_TRIP = "trip";

    private Trip mTrip;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param trip Parameter 1.
     * @return A new instance of fragment TripDetailsFragment.
     */
    public static TripDetailsFragment newInstance(Trip trip) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TRIP, trip);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTrip = (Trip) getArguments().getSerializable(ARG_PARAM_TRIP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTripDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private GoogleMap mMap;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateUI();
    }

    private void updateUI() {

        binding.textViewTitle.setText(mTrip.getName());
        binding.textViewStartedAt.setText(mTrip.getStarted_At());

        // If the trip is completed, set the remaining information
        // Otherwise it stays as the default values
        if (mTrip.getStatus().equals("Completed")) {
            binding.textViewStatus.setText(getResources().getString(R.string.completed_label));
            binding.textViewCompletedAt.setText(mTrip.getCompleted_At());
            binding.textViewDistance.setText(String.valueOf(mTrip.getDistance()));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng start = new LatLng(mTrip.getStart_latitude(), mTrip.getStart_longitude());

        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
    }
}