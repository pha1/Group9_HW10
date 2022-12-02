package com.example.group9_hw10;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TripDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TripDetailsFragment extends Fragment implements OnMapReadyCallback {

    private final OkHttpClient client = new OkHttpClient();

    private FirebaseAuth mAuth;
    final String TAG = "test";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_TRIP = "trip";

    private Trip mTrip;
    private MapView mapView;
    private GoogleMap mMap;

    private Location currentLocation;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    int LOCATION_REQUEST_CODE = 1001;

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // TODO Can we avoid updating to API 31?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY).build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trip_details, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mapView.onResume();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
            checkSettingsAndStartLocationUpdates();
        } else {
            askLocationPermission();
        }
    }

    private void checkSettingsAndStartLocationUpdates() {
    }

    private void startLocationUpdates() {

    }

    private void stopLocationUpdates() {

    }

    private void getLastLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    currentLocation = location;
                    Log.d(TAG, "onSuccess: " + location);
                    Log.d(TAG, "onSuccess: " + location.getLatitude());
                    Log.d(TAG, "onSuccess: " + location.getLongitude());
                } else {
                    Log.d(TAG, "onSuccess: location was null.");
                    // TODO Request for Location
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        // PERMISSION GRANTED
                        getLastLocation();
                        checkSettingsAndStartLocationUpdates();
                    } else {
                        // PERMISSION NOT GRANTED
                    }
                }
            });

    TextView textViewTitle;
    TextView textViewStartedAt;
    TextView textViewStatus;
    TextView textViewCompletedAt;
    TextView textViewDistance;
    Button buttonComplete;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewStartedAt = view.findViewById(R.id.textViewStartDate);
        textViewStatus = view.findViewById(R.id.textViewStatus);
        textViewCompletedAt = view.findViewById(R.id.textViewCompleteDate);
        textViewDistance = view.findViewById(R.id.textViewDistance);
        buttonComplete = view.findViewById(R.id.buttonComplete);

        updateUI();

        view.findViewById(R.id.buttonComplete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This completes the Trip through a various of
                // nested methods
                Log.d(TAG, "onClick: ");
                getDistance();
            }
        });
    }

    private void updateUI() {
        Log.d(TAG, "updateUI: ");
        textViewTitle.setText(mTrip.getName());
        textViewStartedAt.setText(mTrip.getStarted_At());

        // If the trip is completed, set the remaining information
        // Otherwise it stays as the default values
        if (mTrip.getStatus().equals("Completed")) {
            Log.d(TAG, "updateUI: Completed");
            textViewStatus.setText(getResources().getString(R.string.completed_label));
            textViewStatus.setTextColor(getResources().getColor(R.color.green_500));
            textViewCompletedAt.setText(mTrip.getCompleted_At());
            textViewDistance.setText(String.valueOf(mTrip.getDistance()));
            buttonComplete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onMapReady: ");
;

        mMap = googleMap;;
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                loadTrip();
            }
        });
    }

    private void loadTrip() {
        Log.d(TAG, "loadTrip: ");
        LatLng start = new LatLng(mTrip.getStart_latitude(), mTrip.getStart_longitude());
        mMap.addMarker(new MarkerOptions().position(start));

        if (mTrip.getCompleted_At() == null) {
            float zoomLevel = 14.0f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, zoomLevel));
        } else {
            LatLng finish = new LatLng(mTrip.getEnd_latitude(), mTrip.getEnd_longitude());

            mMap.addMarker(new MarkerOptions().position(finish));

            LatLngBounds.Builder builder = LatLngBounds.builder();
            builder.include(start);
            builder.include(finish);
            LatLngBounds bounds = builder.build();

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }

    private void getDistance() {

        mTrip.setEnd_latitude(currentLocation.getLatitude());
        mTrip.setEnd_longitude(currentLocation.getLongitude());

        // Build a URL from given information by the trip selected
        HttpUrl url = HttpUrl.parse("https://maps.googleapis.com/maps/api/directions/json").newBuilder()
                .addQueryParameter("origin", mTrip.getStart_latitude() + "," + mTrip.getStart_longitude())
                .addQueryParameter("destination", mTrip.getEnd_latitude() + "," + mTrip.getEnd_longitude())
                .addQueryParameter("key", "AIzaSyC5PLSXDkDJz-jIPniPW0rRQSLFbcH4rx4")
                .build();

        Log.d(TAG, "getDistance: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: ");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        //Log.d(TAG, "onResponse: " + jsonObject);

                        JSONArray jsonRoutes = jsonObject.getJSONArray("routes");
                        //Log.d(TAG, "onResponse: " + jsonRoutes);

                        JSONObject jsonObject1 = jsonRoutes.getJSONObject(0);
                        //Log.d(TAG, "onResponse: " + jsonObject1);

                        JSONArray jsonLegs = jsonObject1.getJSONArray("legs");
                        //Log.d(TAG, "onResponse: " + jsonLegs);

                        JSONObject jsonObject2 = jsonLegs.getJSONObject(0);
                        //Log.d(TAG, "onResponse: " + jsonObject2);

                        JSONObject jsonObject3 = jsonObject2.getJSONObject("distance");
                        //Log.d(TAG, "onResponse: " + jsonObject3);

                        mTrip.setDistance(jsonObject3.getString("text"));
                        //Log.d(TAG, "onResponse: " + mTrip.getDistance());

                        getUser();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onResponse: " + e.getMessage());
                    }
                }
            }
        });
    }

    private void getUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String date = df.format(Calendar.getInstance().getTime());
        Log.d(TAG, "getUser: " + date);

        db.collection("users").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            mAuth = FirebaseAuth.getInstance();
                            String user_id = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "onSuccess: " + user_id);
                            if (document.getString("user_id").equals(user_id)){
                                String doc_id = document.getId();
                                getTrip(doc_id, date);
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

    private void getTrip(String doc_id, String date) {
        Log.d(TAG, "getTrip: ");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        HashMap<String, Object> trip = new HashMap<>();
        trip.put("completed_At", date);
        trip.put("end_latitude", currentLocation.getLatitude());
        trip.put("end_longitude", currentLocation.getLongitude());
        trip.put("distance", mTrip.getDistance());

        db.collection("users").document(doc_id)
                .collection("trips").document(mTrip.getTrip_id())
                .set(trip, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "onSuccess: merge");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: " + e.getMessage());
                            }
                        });

        db.collection("users").document(doc_id)
                .collection("trips").document(mTrip.getTrip_id())
                .update("status", "Completed")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                                loadTrip();
                            }
                        });
                    }
                });

        updateTrip(date, currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    private void updateTrip(String date, double latitude, double longitude) {
        mTrip.setEnd_latitude(latitude);
        mTrip.setEnd_longitude(longitude);
        mTrip.setCompleted_At(date);
        mTrip.setStatus("Completed");
    }
}