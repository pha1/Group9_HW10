/**
 * Group 9 HW10
 * MainActivity.java
 * Phi Ha
 * Srinath Dittakavi
 */

package com.example.group9_hw10;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.group9_hw10.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements TripsFragment.TripsFragmentListener {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new LoginFragment(), "Login")
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rootView, new TripsFragment(), "Trips")
                    .commit();
        }
    }
}