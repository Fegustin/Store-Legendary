package com.shitshop.storeshits.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shitshop.storeshits.R;

public class ProfileFragment extends Fragment {

    // Firebase
    private FirebaseAuth mAuth;

    //View
    private TextView textViewRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

        } else {

        }

        // View
        textViewRegister = root.findViewById(R.id.textViewRegister);


        // Event

        //Go to registration page
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_profile_to_navigation);
            }
        });

        return root;
    }
}