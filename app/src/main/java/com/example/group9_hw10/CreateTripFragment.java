/**
 * Group 9 HW10
 * CreateTripFragment.java
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw10;

import android.Manifest;
import android.content.Context;
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

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.group9_hw10.databinding.FragmentCreateNewAccountBinding;
import com.example.group9_hw10.databinding.FragmentCreateTripBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateTripFragment extends Fragment {

    FragmentCreateTripBinding binding;
    final String TAG = "test";

    private Location currentLocation;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;

    int LOCATION_REQUEST_CODE = 1001;

    public CreateTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        binding = FragmentCreateTripBinding.inflate(inflater, container, false);
        return binding.getRoot();
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
                    binding.textViewCreateStatus.setText(getResources().getString(R.string.success_label));
                    binding.textViewCreateStatus.setTextColor(getResources().getColor(R.color.green_500));
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.create_trip_label));

        binding.buttonSubmitTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextTripName.getText().toString();
                if (binding.textViewCreateStatus.getText().toString().equals("Loading...")) {
                    Toast.makeText(getContext(), "Please wait for location to load.", Toast.LENGTH_SHORT).show();
                } else {
                    addTripToCollection(name);
                    mListener.backToTrips();
                }
            }
        });
    }

    /**
     * Add the trip to the collection when its created
     * By default, status is "On Going"
     * @param name String, name of the trip
     */
    private void addTripToCollection(String name) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String date = df.format(Calendar.getInstance().getTime());

        // Name, Start Time, Status
        HashMap<String, Object> trip = new HashMap<>();
        trip.put("name", name);
        trip.put("started_At", date);
        trip.put("status", "On Going");
        trip.put("start_latitude", currentLocation.getLatitude());
        trip.put("start_longitude", currentLocation.getLongitude());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateTripFragmentListener) {
            mListener = (CreateTripFragmentListener) context;
        }
    }

    CreateTripFragmentListener mListener;

    public interface CreateTripFragmentListener {
        void backToTrips();
    }
}