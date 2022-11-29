/**
 * Group 9 HW10
 * MainActivity.java
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.group9_hw10.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener, CreateNewAccountFragment.CreateNewAccountFragmentListener,
        TripsFragment.TripsFragmentListener, CreateTripFragment.CreateTripFragmentListener, TripRecyclerViewAdapter.ITripRecycler {

    private FirebaseAuth mAuth;
    final String TAG = "test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "Login")
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new TripsFragment(), "Trips")
                    .commit();
        }
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateNewAccountFragment(), "Create Account")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void trips() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new TripsFragment(), "Trips")
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void newTrip() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CreateTripFragment(), "Create Trip")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new LoginFragment(), "Login")
                .commit();
    }

    @Override
    public void backToTrips() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToDetails(Trip trip) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, TripDetailsFragment.newInstance(trip), "Trip Details")
                .addToBackStack(null)
                .commit();
    }
}