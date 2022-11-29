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

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.group9_hw10.databinding.FragmentTripsBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class TripsFragment extends Fragment {

    FragmentTripsBinding binding;
    final String TAG = "test";

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

        binding.buttonNewTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.newTrip();
            }
        });

        // TODO DELETE THIS WHEN DONE TESTING
        binding.buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.tripDetails();
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
        void tripDetails();
        void logout();
    }
}