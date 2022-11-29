/**
 * Group 9 HW10
 * CreateNewAccountFragment.java
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
import android.view.View;
import android.view.ViewGroup;

import com.example.group9_hw10.databinding.FragmentCreateNewAccountBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewAccountFragment extends Fragment {

    FragmentCreateNewAccountBinding binding;

    public CreateNewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateNewAccountBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.create_account_label));

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Implement Firebase Sign up
                // TODO Add user to database
            }
        });

        binding.textViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancel();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewAccountFragmentListener) {
            mListener = (CreateNewAccountFragmentListener) context;
        }
    }

    CreateNewAccountFragmentListener mListener;

    public interface CreateNewAccountFragmentListener {
        void cancel();
    }
}