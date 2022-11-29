/**
 * Group 9 HW10
 * TripsFragment.java
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw10;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.group9_hw10.databinding.FragmentTripsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {

    FragmentTripsBinding binding;
    private FirebaseAuth mAuth;
    final String TAG = "test";

    private ArrayList<Trip> mTrips = new ArrayList<>();
    private TripRecyclerViewAdapter adapter;

    public TripsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTripsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.trips_label));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TripRecyclerViewAdapter(mTrips, (TripRecyclerViewAdapter.ITripRecycler) getContext());
        binding.recyclerView.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();

        getUser(mAuth.getCurrentUser().getUid());

        binding.buttonNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.newTrip();
            }
        });

    }

    private void getUser(String user_id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.getString("user_id").equals(user_id)) {
                                getData(document.getId());
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
    }

    private void getData(String doc_id) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(doc_id).collection("trips")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        mTrips.clear();
                        Log.d(TAG, "onEvent: ");
                        for (QueryDocumentSnapshot document : value) {
                            Trip trip = new Trip();
                            if (document.getString("status").equals("On Going")) {
                                Log.d(TAG, "onEvent: IF");
                                trip.setName(document.getString("name"));
                                trip.setStatus("On Going");
                                trip.setStarted_At(document.getString("started_At"));
                                trip.setStart_latitude(document.getDouble("start_latitude"));
                                trip.setStart_longitude(document.getDouble("start_longitude"));
                            } else {
                                trip = document.toObject(Trip.class);
                            }
                            mTrips.add(trip);
                        }
                        Log.d(TAG, "onEvent: " + mTrips);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                mListener.logout();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TripsFragmentListener) {
            mListener = (TripsFragmentListener) context;
        }
    }

    TripsFragmentListener mListener;

    public interface TripsFragmentListener {
        void newTrip();
        void logout();
    }
}